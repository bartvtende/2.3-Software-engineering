
public class Task extends Thread {

	private int number;
	
	public Task(int i){
		number = i;
	}
	
	@Override
	public void run(){
		print(number);
	}
	
	private void print(int a){
		synchronized(System.out){
			System.out.print(a);
			System.out.print(a);
			System.out.println();
		}
	}
}
