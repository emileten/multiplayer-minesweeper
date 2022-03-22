package main.domain.Events;

import main.domain.Game.GameProtocol;

public class GameAlreadyStartedEvent implements Event {

	private final String message = "The game has already started ! " + GameProtocol.getHumanReadablePlayerProtocol();;
	
	public String toString() {
		return this.message;
	}

}
