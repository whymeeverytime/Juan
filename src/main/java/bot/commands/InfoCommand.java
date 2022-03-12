package bot.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class InfoCommand implements CommandExecutor {
	/**
	 * Creates random color
	 *
	 * @return Random color
	 */
	private static Color randomColor() {
		Color result;
		double randomInt;
		Random random = new Random();

		randomInt = random.nextInt(10);
		if (randomInt > 5) {
			result = new Color(98, 114, 164);
		} else if (randomInt < 5 && randomInt >= 2) {
			result = new Color(80, 250, 123);
		} else {
			result = new Color(255, 85, 85);
		}

		return result;
	}

	@Command(aliases = {"info",
			"i"}, description = "General information about the bot and the owner.", usage = "j!info [owner]")
	public String onInfoCommand(String[] args, TextChannel channel, Server server, DiscordApi api)
			throws InterruptedException, ExecutionException {
		if (args.length > 1) {
			return "There is too many arguments. Juan is confused.";
		} else if (args.length == 0) {
			EmbedBuilder info = new EmbedBuilder().setAuthor(api.getUserById("833967657123840020").get())
					.setThumbnail(new File("src/main/java/bot/assets/img/juan-logo2.jpg"))
					.setDescription("juan. is a bot with integrity." + " Despite this fact, juan. is under development."
							+ " Thus, some inconsistencies should be expected with its behaviour."
							+ "\n\nFor more information: http://juan.bhaktan.me")
					.setColor(randomColor());
			MessageBuilder message = new MessageBuilder().setEmbed(info);
			message.send(channel);
			channel.sendMessage("**For Commands:** j![help|h|yardım]");
			return "";
		} else if (args.length == 1 && args[0].equals("owner")) {
			String ownerInf = "";
			ownerInf += "`Juan is written, created and maintained by Borga Haktan Bilen`\n";
			ownerInf += "- **Personal Github:** https://github.com/whymeeverytime\n";
			ownerInf += "- **Personal Website:** https://www.bhaktan.me\n";
			// noinspection SpellCheckingInspection
			ownerInf += "- **Personal Discord:** bHaktanb#4168";
			return ownerInf;
		}
		return "Invalid command";
	}
}
