package main.domain.Events;

import main.domain.Game.*;

public class GameStartedEvent implements Event {

	private final String message = "Game Started ! " + GameProtocol.getHumanReadablePlayerProtocol();
	
	public String toString() {
		return this.message;
	}

}


