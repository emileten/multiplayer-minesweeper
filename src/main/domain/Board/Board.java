package main.domain.Board;

import main.domain.Events.*;

/** Represents the board of a minesweeper game.
 * The board of a minesweeper game is made up of an integer indexed collection of BoardCell objects. 
 * Players can alter one of the three properties of these cells through the board object. 
 * */
public interface Board {
	
	/** attempts to dig into a location 
	 * @param position
	 * @return an DigEvent object 
	 */
	Event dig(int position);
	
	/** attempts to assign the 'flagged' property to a location
	 * does nothing if it's already assigned to that location, or if it's already dug. 
	 * @param position
	 */
	void flag(int position);

	/** attempts to turn off the 'flagged' property of a location
	 * does nothing if it's already non-flagged, or dug. 
	 * @param position
	 */
	void deflag(int position);
	
	/**
	 * 
	 * @param position
	 * @return True if this location is dug
	 */
	boolean isDug(int position);
	
	
	/**
	 * 
	 * @param position
	 * @return True if this location is dug
	 */
	boolean isFlagged(int position);
	 
	
	/**
	 * @return the size of this board
	 */
	int getSize();
	
}
