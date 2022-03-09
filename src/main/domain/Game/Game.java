package main.domain.Game;

import main.domain.Board.*;
import main.domain.Events.*;
import main.domain.Exceptions.MaxNumOfPlayersReachedException;
import main.domain.Exceptions.NoPlayersInGameException;
import main.domain.Players.BoundedPlayersQueue;
import main.domain.Players.ConcurrentBoundedPlayersQueue;
import main.domain.Players.Player;
import java.util.*;
import java.util.stream.IntStream;

import org.hamcrest.core.Is;

public class Game {

	public BoundedPlayersQueue players;
	public Board board;
	private boolean started = false;
	private boolean ended = false; //TODO make use of that 
	
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
	public Event startGame(Player player, int size, int numberOfPlayers) {
		Random rand = new Random();
		int[] bombsLocations = rand.ints(0, size-1).toArray();
		this.board = new ArrayBoard(size, bombsLocations);
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
		} else { //TODO add an if that checks if the player is somewhere in the queue and if not throw a 
			// no such player in game event.
			return handleGameAction(player, action);
		}
	}
	
	/**
	 * @param player
	 * @param action
	 * @return the event associated with the player's action
	 */
	public Event handleGameAction(Player player, String action) {
		if (action.matches(getProtocolModificationAction())) {
			return handleGameModificationAction(player, action);
		} else if (action.matches(getProtocolObservationAction())) {
			return handleGameObservationAction(action);
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
		        if (tokens[0].equals("dig")) {
			    	return new NotSupportedPlayerActionEvent(); //TODO
		        } else if (tokens[0].equals("flag")) {
			    	return new NotSupportedPlayerActionEvent(); //TODO
		        } else {
			    	return new NotSupportedPlayerActionEvent(); //TODO
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
	private Event handleGameObservationAction(String action) {
	
		String[] tokens = action.split(" ");
	    if (tokens[0].equals("look")) {
	    	//look
	    	return new NotSupportedPlayerActionEvent(); //TODO return a BoardRepresentationEvent or sth like that 
	    	// with String rep of board.
	    } else if (tokens[0].equals("help")) {
	    	//help
	    	return new NotSupportedPlayerActionEvent(); //TODO return nice string formatting of possible user input.
	    } else {
	    	//bye
	    	return new NotSupportedPlayerActionEvent(); //TODO message that informs the player was removed from the game
	    } 
					
	}
	
	/**
	 * @return a regex String representing the set of symbols identifying observation actions //TODO consider making
	 * this a constant variable OR have this in a separate class because you'll also need a pretty formatting of it.
	 */
	public static String getProtocolObservationAction() {
		return "(look)|(help)|(bye)|";
	}
	
	/**
	 * @return a regex String representing the set of symbols identifying modification actions. //TODO consider making 
	 * this a constant variable. 
	 */
	public static String getProtocolModificationAction() {
		return "(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|(deflag -?\\d+ -?\\d+)";
	}
	

}
