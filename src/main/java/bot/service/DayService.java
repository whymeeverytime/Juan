package bot.service;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DayService implements Runnable {
	DateTimeFormatter dateFormatter;
	ZonedDateTime today;
	MessageBuilder message;
	TextChannel channel;

	public DayService(ZonedDateTime today, String channel, MessageBuilder message, DiscordApi api) {
		this.today = today;
		this.message = message;
		if (api.getTextChannelById(channel).isPresent()) {
			this.channel = api.getTextChannelById(channel).get();
		} else {
			throw new NullPointerException("Text Channel Couldn't Fetched in Day Service");
		}
		dateFormatter = DateTimeFormatter.ofPattern("E");
	}

	@Override
	public void run() {
		String dayToday = today.format(dateFormatter);
		if (dayToday.equals("Fri") || dayToday.equals("Friday")) {
			message.send(channel);
		}
	}
}
