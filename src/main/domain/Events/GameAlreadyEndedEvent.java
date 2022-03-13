package main.domain.Events;

public class GameAlreadyEndedEvent implements Event {

	private final String message = "The game has already ended !";
	
	public String toString() {
		return this.message;
	}

}
