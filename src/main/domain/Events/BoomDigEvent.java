package main.domain.Events;

public class BoomDigEvent implements DigEvent {

	private final String message;
	
	public BoomDigEvent(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		String out = new String(this.message);
		return out;
	}

}
