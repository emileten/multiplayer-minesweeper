package main.domain.Exceptions;

public class NoSuchPlayerInGameException extends Exception {


	private static final long serialVersionUID = 2405972401170778674L;

	public NoSuchPlayerInGameException(String message) {
		super(message);
	}
	
}
