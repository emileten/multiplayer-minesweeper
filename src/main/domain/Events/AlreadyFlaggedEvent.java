package main.domain.Events;

public class AlreadyFlaggedEvent implements Event {

	private final String message = "Already flagged !";
	
	public String toString() {
		return this.message;
	}


}
