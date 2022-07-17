package com.github.artyitis.discordbot.command.admin;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.MusicHelper;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Shutdown extends CommandAdmin {
    public Shutdown() {
        super(STATICS.SHUTDOWN[0], STATICS.SHUTDOWN[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) {
        event.reply("bot is going offline").setEphemeral(true).complete();
        MusicHelper.getInstance().getAudioPlayerManager().shutdown();
        MusicHelper.getInstance().shutdown();
        event.getJDA().shutdown();
        System.exit(0);
    }
}
