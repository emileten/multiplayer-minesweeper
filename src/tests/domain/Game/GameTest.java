package tests.domain.Game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.domain.Game.Game;
import main.domain.Board.*;
import main.domain.Events.AllDugEvent;
import main.domain.Events.BoomEvent;
import main.domain.Events.DugEvent;
import main.domain.Events.Event;
import main.domain.Events.GameAlreadyStartedEvent;
import main.domain.Events.GameStartedEvent;
import main.domain.Players.*;

class GameTest {
	
	int numberOfRows = 5;
	int numberOfColumns = 5;
	int playerCapacity = 2;
	Game testGame;
	List<Integer> bombLocations;
	Player testPlayer;
	Event startedGameResultEvent;
	
	@BeforeAll
	@Test
	void constructorTest() {
		Game testGame = new Game();
		assertFalse(testGame.hasStarted());
	}
	
	@Before
	public void initializeGame() {
		Game testGame = new Game();
		assertFalse(testGame.hasStarted());
		List<Integer> bombLocations = ArrayBoard.smallFactoryBoardBombsLocation();
		Player testPlayer = new StringPlayer("testPlayer");
		Event startedGameResultEvent = testGame.startGame(testPlayer, numberOfRows, numberOfColumns, playerCapacity, bombLocations);		
	}
	
	
	@Test
	void startTestGameStartedEvent() {
		assertTrue(startedGameResultEvent instanceof GameStartedEvent);
		assertTrue(testGame.hasStarted());
		
	}
	
	@Test
	void startTestGameAlreadyStartedEvent() {
		assertTrue(testGame.startGame(testPlayer, 5, 5, 1, bombLocations) instanceof GameAlreadyStartedEvent);
	}
	
	@Test
	void startTestAssignsPlayers() {
		assertTrue(testGame.players.hasPlayer(testPlayer));
	}
	
	@Test
	void startTestAssignsBoard() {
		
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
