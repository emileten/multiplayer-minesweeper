package main.domain.Game;

import main.domain.Board.*;
import main.domain.Events.*;
import main.domain.Exceptions.MaxNumOfPlayersReachedException;
import main.domain.Exceptions.NoPlayersInGameException;
import main.domain.Exceptions.NoSuchPlayerInGameException;
import main.domain.Players.BoundedPlayersQueue;
import main.domain.Players.ConcurrentBoundedPlayersQueue;
import main.domain.Players.Player;

import java.util.*;

public class Game {

	public BoundedPlayersQueue players;
	public Board board;
	private boolean started = false;
	
	/**
	 * constructor, does nothing for now
	 */
	public Game() {
		
	}
	

	/**
	 * initiates data : a player queue and a board, except if the game is already going on, as indicated by the 
	 * started field.
	 * @param player 
	 * @param size
	 * @param numberOfRows
	 * @param numberOfColumns
	 * @param bombsLocations
	 * 
	 * @return GameAlreadyStartedEvent, MaxNumberOfPlayersReachedEvent, GameStartedEvent
	 */
	public Event startGame(Player player, int numberOfRows, int numberOfColumns, int numberOfPlayers, List<Integer> bombsLocations) {
		if (this.started == true) {
			return new GameAlreadyStartedEvent();
		}
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
	 * @param player. Player joining the game. 
	 * @return PlayerAddedEvent, MaxNumberOfPlayersReachedEvent 
	 */
	public Event joinGame(Player player) {
		try {
			this.players.addPlayer(player);
			return new PlayerAddedEvent();
		} catch (MaxNumOfPlayersReachedException e) {
			return new MaxNumberOfPlayersReachedEvent();
		}
	}
	
	/**
	 * @param player. Player leaving the game. 
	 * @return PlayerRemovedEvent, NoSuchPlayerInGameEvent
	 */
	public Event quitGame(Player player) {
		try {
			this.players.removePlayer(player);
			return new PlayerRemovedEvent();
		} catch (NoSuchPlayerInGameException e) {
			return new NoSuchPlayerInGameEvent();
		}
	}
	
	/**
	 * @return true if started
	 */
	public boolean hasStarted() {
		boolean isStarted = this.started;
		return isStarted;
	}
	/**
	 * 
	 * @param player
	 * @param action
	 * @return GameNotStartedEvent, NoSuchPlayerInGameEvent, or Event returned by handleGameAction(). 
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
	 * @return NotSupportedPlayerActionEvent, or Event returned by handleGameModificationAction() or handleGameObservationAction(). 
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
	 * @return NotThisPlayerTurnEvent, NoPlayerInGameEvent, or other Event. TODO. 
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
	 * @param player
	 * @return LookBoardEvent, HelpEvent
	 */
	private Event handleGameObservationAction(Player player, String action) {
	
		String[] tokens = action.split(" ");
	    if (tokens[0].equals("look")) {
	    	return new LookBoardEvent(this.board);
	    } else if (tokens[0].equals("help")) {
	    	return new HelpEvent();
	    } else {
	    	return this.quitGame(player);
	    } 
					
	}
	

	
	/**
	 * Provides a random 1D array of integers
	 * @param numberOfBombs
	 * @param boardSize
	 * @return a 1D array of length equal to numberOfBombs and filled with integers randomly drawn from the interval
	 * [0, boardSize-1]
	 */
	public static List<Integer> randomBombLocations(int numberOfBombs, int boardSize){
	    ArrayList<Integer> list = new ArrayList<Integer>(numberOfBombs);
	    Random random = new Random();
	    
	    for (int i = 0; i < numberOfBombs; i++)
	    {
	        list.add(random.nextInt(boardSize));
	    }
	    
	    return list;
	}  
	

}
