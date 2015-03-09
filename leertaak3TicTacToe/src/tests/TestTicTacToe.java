package tests;

import junit.framework.TestCase;
import ttt.*;

public class TestTicTacToe extends TestCase {

	private TicTacToe ticTacToe;

	public TestTicTacToe(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test: positionValue(), isAWin(), chooseMove()
	 * 
	 * De unittest van chooseMove moet testen dat de methode de beste zet kiest.
	 * @return 
	 */
	public void testTicTacToe() {
		TicTacToe t = new TicTacToe();

		t.setComputerPlays();
		
		t.playMove(2);
		t.playMove(5);
		t.playMove(0);
		t.playMove(1);
		t.playMove(4);
		t.playMove(6);
		t.playMove(8); // Computer wins
		t.playMove(3);
		
		// Testing positionValue()
		assertEquals(t.COMPUTER_WIN, t.positionValue());
		
		// Testing isAWin()
		assertTrue(t.isAWin(ticTacToe.COMPUTER));
		assertFalse(t.isAWin(ticTacToe.HUMAN));
		
		// Testing chooseMove()
		t.clearBoard(); // Clear board
		
		/*
		 * Simulate game:
		 * 
		 * ooo
		 * -x-
		 * x-x
		 */

		t.setHumanPlays();
		
		t.playMove(4);
		t.playMove(0);
		t.playMove(8);
		t.playMove(2);
		t.playMove(6);
		
		// Should equal 1 (best move)
		assertEquals(1, t.chooseMove());
	}
	
}
