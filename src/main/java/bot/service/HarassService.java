package bot.service;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class HarassService implements MessageCreateListener {
	// Constants
	private final String PARENT_DIR = "src/main/java/bot/bin/";
	private final String USER_FILE = "harass.bin";
	private final DiscordApi api;
	private final File USER_FILE_F;
	private final Path DATA_PARENT;
	private final Path USER_FILE_P;

	// Properties
	private int threshold;
	private ArrayList<UserService> users;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	private boolean isAdded;

	// Constructor
	public HarassService(DiscordApi API) {
		isAdded = false;
		this.api = API;
		DATA_PARENT = Paths.get(PARENT_DIR);
		USER_FILE_P = DATA_PARENT.resolve(USER_FILE);
		USER_FILE_F = new File(USER_FILE_P.toString());
		threshold = 5;
	}

	// Methods
	public boolean run() throws IOException, ClassNotFoundException {
		if (Files.exists(DATA_PARENT)) {
			if (Files.exists(USER_FILE_P)) {
				objectInput = new ObjectInputStream(new FileInputStream(USER_FILE_F));
				users = (ArrayList<UserService>) objectInput.readObject();
				objectOutput = new ObjectOutputStream(new FileOutputStream(USER_FILE_F));
			} else {
				Files.createFile(USER_FILE_P);
				users = new ArrayList<UserService>();
				objectOutput = new ObjectOutputStream(new FileOutputStream(USER_FILE_F));
				objectOutput.writeObject(users);
				objectOutput.flush();
				objectInput = new ObjectInputStream(new FileInputStream(USER_FILE_F));
				run();
			}
		} else {
			Files.createDirectory(DATA_PARENT);
			Files.createFile(USER_FILE_P);
			users = new ArrayList<>();
			objectOutput = new ObjectOutputStream(new FileOutputStream(USER_FILE_F));
			objectOutput.writeObject(users);
			objectOutput.flush();
			objectInput = new ObjectInputStream(new FileInputStream(USER_FILE_F));
			run();
		}

		api.addMessageCreateListener(this);

		isAdded = true;
		return true;
	}

	public boolean addUser(String id, long photoId, String name) throws IOException, ClassNotFoundException {
		if (!doesExist(id)) {
			UserService temp = new UserService(id, name);
			temp.setPhotoId(photoId);
			users.add(temp);

			objectOutput.writeObject(users);
			objectOutput.flush();

			return true;
		} else {
			return false;
		}
	}

	public boolean removeUser(String id) throws IOException, ClassNotFoundException {
		if (doesExist(id)) {
			users.remove(getUserInArr(id));
			try {
				objectOutput.writeObject(users);
				objectOutput.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return true;
		} else {
			return false;
		}
	}

	public String getFiles() {
		StringBuilder result = new StringBuilder();

		try {
			Stream<Path> temp = Files.list(Paths.get("src/main/java/bot/assets/img/harass/"));
			temp.forEach(p -> result.append(p.getFileName().toString()).append("\n"));
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Command IOException!";
	}

	public ArrayList<MessageBuilder> filesAsMessageBuilder() {
		ArrayList<MessageBuilder> result = new ArrayList<>();
		MessageBuilder intro = new MessageBuilder().setContent("\nAvailable Files:");
		result.add(intro);

		try {
			Stream<Path> walker = Files.list(Paths.get("src/main/java/bot/assets/img/harass/"));
			walker.forEach(p -> {
				MessageBuilder temp = new MessageBuilder().setContent("\n" + p.getFileName().toString() + ":\n");
				temp.addAttachment(new File("src/main/java/bot/assets/img/harass/" + p.getFileName().toString()));
				result.add(temp);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isAdded() {
		return isAdded;
	}

	private boolean doesExist(String id) {
		if (users.size() != 0) {
			for (UserService user : users) {
				if (user.getId().equals(id)) {
					return true;
				}
			}
		}
		return false;
	}

	private UserService getUserInArr(String id) {
		if (doesExist(id)) {
			for (UserService user : users) {
				if (user.getId().equals(id)) {
					return user;
				}
			}
		}
		return null;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {

		if (!event.getMessageAuthor().isBotUser()) {
			String userId = event.getMessageAuthor().getIdAsString();

			if (doesExist(userId)) {
				UserService current = getUserInArr(userId);

				if (current != null) {
					current.incrementMes();
					if (current.getNumberOfMes() % threshold == 0) {
						if (event.getMessageAuthor().asUser().isPresent()) {
							User discordCurrent = event.getMessageAuthor().asUser().get();
							MessageBuilder message = new MessageBuilder().addAttachment(
									new File("src/main/java/bot/assets/img/harass/" + current.getPhotoId() + ".jpg"));
							message.send(discordCurrent);
						}
					}
					try {
						objectOutput.writeObject(users);
						objectOutput.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return users.size() > 0 ? "```xml\n" + users + "\n```" : "There is no user waiting to be harassed";
	}
}
