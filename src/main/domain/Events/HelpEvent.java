package main.domain.Events;

import main.domain.Game.GameProtocol;

public class HelpEvent implements Event {

	private final String message = GameProtocol.getHumanReadableProtocol();
	
	public String toString() {
		return this.message;
	}

}
