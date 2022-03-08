package main.domain.Events;


public class BoomEvent implements Event {
	
	private final String message = "Boom !";
	
	public String toString() {
		return this.message;
	}


}