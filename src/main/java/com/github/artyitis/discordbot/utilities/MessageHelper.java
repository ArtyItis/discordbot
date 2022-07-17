package com.github.artyitis.discordbot.utilities;

import com.github.artyitis.javaredditwrapper.RedditPost;
import com.github.artyitis.utilities.LoggerConfig;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class MessageHelper {

    /**
     * Send message to a specific MessageChannel. This can be a privatechannel (DMs)
     * or a textchannel from a guild
     *
     * @param channel the channel
     * @param message the message
     */
    public static void sendMessage(final MessageChannel channel, final String message) {
        channel.sendMessage(message).queue();
    }

    // DEFAULT----------------------------------------
    public static void replyCommand(Member member, ReplyCallbackAction replyAction, boolean ephemeral,
            String commandName,
            String commandAction) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setCommandReplyFormat(member, commandName, commandAction);
        replyAction.addEmbeds(embedBuilder.build()).setEphemeral(ephemeral).queue();
    }

    public static void replyCommandLogged(Member member, ReplyCallbackAction replyAction, boolean ephemeral,
            String commandName, String commandAction) {
        LoggerConfig.getLogger(member.getGuild().getId()).info(commandAction);
        replyCommand(member, replyAction, ephemeral, commandName, commandAction);
    }

    // MUSIC------------------------------------------
    public static void replyMusic(Member member, ReplyCallbackAction replyAction, String commandName,
            String commandAction) {
        replyMusic(member, replyAction, commandName, commandAction, null);
    }

    public static void replyMusic(Member member, ReplyCallbackAction replyAction, String commandName,
            String commandAction, AudioTrackInfo trackInfo) {
        Guild guild = member.getGuild();
        long id = GuildManager.getConfig(guild).getMusicLogChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setCommandReplyFormat(member, commandName, commandAction);
        embedBuilder.setMusicFormat(trackInfo);

        if (id != -1)
            guild.getTextChannelById(id).sendMessageEmbeds(embedBuilder.build()).queue();

        replyAction.addEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    // REDDIT-----------------------------------------
    public static void sendRedditPost(TextChannel channel, RedditPost redditPost, String url) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setRedditFormat(channel.getGuild(), redditPost, url);
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

}
