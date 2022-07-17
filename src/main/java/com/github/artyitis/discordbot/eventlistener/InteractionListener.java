package com.github.artyitis.discordbot.eventlistener;

import java.util.logging.Logger;

import com.github.artyitis.discordbot.button.Buttonlist;
import com.github.artyitis.discordbot.button.MyButton;
import com.github.artyitis.discordbot.command.Command;
import com.github.artyitis.discordbot.command.Commandlist;
import com.github.artyitis.utilities.LoggerConfig;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InteractionListener extends ListenerAdapter {

    private Logger logger;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        logger = LoggerConfig.getLogger(event.getGuild().getId());
        // find command
        for (Command command : Commandlist.getInstance()) {
            if (event.getName().equalsIgnoreCase(command.getName())) {
                // execute command, subcommand execution should be handled in execute() method
                logger.info(event.getMember().getEffectiveName() + " executed " + command.getName() + " on " + event.getGuild().getName());
                command.execute(event);
                break;
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        logger = LoggerConfig.getLogger(event.getGuild().getId());
        // find command
        for (MyButton button : Buttonlist.getInstance()) {
            if (button.getButton().getId().equalsIgnoreCase(event.getButton().getId())) {
                // execute button
                logger.info(event.getMember().getEffectiveName() + " pressed " + button.getButton().getLabel()+ " on " + event.getGuild().getName());
                button.execute(event);
                break;
            }
        }
    }
}
