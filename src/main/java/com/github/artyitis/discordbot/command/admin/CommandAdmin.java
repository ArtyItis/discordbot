package com.github.artyitis.discordbot.command.admin;

import java.util.List;

import com.github.artyitis.discordbot.Secrets;
import com.github.artyitis.discordbot.command.Command;
import com.github.artyitis.discordbot.utilities.GuildConfig;
import com.github.artyitis.discordbot.utilities.GuildManager;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class CommandAdmin extends Command {

    public CommandAdmin(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean isAllowed(SlashCommandInteractionEvent event) {
        GuildConfig config = GuildManager.getConfig(event.getGuild());
        Member member = event.getMember();
        Member owner = config.getOwner();
        // stop execution if member is not the guild owner or an admin
        // check if member is not the owner
        if (member.getIdLong() == owner.getIdLong() || member.getIdLong() == Secrets.DEVELOPER_ID) {
            return true;
        }
        // check if is an admin
        long adminRoleID = config.getAdminRole();
        List<Role> memberRoles = member.getRoles();
        for (Role role : memberRoles) {
            if (adminRoleID == role.getIdLong()) {
                return true;
            }
        }
        return false;

    }

}
