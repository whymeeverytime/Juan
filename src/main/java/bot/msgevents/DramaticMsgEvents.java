package bot.msgevents;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class DramaticMsgEvents implements MessageCreateListener {

	DiscordApi api;

	public DramaticMsgEvents(DiscordApi api) {
		this.api = api;
	}

	public void onMessageCreate(MessageCreateEvent event) {
		TextChannel temp;
		temp = event.getChannel();

		if (event.getMessageContent().equals("j! terminate")
				&& event.getMessageAuthor().getIdAsString().equals("335426951361593345")) {
			temp.sendMessage("What have you done!");
			try {
				Thread.sleep(2500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			temp.sendMessage("Termination Completed!");
			System.exit(0);
		}
	}
}
