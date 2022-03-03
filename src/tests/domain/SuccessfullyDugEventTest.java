package tests.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.domain.Events.SuccessfullyDugEvent;


class SuccessfullyDugEventTest {

	@Test
	void testGetMessage() {
		String testMessageString = "Pfiou ! No bomb there !";
		SuccessfullyDugEvent testEvent = new SuccessfullyDugEvent(testMessageString);
		
		assertEquals(testEvent.getMessage(), testMessageString, "expected the following String : ".concat(testMessageString));
	}

}
