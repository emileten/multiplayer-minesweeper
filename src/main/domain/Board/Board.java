package main.domain.Board;


import main.domain.Events.*;

/** Represents the board of a minesweeper game.
 * The board of a minesweeper game is made up of an 1D integer indexed collection of BoardCell objects. 
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
	Event flag(int position);

	/** attempts to turn off the 'flagged' property of a location
	 * does nothing if it's already non-flagged, or dug. 
	 * @param position
	 */
	Event deflag(int position);
	
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
	
	/**
	 * @return string row wise matrix (2D) representation of the board. 
	 * the matrix is built row wise. 
	 */
	String toString();
	
	/**
	 * @param other an object of the same type
	 * @return True if this is equal to other*/
	boolean equals(Object o);
	
	/**
	 * @return a hash code value for this board
	 */
	int hashCode();
	
	/*
	 * Input not using zero indexing. 
	 * @param x row, strictly above 0
	 * @param y column, strictly above 0
	 * @return index in the (zero-indexed) 1D array, i.e :
	 * (numberOfColumns * x) - (numberOfColumns - y) - 1
	 */
	int convertMatrixIndices(int x, int y);

}
