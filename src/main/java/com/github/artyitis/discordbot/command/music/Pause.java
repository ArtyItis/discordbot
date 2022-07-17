package com.github.artyitis.discordbot.command.music;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Pause extends CommandMusic {

    public Pause() {
        super(STATICS.PAUSE[0], STATICS.PAUSE[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        final GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(event.getGuild());
        String commandAction = "";
        AudioTrackInfo audioTrackInfo = null;
        if (musicManager.player.getPlayingTrack() == null) {
            commandAction = "There is no track playing at the moment";
            return;
        } else {
            if (musicManager.player.isPaused()) {
                commandAction = "The track was already paused.";
            } else {
                musicManager.scheduler.onPlayerPause(musicManager.player);
                commandAction = ":pause_button: The track was paused";
            }
            audioTrackInfo = musicManager.player.getPlayingTrack().getInfo();
        }
        MessageHelper.replyMusic(event.getMember(), event.deferReply(), name, commandAction, audioTrackInfo);
    }

}
