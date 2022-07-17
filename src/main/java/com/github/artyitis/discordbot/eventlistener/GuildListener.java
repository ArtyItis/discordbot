package com.github.artyitis.discordbot.eventlistener;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.github.artyitis.discordbot.command.Commandlist;
import com.github.artyitis.discordbot.utilities.GuildConfig;
import com.github.artyitis.discordbot.utilities.GuildManager;
import com.github.artyitis.utilities.LoggerConfig;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter {

    private GuildConfig config;
    private Logger logger;

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Guild guild = event.getGuild();
        config = GuildManager.getConfig(guild);
        config.setName(guild.getName());
        config.setOwner(guild.getOwner());
        // delete all old dynamic Channels which are empty
        ArrayList<Long> dynamicVoiceChannels = config.getDynamicVoiceChannels();
        if (dynamicVoiceChannels != null) {
            for (Long id : dynamicVoiceChannels) {
                VoiceChannel vc = guild.getVoiceChannelById(id);
                if (vc.getMembers().size() < 1) {
                    vc.delete().queue();
                    config.removeDynamicVoiceChannel(id);
                }
            }
        }
        // reload all gallery tasks
        GuildManager.getGalleryManager(guild).reloadTasks();
        update(guild);
    }

    public void update(Guild guild) {
        logger = LoggerConfig.getLogger(guild.getId());
        logger.info("updating commands");
        // update guild command list
        guild.updateCommands().addCommands(Commandlist.getInstance().getCommandDatas()).queue();
        logger.info("finished updating commands");
    }
}
