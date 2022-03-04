package main.domain.Exceptions;

public class MaxNumOfPlayersReachedException extends Exception {

	private static final long serialVersionUID = -6388016224992779946L;

	public MaxNumOfPlayersReachedException(String message) {
		super(message);
	}
}
