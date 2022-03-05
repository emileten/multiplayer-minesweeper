package main.domain.Players;

import java.util.NoSuchElementException;
import main.domain.Exceptions.*;

/** Represents the players of a MineSweeper game, the order in which they should play,
 * and enforces a maximum number of players, which should be strictly positive.
 */
public interface BoundedPlayersQueue {
	
	/**
	 * @param player check if that player is in the queue
	 * @return True if that player is in the queue 
	 */
	boolean hasPlayer(Player player);

	/** 
	 * Adds a player to the queue at the last position, under capacity constraints.
	 * @param player the player to be added to the queue
	 * @throws RunTimeException of there isn't capacity for new players. 
	 */
	void addPlayer(Player player) throws MaxNumOfPlayersReachedException;
	
	/**
	 * Retrieves and removes the first player of the queue and inserts it at the last position
	 * @return the first player of this queue
	 */
	Player nextPlayer() throws NoPlayersInGameException;

	/**
	 * Remove a player from the queue
	 * @param player the player to be removed
	 * @throws NoPlayersInGameException
	 */
	void removePlayer(Player player) throws NoSuchElementException;
	
	/**
	 * @return the maximum capacity of this queue
	 */
	int getMaximumCapacity();
	
	/**
	 * @return the number of players in this queue
	 */
	int getNumberOfPlayers();

}