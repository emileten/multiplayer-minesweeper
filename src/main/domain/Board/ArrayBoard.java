package main.domain.Board;

import main.domain.Events.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.LinkedList;


public class ArrayBoard implements Board {

	private BoardCell[] board;
	private final int size;
	private final int numberOfRows;
	private final int numberOfColumns;
	private final int numberOfBombs;
	private final List<Integer> bombsLocations;

	private int numberOfDugCells;
	
	/**
	 * initializes an array of BoardCells. 
	 * @param numberOfColumns
	 * @param numberOfRows
	 * @param bombs an 1D array of integers indicating where to place bombs on the board.
	 */
	public ArrayBoard(int numberOfRows, int numberOfColumns, List<Integer> bombs) {
		//TODO it's kinda cluttered.
		// might consider a version where you don't even have an array...
		// the bombs locations tell everything !
		this.numberOfColumns = numberOfColumns;
		this.numberOfRows = numberOfRows;
		this.size = this.numberOfColumns * this.numberOfRows;
		this.board = new BoardCell[size];
		this.numberOfBombs = bombs.size();
		this.numberOfDugCells = 0;
		this.bombsLocations = bombs;
		// construct cell object
		for (int i = 0; i < this.size; i++) {
			this.board[i] = new BoardCell();
		}
		// assign it the 'bomb' state where requested
		for (int i = 0; i < bombs.size(); i++) {
			this.board[bombs.get(i)].hasBomb = true;
		}
		// update the count of adjacent bombs accordingly
		for (int i = 0; i < this.size; i++) {
			this.board[i].numberOfAdjacentBombs = getNumberOfAdjacentBombs(i);
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
	public Event flag(int position) {
		if (this.board[position].isFlagged == true) {
			return new AlreadyFlaggedEvent();
		} else {
			this.board[position].isFlagged = true;
			return new FlaggedEvent();
		}
	}

	@Override
	public Event deflag(int position) {
		if (this.board[position].isFlagged == false) {
			return new AlreadyNotFlaggedEvent();
		} else {
			this.board[position].isFlagged = false;
			return new DeflaggedEvent();
		}
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
		return stringRep;
	}
	
	/**
	 * TODO the fact that the matrix is filled row wise should be centrally documented.
	 * Also, since it's key in the game that the viz is a matrix, and in the Board interface 
	 * a 1D array is contract, you probably want these conversino methods to be in the interface. 
	 * 
	 * finds the cells adjacent to a given cell in a row wise matrix representation of the board of cells. 
	 * @param position. Position of the cell of which to find the neighbors in the array.
	 * @return the indices of the cells adjacent to that cell per the row wise matrix representation.
	 * Makes no guarantee regarding the order in which it fills this list.
	 * 
	 * 
	 * Example with a 5 by 5 board. The 1D array indices are spread the following way in the matrix rep :
	 * 
	 * 0  1  2  3  4
	 * 5  6  7  8  9 
	 * 10 11 12 13 14
	 * 15 16 17 18 19
	 * 20 21 22 23 24
	 * 
	 * So for example, the neighbors of the 12th position in the 1D array are :
	 * 
	 * 6  7  8
	 * 11 12 13
	 * 16 17 18
	 * 
	 * Therefore, [6, 7, 8, 11, 13, 16, 17, 18]
	 * 
	 * Of the 0th position in the 1D array : 
	 * 
	 * [1, 5, 6]
	 */
	public List<Integer> neighBoringCells(int position) {
	
		
				
		// standard case, outside of edges : 8 neighbors 
		List<Integer> potentialNeighbors = new LinkedList<Integer>(Arrays.asList(
				position-this.numberOfColumns-1,
				position-this.numberOfColumns,
				position-this.numberOfColumns+1,
				position-1,
				position+1,
				position+this.numberOfColumns-1,
				position+this.numberOfColumns,
				position+this.numberOfColumns+1
			    ));

	
		// handles left and right edges
		if (this.isLeftEdge(position)) {
			potentialNeighbors.removeAll(Arrays.asList(
					potentialNeighbors.get(0),
					potentialNeighbors.get(3),
					potentialNeighbors.get(5)));
		} else if(this.isRightEdge(position)) {
			potentialNeighbors.removeAll(Arrays.asList(
					potentialNeighbors.get(2),
					potentialNeighbors.get(4),
					potentialNeighbors.get(7)));
		}
		
		// handles upper and lower edges
		potentialNeighbors.removeIf(value -> value < 0 | value >= this.size);
				
		return potentialNeighbors;
	}
	
	public int convertMatrixIndices(int x, int y) {
		return (this.numberOfColumns * x) - (this.numberOfColumns - y) - 1;
	}
	
	/**
	 * @param position 
	 * @return true if this index is in left edge in the row wise matrix representation of the 1D array
	 */
	private boolean isLeftEdge(int position) {
		return position % this.numberOfColumns == 0;
	}
	
	/**
	 * @param position 
	 * @return true if this index is in right edge in the row wise matrix representation of the 1D array
	 */
	private boolean isRightEdge(int position) {
		return (position+1) % this.numberOfColumns == 0;
	}
	
	/**
	 * @param position. the position of a cell in the array. 
	 * @return the number of adjacent cells containing a bomb
	 */
	private int getNumberOfAdjacentBombs(int position) {
		int count = 0;
		for (Integer element: neighBoringCells(position)) {
			if (this.board[element].hasBomb){
				count = count + 1;
			} 
		}
		return count;
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
	public static List<Integer> smallFactoryBoardBombsLocation() {
		List<Integer> bombsLocation = new LinkedList<Integer>(Arrays.asList(4, 9, 14, 19));
		return bombsLocation;
	}
	

	@Override
	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode();
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(this.numberOfRows, 
				this.numberOfColumns,
				this.size,
				this.numberOfBombs,
				this.numberOfDugCells,
				this.bombsLocations);
	}
	

}


