package com.github.artyitis.discordbot.utilities;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.github.artyitis.javaredditwrapper.RedditPost;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class EmbedBuilder extends net.dv8tion.jda.api.EmbedBuilder {

    public EmbedBuilder() {
        super();
    }

    public EmbedBuilder setRedditFormat(Guild guild, RedditPost redditPost, String url) {
        setColor(guild);
        setImage(url);
        setTitle(redditPost.getTitle(), redditPost.getPermalink());
        setFooter(" 👍 " + redditPost.getUps() + " |" + " 💬 " + redditPost.getNum_comments() + " - "
                + redditPost.getSubReddit());
        return this;
    }

    public EmbedBuilder setMusicFormat(AudioTrackInfo trackInfo) {
        if (trackInfo != null) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(trackInfo.length);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(trackInfo.length) % 60;
            String second = String.valueOf(seconds);
            if (second.length() < 2) {
                second = "0" + second;
            }
            addField("track", "[" + trackInfo.title + "](" + trackInfo.uri + ")", true);
            addField("length", minutes + ":" + second + " minutes", true);
            addField("youtube channel", trackInfo.author, true);
        }
        return this;
    }

    public EmbedBuilder setCommandReplyFormat(Member member, String commandName, String commandAction) {
        setColor(member.getGuild());
        setTitle("Command: " + commandName);
        setDescription(commandAction);
        // configurate footer
        setFooter(member.getEffectiveName() + " used " + commandName + " | " + member.getJDA().getSelfUser().getName(),
                member.getEffectiveAvatarUrl());
        return this;
    }

    public EmbedBuilder setColor(Guild guild) {
        Color color = GuildManager.getConfig(guild).getColor();
        if (color == null) {
            Random random = new Random();
            color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }
        setColor(color);
        return this;
    }
}
