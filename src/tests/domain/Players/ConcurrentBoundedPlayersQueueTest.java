package tests.domain.Players;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

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
	void testRemovePlayer() throws MaxNumOfPlayersReachedException {
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
	void testAddPlayer() throws MaxNumOfPlayersReachedException {
		//a queue with one player and a max capacity of one should not accept more players.
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		StringPlayer testplayer = new StringPlayer("testplayer");
		testqueue.addPlayer(testplayer);
		MaxNumOfPlayersReachedException thrown = Assertions.assertThrows(MaxNumOfPlayersReachedException.class, () -> {
			testqueue.addPlayer(testplayer);
		});
		
		assertEquals("The maximum number of player : " + testqueue.getMaximumCapacity() + " is reached, cannot add a new player", thrown.getMessage());

	}
	
	class PlayerAdder implements Runnable {
		
		private final BoundedPlayersQueue playerQueue;
		private final String playerID;
		
		public PlayerAdder(BoundedPlayersQueue queue, String id) {
			this.playerQueue = queue;
			this.playerID = id;
		}
		
		public void run() {
			try {
				int sizeBefore = this.playerQueue.getNumberOfPlayers();
				this.playerQueue.addPlayer(new StringPlayer("player " + this.playerID));
				System.out.println("added player : " + this.playerID + ". Size before : " + String.valueOf(sizeBefore) + ". Size after : " + String.valueOf(this.playerQueue.getNumberOfPlayers()));
			} catch (MaxNumOfPlayersReachedException e) {
				System.out.println(e.getMessage());
				System.out.println("pass");
			}
		}
		
	}

	
	@Test
	void testAddPlayerConcurrent() throws InterruptedException, MaxNumOfPlayersReachedException {
		int numberOfThreads = 40;
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(10);
	    for (int i = 0; i < numberOfThreads; i++) {
	    	final int local_i = i; // workaround 
	    	executor.execute(() -> {
	    		new PlayerAdder(testqueue, String.valueOf(local_i));
	    		latch.countDown();
	    	});
	    	
	    }
	    latch.await();
	    System.out.println(testqueue.getNumberOfPlayers());
	    //assertEquals(numberOfThreads, testqueue.getNumberOfPlayers());
	}
	

	@Test
	void testNextPlayerOnePlayer() throws NoPlayersInGameException, MaxNumOfPlayersReachedException {
		//a queue with one player should always have as next player that single player it contains 
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		StringPlayer testplayer = new StringPlayer("testplayer");
		testqueue.addPlayer(testplayer);
		assertTrue(testqueue.nextPlayer() instanceof StringPlayer);
		assertEquals(testplayer, testqueue.nextPlayer());
		assertEquals(testplayer, testqueue.nextPlayer());
	}
	
	@Test
	void testNextPlayerTwoPlayers() throws NoPlayersInGameException, MaxNumOfPlayersReachedException {
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
	void testNextPlayerNoPlayer() {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		
		NoPlayersInGameException thrown = Assertions.assertThrows(NoPlayersInGameException.class, () -> {
			testqueue.nextPlayer();
		});
		
		assertEquals("Cannot return the next player of an empty player queue", thrown.getMessage());
	}
	
	@Test
	void testGetNumberOfPlayers() throws MaxNumOfPlayersReachedException {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(2);
		assertEquals(testqueue.getNumberOfPlayers(), 0);
		Player testplayer1 = new StringPlayer("testplayer1");
		Player testplayer2 = new StringPlayer("testplayer2");
		testqueue.addPlayer(testplayer1);
		testqueue.addPlayer(testplayer2);
		assertEquals(testqueue.getNumberOfPlayers(), 2);
		
	}

	

}
