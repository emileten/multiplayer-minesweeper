package tests.domain.Board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.Board.BoardCell;

class BoardCellTest {

	public BoardCell boardCell;

	@BeforeEach
	void setUp() throws Exception {
		this.boardCell = new BoardCell();
	}

	@Test
	void testToString() {
		System.out.println(boardCell.toString());
		assertTrue(boardCell.toString().equals("-"));
		boardCell.hasBomb = true;
		assertTrue(boardCell.toString().equals("-"));		
		boardCell.isDug = true;
		assertTrue(boardCell.toString().equals("*"));
		boardCell.numberOfAdjacentBombs = 1;
		assertTrue(boardCell.toString().equals("1"));
		boardCell.isDug = false;
		boardCell.isFlagged = true;
		assertTrue(boardCell.toString().equals("F"));
	}
	
	@Test
	void testEquals() {
		assertTrue(boardCell.equals(new BoardCell()));
		BoardCell otherBoardCell = new BoardCell();
		otherBoardCell.hasBomb = true;
		assertFalse(boardCell.equals(otherBoardCell));
		boardCell.hasBomb = true;
		assertTrue(boardCell.equals(otherBoardCell));
	}

}
