package dynProg.solvers;

public class printMatrix {

	BottomUpSolver bottomSolver = new BottomUpSolver();
	TopDownSolver topSolver = new TopDownSolver();
	
	public printMatrix() {
		bottomSolver.solve(new int[] { 3, 5, 7, 9, 11 }, 17);
		bottomSolver.printMatrix();
	}
	
	public static void main(String[] args) {
		new printMatrix();
	}
	
}
