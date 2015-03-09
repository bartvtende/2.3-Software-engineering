package cards5;
import java.util.Scanner;

public class Problem
{
private Candidates candidates = new Candidates();
private Solution   solution   = new Solution();
private Scanner    reader = new Scanner(System.in);
    
    public void solve()
{
     //System.out.println(candidates);
     //System.out.println(solution);
     //reader.nextLine();
     int index=0;
     while (index<candidates.size())
     {
    	 System.out.println("fire index" + index);
         if (solution.fits(candidates.get(index)))
         {
        	 System.out.println("fire fits");
             solution.record(candidates.remove(index)); //move candidate to solution
             if (solution.complete())
             {
            	 System.out.println("fire show");
                 solution.show();
                 System.exit(0);
             }
             else
             {
            	 System.out.println("fire solve");
                 solve();
             }
             candidates.add(index, solution.eraseRecording()); //move candidate to candidates
  
           }
           index++;
        }
}

}
        
          
         









