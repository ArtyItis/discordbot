package com.github.artyitis.discordbot.command.music;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Disconnect extends CommandMusic {

    public Disconnect() {
        super(STATICS.DISCONNECT[0], STATICS.DISCONNECT[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        disconnect(event.getGuild());

        MessageHelper.replyMusic(event.getMember(), event.deferReply(), name,
                "The queue was reset and the bot was disconnected");
    }

    public static void disconnect(Guild guild) {
        final GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(guild);
        musicManager.scheduler.clearQueue();
        musicManager.scheduler.nextTrack();
        final AudioManager audioManager = guild.getAudioManager();
        audioManager.closeAudioConnection();
    }

}
