package com.github.artyitis.discordbot;

import javax.security.auth.login.LoginException;

import com.github.artyitis.discordbot.eventlistener.GuildListener;
import com.github.artyitis.discordbot.eventlistener.GuildVoiceListener;
import com.github.artyitis.discordbot.eventlistener.InteractionListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;

public class Main {
    public static void main(String[] args) {
        try {
            JDABuilder builder = JDABuilder.createDefault(Secrets.DISCORD_TOKEN);
            builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            builder.setChunkingFilter(ChunkingFilter.ALL);
            builder.setLargeThreshold(50);
            JDA jda = builder.build();
            jda.addEventListener(new InteractionListener());
            jda.addEventListener(new GuildListener());
            jda.addEventListener(GuildVoiceListener.getInstance());
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
