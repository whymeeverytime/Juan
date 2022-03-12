package bot.msgevents;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class GenericMsgEvents implements MessageCreateListener {

	// Variables
	String respond;

	// Constructor
	public GenericMsgEvents(String respond) {
		this.respond = respond;
	}

	// Methods
	public void onMessageCreate(MessageCreateEvent event) {
		if (event.getMessageContent().equalsIgnoreCase("sa")) {
			event.getChannel().sendMessage(respond);
		}
	}

	public void setRespond(String respond) {
		this.respond = respond;
	}
}
