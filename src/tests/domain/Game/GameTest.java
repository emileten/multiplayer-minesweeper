package tests.domain.Game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import main.domain.Game.Game;
import main.domain.Game.GameProtocol;
import main.domain.Board.*;
import main.domain.Events.AllDugEvent;
import main.domain.Events.BoomEvent;
import main.domain.Events.DeflaggedEvent;
import main.domain.Events.DugEvent;
import main.domain.Events.Event;
import main.domain.Events.FlaggedEvent;
import main.domain.Events.GameAlreadyStartedEvent;
import main.domain.Events.GameStartedEvent;
import main.domain.Events.MaxNumberOfPlayersReachedEvent;
import main.domain.Events.PlayerAddedEvent;
import main.domain.Events.PlayerRemovedEvent;
import main.domain.Events.UnSupportedPlayerActionEvent;
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
		assertFalse(testGame.hasEnded());
	    bombLocations = ArrayBoard.smallFactoryBoardBombsLocation();
		testPlayer = new StringPlayer("testPlayer");
		startedGameResultEvent = testGame.startGame(testPlayer, numberOfRows, numberOfColumns, playerCapacity, bombLocations);
	}
	
	
	@Test
	void startTestGameStartedEvent() {		
		assertTrue(startedGameResultEvent instanceof GameStartedEvent);
		assertTrue(testGame.hasStarted());
		assertFalse(testGame.hasEnded());
		
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
		assertFalse(testGame.hasEnded());
		assertTrue(testGame.quitGame(testPlayer) instanceof PlayerRemovedEvent);
		assertTrue(testGame.hasEnded());
	}
	
	@Test
	void testLook() {
		Event resultEvent = testGame.play(testPlayer, "look");
		String expectedString = testGame.board.toString() + 
				"\\line" +
				"Players queue :" +
				"\\line" +
				"testPlayer";
		assertEquals(expectedString, resultEvent.toString());
	}
	
	@Test
	void testHelp() {
		Event resultEvent = testGame.play(testPlayer, "help");
		assertTrue(resultEvent.toString().equals(GameProtocol.getHumanReadablePlayerProtocol()));		
	}
	
	@Test
	void testBye() {
		Player p1 = new StringPlayer("newPlayerWhoCanJoin");
		testGame.joinGame(p1);		
		Event resultEvent = testGame.play(testPlayer, "bye");
		assertTrue(resultEvent instanceof PlayerRemovedEvent);
		Event resultEvent2 = testGame.play(p1, "bye");
		assertTrue(resultEvent2 instanceof PlayerRemovedEvent);
		assertTrue(testGame.hasEnded());		
	}
	
	@Test
	void testPlayTurn() {
		
		assertTrue(testGame.play(testPlayer, "DRUNK MAN TRYING TO PLAY") instanceof UnSupportedPlayerActionEvent);
		assertTrue(testGame.play(testPlayer, "dig 1 1") instanceof DugEvent);
		assertTrue(testGame.board.isDug(0));
		assertTrue(testGame.play(testPlayer, "dig 3 4") instanceof DugEvent);
		assertTrue(testGame.board.isDug(13));
		assertTrue(testGame.play(testPlayer, "flag 2 5") instanceof FlaggedEvent);
		assertTrue(testGame.board.isFlagged(9));
		assertTrue(testGame.play(testPlayer, "deflag 2 5") instanceof DeflaggedEvent);
		assertFalse(testGame.board.isFlagged(9));

	}
	
	@Test
	void testPlayTurnBoomEndsGame() {
		
		assertTrue(testGame.play(testPlayer, "dig 2 5") instanceof BoomEvent);
		assertTrue(testGame.hasEnded());

	}
	
	@Test
	void testPlayTurnAllDugEndsGame() {
		
		testGame = new Game();
	    bombLocations = ArrayBoard.smallFactoryBoardBombsLocation();
		testGame.startGame(testPlayer, 1, 2, 1, Arrays.asList());
		testGame.play(testPlayer, "dig 1 1");
		assertTrue(testGame.play(testPlayer, "dig 1 2") instanceof AllDugEvent);
		assertTrue(testGame.hasEnded());
		
	}
	
	@Test
	void testRandomBombs() {
		List<Integer> randomBombIntegers = Game.randomBombLocations(10, 20);
		assertTrue(randomBombIntegers.size() == 10);
		Collections.sort(randomBombIntegers);
		assertTrue(randomBombIntegers.get(0) >= 0 && randomBombIntegers.get(randomBombIntegers.size()-1) <= 19);
	}
	
	@Test
	void startTestGameCanRestartTheGame() {
		// modify a bit the existing game
		Player p1 = new StringPlayer("newPlayerWhoCanJoin");
		testGame.joinGame(p1);	
		testGame.play(testPlayer, "dig 1 1");
		// try to restart it 
		assertTrue(testGame.startGame(testPlayer, 5, 5, 1, bombLocations) instanceof GameStartedEvent);
		// verify it's a fresh new game as much as you can
		assertEquals(ArrayBoard.smallFactoryBoard(), testGame.board);
		assertTrue(testGame.players.hasPlayer(testPlayer));
		assertFalse(testGame.players.hasPlayer(p1));
		assertFalse(testGame.board.isDug(0));
	}
	
}
