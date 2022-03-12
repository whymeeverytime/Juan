package bot.commands;

import org.javacord.api.AccountUpdater;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;

import java.io.File;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class SuspiciousCommand implements CommandExecutor {
	// Constants
	final String ERROR_MESSAGE = "**Correct Usage:**\n```xml\n-j!sus [copy|default|override]"
			+ " <@mention>\nExample: j!sus copy @bHaktanb"
			+ "\n\nCaution: This command can only be used once in a hour due to Discord ratelimit."
			+ " Don't try to give request over and over again."
			+ "\n(override argument is only for authorized users)```";

	// Fields
	DiscordApi api;
	UserStatus defaultStat;
	AccountUpdater updater;
	User user;
	boolean defaultStatus = true;

	// Constructor
	public SuspiciousCommand(DiscordApi api, UserStatus status) {
		defaultStat = status;
		this.api = api;
		updater = new AccountUpdater(api);
	}

	// Methods
	@Command(aliases = {
			"sus"}, description = "Bot disguises as the mentioned user.", usage = "j!sus [copy|default|override] <@mention>")
	public void onCopyCommand(String[] args, Message commandMessage, TextChannel channel, Server server) {
		if (args.length == 2 && args[0].equals("copy")) {
			user = commandMessage.getMentionedUsers().get(0);
			System.out.println("\nUpdater queue:\n" + updater.setUsername(user.getDisplayName(server)) + "\n"
					+ updater.setAvatar(user.getAvatar()));
			api.updateStatus(user.getStatus());
			updater.update();
			commandMessage.addReaction("👍");
			defaultStatus = false;
		} else if (args.length == 1 && args[0].equals("default") && !defaultStatus) {
			updater.setUsername("juan.");
			updater.setAvatar(new File("src/main/java/bot/assets/img/juan-logo2.jpg"));
			api.updateStatus(defaultStat);
			updater.update();
			commandMessage.addReaction("👍");
		} else if (args.length == 1 && args[0].equals("override") && commandMessage.getAuthor().isBotOwner()) {
			updater.setUsername("juan.");
			updater.setAvatar(new File("src/main/java/bot/assets/img/juan-logo2.jpg"));
			api.updateStatus(defaultStat);
			updater.update();
			commandMessage.addReaction("👍");
		} else {
			channel.sendMessage(ERROR_MESSAGE);
		}
	}
}
