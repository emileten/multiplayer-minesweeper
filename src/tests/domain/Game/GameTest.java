package tests.domain.Game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import main.domain.Game.Game;
import main.domain.Board.*;
import main.domain.Events.AllDugEvent;
import main.domain.Events.BoomEvent;
import main.domain.Events.DugEvent;
import main.domain.Events.Event;
import main.domain.Events.GameAlreadyStartedEvent;
import main.domain.Events.GameStartedEvent;
import main.domain.Events.MaxNumberOfPlayersReachedEvent;
import main.domain.Events.PlayerAddedEvent;
import main.domain.Events.PlayerRemovedEvent;
import main.domain.Players.*;

@TestInstance(Lifecycle.PER_CLASS)
class GameTest {
	
	public int numberOfRows = 5;
	public int numberOfColumns = 5;
	public int playerCapacity = 2;
    public Game testGame;
	public List<Integer> bombLocations;
	public Player testPlayer;
	public Event startedGameResultEvent;
	
	@Test
	@BeforeEach
	void constructorTest() {
		testGame = new Game();
		assertFalse(testGame.hasStarted());
	    bombLocations = ArrayBoard.smallFactoryBoardBombsLocation();
		testPlayer = new StringPlayer("testPlayer");
		startedGameResultEvent = testGame.startGame(testPlayer, numberOfRows, numberOfColumns, playerCapacity, bombLocations);
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
		assertEquals(ArrayBoard.smallFactoryBoard(), testGame.board);
	}
	
	@Test
	void testJoinGame() {
		Player p1 = new StringPlayer("newPlayerWhoCanJoin");
		Player p2 = new StringPlayer("playerWhoCannotJoin");
		assertTrue(testGame.joinGame(p1) instanceof PlayerAddedEvent);
		assertTrue(testGame.players.hasPlayer(p1));
		assertTrue(testGame.joinGame(p2) instanceof MaxNumberOfPlayersReachedEvent);
		assertFalse(testGame.players.hasPlayer(p2));

	}
	
	@Test
	void testQuitGame() {
		Player p1 = new StringPlayer("newPlayerWhoCanJoin");
		testGame.joinGame(p1);
		assertTrue(testGame.quitGame(p1) instanceof PlayerRemovedEvent);	
	}

	/*
	 * TODO
	 * joinGame
	 * quitGame
	 * observingAction
	 * mutatingAction
	 * randomBombs
	 */
	
	

	
	
}
