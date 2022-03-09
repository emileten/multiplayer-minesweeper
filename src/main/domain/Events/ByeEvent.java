package main.domain.Events;

import main.domain.Players.Player;

public class ByeEvent implements Event {

	private final String message;
	
	public ByeEvent(Player player) {
		this.message = "Bye bye " + player.toString();
	}
	
	public String toString() {
		return this.message;
	}

}
