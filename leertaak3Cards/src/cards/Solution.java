package cards;
import java.util.Stack;
/** the solution is a sequence of cards placed on the board according to the card positions
    example without border
*/
public class Solution extends Stack<Candidate>
{
    // The board is an 2D array.
	//  0123
	// 0..-.
	// 1---.
	// 2.---
	// 3..-.
	private Candidate[][] board = new Candidate[4][4];
	
	// card positions on the board
	// the first card position on the board are
	// {0,2}, {1,0}. {1,1}
	private int[] row    = { 0, 1, 1, 1, 2, 2, 2, 3 };
	private int[] column = { 2, 0, 1, 2, 1, 2, 3, 2 };
	//  indices of adjacent cards in the solution.
	//                 0   1  2   3   4    5     6    7   
	int [] [] check = {{},{},{1},{0},{2},{3,4},{5,6},{7}}; 
	
	
	public Solution(){
		
	}

	
	 // Checks whether a candidate with card CardChar is in 
	 // an adjacent position of the board position (row, column)
	 // @param row, column, candidate
	 // @return Boolean indicating if cardChar is found.
	 // can be used in the methods fits and isCorrect
	private boolean bordersCard(int row, int column, char cardChar){
		// TODO
		if (isValidRow(row - 1)) {
			if (board[row - 1][column] != null) {
				//System.out.println(board[row - 1][column].getCardChar());
				if (cardChar == board[row - 1][column].getCardChar()) {
					return true;
				}
			}
		}
		if (isValidRow(row + 1)) {
			if (board[row + 1][column] != null) {
				//System.out.println(board[row + 1][column].getCardChar());
				if (cardChar == board[row + 1][column].getCardChar()) {
					return true;
				}
			}
		}
		if (isValidColumn(column - 1)) {
			if (board[row][column - 1] != null) {
				//System.out.println(board[row][column-1].getCardChar());
				if (cardChar == board[row][column - 1].getCardChar()) {
					return true;
				}
			}
		}
		if (isValidColumn(column + 1)) {
			if (board[row][column + 1] != null) {
				//System.out.println(board[row][column+1].getCardChar());
				if (cardChar == board[row][column + 1].getCardChar()) {
					return true;
				}
			}
			
		}
		return false;
	}
	
	/**
	 * Checks whether candidate card of same kind.
	 * Checks whether by placing candidate the solution sofar still complies with the rules
	 * @param candidate
	 * @return boolean indicating whether this candidate can be put in the
	 * next free position.
	 */
	public boolean fits(Candidate candidate){ 
		//TODO
		int i = this.size();
		
		if(bordersCard(row[i],column[i] , candidate.getCardChar())){
			return false;
		}
	    return true;
    }

	public void record(Candidate candidate)
	{
		int i=this.size(); // i= index in this stack of next for the next candidate
		board [row[i]] [column[i]] = candidate; //x=row, y=column
		this.push(candidate);
		
	}

	public boolean complete()
	{
		return this.size()==8;
	}

	public void show()
	{
		System.out.println(this); 
	}

	public Candidate eraseRecording()
	{
		int i=this.size()-1;           // i= index of the candidate that is removed from this Stack;
		board[row[i]][column[i]]=null; // remove candidate from board
		return this.pop();
    }
	
	// can be used in method isCorrect
    private char mustBeAdjacentTo(char card)
    {  
      if (card=='A') return 'K'; 
      if (card=='K') return 'Q'; 
      if (card=='Q') return 'J';
      return '?'; //error
    }
	
	/**
	 * Checks whether the rules below are fulfilled
	 * For the positions that can be checked for solution sofar.
	 * Rules:
	 * Elke aas (ace) grenst (horizontaal of verticaal) aan een heer (king).
	 * Elke heer grenst aan een vrouw (queen).
	 * Elke vrouw grenst aan een boer (jack).
	 * @return true if all checks are correct.
	 */
	// uses methods borderCard and mustBeAdjacent to
	private boolean isCorrect() {
         //TODO
         return true;
     }     
            
	
	/**
	 * @return a representation of the solution on the board
	 */
     public String toString(){
	    //TODO
    	 StringBuilder output = new StringBuilder();
    	 while(!this.empty()){
    		 output.append(this.pop().getCardChar());
    	 }
    	 
    	 return output.toString();
	}
     
     //checks if row is on the the board
     private boolean isValidRow(int row){
    	 if(row<0){
    		 return false;
    	 }
    	 if(row>=board.length){
    		 return false;
    	 }
    	 return true;
     }

     //checks if column is on the the board
     private boolean isValidColumn(int column){
    	 if(column<0){
    		 return false;
    	 }
    	 if(board.length>0){
    		 if(column>=board[0].length){
    			 return false;
    		 }
    	 }
    	 return true;
     }
     
     private boolean isValidCell(int row, int column){
    	 for(int i = 0; i<this.row.length;i++){
    		 if(this.row[i]==row && this.column[i] == column){
    			 return true;
    		 }
    	 }
    	 return false;
     }
}
