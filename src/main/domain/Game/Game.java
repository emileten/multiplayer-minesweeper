package main.domain.Game;

import main.domain.Board.*;
import main.domain.Events.*;
import main.domain.Exceptions.MaxNumOfPlayersReachedException;
import main.domain.Exceptions.NoPlayersInGameException;
import main.domain.Players.BoundedPlayersQueue;
import main.domain.Players.ConcurrentBoundedPlayersQueue;
import main.domain.Players.Player;
import java.util.*;

public class Game {

	public BoundedPlayersQueue players;
	public Board board;
	private boolean started = false;
	//TODO an 'ended' field ?
	
	/**
	 * constructor, does nothing for now
	 */
	public Game() {
		
	}
	
	/**
	 * initiates a player queue and a board
	 * @param player 
	 * @param size
	 * randomly fills the board with bombs
	 */
	public Event startGame(Player player, int size, int numberOfRows, int numberOfColumns, int numberOfPlayers) {
		Random rand = new Random();
		int[] bombsLocations = rand.ints(0, size-1).toArray();
		this.board = new ArrayBoard(numberOfRows, numberOfColumns, bombsLocations);
		BoundedPlayersQueue playersQueue = new ConcurrentBoundedPlayersQueue(numberOfPlayers);
		try {
			playersQueue.addPlayer(player);
		} catch (MaxNumOfPlayersReachedException e) {
			return new MaxNumberOfPlayersReachedEvent();
		}
		this.players = playersQueue;
		this.started = true;
		return new GameStartedEvent();
	}
	
	/**
	 * 
	 * @param player
	 * @param action
	 * @return the event associated with the player's action
	 */
	public Event play(Player player, String action) {
		if (this.started == false) {
			return new GameNotStartedEvent();
		} else if (!this.players.hasPlayer(player)){
			return new NoSuchPlayerInGameEvent();
		} else {
			return handleGameAction(player, action);
		}
	}
	
	/**
	 * @param player
	 * @param action
	 * @return the event associated with the player's action
	 */
	public Event handleGameAction(Player player, String action) {
		
		if (action.matches(GameProtocol.getRegexProtocolModificationAction())) {
			return handleGameModificationAction(player, action);
		} else if (action.matches(GameProtocol.getRegexProtocolObservationAction())) {
			return handleGameObservationAction(player, action);
		} else {
			return new NotSupportedPlayerActionEvent();
		}
	}
	
	/**
	 * Handle a modification action, assuming it matches the protocol given in getProtocolModificationAction.
	 * @param player 
	 * @param action
	 * @return Event associated with the action or NotThisPlayerTurnEvent if according to the queue, 
	 * it's not the turn of the given player. TODO list possible events. Or NotSupportedPlayerActionEvent.
	 */
	private Event handleGameModificationAction(Player player, String action) {
		try {
			Player nextPlayer = this.players.showNextPlayer();
			if (nextPlayer.equals(player)) {
				String[] tokens = action.split(" ");
		    	int x = Integer.parseInt(tokens[1]);
		        int y = Integer.parseInt(tokens[2]);
		        int position = x*y;
		        if (tokens[0].equals("dig")) {
		        	return this.board.dig(position);
		        } else if (tokens[0].equals("flag")) {
		        	return this.board.flag(position);
		        } else {
		        	return this.board.deflag(position);
		        }
			}
			else {
				return new NotThisPlayerTurnEvent();
			}
		} catch (NoPlayersInGameException e) {
			return new NoPlayerInGameEvent();
		}		
				
	}
	
	
	
	/**
	 * Handle an observation action, assuming it matches the protocol given in getProtocolObservationAction. 
	 * @param action
	 */
	private Event handleGameObservationAction(Player player, String action) {
	
		String[] tokens = action.split(" ");
	    if (tokens[0].equals("look")) {
	    	return new LookBoardEvent(this.board);
	    } else if (tokens[0].equals("help")) {
	    	return new HelpEvent();
	    } else {
	    	this.players.removePlayer(player);
	    	return new ByeEvent(player);
	    } 
					
	}

}
