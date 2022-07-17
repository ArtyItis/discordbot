package com.github.artyitis.discordbot.music;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.entities.Guild;

/**
 * The Class MusicHelper.
 */
public class MusicHelper {

	/** The instance. */
	private static MusicHelper instance;

	/**
	 * Gets the single instance of MusicHelper.
	 *
	 * @return single instance of MusicHelper
	 */
	public static MusicHelper getInstance() {
		if (instance == null)
			instance = new MusicHelper();
		return instance;
	}

	/** The music managers. */
	private final Map<Long, GuildMusicManager> musicManagers;

	/** The player manager. */
	private final AudioPlayerManager playerManager;

	/**
	 * Instantiates a new music helper.
	 */
	private MusicHelper() {
		musicManagers = new HashMap<>();
		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	/**
	 * Gets the audio player.
	 *
	 * @param guild the guild
	 * @return the audio player
	 */
	public AudioPlayer getAudioPlayer(Guild guild) {
		final GuildMusicManager musicManager = getGuildAudioPlayer(guild);
		return musicManager.player;
	}

	/**
	 * Gets the guild audio player.
	 *
	 * @param guild the guild
	 * @return the guild audio player
	 */
	public synchronized GuildMusicManager getGuildAudioPlayer(final Guild guild) {
		GuildMusicManager musicManager = musicManagers.get(guild.getIdLong());
		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guild.getIdLong(), musicManager);
		}
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		return musicManager;
	}

	public AudioPlayerManager getAudioPlayerManager() {
		return playerManager;
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		playerManager.shutdown();
		musicManagers.clear();
	}
}
