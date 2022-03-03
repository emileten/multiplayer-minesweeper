package tests.domain.Players;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.domain.Players.*;

class StringPlayerTest {

	@Test
	void stringGetID() {
		Player testplayer = new StringPlayer("test_player");
		assertEquals(testplayer.getID(), "test_player", "expected : test_player");
	}
	
	@Test 
	void equalsTest() {
		Player testplayer1 = new StringPlayer("test_player1");
		Player testplayer2 = new StringPlayer("test_player2");
		Player testplayer3 = new StringPlayer("test_player1");

		assertTrue(testplayer1.equals(testplayer3));
		assertFalse(testplayer1.equals(testplayer2));

	}

}
