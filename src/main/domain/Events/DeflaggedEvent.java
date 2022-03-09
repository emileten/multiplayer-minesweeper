package main.domain.Events;

public class DeflaggedEvent implements Event {

	private final String message = "Deflagged!";
	
	public String toString() {
		return this.message;
	}


}
