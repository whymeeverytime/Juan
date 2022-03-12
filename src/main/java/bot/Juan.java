package bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import bot.commands.*;
import bot.msgevents.DramaticMsgEvents;
import bot.msgevents.GenericMsgEvents;
import bot.service.HarassService;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;

/**
 * Juan main class
 *
 * @author Borga Haktan Bilen
 * @version V.0.7
 */
public final class Juan {
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		// Constants
		final UserStatus DEFAULT_STATUS = UserStatus.ONLINE;
		final String PRE_FIX = "j!";
		final String ONLINE_MESSAGE = "`Juan is up on the balcony waiting for your command.`";
		final String UPDATE_MESSAGE = "`Deployment completed. Bot updated.`";
		final String JOIN_MESSAGE = "Hey There!\n" + "**For information `" + PRE_FIX + "i` or `" + PRE_FIX
				+ "info` or `" + PRE_FIX + "i owner`**";

		// Other fields
		String input; // Console input
		ArrayList<String> illegalLinks = new ArrayList<>();
		illegalLinks.add("PLACEHOLDER");
		Scanner consoleScanner = new Scanner(System.in);
		Scanner fScan = new Scanner(new File("discordtoken.txt"));
		String token = fScan.nextLine();
		fScan.close();

		// To-Do initialize api after the listeners
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

		// Msg event initialization
		HarassService harassService = new HarassService(api);
		harassService.run();

		GenericMsgEvents responder = new GenericMsgEvents("As");
		DramaticMsgEvents dramaticEvent = new DramaticMsgEvents(api);
		MessageBuilder arasKargo = new MessageBuilder().append("Understandable")
				.addAttachment(new File("src/main/java/bot/assets/img/1.jpg"));
		MessageBuilder arasKargo2 = new MessageBuilder().append("Understandable")
				.addAttachment(new File("src/main/java/bot/assets/img/1.jpg"));
		EmbedBuilder youtubeVideo = new EmbedBuilder().setDescription("I won't let you expose to expose me.\n💀🔫🐎")
				.setColor(Color.RED);
		EmbedBuilder youtubeVideo2 = new EmbedBuilder()
				.setDescription("Oh. You don't have any idea what you have done.\nYou will pay for this")
				.setColor(Color.RED);
		MessageBuilder replyMessage = new MessageBuilder();
		MessageBuilder howToReadMeme = new MessageBuilder()
				.addAttachment(new File("src/main/java/bot/assets/img/how-to-read.jpg"));

		// Commands
		CommandHandler comHandler = new JavacordHandler(api);
		comHandler.setDefaultPrefix(PRE_FIX);

		// Command registration
		comHandler.registerCommand(new InfoCommand());
		comHandler.registerCommand(new IllegalCommand(arasKargo));
		comHandler.registerCommand(new DaySpecificCommand());
		comHandler.registerCommand(new SuspiciousCommand(api, DEFAULT_STATUS));
		comHandler.registerCommand(new HelpCommand(comHandler, illegalLinks));
		comHandler.registerCommand(new HarassCommand(harassService));
		// comHandler.registerCommand( new MediaCommand() );

		// Bot Status
		api.updateActivity(ActivityType.PLAYING, "For info: j!help or j!i");
		api.updateStatus(DEFAULT_STATUS);

		// Console status outputs
		System.out.println("Bot has been initialized");
		System.out.println(api.getServers());

		// Listeners - Will create commands for most of them (performance related
		// issues)
		api.addMessageCreateListener(dramaticEvent);
		api.addMessageCreateListener(responder);

		api.addReactionAddListener(reactionEvent -> {
			if (reactionEvent.getEmoji().equalsEmoji("⛓️") && (reactionEvent.getCount().get() == 1)) {
				arasKargo2.replyTo(reactionEvent.getMessage().get()).send(reactionEvent.getChannel());
			} else if (reactionEvent.getEmoji().getMentionTag().equals("<:badassthonk:798961432955846726>")
					&& (reactionEvent.getCount().get() == 1)) {
				howToReadMeme.replyTo(reactionEvent.getMessage().get()).send(reactionEvent.getChannel());
			}
		});

		api.addMessageCreateListener((MessageCreateEvent event) -> {
			if (event.getMessageContent().contains(illegalLinks.get(0)) && !event.getMessageAuthor().isBotUser()) {
				int randomInt;
				randomInt = (int) (Math.random() * (10) + 1);

				if (randomInt >= 5) {
					replyMessage.setEmbed(youtubeVideo);
				} else {
					replyMessage.setEmbed(youtubeVideo2);
				}

				replyMessage.replyTo(event.getMessage()).send(event.getChannel());
				event.getMessage().delete();
			}
		});

		// Server join message
		api.addServerJoinListener(event -> {
			Server server = event.getServer();
			try {
				Thread.sleep(800);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			if (server.getSystemChannel().isPresent()) {
				server.getSystemChannel().get().sendMessage(ONLINE_MESSAGE);
			} else if (!server.getTextChannelsByNameIgnoreCase("bot-log").isEmpty()) {
				server.getTextChannelsByNameIgnoreCase("bot-log").iterator().next().sendMessage(JOIN_MESSAGE);
			} else if (!server.getTextChannelsByNameIgnoreCase("genel").isEmpty()) {
				server.getTextChannelsByNameIgnoreCase("genel").iterator().next().sendMessage(JOIN_MESSAGE);
			} else if (!server.getTextChannelsByNameIgnoreCase("general").isEmpty()) {
				server.getTextChannelsByNameIgnoreCase("general").iterator().next().sendMessage(JOIN_MESSAGE);
			}
			System.out.println(api.getServers());
		});

		// Console interface
		System.out.print("\nAdmin commands:");
		do {
			input = consoleScanner.nextLine();
			switch (input) {
				case "invite" :
					invite(token, api);
					break;
				case "online" :
					// Print the online status to every server
					for (Server server : api.getServers()) {
						if (server.getSystemChannel().isPresent()) {
							server.getSystemChannel().get().sendMessage(ONLINE_MESSAGE);
						} else if (!server.getTextChannelsByNameIgnoreCase("bot-log").isEmpty()) {
							server.getTextChannelsByNameIgnoreCase("bot-log").iterator().next()
									.sendMessage(ONLINE_MESSAGE);
						} else if (!server.getTextChannelsByNameIgnoreCase("genel").isEmpty()) {
							server.getTextChannelsByNameIgnoreCase("genel").iterator().next()
									.sendMessage(ONLINE_MESSAGE);
						} else if (!server.getTextChannelsByNameIgnoreCase("general").isEmpty()) {
							server.getTextChannelsByNameIgnoreCase("general").iterator().next()
									.sendMessage(ONLINE_MESSAGE);
						}
					}
					break;
				case "update" :
					// Update message
					for (Server server : api.getServers()) {
						if (server.getSystemChannel().isPresent()) {
							server.getSystemChannel().get().sendMessage(UPDATE_MESSAGE);
						} else if (!server.getTextChannelsByNameIgnoreCase("bot-log").isEmpty()) {
							server.getTextChannelsByNameIgnoreCase("bot-log").iterator().next()
									.sendMessage(UPDATE_MESSAGE);
						} else if (!server.getTextChannelsByNameIgnoreCase("genel").isEmpty()) {
							server.getTextChannelsByNameIgnoreCase("genel").iterator().next()
									.sendMessage(UPDATE_MESSAGE);
						} else if (!server.getTextChannelsByNameIgnoreCase("general").isEmpty()) {
							server.getTextChannelsByNameIgnoreCase("general").iterator().next()
									.sendMessage(UPDATE_MESSAGE);
						}
					}
					break;
			}
		} while (!input.equals("exit"));

		consoleScanner.close();
	}

	/**
	 * Creates invite link
	 *
	 * @param token
	 *            Bot's api token
	 */
	private static void invite(String token, DiscordApi api) {
		// Permissions are future-proof
		org.javacord.api.entity.permission.Permissions perms = new PermissionsBuilder()
				.setAllowed(PermissionType.ADD_REACTIONS).setAllowed(PermissionType.ATTACH_FILE)
				.setAllowed(PermissionType.CONNECT).setAllowed(PermissionType.EMBED_LINKS)
				.setAllowed(PermissionType.MANAGE_CHANNELS).setAllowed(PermissionType.MANAGE_MESSAGES)
				.setAllowed(PermissionType.MENTION_EVERYONE).setAllowed(PermissionType.MANAGE_WEBHOOKS)
				.setAllowed(PermissionType.READ_MESSAGES).setAllowed(PermissionType.READ_MESSAGE_HISTORY)
				.setAllowed(PermissionType.SEND_MESSAGES).setAllowed(PermissionType.USE_EXTERNAL_EMOJIS).build();

		System.out.println(api.createBotInvite(perms));
	}
}
