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
		TicTacToe ticTacToe = new TicTacToe();

		ticTacToe.setComputerPlays();
		
		ticTacToe.playMove(2);
		ticTacToe.playMove(5);
		ticTacToe.playMove(0);
		ticTacToe.playMove(1);
		ticTacToe.playMove(4);
		ticTacToe.playMove(6);
		ticTacToe.playMove(8); // Computer wins
		ticTacToe.playMove(3);
		
		// Testing positionValue()
		assertEquals(7, ticTacToe.positionValue());
		
		// Testing isAWin()
		assertTrue(ticTacToe.isAWin(ticTacToe.COMPUTER));
		assertFalse(ticTacToe.isAWin(ticTacToe.HUMAN));
		
		// Testing chooseMove()
		ticTacToe.clearBoard(); // Clear board
		
		/*
		 * Simulate game:
		 * 
		 * x-o
		 * -x-
		 * --o
		 */
		
		ticTacToe.playMove(4);
		ticTacToe.playMove(2);
		ticTacToe.playMove(0);
		ticTacToe.playMove(6);
		
		// Should equal 5 (best move)
		assertEquals(5, ticTacToe.chooseMove());
	}
	
}
