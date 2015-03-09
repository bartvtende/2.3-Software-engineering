package cards5;
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
		boolean fits = false;
		//if(next to card)
		//(fits if card next to it has its need fulfilled) || (fulfills this need) || (the needy has a blank space left)
		//&&(!bordersCard(row[i], column[i], candidate.getCardChar())
		
		//if (this.size()==7 bordersCard(row[i], column[i], (mustBeAdjacentTo(candidate.getCardChar()))) || (mustBeAdjacentTo(candidate.getCardChar())=='?')
		
		int[] borderRow = getBorderingRows(row[i]);
		int[] borderColumn = getBorderingColumns(column[i]);
		
		//If borders all empty
		if(bordersNothingButEmptySpaces(row[i], column[i])){
			return true;
		}
		
		boolean cardRequirementsMet = true;
		
		//if(next to card)
		//TODO split in multiple if statements so i know what the fuck is happening
		for(int j = 0; j<4;j++){
			if(isValidCell(borderRow[j], borderColumn[j])){
				if(!spaceIsEmpty(borderRow[j], borderColumn[j])){
					
					
					int[] borderCellBorderRow = getBorderingRows(borderRow[j]);
					int[] borderCellBorderColumn = getBorderingColumns(borderColumn[j]);
					
					//splitting of the if statements
					boolean requirementsMet;
					boolean hasMoreThanOneBlank;
					boolean fulfillsRequirement;
					boolean bordersSelf;
					//boolean requirementsMet;
					
					//checks if the bordering card has it requirement fulfilled
					if(this.cardMetRequirements(borderRow[j], borderColumn[j])){
						requirementsMet = true;
					}
					else{
						requirementsMet = false;
					}
					
					//checks if the candidate card fulfills the borderingscard requirement
					if(mustBeAdjacentTo(board[borderRow[j]][borderColumn[j]].getCardChar())==candidate.getCardChar() || mustBeAdjacentTo(board[borderRow[j]][borderColumn[j]].getCardChar()) == '?'){
						fulfillsRequirement = true;
					}
					else{
						fulfillsRequirement = false;
					}
					
					//checks if the bordering card has more adjacent empty space than the one the candidate card place
					if(adjacentBlanks(borderRow[j], borderColumn[j])>1){
						hasMoreThanOneBlank = true;
					}
					else{
						hasMoreThanOneBlank = false;
					}
					/*
					if(!requirementsMet){
						if(fulfillsRequirement || hasMoreThanOneBlank){
							//go ahead
						}
						else{
							cardRequirementsMet = false;
							//fails
						}
						
					}*/
					
					if(this.bordersCard(borderRow[j], borderColumn[j], candidate.getCardChar())){
						//fails
						cardRequirementsMet = false;
					}
					
				}
				
			}
		}
		boolean lastOk = false;
		/*
		if(this.size()==7){
			if(bordersCard(row[i], column[i], mustBeAdjacentTo(candidate.getCardChar()))||candidate.getCardChar()=='J'){
				lastOk = true;
			}
		}
		*/
		System.out.println(" returing " + cardRequirementsMet +":"+lastOk+":" +(cardRequirementsMet && !lastOk));
		return (cardRequirementsMet && !lastOk);
		
		/*
		if(!bordersCard(row[i], column[i], candidate.getCardChar()) && ((mustBeAdjacentTo(candidate.getCardChar())=='?')||(bordersCard(row[i], column[i], mustBeAdjacentTo(candidate.getCardChar())))|| (bordersCard(row[i], column[i], null)))){
			System.out.println("fits");
			return true;
	    }
		System.out.println("no fit");*/
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
     
	private boolean spaceIsEmpty(int row, int column) {
		try {
			if (board[row][column] == null) {

				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}
	
	//any emptySpace
	private boolean bordersEmptySpace(int row, int column){
		// TODO
		if (isValidRow(row - 1)) {
			if (spaceIsEmpty(row-1, column)) {
				return true;
			}
		}
		if (isValidRow(row + 1)) {
			if (spaceIsEmpty(row+1, column)) {
				return true;
			}
		}
		if (isValidColumn(column - 1)) {
			if (spaceIsEmpty(row, column-1)) {
				return true;
			}
		}
		if (isValidColumn(column + 1)) {
			if (spaceIsEmpty(row, column+1)) {
				return true;
			}
			
		}
		return false;
    }
	
	//any !emptySpace
	private boolean bordersNotEmptySpace(int row, int column){
		// TODO
		if (isValidRow(row - 1)) {
			if (spaceIsEmpty(row-1, column)) {
				return false;
			}
		}
		if (isValidRow(row + 1)) {
			if (spaceIsEmpty(row+1, column)) {
				return false;
			}
		}
		if (isValidColumn(column - 1)) {
			if (spaceIsEmpty(row, column-1)) {
				return false;
			}
		}
		if (isValidColumn(column + 1)) {
			if (spaceIsEmpty(row, column+1)) {
				return false;
			}
			
		}
		return true;
    }
	
	/**
	 * returns true if this cards requirements have been met.
	 * @param row
	 * @param column
	 * @return 
	 */
	private boolean cardMetRequirements(int row, int column){
		if(bordersCard(row, column, mustBeAdjacentTo(board[row][column].getCardChar()))||(board[row][column].getCardChar()=='J')){
			return true;
		}
		return false;
	}
	
	//used with getBoderingColumns to get surrounding cells
	private int[] getBorderingRows(int row){
		int[] r = {row-1, row, row+1,row};
		return r;
	}
	
	//used with getBorderingRows to get surrounding cells
	private int[] getBorderingColumns(int column){
		int[] c = {column, column+1, column, column-1};
		return c;
	}
	
	private boolean bordersNothingButEmptySpaces(int row, int column){
		boolean bordersNothingButEmpty = true;
		int[] borderRows = this.getBorderingRows(row);
		int[] borderColumns = this.getBorderingColumns(column);
		for(int i=0;i<4;i++){
			if(isValidCell(borderRows[i], borderColumns[i])){
				if(board[borderRows[i]][borderColumns[i]] != null){
					bordersNothingButEmpty = false;
				}
			}
		}
		return bordersNothingButEmpty;
	}
	
	private int adjacentBlanks(int row, int column){
		int[] borderRows = getBorderingRows(row);
		int[] borderColumns = getBorderingColumns(column);
		int blanks = 0;
		for(int i=0;i<4;i++){
			if(isValidCell(borderRows[i], borderColumns[i])){
				if(board[borderRows[i]][borderColumns[i]]==null){
					blanks++;
				}
			}
		}
		return blanks;
	}
	
	
}
