package com.github.artyitis.discordbot;

public class STATICS {
    public static String REDDIT_LOGGER = "reddit";
    /** COMMAND TEXTS 
     * { "name", "description" }
     * {    0  ,        1      }
     * name cannot include spaces or uppercase letters!
    */
                  
    // ADMIN-------------------------------------------------------------------------------------------
    public static final String[] CLEAR = { "clear", "clear the last messages from this chat" };
    public static final String[] CLEAR_OPTION = { "amount", "How many messages to prune (Default 100)" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] REDDITGALLERY = { "redditgallery", "placeholder" };

    public static final String[] REDDITGALLERY_SUB_CREATE = { "create", "create a redditgallery" };
    public static final String[] REDDITGALLERY_SUB_CREATE_OPTION = { "subreddit", "define a subreddit" };

    public static final String[] REDDITGALLERY_SUB_DELETE = { "delete", "delete a redditgallery" };
    public static final String[] REDDITGALLERY_SUB_DELETE_OPTION = { "gallery", "select one of these" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] SET = { "set", "configurate the bot" };

    public static final String[] SET_SUB_ADMIN = { "admin", "roles" };
    public static final String[] SET_SUB_ADMIN_OPTION = { "admin-role", "role desc" };

    public static final String[] SET_GROUP_MUSIC = { "music", "configure music log" };

    public static final String[] SET_GROUP_MUSIC_SUB_SET = { "channel", "set channel as music log" };
    public static final String[] SET_GROUP_MUSIC_SUB_SET_OPTION = { "music-channel", "select a textchannel where music commands can be logged" };

    public static final String[] SET_GROUP_MUSIC_SUB_REMOVE = { "remove", "remove channel as music log" };

    public static final String[] SET_GROUP_TARGETS = { "target", "configure target voice channels for dynamic channel creation" };
    
    public static final String[] SET_GROUP_TARGETS_SUB_ADD = { "add", "set a channel as a target for dynamic channel creation" };
    public static final String[] SET_GROUP_TARGETS_SUB_ADD_OPTION_CHANNEL = { "target-channel", "has to be a voiceChannel!" };
    public static final String[] SET_GROUP_TARGETS_SUB_ADD_OPTION_NAME = { "dynamic-name", "define the name of the channels that will be created through this one" };

    public static final String[] SET_GROUP_TARGETS_SUB_REMOVE = { "remove", "remove a target channel" };
    public static final String[] SET_GROUP_TARGETS_SUB_REMOVE_OPTION = { "channel", "select a voice channel which has been set as a target channel previously" };

    public static final String[] SET_GROUP_COLOR = { "color", "configure color for all embedded messages from the bot" };

    public static final String[] SET_GROUP_COLOR_SUB_RGB = { "rgb", "set color for all embedded messages from the bot" };
    public static final String[] SET_GROUP_COLOR_SUB_RGB_OPTION_RED = { "red", "red value" };
    public static final String[] SET_GROUP_COLOR_SUB_RGB_OPTION_GREEN = { "green", "green value" };
    public static final String[] SET_GROUP_COLOR_SUB_RGB_OPTION_BLUE = { "blue", "blue value" };

    public static final String[] SET_GROUP_COLOR_SUB_HEX = { "hex", "set color for all embedded messages from the bot" };
    public static final String[] SET_GROUP_COLOR_SUB_HEX_OPTION = { "hex", "hex value" };

    // ------------------------------------------------------------------------------------------------
    public static final String[] SHUTDOWN = { "shutdown", "shutdown discordbot" };
    // GENERAL-----------------------------------------------------------------------------------------
    public static final String[] DYNAMICCHANNEL = { "channel", "customize the dynamic channel you're in" };

    public static final String[] DYNAMICCHANNEL_SUB_NAME = { "name", "rename the channel. This can only be called 2 times within 10 minutes per channel!" };
    public static final String[] DYNAMICCHANNEL_SUB_NAME_OPTION = { "name", "insert new channel name" };

    public static final String[] DYNAMICCHANNEL_SUB_SIZE = { "size", "set the channel size" };
    public static final String[] DYNAMICCHANNEL_SUB_SIZE_OPTION = { "size", "enter a number between 1 and 99" };

    public static final String[] DYNAMICCHANNEL_SUB_LOCK = { "lock", "toggle whether the channel is locked" };
    public static final String[] DYNAMICCHANNEL_SUB_LOCK_OPTION = { "lock", "lock or unlock the channel" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] PING = { "ping", "Pong!" };
    // MUSIC-------------------------------------------------------------------------------------------
    public static final String[] DISCONNECT = { "disconnect", "reset the queue and disconnect the bot" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] PAUSE = { "pause", "pause the current track" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] PLAY = { "play", "play music" };

    public static final String[] PLAY_SUB_QUEUE = { "queue", "queue a track" };
    public static final String[] PLAY_SUB_NEXT = { "next", "play track after the current track" };
    public static final String[] PLAY_SUB_NOW = { "now", "immediate playback of the provided track" };

    public static final String[] PLAY_OPTION = { "youtube-url", "provide a youtube video or playlist to play" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] QUEUE = { "queue", "manage the tracks in the queue" };

    public static final String[] QUEUE_SUB_SHOW = { "show", "post current queue in your DM's" };
    public static final String[] QUEUE_SUB_CLEAR = { "clear", "removes all tracks from the queue" };
    public static final String[] QUEUE_SUB_SHUFFLE = { "shuffle", "shuffle the queue" };
    public static final String[] QUEUE_SUB_SIZE = { "size", "show current queue size" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] RESUME = { "resume", "resume the paused track" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] SKIP = { "skip", "skip current track" };
    // ------------------------------------------------------------------------------------------------
    public static final String[] SOURCE = { "source", "Returns the title of the currently playing song." };


}
