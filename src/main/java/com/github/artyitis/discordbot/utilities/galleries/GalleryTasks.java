package com.github.artyitis.discordbot.utilities.galleries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Logger;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.utilities.GuildConfig;
import com.github.artyitis.discordbot.utilities.GuildManager;
import com.github.artyitis.utilities.LoggerConfig;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.dv8tion.jda.api.entities.Guild;

public class GalleryTasks extends ArrayList<GalleryTask> {

    private Guild guild;
    private static int HOUR = 3600000;
    // private static int SECOND10 = 10000;
    private static Logger logger = LoggerConfig.getLogger(STATICS.REDDIT_LOGGER);
    private Timer timer;

    public GalleryTasks(Guild guild) {
        this.guild = guild;
        timer = new Timer();
    }

    public void reloadTasks() {
        GuildConfig config = GuildManager.getConfig(guild);
        JSONArray galleries = config.getGalleries();
        if (galleries == null)
            return;
        clear();
        for (Object o : galleries) {
            JSONObject gallery = (JSONObject) o;
            GalleryTask galleryTask = new GalleryTask((String) gallery.get("name"),
                    guild.getTextChannelById((Long) gallery.get("id")));
            add(galleryTask);
            logger.info("scheduled galleryTask for " + gallery.get("name"));
            timer.schedule(galleryTask, nextFullHour(), HOUR);
            // timer.schedule(galleryTask, nextFullMinute(), SECOND10);
        }
    }

    @Override
    public void clear() {
        super.clear();
        timer.cancel();
        logger.info("canceled timer and therefore all scheduled tasks.\ninstantiating new timer");
        timer = new Timer();
    }

    /**
     * Next full hour.
     *
     * @return Date today's next full hour
     */
    protected Date nextFullHour() {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Next full minute.
     *
     * @return the date
     */
    protected Date nextFullMinute() {
        final Calendar calendar = Calendar.getInstance();
        final int minute = calendar.get(Calendar.MINUTE) + 1;
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
