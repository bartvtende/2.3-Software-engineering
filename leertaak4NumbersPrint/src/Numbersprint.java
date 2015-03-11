
public class Numbersprint {

	Thread a = new Thread(){
		public void run(){
			print(1);
		};
	};
	
	Thread b = new Thread(){
		public void run(){
			print(2);
		};
	};
	
	Thread c = new Thread(){
		public void run(){
			print(3);
		};
	};
	
	Thread d = new Thread(){
		public void run(){
			print(4);
		};
	};
	
	public Numbersprint(){
		a.start();
		b.start();
		c.start();
		d.start();
	}
	
	private void print(int a){
		synchronized(System.out){
			System.out.print(a);
			System.out.print(a);
			System.out.println();
		}
	}
}
