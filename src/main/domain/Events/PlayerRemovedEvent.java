package main.domain.Events;


public class PlayerRemovedEvent implements Event {
	
	private final String message = "Player removed!";
	
	public String toString() {
		return this.message;
	}


}
