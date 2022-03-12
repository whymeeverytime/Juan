package bot.service;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public class UserService implements Serializable {
	// Properties
	private static final long serialVersionUID = 41L;
	private String id;
	private String name;
	private long photoId;
	private long numberOfMes;

	// Constructor
	public UserService(String id, String name) {
		this.id = id;
		numberOfMes = 0;
		this.name = name;
	}

	// Methods

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public long getNumberOfMes() {
		return numberOfMes;
	}

	public void setNumberOfMes(long numberOfMes) {
		this.numberOfMes = numberOfMes;
	}

	public void incrementMes() {
		numberOfMes++;
	}

	@Override
	public String toString() {
		return name + " Photo Code = " + photoId;
	}
}
