package main.domain.Events;

import main.domain.Board.Board;

public class LookBoardEvent implements Event {

	private final String message;
	
	public LookBoardEvent(Board board) {
		this.message = board.toString();
	}
	
	public String toString() {
		return this.message;
	}


}
