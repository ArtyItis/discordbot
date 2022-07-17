package com.github.artyitis.discordbot.command;

import java.util.ArrayList;

import com.github.artyitis.discordbot.command.admin.Clear;
import com.github.artyitis.discordbot.command.admin.RedditGallery;
import com.github.artyitis.discordbot.command.admin.Set;
import com.github.artyitis.discordbot.command.admin.Shutdown;
import com.github.artyitis.discordbot.command.general.DynamicChannel;
import com.github.artyitis.discordbot.command.general.Ping;
import com.github.artyitis.discordbot.command.music.Disconnect;
import com.github.artyitis.discordbot.command.music.Pause;
import com.github.artyitis.discordbot.command.music.Play;
import com.github.artyitis.discordbot.command.music.Queue;
import com.github.artyitis.discordbot.command.music.Resume;
import com.github.artyitis.discordbot.command.music.Skip;
import com.github.artyitis.discordbot.command.music.Source;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Commandlist extends ArrayList<Command> {

    private static Commandlist INSTANCE;

    private Commandlist() {
        add(new Set());
        add(new Shutdown());
        add(new RedditGallery());
        add(new DynamicChannel());
        add(new Clear());
        add(new Ping());
        // MUSIC COMMANDS
        add(new Disconnect());
        add(new Pause());
        add(new Play());
        add(new Resume());
        add(new Queue());
        add(new Skip());
        add(new Source());
    }

    public static Commandlist getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Commandlist();
        }
        return INSTANCE;
    }

    public ArrayList<CommandData> getCommandDatas() {
        ArrayList<CommandData> result = new ArrayList<>();
        for (Command command : this) {
            result.add(command.getCommandData());
        }
        return result;
    }
}
