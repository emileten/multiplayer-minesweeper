package main.domain.Events;

public class DugEvent implements Event {

	private final String message = "Dug!";
	
	public String toString() {
		return this.message;
	}

}
