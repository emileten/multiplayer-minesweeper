package tests.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import main.client.*;

class MultiPlayerMineSweeperClientTest {
	


	@Test
	void parseNewLinesTestNoNewLine() {		
		assertEquals("some message", MultiPlayerMineSweeperClient.parseNewLines("some message"));
		assertEquals("some message\n", MultiPlayerMineSweeperClient.parseNewLines("some message\\line"));
		assertEquals("some message\nand then a line\nand then another line",
				MultiPlayerMineSweeperClient.parseNewLines("some message\\lineand then a line\\lineand then another line"));
		assertEquals("I \n Am \n Drunk \n Whoo",
				MultiPlayerMineSweeperClient.parseNewLines("I \\line Am \\line Drunk \\line Whoo"));
	}
}
