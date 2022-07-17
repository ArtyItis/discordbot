package com.github.artyitis.discordbot.utilities;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import com.github.artyitis.json.JSONFile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class GuildConfig extends JSONFile {
    private Guild guild;
    private static String directoryPath = "resources" + File.separator + "guildconfigs" + File.separator;

    public GuildConfig(Guild guild) {
        super(directoryPath, guild.getId());
        this.guild = guild;
    }

    // SET---------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void setOwner(Member owner) {
        JSONObject ownerJSON = new JSONObject();
        ownerJSON.put("id", owner.getIdLong());
        ownerJSON.put("name", owner.getUser().getName());
        put("owner", ownerJSON, true);
    }

    public void setName(String name) {
        put("name", name, true);
    }

    public void setMusicLogChannel(long id) {
        put("musicLogChannel", id, true);
    }

    public void setAdminRole(long id) {
        put("adminRole", id, true);
    }

    @SuppressWarnings("unchecked")
    public void setColor(long red, long green, long blue) {
        JSONObject color = new JSONObject();
        color.put("red", red);
        color.put("green", green);
        color.put("blue", blue);
        put("color", color, true);
    }

    @SuppressWarnings("unchecked")
    public void setRedditGalleryCategory(long id) {
        JSONObject redditgalleriesJSON = new JSONObject();
        redditgalleriesJSON.put("id", id);
        redditgalleriesJSON.put("subreddits", new JSONArray());
        put("redditgalleries", redditgalleriesJSON, true);
    }

    @SuppressWarnings("unchecked")
    public void setRedditGallery(String subreddit, long id) {
        JSONObject redditgalleriesJSON = (JSONObject) root.get("redditgalleries");
        JSONArray subreddits = (JSONArray) redditgalleriesJSON.get("subreddits");
        JSONObject gallery = new JSONObject();
        gallery.put("name", subreddit);
        gallery.put("id", id);
        subreddits.add(gallery);
        redditgalleriesJSON.put("subreddits", subreddits);
        put("redditgalleries", redditgalleriesJSON, true);
    }

    @SuppressWarnings("unchecked")
    public void setTargetVoiceChannel(long id, String dynamicName) {
        Object o = root.get("targetVoiceChannels");
        // if o exists return o as JSONArray else create JSONArray()
        JSONArray targetVoiceChannels = o == null ? new JSONArray() : (JSONArray) o;
        // create entry for array
        JSONObject targetVoiceChannel = new JSONObject();
        targetVoiceChannel.put("id", id);
        targetVoiceChannel.put("dynamicName", dynamicName);
        // put entry in array
        targetVoiceChannels.add(targetVoiceChannel);
        // save array
        put("targetVoiceChannels", targetVoiceChannels, true);
    }

    // ADD---------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void addDynamicVoiceChannel(long id) {
        Object o = root.get("dynamicVoiceChannels");
        // if o exists return o as JSONArray else create JSONArray()
        JSONArray dynamicVoiceChannels = o == null ? new JSONArray() : (JSONArray) o;
        dynamicVoiceChannels.add(id);
        put("dynamicVoiceChannels", dynamicVoiceChannels, true);
    }

    // GET---------------------------------------------------------------------
    public Member getOwner() {
        JSONObject ownerJSON = (JSONObject) root.get("owner");
        return guild.getMemberById((long) ownerJSON.get("id"));
    }

    public String getName() {
        return (String) root.get("name");
    }

    /**
     * 
     * @return textchannel id as long or -1 if there's no id
     */
    public long getMusicLogChannel() {
        Object o = root.get("musicLogChannel");
        // if o exists return o as long else return -1
        return o == null ? -1 : (long) o;
    }

    /**
     * 
     * @return id of configurated adminrole OR -1 if no role has been set
     */
    public long getAdminRole() {
        Object o = root.get("adminRole");
        // if o exists return o as long else return -1
        return o == null ? -1 : (long) o;
    }

    /**
     * 
     * @return saved color || null
     */
    public Color getColor() {
        JSONObject color = (JSONObject) root.get("color");
        if (color != null) {
            long red = (long) color.get("red");
            long green = (long) color.get("green");
            long blue = (long) color.get("blue");
            return new Color((int) red, (int) green, (int) blue);
        }
        return null;
    }

    public long getRedditGalleryCategory() {
        JSONObject o = ((JSONObject) (root.get("redditgalleries")));
        // if o exists return o as long else return -1
        return o == null ? -1 : (long) o.get("id");
    }

    public JSONArray getGalleries() {
        if (root.get("redditgalleries") == null)
            return null;

        Object o = ((JSONObject) (root.get("redditgalleries"))).get("subreddits");
        // if o exists return o as JSONArray else return null
        return o == null ? null : (JSONArray) o;
    }

    public String getDynamicNameByTargetChannelId(long id) {
        JSONArray targetVoiceChannels = (JSONArray) root.get("targetVoiceChannels");
        if (targetVoiceChannels == null) {
            return null;
        }
        JSONObject targetVoiceChannel;
        for (Object o : targetVoiceChannels) {
            targetVoiceChannel = (JSONObject) o;
            if ((long) targetVoiceChannel.get("id") == id) {
                return (String) targetVoiceChannel.get("dynamicName");
            }
        }
        return null;
    }

    public ArrayList<Long> getDynamicVoiceChannels() {
        JSONArray dynamicVoiceChannels = (JSONArray) root.get("dynamicVoiceChannels");
        if (dynamicVoiceChannels != null && dynamicVoiceChannels.size() > 0) {
            ArrayList<Long> result = new ArrayList<>();
            for (int i = 0; i < dynamicVoiceChannels.size(); i++) {
                result.add((long) dynamicVoiceChannels.get(i));
            }
            return result;
        }
        return null;
    }

    // REMOVE---------------------------------------------------------------------
    public void removeMusicLogChannel() {
        root.remove("musicLogChannel");
        save();
    }

    public void removeTargetVoiceChannel(long id) {
        JSONArray targetVoiceChannels = (JSONArray) root.get("targetVoiceChannels");
        JSONObject targetVoiceChannel;
        for (Object o : targetVoiceChannels) {
            targetVoiceChannel = (JSONObject) o;
            if ((long) targetVoiceChannel.get("id") == id) {
                targetVoiceChannels.remove(targetVoiceChannel);
                break;
            }
        }
        // save array
        put("targetVoiceChannels", targetVoiceChannels, true);
    }

    public void removeDynamicVoiceChannel(long id) {
        JSONArray dynamicVoiceChannels = (JSONArray) root.get("dynamicVoiceChannels");
        dynamicVoiceChannels.remove(id);
        put("dynamicVoiceChannels", dynamicVoiceChannels, true);
    }

    @SuppressWarnings("unchecked")
    public void removeGallery(long id) {
        JSONObject redditgalleries = (JSONObject) (root.get("redditgalleries"));
        JSONArray subreddits = (JSONArray) redditgalleries.get("subreddits");
        JSONObject subreddit;
        for (Object object : subreddits) {
            subreddit = (JSONObject) object;
            if ((long) subreddit.get("id") == id) {
                subreddits.remove(subreddit);
                break;
            }
        }
        // save changes
        redditgalleries.put("subreddits", subreddits);
        put("redditgalleries", redditgalleries, true);
    }

    // IS---------------------------------------------------------------------
    public boolean isTargetChannel(long id) {
        JSONArray targetVoiceChannels = (JSONArray) root.get("targetVoiceChannels");
        if (targetVoiceChannels == null) {
            return false;
        }
        JSONObject targetVoiceChannel;
        for (Object o : targetVoiceChannels) {
            targetVoiceChannel = (JSONObject) o;
            if ((long) targetVoiceChannel.get("id") == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isDynamicVoiceChannel(long id) {
        JSONArray dynamicVoiceChannels = (JSONArray) root.get("dynamicVoiceChannels");
        return dynamicVoiceChannels != null && dynamicVoiceChannels.size() > 0 ? dynamicVoiceChannels.contains(id)
                : false;
    }

    public void save() {
        super.save();
    }
}
