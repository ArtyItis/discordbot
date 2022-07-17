package com.github.artyitis.discordbot.command.admin;

import java.awt.Color;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.utilities.GuildConfig;
import com.github.artyitis.discordbot.utilities.GuildManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class Set extends CommandAdmin {

        public Set() {
                super(STATICS.SET[0], STATICS.SET[1]);
                // ADMIN
                SubcommandData admin = new SubcommandData(STATICS.SET_SUB_ADMIN[0], STATICS.SET_SUB_ADMIN[1]);
                admin.addOption(OptionType.ROLE, STATICS.SET_SUB_ADMIN_OPTION[0], STATICS.SET_SUB_ADMIN_OPTION[1],
                                true);
                // MUSIC
                SubcommandGroupData music = new SubcommandGroupData(STATICS.SET_GROUP_MUSIC[0],
                                STATICS.SET_GROUP_MUSIC[1]);
                SubcommandData musicSet = new SubcommandData(STATICS.SET_GROUP_MUSIC_SUB_SET[0],
                                STATICS.SET_GROUP_MUSIC_SUB_SET[0]);
                musicSet.addOption(OptionType.CHANNEL, STATICS.SET_GROUP_MUSIC_SUB_SET_OPTION[0],
                                STATICS.SET_GROUP_MUSIC_SUB_SET_OPTION[1], true);

                SubcommandData musicRemove = new SubcommandData(STATICS.SET_GROUP_MUSIC_SUB_REMOVE[0],
                                STATICS.SET_GROUP_MUSIC_SUB_REMOVE[1]);
                music.addSubcommands(musicSet, musicRemove);
                // TARGETS
                SubcommandGroupData targets = new SubcommandGroupData(STATICS.SET_GROUP_TARGETS[0],
                                STATICS.SET_GROUP_TARGETS[1]);
                SubcommandData targetsAdd = new SubcommandData(STATICS.SET_GROUP_TARGETS_SUB_ADD[0],
                                STATICS.SET_GROUP_TARGETS_SUB_ADD[1]);
                targetsAdd.addOption(OptionType.CHANNEL, STATICS.SET_GROUP_TARGETS_SUB_ADD_OPTION_CHANNEL[0],
                                STATICS.SET_GROUP_TARGETS_SUB_ADD_OPTION_CHANNEL[1], true);
                targetsAdd.addOption(OptionType.STRING, STATICS.SET_GROUP_TARGETS_SUB_ADD_OPTION_NAME[0],
                                STATICS.SET_GROUP_TARGETS_SUB_ADD_OPTION_NAME[1], true);

                SubcommandData targetsRemove = new SubcommandData(STATICS.SET_GROUP_TARGETS_SUB_REMOVE[0],
                                STATICS.SET_GROUP_TARGETS_SUB_REMOVE[1]);
                targetsRemove.addOption(OptionType.CHANNEL, STATICS.SET_GROUP_TARGETS_SUB_REMOVE_OPTION[0],
                                STATICS.SET_GROUP_TARGETS_SUB_REMOVE_OPTION[1], true);
                targets.addSubcommands(targetsAdd, targetsRemove);
                // COLOR
                SubcommandGroupData color = new SubcommandGroupData(STATICS.SET_GROUP_COLOR[0],
                                STATICS.SET_GROUP_COLOR[1]);
                SubcommandData colorRGB = new SubcommandData(STATICS.SET_GROUP_COLOR_SUB_RGB[0],
                                STATICS.SET_GROUP_COLOR_SUB_RGB[1]);
                colorRGB.addOption(OptionType.INTEGER, STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_RED[0],
                                STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_RED[1], true);
                colorRGB.addOption(OptionType.INTEGER, STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_GREEN[0],
                                STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_GREEN[1], true);
                colorRGB.addOption(OptionType.INTEGER, STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_BLUE[0],
                                STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_BLUE[1], true);
                SubcommandData colorHEX = new SubcommandData(STATICS.SET_GROUP_COLOR_SUB_HEX[0],
                                STATICS.SET_GROUP_COLOR_SUB_HEX[1]);
                colorHEX.addOption(OptionType.STRING, STATICS.SET_GROUP_COLOR_SUB_HEX_OPTION[0],
                                STATICS.SET_GROUP_COLOR_SUB_HEX_OPTION[1], true);
                // TODO colorRemove
                color.addSubcommands(colorRGB, colorHEX);

                commandData.addSubcommands(admin);
                commandData.addSubcommandGroups(targets, music, color);
        }

        @Override
        public void eventHandler(SlashCommandInteractionEvent event) {
                GuildConfig config = GuildManager.getConfig(event.getGuild());
                String subcommandName = event.getSubcommandName();
                String subCommandGroup = event.getSubcommandGroup();
                String msg = "";
                String commandName = "";
                if (event.getSubcommandGroup() != null) {
                        commandName = name + " " + subCommandGroup + " " + subcommandName;
                        if (subCommandGroup.equals(STATICS.SET_GROUP_TARGETS[0])) {
                                msg = targetHandling(event, config);
                        } else if (subCommandGroup.equals(STATICS.SET_GROUP_MUSIC[0])) {
                                msg = musicHandling(event, config);
                        } else if (subCommandGroup.equals(STATICS.SET_GROUP_COLOR[0])) {
                                msg = colorHandling(event, config);
                        }
                } else {
                        commandName = name + " " + subcommandName;
                }
                if (subcommandName.equals(STATICS.SET_SUB_ADMIN[0])) {
                        // set admin role id in config
                        Role adminRole = event.getOption(STATICS.SET_SUB_ADMIN_OPTION[0]).getAsRole();
                        config.setAdminRole(adminRole.getIdLong());
                        msg = "adminRole has been set to " + adminRole.getAsMention();
                }
                MessageHelper.replyCommandLogged(event.getMember(), event.deferReply(), false, commandName,
                                msg);
        }

        public String targetHandling(SlashCommandInteractionEvent event, GuildConfig config) {
                String msg = "";
                VoiceChannel vc;
                String subcommandName = event.getSubcommandName();
                if (subcommandName.equals(STATICS.SET_GROUP_TARGETS_SUB_ADD[0])) {
                        // set target voiceChannels in config
                        vc = event.getOption(STATICS.SET_GROUP_TARGETS_SUB_ADD_OPTION_CHANNEL[0]).getAsChannel().asVoiceChannel();
                        String name = event.getOption(STATICS.SET_GROUP_TARGETS_SUB_ADD_OPTION_NAME[0]).getAsString();
                        config.setTargetVoiceChannel(vc.getIdLong(), name);
                        msg = vc.getAsMention()
                                        + " has been set as a targetVoiceChannel for dynamicVoiceChannel creation.\nThe dynamic channels will be named: "
                                        + name;
                } else if (subcommandName.equals(STATICS.SET_GROUP_TARGETS_SUB_REMOVE[0])) {
                        vc = event.getOption(STATICS.SET_GROUP_TARGETS_SUB_REMOVE_OPTION[0]).getAsChannel().asVoiceChannel();
                        // check if channel is a target channel
                        if (config.isTargetChannel(vc.getIdLong())) {
                                // remove target channel in config
                                config.removeTargetVoiceChannel(vc.getIdLong());
                                // delete target channel
                                vc.delete().queue();
                                msg = "target channel has been successfully removed";
                        } else {
                                msg = "selected channel is not a target channel";
                        }
                }
                return msg;
        }

        public String musicHandling(SlashCommandInteractionEvent event, GuildConfig config) {
                String subcommandName = event.getSubcommandName();
                String msg = "";
                if (subcommandName.equals(STATICS.SET_GROUP_MUSIC_SUB_SET[0])) {
                        // set music channel id in config
                        TextChannel tc = event.getOption(STATICS.SET_GROUP_MUSIC_SUB_SET_OPTION[0]).getAsChannel().asTextChannel();
                        config.setMusicLogChannel(tc.getIdLong());
                        msg = "musicLogChannel has been set to " + tc.getAsMention();
                } else if (subcommandName.equals(STATICS.SET_GROUP_MUSIC_SUB_REMOVE[0])) {
                        long id = config.getMusicLogChannel();
                        if (id != -1) {
                                event.getGuild().getTextChannelById(id).delete().queue();
                                config.removeMusicLogChannel();
                                msg = "musicLogChannel has been deleted";
                        } else {
                                msg = "there is no musicLogChannel to delete";
                        }
                }
                return msg;
        }

        public String colorHandling(SlashCommandInteractionEvent event, GuildConfig config) {
                String subcommandName = event.getSubcommandName();
                int red = 0, green = 0, blue = 0;
                Color color;
                // RGB
                if (subcommandName.equals(STATICS.SET_GROUP_COLOR_SUB_RGB[0])) {
                        red = event.getOption(STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_RED[0]).getAsInt();
                        green = event.getOption(STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_GREEN[0]).getAsInt();
                        blue = event.getOption(STATICS.SET_GROUP_COLOR_SUB_RGB_OPTION_BLUE[0]).getAsInt();
                } // HEX
                else if (subcommandName.equals(STATICS.SET_GROUP_COLOR_SUB_HEX[0])) {
                        String hex = event.getOption(STATICS.SET_GROUP_COLOR_SUB_HEX_OPTION[0]).getAsString();
                        color = Color.decode(hex);
                        red = color.getRed();
                        green = color.getGreen();
                        blue = color.getBlue();
                }
                config.setColor(red, green, blue);
                color = new Color(red, green, blue);
                return "color for embedded messages has been set to #"
                                + Integer.toHexString(color.getRGB()).substring(2);

        }

}
