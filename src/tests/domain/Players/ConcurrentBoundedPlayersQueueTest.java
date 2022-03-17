package tests.domain.Players;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import main.domain.Players.*;
import main.domain.Exceptions.*;

//TODO make use of fixtures like in the other tests 

class ConcurrentBoundedPlayersQueueTest {

	@Test
	void testHasPlayer() throws MaxNumOfPlayersReachedException {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(
				ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		testqueue.addPlayer(new StringPlayer("testplayer1"));
		assertTrue(testqueue.hasPlayer(new StringPlayer("testplayer1")));
		assertFalse(testqueue.hasPlayer(new StringPlayer("testplayer2")));
		Player testplayer = new StringPlayer("testplayer1");
		assertTrue(testqueue.hasPlayer(testplayer));
	}

	@Test
	void testNextPlayerOnePlayer() throws NoPlayersInGameException, MaxNumOfPlayersReachedException {
		// a queue with one player should always have as next player that single player
		// it contains
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(
				ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
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
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(
				ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());

		NoPlayersInGameException thrown = Assertions.assertThrows(NoPlayersInGameException.class, () -> {
			testqueue.nextPlayer();
		});

		assertEquals("Cannot return the next player of an empty player queue", thrown.getMessage());
	}
	
	@Test
	void testNextPlayerConcurrent() throws InterruptedException, MaxNumOfPlayersReachedException, NoPlayersInGameException {
		int numberOfThreads = 14;
		int numberOfPlayers = 3;
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(numberOfPlayers);
		// filling the queue first. 
		for (int i = 0; i < numberOfPlayers; i++) {
	    	testqueue.addPlayer(new StringPlayer("player " + String.valueOf(i)));
	    }
		
		for (int i = 0; i < numberOfThreads; i++) {
	    	executor.execute(() -> {
	    		try {
					testqueue.nextPlayer();					
				} catch (NoPlayersInGameException e) {
					System.out.println("Passing exception : " + e.getMessage());
				}					
	    		latch.countDown();
	    	});
	    }
	    latch.await();
	    assertTrue(testqueue.showNextPlayer().equals(new StringPlayer("player 2")));
	    
	}
	
	@Test
	void testAddPlayerPutsInLast() throws NoPlayersInGameException, MaxNumOfPlayersReachedException {
		ConcurrentBoundedPlayersQueue testQueue = new ConcurrentBoundedPlayersQueue(2);
		Player testPlayer1 = new StringPlayer("testplayer1");
		testQueue.addPlayer(testPlayer1);
		assertTrue(testQueue.showNextPlayer().equals(testPlayer1));
		Player testPlayer2 = new StringPlayer("testplayer2");
		testQueue.addPlayer(testPlayer2);
		assertTrue(testQueue.showNextPlayer().equals(testPlayer1));
	}

	@Test
	void testAddPlayerMaxReached() throws MaxNumOfPlayersReachedException {
		// a queue with one player and a max capacity of one should not accept more
		// players.
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(
				ConcurrentBoundedPlayersQueue.getDefaultMaximumCapacity());
		StringPlayer testplayer = new StringPlayer("testplayer");
		testqueue.addPlayer(testplayer);
		MaxNumOfPlayersReachedException thrown = Assertions.assertThrows(MaxNumOfPlayersReachedException.class, () -> {
			testqueue.addPlayer(testplayer);
		});

		assertEquals(("number of players is already " + String.valueOf(testqueue.getNumberOfPlayers()) + " and the maximum is " + String.valueOf(testqueue.getMaximumCapacity())), thrown.getMessage());

	}

	@Test
	void testAddPlayerConcurrent() throws InterruptedException, MaxNumOfPlayersReachedException {
		int numberOfThreads = 10;
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(numberOfThreads);
		for (int i = 0; i < numberOfThreads; i++) {
			final int local_i = i; // needs to be final for the lambda
			executor.execute(() -> {
				try {
					testqueue.addPlayer(new StringPlayer("player " + String.valueOf(local_i)));
				} catch (MaxNumOfPlayersReachedException e) {
					System.out.println("Passing caught exception : " + e.getMessage());
				}
				latch.countDown();
			});
		}
		latch.await();
		assertEquals(numberOfThreads, testqueue.getNumberOfPlayers());
	}

	@Test
	void testRemovePlayer() throws NoSuchPlayerInGameException, MaxNumOfPlayersReachedException {
		// should be able to remove a player that's in a queue, but not a player that's
		// not in it
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
	void testRemovePlayerConcurrent() throws InterruptedException, MaxNumOfPlayersReachedException {
		int numberOfThreads = 10;
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(numberOfThreads);
		// filling the queue first 
		for (int i = 0; i < numberOfThreads; i++) {
	    	testqueue.addPlayer(new StringPlayer("player " + String.valueOf(i)));
	    }
		assertTrue(testqueue.hasPlayer(new StringPlayer(("player " + "1"))));
		// then multithreaded removal
		for (int i = 0; i < numberOfThreads; i++) {
	    	final int local_i = i; // needs to be final for the lambdas
	    	executor.execute(() -> {
	    		try {
			    	testqueue.removePlayer(new StringPlayer("player " + String.valueOf(local_i)));										
				} catch (NoSuchPlayerInGameException e) {
					System.out.println("Passing caught exception : " + e.getMessage());
				}
	    		latch.countDown();
	    	});
	    }
	    latch.await();
	    assertEquals(0, testqueue.getNumberOfPlayers());
	}

	@Test
	void testShowNextPlayer() throws NoPlayersInGameException, MaxNumOfPlayersReachedException{
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(2);
		Player testplayer1 = new StringPlayer("testplayer1");
		Player testplayer2 = new StringPlayer("testplayer2");
		testqueue.addPlayer(testplayer1);
		testqueue.addPlayer(testplayer2);
		assertTrue(testqueue.showNextPlayer().equals(testplayer1));
		testqueue.nextPlayer();
		assertTrue(testqueue.showNextPlayer().equals(testplayer2));		
	}
	
	@Test
	void testMaximumCapacity() throws IllegalArgumentException {
		// a queue with one player and a max capacity of one should not accept more
		// players.
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
	void testGetNumberOfPlayers() throws MaxNumOfPlayersReachedException {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(2);
		assertEquals(testqueue.getNumberOfPlayers(), 0);
		Player testplayer1 = new StringPlayer("testplayer1");
		Player testplayer2 = new StringPlayer("testplayer2");
		testqueue.addPlayer(testplayer1);
		testqueue.addPlayer(testplayer2);
		assertEquals(testqueue.getNumberOfPlayers(), 2);

	}
	
	@Test
	void testToString() throws MaxNumOfPlayersReachedException, NoPlayersInGameException {
		ConcurrentBoundedPlayersQueue testqueue = new ConcurrentBoundedPlayersQueue(2);
		Player testplayer1 = new StringPlayer("testplayer1");
		Player testplayer2 = new StringPlayer("testplayer2");	
		testqueue.addPlayer(testplayer1);
		testqueue.addPlayer(testplayer2);	
		String expectedString = "testplayer1\\linetestplayer2";
		assertEquals(expectedString, testqueue.toString());
		testqueue.nextPlayer();
		String expectedString2 = "testplayer2\\linetestplayer1";
		assertEquals(expectedString2, testqueue.toString());
		
	}
	

}
