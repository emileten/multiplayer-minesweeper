package tests.domain.Game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.domain.Game.Game;
import main.domain.Board.*;
import main.domain.Events.AllDugEvent;
import main.domain.Events.BoomEvent;
import main.domain.Events.DugEvent;
import main.domain.Events.Event;
import main.domain.Players.*;

class GameTest {

	@Test
	void constructorTest() {
		Game testGame = new Game();
	}
	
	@Test
	void startTest() {
		Game testGame = new Game();
		assertFalse(testGame.hasStarted());
		List<Integer> bombLocations = ArrayBoard.smallFactoryBoardBombsLocation();
		Player testPlayer = new StringPlayer("testPlayer");
		testGame.startGame(testPlayer, 5, 5, 1, bombLocations);
		assertTrue(testGame.hasStarted());
		assertTrue(testGame.players.hasPlayer(testPlayer));
		assertTrue(testGame.board.getSize() == 25);
	}
	
	@Test
	void startTestBombLocations() {
		Game testGame = new Game();
		assertFalse(testGame.hasStarted());
		List<Integer> bombLocations = ArrayBoard.smallFactoryBoardBombsLocation();
		Player testPlayer = new StringPlayer("testPlayer");
		testGame.startGame(testPlayer, 5, 5, 1, bombLocations);
		
		// checking the bombs are where they should be...
		for (Integer location : bombLocations) {
			Event resultEvent = testGame.board.dig(location); // hacking away using the underlying dig method
			assertTrue(resultEvent instanceof BoomEvent);
		}
		
		// and not where they should not be...
		for (int i = 0; i < testGame.board.getSize(); i++) {
			if (!bombLocations.contains(i)) {
				Event resultEvent = testGame.board.dig(i);
				if (i != testGame.board.getSize()-1) {
					assertTrue(resultEvent instanceof DugEvent);
				} else {
					assertTrue(resultEvent instanceof AllDugEvent);
				}
			}
		}
	}
	

	@Test
	void playTest() {
		Game testGame = new Game();
		assertFalse(testGame.hasStarted());
		List<Integer> bombLocations = ArrayBoard.smallFactoryBoardBombsLocation();
		Player testPlayer = new StringPlayer("testPlayer");
		testGame.startGame(testPlayer, 5, 5, 1, bombLocations);
		assertTrue(testGame.hasStarted());
		assertTrue(testGame.players.hasPlayer(testPlayer));
		assertTrue(testGame.board.getSize() == 25);
	}
	
}
