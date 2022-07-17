package com.github.artyitis.discordbot.command.music;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Source extends CommandMusic {

    public Source() {
        super(STATICS.SOURCE[0], STATICS.SOURCE[1]);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        final GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(event.getGuild());
        String commandAction = "";
        AudioTrackInfo audioTrackInfo = null;
        if (musicManager.player.getPlayingTrack() == null) {
            commandAction = "nothing's playing at the moment";
        } else {
            commandAction = "you're listening to " + musicManager.player.getPlayingTrack().getInfo().title;
            audioTrackInfo = musicManager.player.getPlayingTrack().getInfo();
        }

        MessageHelper.replyMusic(event.getMember(), event.deferReply(), name, commandAction,
                audioTrackInfo);
    }

}
