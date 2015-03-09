package dynProg.solvers;

import dynProg.Solver;

public class BottomUpSolver implements Solver {

	private boolean[][] matrix;
	private int[] numbers;
	private int sum;

	@Override
	public boolean solve(int[] numbers, int sum) {
		int n = numbers.length;
		this.numbers = numbers;
		this.sum = sum;

		// Initialize matrix with booleans (+1 row and columns)
		matrix = new boolean[n + 1][sum + 1];
		int rowLength = n + 1;
		int columnLength = sum + 1;
		
		// Fill the rows to true if column is zero
		for (int i = 0; i < rowLength; i++) {
			matrix[i][0] = true;
		}

		// Same, but then set to false if row is zero
		for (int j = 1; j < columnLength; j++) {
			matrix[0][j] = false;
		}

		for (int i = 1; i < rowLength; i++) {
			for (int j = 1; j < columnLength; j++) {
				// Check if the sum can be found by including or excluding the last element
				matrix[i][j] = matrix[i - 1][j]; // Excluding last element
				if (j >= numbers[i - 1]) {
					matrix[i][j] = matrix[i][j]
							|| matrix[i - 1][j - numbers[i - 1]]; // Including last element
				}
			}
		}
		return matrix[n][sum]; // Return boolean outcome
	}

	/**
	 * Prints the matrix
	 */
	public void printMatrix() {
		System.out.print(" ");
		for (int i = 0; i < sum; i++) {
			System.out.print(" " + i + " ");
		}
		System.out.println("");
		for (int i = 0; i < matrix.length - 1; i++) {
			System.out.print(numbers[i]);
			for (int j = 0; j < matrix[0].length - 1; j++) {
				if (matrix[i][j] == true) {
					System.out.print(" T ");
				} else {
					System.out.print(" F ");
				}
			}
			System.out.println("");
		}
	}

}