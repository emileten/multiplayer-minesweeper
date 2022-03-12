package main.domain.Events;

public class UnSupportedPlayerActionEvent implements Event {

	private final String message = "Unknown action";
	
	public String toString() {
		return this.message;
	}

}
