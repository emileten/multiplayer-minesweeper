package main.domain.Events;

public class GameAlreadyStartedAndAtCapacity implements Event {


		private final String message = "The game has already started and is at capacity !";
		
		public String toString() {
			return this.message;

		}

}
