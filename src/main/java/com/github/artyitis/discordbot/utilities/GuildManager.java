package com.github.artyitis.discordbot.utilities;

import java.util.HashMap;

import com.github.artyitis.discordbot.utilities.galleries.GalleryTasks;

import net.dv8tion.jda.api.entities.Guild;

public class GuildManager {
    private static HashMap<Long, GalleryTasks> galleryMap = new HashMap<>();
    private static HashMap<Long, GuildConfig> guildconfigMap = new HashMap<>();

    public static GalleryTasks getGalleryManager(Guild guild) {
        long id = guild.getIdLong();
        if (galleryMap.containsKey(id)) {
            return galleryMap.get(id);
        }
        GalleryTasks tasks = new GalleryTasks(guild);
        galleryMap.put(id, tasks);
        return tasks;
    }

    public static GuildConfig getConfig(Guild guild) {
        long id = guild.getIdLong();
        if (guildconfigMap.containsKey(id))
            return guildconfigMap.get(id);
        GuildConfig config = new GuildConfig(guild);
        guildconfigMap.put(id, config);
        return config;
    }

}
