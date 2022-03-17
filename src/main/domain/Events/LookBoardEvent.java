package main.domain.Events;

import main.domain.Board.Board;
import main.domain.Players.BoundedPlayersQueue;

public class LookBoardEvent implements Event {

	private final String message;
	
	public LookBoardEvent(Board board, BoundedPlayersQueue playersQueue) {
		this.message = board.toString() +
				"\\line" + 
				"Players queue :" +
				"\\line" +
				playersQueue.toString();	
	}
	
	public String toString() {
		return this.message;
	}


}
