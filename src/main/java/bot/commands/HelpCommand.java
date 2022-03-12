package bot.commands;

import java.util.ArrayList;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;

public class HelpCommand implements CommandExecutor {
	// Constants
	private final CommandHandler handler;

	// Fields
	private final ArrayList<String> illegalLinks;

	// Constructor
	public HelpCommand(CommandHandler handler, ArrayList<String> illegalLinks) {
		this.handler = handler;
		this.illegalLinks = illegalLinks;
	}

	// Methods
	@Command(aliases = {"help", "h",
			"yardım"}, async = true, description = "Displays help message.", usage = "j![help|h|yardım]")
	public String onHelpCommand() {
		// Initialize in every call
		StringBuilder finalString = new StringBuilder();

		finalString.append("**Default Prefix:** ").append(handler.getDefaultPrefix());
		finalString.append("\n```xml");
		finalString.append("\n---------------------------------\n");
		finalString.append("| aliases | usage | description |\n");
		finalString.append("---------------------------------");

		for (CommandHandler.SimpleCommand simpleCommand : handler.getCommands()) {
			if (!simpleCommand.getCommandAnnotation().showInHelpPage()) {
				continue;
			}
			finalString.append("\n");
			for (String alias : simpleCommand.getCommandAnnotation().aliases()) {
				finalString.append(alias).append(", ");
			}
			finalString.deleteCharAt(finalString.length() - 2);
			finalString.append(" | ");
			if (!simpleCommand.getCommandAnnotation().usage().isEmpty()) {
				finalString.append(simpleCommand.getCommandAnnotation().usage()).append(" | ");
			}
			if (!simpleCommand.getCommandAnnotation().description().isEmpty()) {
				finalString.append(simpleCommand.getCommandAnnotation().description()).append(" | ");
			}
		}
		finalString.append("\n```\n").append("**Trigger Reactions:**\n");
		finalString.append("- ").append("⛓ | ").append("Aras kargo meme.\n");
		finalString.append("- ").append("<Custom Emoji> | ").append("I don't know how to read meme.\n");
		finalString.append("**Illegal Links:** ");
		for (String link : illegalLinks) {
			finalString.append("\n- ").append(link);
			finalString.insert(finalString.length() - 1, " / ");
		}
		finalString.append("\n\n*Every command will help you if you use it wrong*");
		return finalString.toString();
	}
}
