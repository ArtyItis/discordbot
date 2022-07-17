package com.github.artyitis.discordbot.music;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

/**
 * This is a wrapper around AudioPlayer which makes it behave as an
 * AudioSendHandler for JDA. As JDA calls canProvide before every call to
 * provide20MsAudio(), we pull the frame in canProvide() and use the frame we
 * already pulled in provide20MsAudio().
 */
public class AudioPlayerSendHandler implements AudioSendHandler {

	/** The audio player. */
	private final AudioPlayer audioPlayer;

	/** The buffer. */
	private final ByteBuffer buffer;

	/** The frame. */
	private final MutableAudioFrame frame;

	/**
	 * Instantiates a new audio player send handler.
	 *
	 * @param audioPlayer Audio player to wrap.
	 */
	public AudioPlayerSendHandler(final AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		buffer = ByteBuffer.allocate(1024);
		frame = new MutableAudioFrame();
		frame.setBuffer(buffer);
	}

	/**
	 * Can provide.
	 *
	 * @return true, if successful
	 */
	@Override
	public boolean canProvide() {
		// returns true if audio was provided
		return audioPlayer.provide(frame);
	}

	/**
	 * Checks if is opus.
	 *
	 * @return true, if is opus
	 */
	@Override
	public boolean isOpus() {
		return true;
	}

	/**
	 * Provide 20 ms audio.
	 *
	 * @return the byte buffer
	 */
	@Override
	public ByteBuffer provide20MsAudio() {
		// flip to make it a read buffer
		((Buffer) buffer).flip();
		return buffer;
	}
}