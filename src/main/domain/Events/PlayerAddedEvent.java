package main.domain.Events;


public class PlayerAddedEvent implements Event {
	
	private final String message = "Player added!";
	
	public String toString() {
		return this.message;
	}


}
