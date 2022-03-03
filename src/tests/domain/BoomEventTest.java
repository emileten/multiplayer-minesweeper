package tests.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.domain.Events.BoomEvent;


class BoomEventTest {

	@Test
	void testGetMessage() {
		String testMessageString = "Boom ! There was a bomb !";
		BoomEvent testEvent = new BoomEvent(testMessageString);
		
		assertEquals(testEvent.getMessage(), testMessageString, "expected the following String : ".concat(testMessageString));
	}

}
