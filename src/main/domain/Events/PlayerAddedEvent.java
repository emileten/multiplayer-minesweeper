package main.domain.Events;

import main.domain.Game.GameProtocol;

public class PlayerAddedEvent implements Event {
	
	private final String message = "Player added!" + GameProtocol.getHumanReadablePlayerProtocol();
	
	public String toString() {
		return this.message;
	}


}
