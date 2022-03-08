package tests.domain.Board;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import main.domain.Events.*;
import main.domain.Board.*;

//TODO
class ArrayBoardTest {

	
	@Test
	void smallFactoryBoardTest() {
		ArrayBoard testBoard = ArrayBoard.smallFactoryBoard();
		assertTrue(testBoard.getSize()==25);
	}
	
	@Test
	void digTestSuccessfulDig() {
		
		ArrayBoard testBoard = ArrayBoard.smallFactoryBoard();
		Event resultDigEvent = testBoard.dig(0);
		assertTrue(resultDigEvent instanceof DugEvent);
		
	}
	
	@Test
	void digTestBomb() {

		ArrayBoard testBoard = ArrayBoard.smallFactoryBoard();
		Event resultDigEvent = testBoard.dig(4);
		assertTrue(resultDigEvent instanceof BoomEvent);
		
	}
	
	@Test
	void digTestAlreadyDug() {
		
		ArrayBoard testBoard = ArrayBoard.smallFactoryBoard();
		testBoard.dig(0);
		Event resultDigEvent2 = testBoard.dig(0);
		assertTrue(resultDigEvent2 instanceof AlreadyDugEvent);

	}
	
	@Test
	void digTestAllDug() {

		ArrayBoard testBoard = ArrayBoard.smallFactoryBoard();
		Event lastDigEvent = new DugEvent();
		// digging all the locations that do not have a bomb. 
		for (int i = 0; i < testBoard.getSize(); i++) {
			if (Arrays.binarySearch(ArrayBoard.smallFactoryBoardBombsLocation(), i) < 0) {
				lastDigEvent = testBoard.dig(i);				
			}
		}
		
		assertTrue(lastDigEvent instanceof AllDugEvent);
	}
	
	@Test
	void flagTest() {
		ArrayBoard testBoard = ArrayBoard.smallFactoryBoard();
		testBoard.flag(0);
		assertTrue(testBoard.isFlagged(0));		
	}
	
	@Test
	void deflagTest() {
		ArrayBoard testBoard = ArrayBoard.smallFactoryBoard();
		testBoard.flag(0);
		testBoard.deflag(0);
		assertFalse(testBoard.isFlagged(0));
	}

}
