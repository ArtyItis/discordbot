package com.github.artyitis.discordbot.command;

import com.github.artyitis.discordbot.utilities.MessageHelper;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {

    protected SlashCommandData commandData;

    protected String name;

    public Command(String name, String description) {
        this.name = name;
        commandData = Commands.slash(name, description);
    }

    public abstract boolean isAllowed(SlashCommandInteractionEvent event);

    public abstract void eventHandler(SlashCommandInteractionEvent event) throws Exception;

    public void execute(SlashCommandInteractionEvent event) {
        try {
            if (isAllowed(event))
                eventHandler(event);
            else
                MessageHelper.replyCommand(event.getMember(), event.deferReply(), true, name,
                        "you are not allowed to do this");
        } catch (Exception e) {
            MessageHelper.replyCommandLogged(event.getMember(), event.deferReply(), true, "Error",
                    "something went terribly wrong\n" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public SlashCommandData getCommandData() {
        return commandData;
    }

    public String getName() {
        return name;
    }
}
