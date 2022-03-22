package main.domain.Events;

public class NoSuchPlayerInGameEvent implements Event {

	private final String message = "This player is not in the game !";
	
	public String toString() {
		return this.message;
	}


}



