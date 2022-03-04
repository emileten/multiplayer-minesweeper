package tests.domain.Players;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import main.domain.Players.*;
import main.domain.Exceptions.*;

class ConcurrentBoundedPlayersQueueTest {
	
	@Test
	void testHasPlayer() throws MaxNumOfPlayersReachedException {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		StringPlayer testplayer1 = new StringPlayer("testplayer1");
		StringPlayer testplayer2 = new StringPlayer("testplayer2");
		testqueue.addPlayer(testplayer1);
		assertTrue(testqueue.hasPlayer(testplayer1));
		assertFalse(testqueue.hasPlayer(testplayer2));
		
	}
	@Test
	void testMaximumCapacity() throws IllegalArgumentException {
		//a queue with one player and a max capacity of one should not accept more players.
		ConcurrentBoundedPlayersQueue testqueue1 = new ConcurrentBoundedPlayersQueue(5);
		assertEquals(testqueue1.getMaximumCapacity(), 5);
		ConcurrentBoundedPlayersQueue testqueue2 = new ConcurrentBoundedPlayersQueue(2);
		assertEquals(testqueue2.getMaximumCapacity(), 2);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ConcurrentBoundedPlayersQueue(0);
		});
		
		assertEquals("0 is an invalid players capacity, it should be a strictly positive integer", thrown.getMessage());


	}


	@Test
	void removePlayer() throws MaxNumOfPlayersReachedException {
		//should be able to remove a player that's in a queue, but not a player that's not in it
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(2);
		Player testplayer1 = new StringPlayer("testplayer1");
		Player testplayer2 = new StringPlayer("testplayer2");
		testqueue.addPlayer(testplayer1);
		testqueue.addPlayer(testplayer2);
		testqueue.removePlayer(testplayer1);
		assertFalse(testqueue.hasPlayer(testplayer1));
		assertTrue(testqueue.hasPlayer(testplayer2));
		testqueue.removePlayer(testplayer2);
		assertFalse(testqueue.hasPlayer(testplayer2));

	}
	
	@Test
	void addPlayer() throws MaxNumOfPlayersReachedException {
		//a queue with one player and a max capacity of one should not accept more players.
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		StringPlayer testplayer = new StringPlayer("testplayer");
		testqueue.addPlayer(testplayer);
		MaxNumOfPlayersReachedException thrown = Assertions.assertThrows(MaxNumOfPlayersReachedException.class, () -> {
			testqueue.addPlayer(testplayer);
		});
		
		assertEquals("The maximum number of player : " + testqueue.getMaximumCapacity() + " is reached, cannot add a new player", thrown.getMessage());

	}

	@Test
	void nextPlayerTestOnePlayer() throws NoPlayersInGameException, MaxNumOfPlayersReachedException {
		//a queue with one player should always have as next player that single player it contains 
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		StringPlayer testplayer = new StringPlayer("testplayer");
		testqueue.addPlayer(testplayer);
		assertTrue(testqueue.nextPlayer() instanceof StringPlayer);
		assertEquals(testplayer, testqueue.nextPlayer());
		assertEquals(testplayer, testqueue.nextPlayer());
	}
	
	@Test
	void nextPlayerTestTwoPlayers() throws NoPlayersInGameException, MaxNumOfPlayersReachedException {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(2);
		Player testplayer1 = new StringPlayer("testplayer1");
		Player testplayer2 = new StringPlayer("testplayer2");
		testqueue.addPlayer(testplayer1);
		testqueue.addPlayer(testplayer2);
		assertEquals(testplayer1, testqueue.nextPlayer());
		assertEquals(testplayer2, testqueue.nextPlayer());
		assertEquals(testplayer1, testqueue.nextPlayer());
	}
	
	@Test
	void nextPlayerTestNoPlayer() {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		
		NoPlayersInGameException thrown = Assertions.assertThrows(NoPlayersInGameException.class, () -> {
			testqueue.nextPlayer();
		});
		
		assertEquals("Cannot return the next player of an empty player queue", thrown.getMessage());
	}


}
