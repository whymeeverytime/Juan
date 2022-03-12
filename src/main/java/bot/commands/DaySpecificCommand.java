package bot.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.mortbay.jetty.Server;

import java.io.File;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bot.service.DayService;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class DaySpecificCommand implements CommandExecutor {
	// Constants
	final String ERROR_MESSAGE = "**Correct Usage:**\n```\n(Only for authorized users)\n-j!day on|off ```";

	// Fields
	String willSend;
	// TODO add server check
	ArrayList<Server> activeServers;
	boolean active = false;
	ZonedDateTime today = ZonedDateTime.now(ZoneId.of("GMT+3"));
	ZonedDateTime plannedRun = today.withHour(9).withMinute(0).withSecond(0);
	MessageBuilder message = new MessageBuilder().addAttachment(new File("src/main/java/bot/assets/img/cuma.jpg"));
	Duration setOff = Duration.between(today, plannedRun);
	ScheduledExecutorService scheduler;

	// Command method
	@Command(aliases = {
			"day"}, description = "Sends day specific message at the specified day.", usage = "j!day [on|off]")
	public void onDayCommand(String[] args, Message commandMessage, TextChannel channel, Server server,
			DiscordApi api) {

		if (commandMessage.getAuthor().isBotOwner()) {
			// TODO add server check
			if (args.length == 1) {

				if (args[0].equals("on") && !active) {
					willSend = channel.getIdAsString();
					scheduler = Executors.newScheduledThreadPool(1);

					if (today.compareTo(plannedRun) > 0) {
						plannedRun = plannedRun.plusDays(1);
						setOff = Duration.between(today, plannedRun);
					}

					long longSetOff = setOff.getSeconds();

					commandMessage.addReaction("👍");

					scheduler.scheduleAtFixedRate(new DayService(today, willSend, message, api), longSetOff,
							TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
					active = true;

				} else if (args[0].equals("on") && active) {
					commandMessage.addReaction("👎");
					// TODO Create template for this to avoid writing the same thing
					MessageBuilder temp = new MessageBuilder().replyTo(commandMessage.getId())
							.setContent("Already activated :frowning:");
					temp.send(channel).thenAcceptAsync(message1 -> {
						// TODO change to scheduler
						try {
							Thread.sleep(2000);
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
						message1.delete().join();
					});

				} else if (args[0].equals("off") && active) {
					try {
						System.out.println("\nCanceled Threads: \n" + scheduler.shutdownNow());
					} catch (Exception e) {
						e.printStackTrace();
						commandMessage.addReaction("👎");
						return;
					}
					commandMessage.addReaction("👍");
					active = false;

				} else if (args[0].equals("off") && !active) {
					commandMessage.addReaction("👎");
					MessageBuilder temp = new MessageBuilder().replyTo(commandMessage.getId())
							.setContent("Schedules are already empty :frowning:");
					temp.send(channel).thenAcceptAsync(message1 -> {
						// TODO change to scheduler
						try {
							Thread.sleep(2000);
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
						message1.delete().join();
					});
				} else {
					channel.sendMessage(ERROR_MESSAGE);
				}
			} else {
				channel.sendMessage(ERROR_MESSAGE);
			}
		} else {
			commandMessage.addReaction("👎");
			MessageBuilder temp = new MessageBuilder().setContent(commandMessage.getUserAuthor().get().getMentionTag()
					+ "you are not authorized to use that command :cry:");

			temp.send(channel).thenAcceptAsync(message1 -> {
				// TODO change to scheduler
				try {
					Thread.sleep(3850);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				message1.delete().join();
			});
		}
	}
}
