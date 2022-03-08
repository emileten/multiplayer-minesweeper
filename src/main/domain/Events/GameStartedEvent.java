package main.domain.Events;

public class GameStartedEvent implements Event {

	private final String message = "Game Started !";
	
	public String toString() {
		return this.message;
	}

}


