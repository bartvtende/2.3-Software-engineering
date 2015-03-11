
public class Numbersprint {

	Thread a = new Thread(){
		public void run(){
			synchronized (System.out){
			System.out.print(1);
			System.out.print(1);
			System.out.println();
			}
		};
	};
	
	Thread b = new Thread(){
		public void run(){
			synchronized (System.out){
				System.out.print(2);
				System.out.print(2);
				System.out.println();
				}
		};
	};
	
	Thread c = new Thread(){
		public void run(){
			synchronized (System.out){
				System.out.print(3);
				System.out.print(3);
				System.out.println();
				}
		};
	};
	
	Thread d = new Thread(){
		public void run(){
			synchronized (System.out){
				System.out.print(4);
				System.out.print(4);
				System.out.println();
				}
		};
	};
	
	public Numbersprint(){
		a.start();
		b.start();
		c.start();
		d.start();
	}
}
