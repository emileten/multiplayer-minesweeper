package main.domain.Events;

public class NoPlayerInGameEvent implements Event {

	private final String message = "No player in this game !";
	
	public String toString() {
		return this.message;
	}

}
