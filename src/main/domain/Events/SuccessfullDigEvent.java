package main.domain.Events;

public class SuccessfullDigEvent implements DigEvent {

	private final String message; 
	
	public SuccessfullDigEvent(String message) {
		this.message = message;
	}
	@Override
	public String getMessage() {
		String out = new String(this.message);
		return out;
	}

}
