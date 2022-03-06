package tests.domain.Events;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.domain.Events.SuccessfullDigEvent;


class SuccessfullDigEventTest {

	@Test
	void testGetMessage() {
		String testMessageString = "Pfiou ! No bomb there !";
		SuccessfullDigEvent testEvent = new SuccessfullDigEvent(testMessageString);
		
		assertEquals(testEvent.getMessage(), testMessageString, "expected the following String : ".concat(testMessageString));
	}

}
