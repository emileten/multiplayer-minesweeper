package main.domain.Players;

import java.util.concurrent.*;
import main.domain.Exceptions.NoPlayersInGameException;


/** Represents the players of a MineSweeper game and the order in which they should play
 * Enforces a maximum number of players */
public class PlayersQueue extends ConcurrentLinkedDeque<Player> {

	private static final long serialVersionUID = 1L;
	private final int maximum_capacity;
	private static final int DEFAULT_MAXIMUM_CAPACITY = 1;

	public PlayersQueue(int capacity) {
		super();
		this.maximum_capacity = capacity;
	}
	
	/**
	 * Retrieves and removes the first player of the queue and inserts it at the last position
	 * @return the first player of this queue
	 */
	public Player nextPlayer() throws NoPlayersInGameException  {
		
		if (this.isEmpty()){
			throw new NoPlayersInGameException("Cannot return the next player of an empty player queue");
		}
		else {
			Player next_Player = this.pollFirst();
			this.addLast(next_Player);
			return next_Player;			
		}
		

	}
	
	/** 
	 * Adds a player to the queue at the last position, under capacity constraints.
	 * @param the player to be added to the queue
	 * @throws RunTimeException of there isn't capacity for new players. 
	 */
	public void addLastIfCapacity(Player player) throws RuntimeException {
		if (this.size() >= getMaximumCapacity()){
			throw new RuntimeException("Maximum number of players already reached");
		}
		super.addLast(player);
	}
	
	/**
	 * @return the maximum capacity of this queue
	 */
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
	

}
