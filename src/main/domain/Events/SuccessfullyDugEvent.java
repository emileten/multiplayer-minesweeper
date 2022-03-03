package main.domain.Events;

public class SuccessfullyDugEvent implements Event {

	private final String message; 
	
	public SuccessfullyDugEvent(String message) {
		this.message = message;
	}
	@Override
	public String getMessage() {
		String out = new String(this.message);
		return out;
	}

}
