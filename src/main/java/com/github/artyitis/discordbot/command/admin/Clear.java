package com.github.artyitis.discordbot.command.admin;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.utilities.MessageHelper;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class Clear extends CommandAdmin {

    public Clear() {
        super(STATICS.CLEAR[0], STATICS.CLEAR[1]);
        commandData.addOption(OptionType.INTEGER, STATICS.CLEAR_OPTION[0], STATICS.CLEAR_OPTION[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        OptionMapping amountOption = event.getOption(STATICS.CLEAR_OPTION[0]); // This is configured to be optional so
                                                                               // check for null
        int amount = amountOption == null
                ? 100 // default 100
                : (int) Math.min(200, Math.max(2, amountOption.getAsLong())); // enforcement: must be between 2-200

        event.getMessageChannel().getIterableHistory().takeAsync(amount)
                .thenAccept(event.getMessageChannel()::purgeMessages);

        MessageHelper.replyCommand(event.getMember(), event.deferReply(), false, name,
                "deleting " + amount + " messages");

    }

}
