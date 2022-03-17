package tests.domain.Board;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.Events.*;
import main.domain.Board.*;

class ArrayBoardTest {


	ArrayBoard testBoard;
	
	//TODO this method is used only here, consider removing the smallfactoryboard from arrayboard 
	// and plug the code here
	@BeforeEach
	void initializeSmallFactoryBoard() {
		testBoard = ArrayBoard.smallFactoryBoard();
	}
	
	@Test
	void smallFactoryBoardTest() {
		assertTrue(testBoard.getSize()==25);
	}
	
	@Test
	void digTestSuccessfulDig() {
		Event resultDigEvent = testBoard.dig(0);
		assertTrue(resultDigEvent instanceof DugEvent);
		
	}
	
	@Test
	void digTestBomb() {
		Event resultDigEvent = testBoard.dig(4);
		assertTrue(resultDigEvent instanceof BoomEvent);
		
	}
	
	@Test
	void digTestAlreadyDug() {		
		testBoard.dig(0);
		Event resultDigEvent2 = testBoard.dig(0);
		assertTrue(resultDigEvent2 instanceof AlreadyDugEvent);

	}
	
	@Test
	void digTestAllDug() {
		Event lastDigEvent = new DugEvent();
		// digging all the locations that do not have a bomb. 
		for (int i = 0; i < testBoard.getSize(); i++) {
			if (!(ArrayBoard.smallFactoryBoardBombsLocation().contains(i))) {
				lastDigEvent = testBoard.dig(i);				
			}
		}
		assertTrue(lastDigEvent instanceof AllDugEvent);
	}
	
	@Test
	void flagTest() {
		assertTrue(testBoard.flag(0) instanceof FlaggedEvent);
		assertTrue(testBoard.isFlagged(0));		
	}
	
	@Test
	void deflagTest() {
		assertTrue(testBoard.flag(0) instanceof FlaggedEvent);
		assertTrue(testBoard.deflag(0) instanceof DeflaggedEvent);
		assertFalse(testBoard.isFlagged(0));
	}
	
	@Test
	void alreadyFlaggedTest() {
		assertTrue(testBoard.flag(0) instanceof FlaggedEvent);
		assertTrue(testBoard.flag(0) instanceof AlreadyFlaggedEvent);
	}
	
	
	@Test
	void testEquals() {
		ArrayBoard identicalBoard = ArrayBoard.smallFactoryBoard();
		assertTrue(testBoard.equals(identicalBoard));
		assertFalse(testBoard.equals(new ArrayBoard(5, 5, new LinkedList<Integer>(Arrays.asList(2, 3, 14, 11)))));
		assertFalse(testBoard.equals(new ArrayBoard(5, 4, new LinkedList<Integer>(Arrays.asList(4, 9, 14, 19)))));

	}
	
	@Test
	void testToString() {
		String sameLine = "- - - - -\newline";
		assertEquals(sameLine.repeat(5), testBoard.toString());
		testBoard.dig(0);
		assertEquals("  - - - -\newline" + sameLine.repeat(4), testBoard.toString());
		testBoard.flag(4);
		assertEquals("  - - - F\newline" + sameLine.repeat(4), testBoard.toString());
		testBoard.deflag(4);
		assertEquals("  - - - -\newline" + sameLine.repeat(4), testBoard.toString());
		testBoard.dig(3);
		assertEquals("  - - 2 -\newline" + sameLine.repeat(4), testBoard.toString());
		testBoard.dig(8);
		assertEquals("  - - 2 -\newline" + "- - - 3 -\newline" + sameLine.repeat(3), testBoard.toString());
		testBoard.dig(23);
		assertEquals("  - - 2 -\newline" + "- - - 3 -\newline" + sameLine.repeat(2) + "- - - 1 -\newline", testBoard.toString());	
	}
	
	
	@Test
	void testNeighboringCells() {
		assertEquals(Arrays.asList(1, 5, 6),testBoard.neighBoringCells(0));
		assertEquals(Arrays.asList(0, 2, 5, 6, 7),testBoard.neighBoringCells(1));
		assertEquals(Arrays.asList(6, 7, 8, 11, 13, 16, 17, 18),testBoard.neighBoringCells(12));
		assertEquals(Arrays.asList(18, 19, 23), testBoard.neighBoringCells(24));
	}
	
	@Test
	void testConvertMatrixIndices() {
		assertEquals(24, testBoard.convertMatrixIndices(5, 5));
		assertEquals(0, testBoard.convertMatrixIndices(1, 1));
		assertEquals(13, testBoard.convertMatrixIndices(3, 4));
		assertEquals(16, testBoard.convertMatrixIndices(4, 2));
	}
	
	//TODO test bombs locations 

}
