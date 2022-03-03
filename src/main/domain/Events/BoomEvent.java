package main.domain.Events;

public class BoomEvent implements Event {

	private final String message;
	
	public BoomEvent(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		String out = new String(this.message);
		return out;
	}

}
