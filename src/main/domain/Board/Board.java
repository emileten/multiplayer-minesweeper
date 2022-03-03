package main.domain.Board;

import main.domain.Events.Event;

/** Represents the board of a minesweeper game.
 * Abstractly, in a minesweeper game, players manipulate a set of locations that are all hidden in the first place.
 * Players can 'dig' locations to uncover the state of these locations. A location may contain a bomb, or not. 
 * If it does, the player looses. The goal is to uncover all the locations that do not contain a bomb. 
 * The player may also assign the 'flagged' state to a location it believes does contain a bomb. 
 * The goal is to uncover all the locations that do not contain a bomb 
 * 
 * Therefore, a given location has three key binary properties, indicating
 * (1) whether it contains a bomb
 * (2) whether it is dug (uncovered)
 * (3) whether it is flagged
 * 
 * A location can never have at the same time properties (1) and (2). 
 * 
 * */
public interface Board {
	
	/** attempts to discover a location 
	 * @param location
	 * @return an Event object indicating the outcome of the action */
	public Event dig(Object location);
	
	/** attempts to assign the 'flagged' property to a location
	 * does nothing if it's already assigned to that location, or if it's already dug. 
	 * @param location */
	public void flag(Object location);

	/** attempts to turn off the 'flagged' property of a location
	 * does nothing if it's already non-flagged, or dug. 
	 * @param location */
	public void deflag(Object location);
	

}
