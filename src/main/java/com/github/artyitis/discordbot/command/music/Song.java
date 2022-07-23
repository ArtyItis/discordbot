package com.github.artyitis.discordbot.command.music;

import java.util.concurrent.TimeUnit;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class Song extends CommandMusic {

    public Song() {
        super(STATICS.SONG[0], STATICS.SONG[1]);
        SubcommandData source = new SubcommandData(STATICS.SONG_SUB_SOURCE[0], STATICS.SONG_SUB_SOURCE[1]);
        SubcommandData timestamp = new SubcommandData(STATICS.SONG_SUB_TIMESTAMP[0], STATICS.SONG_SUB_TIMESTAMP[1]);
        commandData.addSubcommands(source, timestamp);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        String subcommandName = event.getSubcommandName();
        final GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(event.getGuild());
        String commandAction = "";
        AudioTrackInfo audioTrackInfo = null;
        if (subcommandName.equals(STATICS.SONG_SUB_SOURCE[0])) {
            if (musicManager.player.getPlayingTrack() == null) {
                commandAction = "nothing's playing at the moment";
            } else {
                audioTrackInfo = musicManager.player.getPlayingTrack().getInfo();
                commandAction = "you're listening to " + audioTrackInfo.title;
            }
        } else if (subcommandName.equals(STATICS.SONG_SUB_TIMESTAMP[0])) {
            if (musicManager.player.getPlayingTrack() == null) {
                commandAction = "nothing's playing at the moment";
            } else {
                audioTrackInfo = musicManager.player.getPlayingTrack().getInfo();
                long timestamp = musicManager.player.getPlayingTrack().getPosition();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(timestamp);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(timestamp) % 60;
                String second = String.valueOf(seconds);
                if (second.length() < 2) {
                    second = "0" + second;
                }
                commandAction = "current timestamp: " + minutes + ":" + second + " minutes";
            }
        }
        MessageHelper.replyMusic(event.getMember(), event.deferReply(), name + " " + subcommandName, commandAction,
                audioTrackInfo);
    }

}
