package main.domain.Events;

public class AlreadyDugEvent implements Event {

	private final String message = "Already Dug !";
	
	public String toString() {
		return this.message;
	}


}
