package main.domain.Board;

import main.domain.Events.*;

import java.util.Arrays;
import java.util.List;


public class ArrayBoard implements Board {

	private BoardCell[] board;
	private final int size;
	private final int numberOfRows;
	private final int numberOfColumns;
	private final int numberOfBombs;
	private int numberOfDugCells;
	
	/**
	 * initializes an array of BoardCells. 
	 * @param numberOfColumns
	 * @param numberOfRows
	 * @param bombs an 1D array of integers indicating where to place bombs on the board.
	 */
	public ArrayBoard(int numberOfRows, int numberOfColumns, int[] bombs) {
		this.numberOfColumns = numberOfColumns;
		this.numberOfRows = numberOfRows;
		this.size = this.numberOfColumns * this.numberOfRows;
		this.board = new BoardCell[size];
		this.numberOfBombs = bombs.length;
		this.numberOfDugCells = 0;
		// construct cell object
		for (int i = 0; i < this.size; i++) {
			this.board[i] = new BoardCell();
		}
		// assign it the 'bomb' state where requested
		for (int i = 0; i < bombs.length; i++) {
			this.board[bombs[i]].hasBomb = true;
		}
		// update the count of adjacent bombs accordingly
		for (int i = 0; i < this.size; i++) {
			int count = 0;
			for (Integer element: neighBoringCells(i)) {
				if (this.board[element].hasBomb){
					count = count + 1;
				} 
			}
			this.board[i].numberOfAdjacentBombs = count;
		}
	}
	
	@Override
	public Event dig(int position) {
		if (this.board[position].hasBomb == true) {
			return new BoomEvent();
		}
		else {
			if (this.board[position].isDug == true) {
				return new AlreadyDugEvent();
			}
			else {
				this.board[position].isDug = true;
				this.numberOfDugCells++;
				if (this.numberOfDugCells == this.size - this.numberOfBombs) {
					return new AllDugEvent();
				}
				else {
					return new DugEvent();	
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
	
	@Override
	public String toString() {
		String stringRep = "";
		int count = 1;
		for (BoardCell cell: this.board) {
			stringRep = stringRep + cell.toString();
			if (count % this.numberOfColumns == 0) { // completed a row : new line 
				stringRep = stringRep + "\n";
			} else {
				stringRep = stringRep + " "; // row still incomplete : a space for next character
			}
			count++;
		}
		return this.board.toString();
	}
	
	private List<Integer> neighBoringCells(int position) {
		
		List<Integer> neighbors = Arrays.asList(position-1,
				position+1 ,
				position-this.numberOfRows,
			    position-this.numberOfRows-1,
			    position-this.numberOfRows+1,
			    position+this.numberOfRows,
			    position+this.numberOfRows-1,
			    position+this.numberOfRows+1
			    );
		
		for (Integer element: neighbors) {
			if (element < 0 | element >= this.size) {
				neighbors.remove(element);
			}
		}
		
		return neighbors;
	}
	
	/**
	 * @return small board of size 25 with a bomb in 4, 9, 14, 19th position.
	 */
	public static ArrayBoard smallFactoryBoard() {
		return new ArrayBoard(5, 5, smallFactoryBoardBombsLocation());
	}
	
	/**
	 * @return the locations of the bombs in the default factory board, sorted in increasing order. 
	 */
	public static int[] smallFactoryBoardBombsLocation() {
		int[] bombsLocation = {4, 9, 14, 19};
		return bombsLocation;
	}
	

}


