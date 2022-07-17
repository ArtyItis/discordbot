package com.github.artyitis.discordbot.command.general;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.command.Command;
import com.github.artyitis.discordbot.utilities.GuildConfig;
import com.github.artyitis.discordbot.utilities.GuildManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;

public class DynamicChannel extends Command {

    public DynamicChannel() {
        super(STATICS.DYNAMICCHANNEL[0], STATICS.DYNAMICCHANNEL[1]);
        // rename subcommand
        SubcommandData name = new SubcommandData(STATICS.DYNAMICCHANNEL_SUB_NAME[0],
                STATICS.DYNAMICCHANNEL_SUB_NAME[1]);
        name.addOption(OptionType.STRING, STATICS.DYNAMICCHANNEL_SUB_NAME_OPTION[0],
                STATICS.DYNAMICCHANNEL_SUB_NAME_OPTION[1], true);
        // size subcommand
        SubcommandData size = new SubcommandData(STATICS.DYNAMICCHANNEL_SUB_SIZE[0],
                STATICS.DYNAMICCHANNEL_SUB_SIZE[1]);
        size.addOption(OptionType.INTEGER, STATICS.DYNAMICCHANNEL_SUB_SIZE_OPTION[0],
                STATICS.DYNAMICCHANNEL_SUB_SIZE_OPTION[1], true);
        SubcommandData lock = new SubcommandData(STATICS.DYNAMICCHANNEL_SUB_LOCK[0],
                STATICS.DYNAMICCHANNEL_SUB_LOCK[1]);
        lock.addOption(OptionType.BOOLEAN, STATICS.DYNAMICCHANNEL_SUB_LOCK_OPTION[0],
                STATICS.DYNAMICCHANNEL_SUB_LOCK_OPTION[1], true);
        commandData.addSubcommands(name, size, lock);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        String subcommandName = event.getSubcommandName();
        Guild guild = event.getGuild();
        // check if user is in a dynamic channel
        VoiceChannel vc = (VoiceChannel) event.getMember().getVoiceState().getChannel();
        VoiceChannelManager manager = vc.getManager();
        GuildConfig config = GuildManager.getConfig(guild);
        ArrayList<Long> dynamicChannels = config.getDynamicVoiceChannels();
        if (dynamicChannels.contains(vc.getIdLong()) == false) {
            MessageHelper.replyCommand(event.getMember(), event.deferReply(), true, name + " " + subcommandName,
                    "the channel you're connected to is not a dynamic voicechannel and cannot be customized");
            return;
        }
        String msg = "";
        if (subcommandName.equals(STATICS.DYNAMICCHANNEL_SUB_NAME[0])) {
            String name = event.getOption(STATICS.DYNAMICCHANNEL_SUB_NAME_OPTION[0]).getAsString();
            String oldName = vc.getName();
            manager.setName(name);
            msg = "updated channel name from **" + oldName + "** to **" + name + "**"
                    + "\nthis can only be called 2 times within 10 minutes per channel!";
        } else if (subcommandName.equals(STATICS.DYNAMICCHANNEL_SUB_SIZE[0])) {
            int size = event.getOption(STATICS.DYNAMICCHANNEL_SUB_SIZE_OPTION[0]).getAsInt();
            if (size < 2)
                size = 2;
            else if (size > 99)
                size = 99;
            manager.setUserLimit(size);
            msg = "updated channel size to " + size;
        } else if (subcommandName.equals(STATICS.DYNAMICCHANNEL_SUB_LOCK[0])) {
            boolean bool = event.getOption(STATICS.DYNAMICCHANNEL_SUB_LOCK_OPTION[0]).getAsBoolean();
            List<PermissionOverride> rolePermissionOverrides = vc.getRolePermissionOverrides();
            if (bool) {
                // allow bot to join/see the channel
                manager.putMemberPermissionOverride(event.getJDA().getSelfUser().getIdLong(),
                        EnumSet.of(Permission.VOICE_CONNECT), null);
                // deny channel access for @everyone
                manager.putRolePermissionOverride(guild.getPublicRole().getIdLong(), null,
                        EnumSet.of(Permission.VOICE_CONNECT));
                // deny channel access for every role that has special permissions at the moment
                for (PermissionOverride permissionOverride : rolePermissionOverrides) {
                    permissionOverride.getManager().setDenied(Permission.VOICE_CONNECT).queue();
                }
                msg = "channel has been **locked** for everyone except admins";
            } else {
                // reset access for @everyone
                vc.getPermissionOverride(guild.getPublicRole()).delete().queue();
                // reset acces for every other role
                for (PermissionOverride permissionOverride : rolePermissionOverrides) {
                    permissionOverride.getManager().clear(Permission.VOICE_CONNECT).queue();
                }
                msg = "channel has been **unlocked**";
            }
        }
        manager.queue();
        MessageHelper.replyCommand(event.getMember(), event.deferReply(), false, name + " " + subcommandName, msg);
    }

    @Override
    public boolean isAllowed(SlashCommandInteractionEvent event) {
        return true;
    }

}
