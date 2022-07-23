package com.github.artyitis.discordbot.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/**
 * This class schedules tracks for the audio player. It contains the queue of
 * tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

	/** The player. */
	private final AudioPlayer player;

	/** The queue. */
	private final LinkedBlockingDeque<AudioTrack> queue;

	/**
	 * Instantiates a new track scheduler.
	 *
	 * @param player The audio player this scheduler uses
	 */
	public TrackScheduler(final AudioPlayer player) {
		this.player = player;
		queue = new LinkedBlockingDeque<>();
	}

	/**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track The track to play or add to queue.
	 */
	public void queue(final AudioTrack track) {
		// Calling startTrack with the noInterrupt set to true will start the track only
		// if nothing is currently playing. If
		// something is playing, it returns false and does nothing. In that case the
		// player was already playing so this
		// track goes to the queue instead.
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	/**
	 * Start the next track, stopping the current one if it is playing.
	 */
	public void nextTrack() {
		// Start the next track, regardless of if something is already playing or not.
		// In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply stop the
		// player.
		player.startTrack(queue.poll(), false);
	}

	/**
	 * Add track on top of queue.
	 *
	 * @param track the track
	 */
	public void addOnTopOfQueue(final AudioTrack track, boolean noInterrupt) {
		if (!player.startTrack(track, noInterrupt))
			queue.offerFirst(track);
	}

	/**
	 * Add playlist on top of queue.
	 *
	 * @param playlist the playlist
	 */
	public void addOnTopOfQueue(final List<AudioTrack> playlist, boolean noInterrupt) {
		for (int i = playlist.size() - 1; i > -1; i--) {
			queue.offerFirst(playlist.get(i));
		}
		player.startTrack(queue.poll(), noInterrupt);
	}

	/**
	 * Adds the to queue.
	 *
	 * @param playlist the playlist
	 */
	public void addToQueue(final List<AudioTrack> playlist) {
		for (int i = playlist.size() - 1; i > -1; i--) {
			queue.offer(playlist.get(i));
		}
		player.startTrack(queue.poll(), true);
	}

	/**
	 * Clear queue.
	 */
	public void clearQueue() {
		queue.clear();
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}

	/**
	 * On player pause.
	 *
	 * @param player the player
	 */
	@Override
	public void onPlayerPause(final AudioPlayer player) {
		// Player was paused
		player.setPaused(true);
	}

	/**
	 * On player resume.
	 *
	 * @param player the player
	 */
	@Override
	public void onPlayerResume(final AudioPlayer player) {
		// Player was resumed
		player.setPaused(false);

	}

	/**
	 * On track end.
	 *
	 * @param player    the player
	 * @param track     the track
	 * @param endReason the end reason
	 */
	@Override
	public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason) {
		// Only start the next track if the end reason is suitable for it (FINISHED or
		// LOAD_FAILED)
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	/**
	 * Shuffle.
	 */
	public void shuffle() {
		final ArrayList<AudioTrack> tmp = new ArrayList<>();
		queue.drainTo(tmp);
		Collections.shuffle(tmp);
		queue.addAll(tmp);
	}
}
