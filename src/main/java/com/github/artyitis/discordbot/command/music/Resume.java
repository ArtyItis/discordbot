package com.github.artyitis.discordbot.command.music;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Resume extends CommandMusic {

    public Resume() {
        super(STATICS.RESUME[0], STATICS.RESUME[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        final GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(event.getGuild());
        String commandAction = "";
        AudioTrackInfo audioTrackInfo = null;
        if (musicManager.player.getPlayingTrack() == null) {
            commandAction = "there's nothing to play";
        } else {
            if (musicManager.player.isPaused()) {
                musicManager.scheduler.onPlayerResume(musicManager.player);
                commandAction = ":play_pause: resume playback of the current track";
            } else
                commandAction = "a track is already playing";
            audioTrackInfo = musicManager.player.getPlayingTrack().getInfo();
        }
        MessageHelper.replyMusic(event.getMember(), event.deferReply(), name, commandAction, audioTrackInfo);
    }
}
