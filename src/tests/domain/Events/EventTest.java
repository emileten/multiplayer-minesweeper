package tests.domain.Events;

import org.junit.jupiter.api.Test;
import main.domain.Events.*;


class EventTest {

	@Test
	void testAllDugEvent() {
		Event myEvent = new AllDugEvent();
	}
	
	@Test
	void testAlreadyDugEvent() {
		Event myEvent = new AlreadyDugEvent();
	}
	
	@Test
	void testAlreadyFlaggedEvent() {
		Event myEvent = new AlreadyFlaggedEvent();
	}
	
	@Test
	void testBoomEvent() {
		Event myEvent = new BoomEvent();
	}
	
	@Test
	void testDugEvent() {
		Event myEvent = new DugEvent();
	}

}
