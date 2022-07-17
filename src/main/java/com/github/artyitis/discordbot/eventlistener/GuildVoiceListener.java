package com.github.artyitis.discordbot.eventlistener;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import com.github.artyitis.discordbot.music.MusicHelper;
import com.github.artyitis.discordbot.utilities.GuildConfig;
import com.github.artyitis.discordbot.utilities.GuildManager;
import com.github.artyitis.utilities.LoggerConfig;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class GuildVoiceListener extends ListenerAdapter {

    private static GuildVoiceListener instance;
    private Logger logger;

    private GuildVoiceListener() {
    }

    public static GuildVoiceListener getInstance() {
        if (instance == null)
            instance = new GuildVoiceListener();
        return instance;
    }

    /** handling dynamic voices */
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        GuildConfig config = GuildManager.getConfig(guild);
        VoiceChannel voiceChannel;
        logger = LoggerConfig.getLogger(guild.getId());
        // create dynamic voice chacnnel whenever a member moves or joins in the
        // specified voice channel
        if ((voiceChannel = (VoiceChannel) event.getChannelJoined()) != null
                && config.isTargetChannel(voiceChannel.getIdLong())) {

            String dynamicName = config.getDynamicNameByTargetChannelId(voiceChannel.getIdLong());
            // create new voicechannel
            VoiceChannel newVoiceChannel = voiceChannel.createCopy().setName(dynamicName).complete();
            // move member to new voiceChannel
            guild.moveVoiceMember(member, newVoiceChannel).queue();
            // add created voiceChannel to config
            config.addDynamicVoiceChannel(newVoiceChannel.getIdLong());
        }
        // delete dynamic voice channel if it is empty
        if ((voiceChannel = (VoiceChannel) event.getChannelLeft()) != null) {
            Long id = voiceChannel.getIdLong();
            // check if channel is dynamic and is empty
            if (config.isDynamicVoiceChannel(id) && voiceChannel.getMembers().size() == 0) {
                config.removeDynamicVoiceChannel(id);
                voiceChannel.delete().queue();
            }
        }
    }

    /**
     * On guild voice join.
     *
     * @param event the event
     */
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        logger = LoggerConfig.getLogger(event.getGuild().getId());
        // check if the bot joined to play music and handle auto disconnect after 5mins
        final Member member = event.getMember();
        final Member bot = event.getGuild().getSelfMember();
        if (member.equals(bot)) { // bot joined voicechannel
            final HiddenThread thread = new HiddenThread((VoiceChannel) bot.getVoiceState().getChannel());
            thread.start();
        }
    }

    /**
     * The Class HiddenThread.
     * used for auomatic disconnect if the bot isn't used anymore
     */
    private class HiddenThread extends Thread {

        /** The channel. */
        VoiceChannel channel;

        /** The music helper. */
        MusicHelper musicHelper;

        /**
         * Instantiates a new hidden thread.
         *
         * @param channel the channel
         */
        HiddenThread(VoiceChannel channel) {
            this.musicHelper = MusicHelper.getInstance();
            this.channel = channel;
        }

        /**
         * Run.
         */
        @Override
        public void run() {
            try {
                AudioPlayer player = musicHelper.getAudioPlayer(channel.getGuild());
                while (true) {
                    if (player.getPlayingTrack() == null) {
                        sleep(300000);
                    }
                    player = musicHelper.getAudioPlayer(channel.getGuild());
                    if (player.getPlayingTrack() == null) {
                        final AudioManager manager = channel.getGuild().getAudioManager();
                        manager.closeAudioConnection();
                        return;
                    }
                    sleep(10000);
                }
            } catch (final InterruptedException e) {
                logger.warning(e.getLocalizedMessage());
            }
        }
    }

}
