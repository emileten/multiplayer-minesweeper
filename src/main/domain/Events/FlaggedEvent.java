package main.domain.Events;

public class FlaggedEvent implements Event {

	private final String message = "Flagged!";
	
	public String toString() {
		return this.message;
	}


}
