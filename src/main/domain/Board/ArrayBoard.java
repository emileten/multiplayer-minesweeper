package main.domain.Board;

import main.domain.Events.*;

public class ArrayBoard implements Board {

	private BoardCell[] board;
	private final int size;
	private final int numberOfBombs;
	private int numberOfDugCells;
	
	/**
	 * initializes an array of BoardCells. 
	 * @param size the size of the board. 
	 * @param bombs an 1D array of integers indicating where to place bombs on the board.
	 */
	public ArrayBoard(int size, int[] bombs) {
		this.size = size;
		this.board = new BoardCell[size];
		this.numberOfBombs = bombs.length;
		this.numberOfDugCells = 0;
		for (int i = 0; i < this.size; i++) {
			this.board[i] = new BoardCell();
		}
		for (int i = 0; i < bombs.length; i++) {
			this.board[bombs[i]].hasBomb = true;
		}
	}
	
	@Override
	public DigEvent dig(int position) {
		if (this.board[position].hasBomb == true) {
			return new BoomDigEvent("Boom!");
		}
		else {
			if (this.board[position].isDug == true) {
				return new SuccessfullDigEvent("Already Dug!");	//TODO should have an event for that
			}
			else {
				this.board[position].isDug = true;
				this.numberOfDugCells++;
				if (this.numberOfDugCells == this.size - this.numberOfBombs) {
					return new AllDugEvent("Congratulations ! You found all cells without a bomb.");
				}
				else {
					return new SuccessfullDigEvent("Dug!");	
				}
			}		
		}
	}

	@Override
	public void flag(int position) {
		this.board[position].isFlagged = true;
	}

	@Override
	public void deflag(int position) {
		this.board[position].isFlagged = false;
	}
	
	@Override
	public boolean isDug(int position) {
		final boolean positionIsDug = this.board[position].isDug;
		return positionIsDug;
	}
	
	@Override
	public boolean isFlagged(int position) {
		final boolean positionIsFlagged = this.board[position].isFlagged;
		return positionIsFlagged;
	}
	
	@Override
	public int getSize() {
		return this.size;
	}
	
	/**
	 * @return small board of size 25 with a bomb in 4, 9, 14, 19th position.
	 */
	public static ArrayBoard smallFactoryBoard() {
		return new ArrayBoard(25, smallFactoryBoardBombsLocation());
	}
	
	/**
	 * @return the locations of the bombs in the default factory board, sorted in increasing order. 
	 */
	public static int[] smallFactoryBoardBombsLocation() {
		int[] bombsLocation = {4, 9, 14, 19};
		return bombsLocation;
	}
	
}


