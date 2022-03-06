package main.domain.Events;

/** Represents events happening when one attempts to dig into a cell.*/
public interface DigEvent extends Event {

	/** @return the message associated with this event **/
	public String getMessage();
	
}
