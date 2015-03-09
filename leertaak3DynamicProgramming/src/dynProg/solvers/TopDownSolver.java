package dynProg.solvers;

import java.util.Arrays;

import dynProg.Solver;

public class TopDownSolver implements Solver {

	private Boolean[][] matrix;
	private int[] numbers;
	int sum;

	@Override
	public boolean solve(int[] numbers, int sum) {
		matrix = new Boolean[numbers.length][sum + 1];
		return solving(numbers, sum);
	}

	private boolean solving(int[] numbers, int sum) {
		int n = numbers.length;
		
		// Base cases
		if (sum == 0) {
			return true;
		}
		if (n == 0) {
			return false;
		}
		
		if (matrix[n - 1][sum] == null) {
			matrix[n - 1][sum] = doSolve(numbers, sum);
		}
		
		return matrix[n - 1][sum];
	}

	public boolean doSolve(int[] numbers, int sum) {
		int n = numbers.length - 1;

		// Make new numbers array with one less element
		int[] newNumbers = Arrays.copyOf(numbers, n);

		// Check if the sum can be with including and excluding the last element
		return solving(newNumbers, sum)
				|| solving(newNumbers, sum - numbers[n]);
	}

}
