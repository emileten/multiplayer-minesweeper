package tests.domain.Players;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import main.domain.Players.*;
import main.domain.Exceptions.*;

class PlayersQueueTest {
	
//	@Test
//	void addLast() throws RuntimeException {
//		//a queue with one player and a max capacity of one should not accept more players.
//		PlayersQueue testqueue = new PlayersQueue(PlayersQueue.getDefaultMaximumCapacity());
//		StringPlayer testplayer = new StringPlayer("testplayer");
//		testqueue.add(testplayer);
//		RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
//			testqueue.add(testplayer)
//		});
//		
//		assertEquals("Cannot return the next player of an empty player queue", thrown.getMessage());		
//		assertTrue(testqueue.nextPlayer() instanceof StringPlayer);
//		assertEquals(testplayer, testqueue.nextPlayer());
//		assertEquals(testplayer, testqueue.nextPlayer());
//	}

	@Test
	void nextPlayerTestOnePlayer() throws NoPlayersInGameException {
		//a queue with one player should always have as next player that single player it contains 
		PlayersQueue testqueue = new PlayersQueue(PlayersQueue.getDefaultMaximumCapacity());
		StringPlayer testplayer = new StringPlayer("testplayer");
		testqueue.addLastIfCapacity(testplayer);
		assertTrue(testqueue.nextPlayer() instanceof StringPlayer);
		assertEquals(testplayer, testqueue.nextPlayer());
		assertEquals(testplayer, testqueue.nextPlayer());
	}
	
	@Test
	void nextPlayerTestTwoPlayers() throws NoPlayersInGameException {
		PlayersQueue testqueue = new PlayersQueue(2);
		Player testplayer1 = new StringPlayer("testplayer1");
		Player testplayer2 = new StringPlayer("testplayer2");
		testqueue.addLastIfCapacity(testplayer1);
		testqueue.addLastIfCapacity(testplayer2);
		assertEquals(testplayer1, testqueue.nextPlayer());
		assertEquals(testplayer2, testqueue.nextPlayer());
	}
	
	@Test
	void nextPlayerTestNoPlayer() {
		PlayersQueue testqueue = new PlayersQueue(PlayersQueue.getDefaultMaximumCapacity());
		
		NoPlayersInGameException thrown = Assertions.assertThrows(NoPlayersInGameException.class, () -> {
			testqueue.nextPlayer();
		});
		
		assertEquals("Cannot return the next player of an empty player queue", thrown.getMessage());
	}


}
