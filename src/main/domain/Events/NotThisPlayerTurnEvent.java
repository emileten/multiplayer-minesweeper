package main.domain.Events;

public class NotThisPlayerTurnEvent implements Event {

	private final String message = "It's another player's turn !";
	
	public String toString() {
		return this.message;
	}

}
