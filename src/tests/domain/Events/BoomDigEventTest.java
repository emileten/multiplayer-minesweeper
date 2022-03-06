package tests.domain.Events;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.domain.Events.BoomDigEvent;


class BoomDigEventTest {

	@Test
	void testGetMessage() {
		String testMessageString = "Boom ! There was a bomb !";
		BoomDigEvent testEvent = new BoomDigEvent(testMessageString);
		
		assertEquals(testEvent.getMessage(), testMessageString, "expected the following String : ".concat(testMessageString));
	}

}
