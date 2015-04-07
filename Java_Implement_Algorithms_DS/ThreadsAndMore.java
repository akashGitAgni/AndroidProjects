package JavaFun;


public class ThreadsAndMore {

	/**
	 * @param args
	 */
	static Processor p ;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new Worker();
		
		p = new Processor();
		
		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				p.consumer();
			}
		});
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				p.producer();
			}
		});
		t2.start();
		t1.start();
		
	}

}
