package main.domain.Events;


public class AllDugEvent implements Event {
	
	private final String message = "Congratulations ! Every cell is discovered.";
	
	public String toString() {
		return this.message;
	}


}
