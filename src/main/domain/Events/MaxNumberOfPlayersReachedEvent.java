package main.domain.Events;

public class MaxNumberOfPlayersReachedEvent implements Event {

	private final String message = "The maximum number of players is already reached !";
	
	public String toString() {
		return this.message;
	}

}