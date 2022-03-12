package bot.mediaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.audio.AudioSourceBase;

public class LavaplayerSource extends AudioSourceBase {

	// Fields
	private final AudioPlayer player;
	private AudioFrame lastFrame;

	// Constructor
	public LavaplayerSource(DiscordApi api, AudioPlayer audioPlayer) {
		super(api);
		this.player = audioPlayer;
	}

	// Methods
	@Override
	public byte[] getNextFrame() {
		if (lastFrame == null) {
			return null;
		}
		return applyTransformers(lastFrame.getData());
	}
	@Override
	public boolean hasNextFrame() {
		return false;
	}
	@Override
	public AudioSource copy() {
		return new LavaplayerSource(getApi(), player);
	}
}
