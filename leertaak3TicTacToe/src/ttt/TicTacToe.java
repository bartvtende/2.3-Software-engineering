package ttt;

import java.util.Arrays;
import java.util.Random;

public class TicTacToe {
	public static final int HUMAN = 0;
	public static final int COMPUTER = 1;
	public static final int EMPTY = 2;

	public static final int HUMAN_WIN = 0;
	public static final int DRAW = 1;
	public static final int UNCLEAR = 2;
	public static final int COMPUTER_WIN = 3;

	private int[][] board = new int[3][3];
	private Random random = new Random();
	private int side = random.nextInt(2);
	private int position = UNCLEAR;
	private char computerChar, humanChar;

	// Constructor
	public TicTacToe() {
		clearBoard();
		initSide();
	}

	private void initSide() {
		if (this.side == COMPUTER) {
			computerChar = 'X';
			humanChar = 'O';
		} else {
			computerChar = 'O';
			humanChar = 'X';
		}
	}

	public void setComputerPlays() {
		this.side = COMPUTER;
		initSide();
	}

	public void setHumanPlays() {
		this.side = HUMAN;
		initSide();
	}

	public boolean computerPlays() {
		return side == COMPUTER;
	}

	public int chooseMove() {
		Best best = chooseMove(COMPUTER);
		return best.row * 3 + best.column;
	}

	// Find optimal move
	private Best chooseMove(int side) {
		int opp; // The other side

		if (side == COMPUTER)
			opp = HUMAN;
		else
			opp = COMPUTER;

		Best reply; // Opponent's best reply

		if (side == COMPUTER)
			reply = new Best(HUMAN_WIN);
		else
			reply = new Best(COMPUTER_WIN);

		int simpleEval; // Result of an immediate evaluation
		int bestRow = 0;
		int bestColumn = 0;
		int value;

		if ((simpleEval = positionValue()) != UNCLEAR)
			return new Best(simpleEval);

		for (int i = 0; i < 9; i++) {
			if (moveOk(i)) {
				// Place it on the board temporary
				place(i / 3, i % 3, side);
				Best turn = chooseMove(opp);

				// Check the side
				if (side == COMPUTER) {
					if (reply.val < turn.val) {
						reply.val = turn.val;
						reply.row = i / 3;
						reply.column = i % 3;
					}
				} else {
					if (reply.val > turn.val) {
						reply.val = turn.val;
						reply.row = i / 3;
						reply.column = i % 3;
					}
				}
				// Reset the board
				place(i / 3, i % 3, EMPTY);
			}
		}

		return reply;
	}

	// check if move ok
	public boolean moveOk(int move) {
		return (move >= 0 && move <= 8 && board[move / 3][move % 3] == EMPTY);
	}

	// play move
	public void playMove(int move) {
		board[move / 3][move % 3] = this.side;
		if (side == COMPUTER)
			this.side = HUMAN;
		else
			this.side = COMPUTER;
	}

	// Simple supporting routines
	public void clearBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = EMPTY;
			}
		}
	}

	// Check if the board is full
	private boolean boardIsFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// Return false if there's an empty spot
				if (board[i][j] == EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	// Returns whether 'side' has won in this position
	public boolean isAWin(int side) {
		int pos = positionValue();
		if ((pos == COMPUTER_WIN && side == COMPUTER)
				|| (pos == HUMAN_WIN && side == HUMAN)) {
			return true;
		}
		return false;
	}

	// Play a move, possibly clearing a square
	private void place(int row, int column, int piece) {
		board[row][column] = piece;
	}

	private boolean squareIsEmpty(int row, int column) {
		return board[row][column] == EMPTY;
	}

	// Compute static value of current position (win, draw, etc.)
	public int positionValue() {
		// Initialize arrays for the sums of rows, columns and diagonals
		String[] columns = new String[3];
		String[] rows = new String[3];
		String[] diagonals = new String[2];

		Arrays.fill(columns, "");
		Arrays.fill(rows, "");
		Arrays.fill(diagonals, "");
		
		// Check column and row wins
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				columns[i] += board[j][i];
				rows[i] += board[i][j];
			}
		    diagonals[0] += board[i][i];
		    diagonals[1] += board[2-i][2-i];
		}
		
		// Check for column wins
		for (int i = 0; i < 3; i++) {
			if (rows[i].equals("111") || columns[i].equals("111")) {
				return COMPUTER_WIN;
			} else if (rows[i].equals("000") || columns[i].equals("000")) {
				return HUMAN_WIN;
			}
		}
		
		// Check for diagonal wins
		for (int i = 0; i < 2; i++) {
			if (diagonals[i].equals("111")) {
				return COMPUTER_WIN;
			} else if (diagonals[i].equals("000")) {
				return HUMAN_WIN;
			}
		}

		// Check if board is full (draw)
		if (boardIsFull()) {
			return DRAW;
		}

		return UNCLEAR;
	}

	// Prints the board in a string
	public String toString() {
		String str = "";

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == COMPUTER)
					str += computerChar;
				else if (board[i][j] == HUMAN)
					str += humanChar;
				else
					str += ".";
			}
			str += '\n'; // Start new row
		}
		return str;
	}

	public boolean gameOver() {
		this.position = positionValue();
		return this.position != UNCLEAR;
	}

	public String winner() {
		if (this.position == COMPUTER_WIN)
			return "computer";
		else if (this.position == HUMAN_WIN)
			return "human";
		else
			return "nobody";
	}

	private class Best {
		int row;
		int column;
		int val;

		public Best(int v) {
			this(v, 0, 0);
		}

		public Best(int v, int r, int c) {
			val = v;
			row = r;
			column = c;
		}
	}

}
