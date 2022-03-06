package main.domain.Board;

/**
 * a MineSweeper Board cell has three binary properties : contains a bomb, dug, flagged. 
 */
public class BoardCell {
	
	public boolean hasBomb;
	public boolean isDug;
	public boolean isFlagged;
	
	/**
	 * initiate a board cell that
	 */
	public BoardCell() {
		this.hasBomb = false;
		this.isDug = false;
		this.isFlagged = false;
	}
	
}
