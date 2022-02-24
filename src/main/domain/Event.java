package main.domain;

/** Represents events happening during a Minesweeper game. */
public interface Event {

	/** @return the message associated with this event **/
	public String getMessage();
	
}
