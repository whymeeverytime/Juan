package bot.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;

import java.io.IOException;

import bot.service.HarassService;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class HarassCommand implements CommandExecutor {
	// Constants
	private final String ERROR_MESSAGE = "**Correct Usage:**\n```xml\n-j!harass [add|remove <@mention> <photo code>|off|list|files|setth <threshold>|getth] "
			+ "\n\n" + "+ `add|remove <@mention> <photo code>`: Adds or removes the mentioned person to/from the list.\n"
			+ "+ `list`: Shows the list of people in the harassment list.\n" + "+ `off`: Kills the service.\n"
			+ "+ `files`: Shows available files to be used.\n" + "+ `setth <threshold>`: Sets the harassing threshold."
			+ "\n+ `getth`: Prints the harassment threshold." + "\n``` *`list` argument is only for authorized users!*"
			+ "\n\n**Example Usage:** j!harass add @bHaktanb 1";
	private final HarassService service;

	// Constructor
	public HarassCommand(HarassService serivce) {
		this.service = serivce;
	}

	// Command Method
	@Command(aliases = {"harass",
			"har"}, description = "Harrasing the mentioned person.", usage = "j!harass [add|remove <@mention> <photo code>|off|list|files|setth <threshold>|getth]")
	public void onHarassCommand(String[] args, DiscordApi api, TextChannel channel, Message commandMessage) {
		if (args.length == 1 && args[0].equals("off")) {
			if (service.isAdded()) {
				api.removeListener(service);
				commandMessage.addReaction("👍");
			} else {
				channel.sendMessage("Service is already off.");
				commandMessage.addReaction("👎");
			}
		} else if (args.length == 1 && args[0].equals("list")) {
		    if (commandMessage.getAuthor().isBotOwner()) {
                MessageBuilder temp = new MessageBuilder().append("**People waiting to be harrased:**\n");
                temp.append(service.toString());
                temp.send(channel);
            } else {
		        channel.sendMessage("`list` argument is only for authorized users!");
            }
		} else if (args.length == 1 && args[0].equals("files")) {
			channel.sendMessage("Files that are available for harass command:\n" + "```XML\n" + service.getFiles()
					+ "```" + "*For <photo code> only use the file name without the extension.*\n");
			for (MessageBuilder msg : service.filesAsMessageBuilder()) {
				msg.send(channel);
			}
		} else if (args.length == 1 && args[0].equals("getth")) {
			channel.sendMessage("**Harassment Threshold:** " + service.getThreshold());
		} else if (args.length == 2) {

			if (args[0].equals("remove")) {

				User temp = commandMessage.getMentionedUsers().get(0);
				try {
					if (service.removeUser(temp.getIdAsString())) {
						commandMessage.addReaction("👍");
						channel.sendMessage("User removed");
					} else {
						commandMessage.addReaction("👎");
						channel.sendMessage("User is already off the list");
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if (args[0].equals("setth")) {
				try {
					service.setThreshold(Integer.parseInt(args[1]));
					commandMessage.addReaction("👍");
				} catch (NumberFormatException e) {
					channel.sendMessage("Please enter a valid number!");
					commandMessage.addReaction("👎");
				}
			}
		} else if (args.length == 3 && args[0].equals("add")) {
			User temp = commandMessage.getMentionedUsers().get(0);
			try {
				if (service.addUser(temp.getIdAsString(), Long.parseLong(args[2]), temp.getName())) {
					commandMessage.addReaction("👍");
					channel.sendMessage("User added");
				} else {
					commandMessage.addReaction("👎");
					channel.sendMessage("User is already on the list");
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			channel.sendMessage(ERROR_MESSAGE);
		}
	}
}
