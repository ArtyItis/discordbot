package com.github.artyitis.discordbot.command.music;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Skip extends CommandMusic {

    public Skip() {
        super(STATICS.SKIP[0], STATICS.SKIP[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(event.getGuild());
        String commandAction = "";
        AudioTrackInfo audioTrackInfo = null;
        if (musicManager.player.getPlayingTrack() == null) {
            commandAction = "there's nothing to skip";
        } else {
            musicManager.scheduler.nextTrack();
            if (musicManager.player.getPlayingTrack() == null) {
                commandAction = ":fast_forward: Skipped to nothing";
                Disconnect.disconnect(event.getGuild());
            } else {
                commandAction = ":fast_forward: Skipped to "
                        + musicManager.player.getPlayingTrack().getInfo().title + " :thumbsup:";
                audioTrackInfo = musicManager.player.getPlayingTrack().getInfo();
            }
        }
        MessageHelper.replyMusic(event.getMember(), event.deferReply(), name, commandAction, audioTrackInfo);
    }
}
