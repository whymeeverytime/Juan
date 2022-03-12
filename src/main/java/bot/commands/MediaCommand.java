package bot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import bot.mediaplayer.LavaplayerSource;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class MediaCommand implements CommandExecutor {
	@Command(aliases = {"m", "music",
			"media"}, description = "**WORK IN PROGRESS**Plays media on a channel on demand.", usage = "j!m [connect|url|leave]")
	public void onMediaCommand(String[] args, User user, TextChannel tChannel, DiscordApi api, Server server) {
		AudioPlayer player;
		AudioSource source;

		ServerVoiceChannel vChannel = server.getVoiceChannelById("833986892844105762").get();
		AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

		playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		player = playerManager.createPlayer();
		source = new LavaplayerSource(api, player);

		if (args.length == 1) {
			switch (args[0]) {
				case "connect" :
					vChannel.connect();
					break;
				case "leave" :
					tChannel.sendMessage("Good Bye " + "👋");
					break;
				case "as" :
					vChannel.connect().thenAccept(audioConnection -> {
						audioConnection.setAudioSource(source);
						playerManager.loadItem("https://www.youtube.com/watch?v=low6Coqrw9Y",
								new AudioLoadResultHandler() {
									@Override
									public void trackLoaded(AudioTrack track) {
										player.playTrack(track);
									}

									@Override
									public void playlistLoaded(AudioPlaylist playlist) {
										for (AudioTrack track : playlist.getTracks()) {
											player.playTrack(track);
										}
									}

									@Override
									public void noMatches() {
										// Notify the user that we've got nothing
									}

									@Override
									public void loadFailed(FriendlyException throwable) {
										// Notify the user that everything exploded
									}
								});
					});
					break;
			}
		} else {
			tChannel.sendMessage("There is too many arguments. Juan is confused.");
		}
	}
}
