package main.domain.Events;

public class GameAlreadyStartedEvent implements Event {

	private final String message = "The game has already started !";
	
	public String toString() {
		return this.message;
	}

}
