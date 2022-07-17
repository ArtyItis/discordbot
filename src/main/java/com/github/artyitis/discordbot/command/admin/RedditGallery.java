package com.github.artyitis.discordbot.command.admin;

import java.io.IOException;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.utilities.GuildConfig;
import com.github.artyitis.discordbot.utilities.GuildManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.github.artyitis.javaredditwrapper.Reddit;
import com.github.artyitis.javaredditwrapper.subreddit.SubReddit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RedditGallery extends CommandAdmin {

    private static final String CATEGORYNAME = "redditgalleries";

    public RedditGallery() {
        super(STATICS.REDDITGALLERY[0], STATICS.REDDITGALLERY[1]);
        // create subCommand
        SubcommandData create = new SubcommandData(STATICS.REDDITGALLERY_SUB_CREATE[0],
                STATICS.REDDITGALLERY_SUB_CREATE[1]);
        create.addOption(OptionType.STRING, STATICS.REDDITGALLERY_SUB_CREATE_OPTION[0],
                STATICS.REDDITGALLERY_SUB_CREATE_OPTION[1], true);
        // delete subCommand
        SubcommandData delete = new SubcommandData(STATICS.REDDITGALLERY_SUB_DELETE[0],
                STATICS.REDDITGALLERY_SUB_DELETE[1]);
        delete.addOption(OptionType.CHANNEL, STATICS.REDDITGALLERY_SUB_DELETE_OPTION[0],
                STATICS.REDDITGALLERY_SUB_DELETE_OPTION[1], true);
        commandData.addSubcommands(create, delete);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws ParseException, IOException {
        String subcommandName = event.getSubcommandName();
        if (subcommandName.equals(STATICS.REDDITGALLERY_SUB_CREATE[0])) {
            create(event);
        } else if (subcommandName.equals(STATICS.REDDITGALLERY_SUB_DELETE[0])) {
            delete(event);
        }
    }

    private void create(SlashCommandInteractionEvent event) throws ParseException, IOException {
        String parameter = event.getOption(STATICS.REDDITGALLERY_SUB_CREATE_OPTION[0]).getAsString();
        Reddit reddit = new Reddit();
        SubReddit subReddit = reddit.getSubReddit(parameter);
        if (subReddit == null) {
            event.reply("the subreddit: " + parameter + " doesn't exist").queue();
            return;
        }
        Guild guild = event.getGuild();
        GuildConfig config = GuildManager.getConfig(guild);
        Category redditgalleriesCategory;
        // look for redditgallery category
        if (guild.getCategoriesByName(CATEGORYNAME, true).size() < 1) {
            // create category
            redditgalleriesCategory = guild.createCategory(CATEGORYNAME).complete();
            // save categoryid in config
            config.setRedditGalleryCategory(redditgalleriesCategory.getIdLong());
        } else {
            // get category via saved id in config
            long id = config.getRedditGalleryCategory();
            if (id == -1) {
                id = guild.getCategoriesByName(CATEGORYNAME, true).get(0).getIdLong();
                config.setRedditGalleryCategory(id);
            }
            redditgalleriesCategory = guild.getCategoryById(id);
        }
        // create textchannel
        TextChannel textChannel = redditgalleriesCategory.createTextChannel(parameter).complete();
        // mark channel as nsfw if necessary
        if (subReddit.isOver18()) {
            textChannel.getManager().setNSFW(true).queue();
        }
        config.setRedditGallery(parameter, textChannel.getIdLong());
        // reload tasks
        GuildManager.getGalleryManager(guild).reloadTasks();

        MessageHelper.replyCommandLogged(event.getMember(), event.deferReply(), false,
                name + " " + STATICS.REDDITGALLERY_SUB_CREATE[0],
                "created gallery for the subreddit: " + parameter);
    }

    private void delete(SlashCommandInteractionEvent event) {
        TextChannel galleryChannel = event.getOption(STATICS.REDDITGALLERY_SUB_DELETE_OPTION[0]).getAsChannel().asTextChannel();
        Guild guild = event.getGuild();
        GuildConfig config = GuildManager.getConfig(guild);
        // contains all gallerynames
        JSONArray array = config.getGalleries();
        // validate input
        boolean validatedInput = false;
        for (Object object : array) {
            JSONObject jo = (JSONObject) object;
            if (galleryChannel.getIdLong() == (Long) jo.get("id")) {
                validatedInput = true;
                break;
            }
        }
        if (validatedInput == false)
            return;
        // stop task -> remove gallery in config & reload tasks
        config.removeGallery(galleryChannel.getIdLong());
        GuildManager.getGalleryManager(guild).reloadTasks();
        // delete textChannel
        galleryChannel.delete().queue();
        MessageHelper.replyCommandLogged(event.getMember(), event.deferReply(), false,
                name + " " + STATICS.REDDITGALLERY_SUB_DELETE[0],
                "deleted gallery for the subreddit: " + galleryChannel.getName());
    }
}
