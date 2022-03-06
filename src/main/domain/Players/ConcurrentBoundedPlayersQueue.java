package main.domain.Players;


import java.util.NoSuchElementException;
import java.util.concurrent.*;
import main.domain.Exceptions.*;


/**
 * Thread safe BoundedPlayersQueue
 * */
public class ConcurrentBoundedPlayersQueue implements BoundedPlayersQueue {

	private final int maximum_capacity;
	private static final int DEFAULT_MAXIMUM_CAPACITY = 1;
	private static ConcurrentLinkedDeque<Player> player_queue;
	
	/**
	 * 
	 * @param capacity. A strictly positive integer. 
	 */
	public ConcurrentBoundedPlayersQueue(int capacity) throws IllegalArgumentException {
		if (capacity <= 0) {
			throw new IllegalArgumentException(capacity + " is an invalid players capacity, it should be a strictly positive integer");
		}
		player_queue = new ConcurrentLinkedDeque<Player>();
		this.maximum_capacity = capacity;
	}
	
	@Override
	public boolean hasPlayer(Player player) {
		return player_queue.contains(player);
	}

	@Override
	public synchronized Player nextPlayer() throws NoPlayersInGameException  {
		
		if (player_queue.isEmpty()){
			throw new NoPlayersInGameException("Cannot return the next player of an empty player queue");
		}
		else {
			Player next_Player = player_queue.pollFirst();
			player_queue.addLast(next_Player);
			return next_Player;			
		}
		

	}
	
	@Override
	public synchronized void addPlayer(Player player) throws MaxNumOfPlayersReachedException {
		if (player_queue.size() >= getMaximumCapacity()){
			throw new MaxNumOfPlayersReachedException("The maximum number of player : " + getMaximumCapacity() + " is reached, cannot add a new player");
		}
		player_queue.addLast(player);
	}
	
	@Override
	public synchronized void removePlayer(Player player) throws NoSuchElementException {
				
		if (this.hasPlayer(player)) {
			player_queue.remove(player);
		}
		else {
			throw new NoSuchElementException("Player " + player.getID() + " not found in queue");
		}
	}
	
	@Override
	public synchronized Player showNextPlayer() throws NoPlayersInGameException {
		if (this.getNumberOfPlayers()==0){
			throw new NoPlayersInGameException("There are no players in the queue");
		}
		Player nextPlayer = player_queue.peekFirst();
		return nextPlayer;
	}

	@Override
	public int getMaximumCapacity() {
		int max_capacity = this.maximum_capacity;
		return max_capacity;
	}
	
	/**
	 * @return the default maximum capacity
	 */
	public static int getDefaultMaximumCapacity() {
		int default_max_capacity = DEFAULT_MAXIMUM_CAPACITY;
		return default_max_capacity;
	}
	
	public int getNumberOfPlayers() {
		return player_queue.size();
	}
	
	
	

}
