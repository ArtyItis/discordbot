package com.github.artyitis.discordbot.command.music;

import java.util.List;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.AudioManager;

public class Play extends CommandMusic {

    private String commandAction;
    private AudioTrackInfo trackInfo;

    public Play() {
        super(STATICS.PLAY[0], STATICS.PLAY[1]);
        SubcommandData queue = new SubcommandData(STATICS.PLAY_SUB_QUEUE[0], STATICS.PLAY_SUB_QUEUE[1]);
        SubcommandData next = new SubcommandData(STATICS.PLAY_SUB_NEXT[0], STATICS.PLAY_SUB_NEXT[1]);
        SubcommandData now = new SubcommandData(STATICS.PLAY_SUB_NOW[0], STATICS.PLAY_SUB_NOW[1]);
        OptionData option = new OptionData(OptionType.STRING, STATICS.PLAY_OPTION[0], STATICS.PLAY_OPTION[1], true);
        queue.addOptions(option);
        next.addOptions(option);
        now.addOptions(option);

        commandData.addSubcommands(queue, next, now);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        String url = event.getOption(STATICS.PLAY_OPTION[0]).getAsString();
        Member member = event.getMember();
        String subcommandName = event.getSubcommandName();
        final GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(event.getGuild());
        // validate url
        if (url.contains("https://www.youtube.com/") == false) {
            commandAction = "the provided url cannot be used";
        } else if (!connectToMember(member)) { // connect to member
            commandAction = "you must be in a voice channel to execute this command";
            trackInfo = null;
        } else {
            musicHelper.getAudioPlayerManager().loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    if (subcommandName.equals(STATICS.PLAY_SUB_QUEUE[0])) {
                        musicManager.scheduler.queue(track);
                        // check if the track is played immediately
                        if (musicManager.player.getPlayingTrack().equals(track)) {
                            commandAction = "playing " + track.getInfo().title;
                        } else {
                            commandAction = track.getInfo().title + " has been added to queue";
                        }
                    } else if (subcommandName.equals(STATICS.PLAY_SUB_NEXT[0])) {
                        musicManager.scheduler.addOnTopOfQueue(track);
                        commandAction = track.getInfo().title + " is now at the top of the queue";
                    } else if (subcommandName.equals(STATICS.PLAY_SUB_NOW[0])) {
                        musicManager.scheduler.addOnTopOfQueue(track);
                        musicManager.scheduler.nextTrack();
                        commandAction = "playing " + track.getInfo().title;
                    }
                    trackInfo = track.getInfo();
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    final List<AudioTrack> tracks = playlist.getTracks();
                    if (subcommandName.equals(STATICS.PLAY_SUB_QUEUE[0])) {
                        musicManager.scheduler.queue(tracks.remove(0));
                        musicManager.scheduler.addToQueue(tracks);
                    } else if (subcommandName.equals(STATICS.PLAY_SUB_NEXT[0])) {
                        musicManager.scheduler.addOnTopOfQueue(tracks);

                    } else if (subcommandName.equals(STATICS.PLAY_SUB_NOW[0])) {
                        musicManager.scheduler.addOnTopOfQueue(tracks);
                        musicManager.scheduler.nextTrack();
                    }
                    commandAction = "added playlist " + playlist.getName() + " to queue";
                    trackInfo = null;
                }

                @Override
                public void noMatches() {
                    commandAction = ":x: Nothing found";
                    trackInfo = null;
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    commandAction = ":x: Could not load \n" + exception.getMessage();
                    trackInfo = null;
                }
            }).get();
        }
        if (trackInfo != null)
            MessageHelper.replyMusic(member, event.deferReply(), name + " " + subcommandName, commandAction, trackInfo);
        else
            MessageHelper.replyMusic(member, event.deferReply(), name + " " + subcommandName, commandAction);

    }

    /**
     * Connect to member.
     *
     * @param member the member
     * @return true, if successful
     */
    private static boolean connectToMember(final Member member) {
        if (member.getVoiceState().getChannel() == null) {
            return false;
        }
        final VoiceChannel myChannel = (VoiceChannel) member.getVoiceState().getChannel();
        final AudioManager audioManager = member.getGuild().getAudioManager();
        audioManager.openAudioConnection(myChannel);
        return true;
    }

}
