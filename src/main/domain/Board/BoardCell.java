package main.domain.Board;

/**
 * a MineSweeper Board cell has three binary properties : contains a bomb, dug, flagged.
 * And an integer property : the number of cells with a bomb it is surrounded by.  
 */
public class BoardCell {
	
	public boolean hasBomb;
	public boolean isDug;
	public boolean isFlagged;
	public int numberOfAdjacentBombs;
	
	/**
	 * initiate a board cell with all binary properties set to False and a counter
	 * of adjacent cells with a bomb equal to 0. 
	 */
	public BoardCell() {
		this.hasBomb = false;
		this.isDug = false;
		this.isFlagged = false;
		this.numberOfAdjacentBombs = 0;
	}
	
	public String toString() {
		if (this.isDug) {
			if (numberOfAdjacentBombs == 0) {
				return " ";
			} else {
				return String.valueOf(numberOfAdjacentBombs);				
			}
		} else if(this.isFlagged) {
			return "F";
		} else {
			return "-";
		}
	}
	
	
}
