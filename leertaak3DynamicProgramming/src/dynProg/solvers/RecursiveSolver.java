package dynProg.solvers;

import java.util.Arrays;

import dynProg.Solver;

public class RecursiveSolver implements Solver {

	@Override
	public boolean solve(int[] numbers, int sum) {
		int n = numbers.length;

		// Base cases
		if (sum == 0) {
			return true;
		}
		if (n == 0) {
			return false;
		}

		// Make new numbers array with one less element		
		int[] newNumbers = Arrays.copyOf(numbers, n-1);

		// Check if the sum can be with including and excluding the last element
		return solve(newNumbers, sum)
	            || solve(newNumbers, sum - numbers[n-1]);
	}

}
