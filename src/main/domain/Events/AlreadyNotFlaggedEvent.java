package main.domain.Events;

public class AlreadyNotFlaggedEvent implements Event {

	private final String message = "Already not flagged!";
	
	public String toString() {
		return this.message;
	}


}
