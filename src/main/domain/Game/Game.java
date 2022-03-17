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

//TODO is it possible to restart this game ? 
//TODO might not need the 'ended' one .. start is sufficient. 
public class Game {

	public BoundedPlayersQueue players;
	public Board board;
	private boolean started = false;
	private boolean ended = false;
	
	/**
	 * constructor, does nothing for now
	 */
	public Game() {
		
	}
	

	/**
	 * initiates data : a player queue and a board, except if the game is already going on, as indicated by the 
	 * started field.
	 * @param player 
	 * @param numberOfRows
	 * @param numberOfColumns
	 * @param numberOfPlayers
	 * @param bombsLocations
	 * 
	 * @return GameAlreadyStartedEvent if this.started is true and doesn't touch the fields.
	 * MaxNumberofPlayersReachedEvent if numberOfPlayers is below 1, and doesn't touch the fields.  
	 * otherwise the game starts and it returns GameStartedEvent. 
	 */
	public Event startGame(Player player, int numberOfRows, int numberOfColumns, int numberOfPlayers, List<Integer> bombsLocations) {
		if (numberOfPlayers < 1) {
			return new MaxNumberOfPlayersReachedEvent();
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
		this.ended = false;
		return new GameStartedEvent();
	}
	
	
	/**
	 * @param player. Player joining the game. 
	 * @return PlayerAddedEvent if added successfully, MaxNumberOfPlayersReachedEvent if game is at capacity. 
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
	 * @return NoSuchPlayerInGameEvent if the player isn't in the game
	 * PlayerRemovedEvent otherwise. If it was the last player, the game is ended. 
	 */
	public Event quitGame(Player player) {
		try {
			this.players.removePlayer(player);
			if (this.players.getNumberOfPlayers()==0) {
				this.ended = true;
			}
			return new PlayerRemovedEvent();
		} catch (NoSuchPlayerInGameException e) { // was last player 
			return new NoSuchPlayerInGameEvent(); 
		}
	}
	
	/**
	 * @return true if this.started is true. 
	 */
	public boolean hasStarted() {
		boolean isStarted = this.started;
		return isStarted;
	}
	
	/**
	 * @return true if this.ended is true. 
	 */
	public boolean hasEnded() {
		boolean isEnded = this.ended;
		return isEnded;
	}
	
	/**
	 * 
	 * @param player
	 * @param action
	 * @return GameNotStartedEvent if game hasn't started yet and can't play
	 * NoSuchPlayerInGameEvent if the player specified isn't in the game
	 * Otherwise the Event associated with the action.
	 * 
	 * If the event associated with the action is an AllDugEvent or a BoomEvent, this method ends the game by turning on the
	 * this.ended field.
	 */
	public Event play(Player player, String action) {
		if (this.hasStarted() == false) {
			return new GameNotStartedEvent();
		} else if (!this.players.hasPlayer(player)){
			return new NoSuchPlayerInGameEvent();
		} else if (this.hasEnded() == true){
			return new GameAlreadyEndedEvent();
		} else {
			Event playResultEvent = handleGameAction(player, action);
			if (playResultEvent instanceof AllDugEvent | playResultEvent instanceof BoomEvent) {
				this.ended = true;
			}
			return playResultEvent;
		}
	}
	
	/**
	 * @param player
	 * @param action
	 * @return NotSupportedPlayerActionEvent if the action string isn't understandable
	 * otherwise the event associated with the action. 
	 */
	public Event handleGameAction(Player player, String action) {
		
		if (action.matches(GameProtocol.getRegexProtocolModificationAction())) {
			return handleGameModificationAction(player, action);
		} else if (action.matches(GameProtocol.getRegexProtocolObservationAction())) {
			return handleGameObservationAction(player, action);
		} else {
			return new UnSupportedPlayerActionEvent();
		}
	}
	
	/**
	 * Handle a modification action, assuming it matches the protocol given in getProtocolModificationAction.
	 * @param player 
	 * @param action
	 * @return NotThisPlayerTurnEvent if it's not the turn of this player at the moment
	 * NoPlayerInGameEvent if there isn't any player in the game
	 * Otherwise the event associated with the action.
	 */
	private Event handleGameModificationAction(Player player, String action) {
		try {
			Player nextPlayer = this.players.showNextPlayer();
			if (nextPlayer.equals(player)) {
				String[] tokens = action.split(" ");
		    	int x = Integer.parseInt(tokens[1]);
		        int y = Integer.parseInt(tokens[2]);
		        int position = this.board.convertMatrixIndices(x, y);
		        if (tokens[0].equals("dig")) {
		        	return this.board.dig(position);
		        } else if (tokens[0].equals("flag")) {
		        	return this.board.flag(position);
		        } else if (tokens[0].equals("deflag")) {
		        	return this.board.deflag(position);
		        } else {
		        	return new UnSupportedPlayerActionEvent();
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
	 * @param action, one of look, help or bye. 
	 * @param player
	 * @return LookBoardEvent if it's a look-at-the-board action
	 * HelpEvent if the player seeks for help
	 * and if it's bye, the event associated with the quiteGame method of this class. 
	 */
	private Event handleGameObservationAction(Player player, String action) {
	
		String[] tokens = action.split(" ");
	    if (tokens[0].equals("look")) {
	    	return new LookBoardEvent(this.board, this.players);
	    } else if (tokens[0].equals("help")) {
	    	return new HelpEvent();
	    } else if (tokens[0].equals("bye")) {
	    	return this.quitGame(player);
	    } else {
	    	return new UnSupportedPlayerActionEvent();
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
