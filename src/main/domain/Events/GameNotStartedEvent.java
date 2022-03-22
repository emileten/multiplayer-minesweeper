package main.domain.Events;

public class GameNotStartedEvent implements Event {

	private final String message = "The game has not started";
	
	public String toString() {
		return this.message;
	}

}