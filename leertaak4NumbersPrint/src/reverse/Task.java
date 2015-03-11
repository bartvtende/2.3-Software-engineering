package reverse;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Task extends Thread {
	
	private static Lock lock = new ReentrantLock();
	private static Condition tooHigh = lock.newCondition(); 
	
	private int number;
	
	private static int next = 4;
	
	public Task(int i){
		number = i;
	}
	
	@Override
	public void run(){
		try {
			print(number);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void print(int a) throws InterruptedException{
		lock.lock();
		try{
			
			while(number < next){
				tooHigh.await();
			}
			System.out.print(a);
			System.out.print(a);
			System.out.println();
			next=next-1;
			tooHigh.signalAll();
		}
		finally{
			lock.unlock();
		}
	}
}
