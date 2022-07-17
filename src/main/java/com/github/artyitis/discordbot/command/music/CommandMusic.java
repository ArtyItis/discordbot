package com.github.artyitis.discordbot.command.music;

import com.github.artyitis.discordbot.command.Command;
import com.github.artyitis.discordbot.music.MusicHelper;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class CommandMusic extends Command {

    /** The music helper. */
    static MusicHelper musicHelper = MusicHelper.getInstance();

    public CommandMusic(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean isAllowed(SlashCommandInteractionEvent event) {
        return true;
    }

}
