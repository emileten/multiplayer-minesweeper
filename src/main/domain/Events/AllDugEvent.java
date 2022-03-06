package main.domain.Events;

public class AllDugEvent implements DigEvent {

	private final String message;
	
	public AllDugEvent(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		String out = new String(this.message);
		return out;
	}
}
