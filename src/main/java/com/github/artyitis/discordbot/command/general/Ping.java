package com.github.artyitis.discordbot.command.general;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.command.Command;
import com.github.artyitis.discordbot.utilities.MessageHelper;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Ping extends Command {

    public Ping() {
        super(STATICS.PING[0], STATICS.PING[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        MessageHelper.replyCommand(event.getMember(), event.deferReply(), true, name, "Pong!");

    }

    @Override
    public boolean isAllowed(SlashCommandInteractionEvent event) {
        return true;
    }

}
