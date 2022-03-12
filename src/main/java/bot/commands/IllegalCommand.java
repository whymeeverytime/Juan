package bot.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class IllegalCommand implements CommandExecutor {

	MessageBuilder message;

	public IllegalCommand(MessageBuilder message) {
		this.message = message;
	}

	@Command(aliases = {"illegal"}, description = "Responds with illegal meme.", usage = "j!illegal")
	public void onIllegalCommand(String[] args, Message commandMessage, TextChannel channel) {
		if (args.length > 0) {
			new MessageBuilder().append("There is too many arguments. Juan is confused.").send(channel);
		} else {
			commandMessage.delete();
			message.send(channel);
		}
	}
}
