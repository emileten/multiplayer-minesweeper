package main.domain.Exceptions;

public class NoPlayersInGameException extends Exception {

	private static final long serialVersionUID = 6715413283197475275L;

	public NoPlayersInGameException(String message) {
		super(message);
	}
	
}
