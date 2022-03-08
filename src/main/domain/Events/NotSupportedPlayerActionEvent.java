package main.domain.Events;

public class NotSupportedPlayerActionEvent implements Event {

	private final String message = "Unknown action";
	
	public String toString() {
		return this.message;
	}

}
