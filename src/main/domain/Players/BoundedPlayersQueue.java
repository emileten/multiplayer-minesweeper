package main.domain.Players;

import main.domain.Exceptions.*;
import main.domain.Events.*;

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
	Event addPlayer(Player player) throws MaxNumOfPlayersReachedException;
	
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
	Event removePlayer(Player player) throws NoSuchPlayerInGameException;
	
	/**
	 * @return the first player of the queue but does not modify the queue.
	 * @throws NoPlayersInGameException of there are no players in the queue. 
	 */
	Player showNextPlayer() throws NoPlayersInGameException;
	
	/**
	 * @return the maximum capacity of this queue
	 */
	int getMaximumCapacity();
	
	/**
	 * @return the number of players in this queue
	 */
	int getNumberOfPlayers();

}