package JavaFun;

import java.util.LinkedList;
import java.util.Random;

public class Processor {

	LinkedList< Integer> lList=  new LinkedList<>();
	int size =10;
	Object lock = new Object();
	
	
	public void producer(){
		
		Random rr = new Random();
		while(true)
		{
			synchronized (lock) {

				while(lList.size() == size)
				{
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int i = rr.nextInt(10);
				System.out.println("Adding" + i);
				lList.add(i);
				System.out.println("Size list" + lList.size());
				
				lock.notify();
			}
			
		}	
		
	}
	
	public void consumer()
	{
		synchronized (lock) {

		while(true)
		{	
			
			try {
				while(lList.size() <=0)
				{	
					lock.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			System.out.println("Removed" +lList.remove());
			
			lock.notify();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}	
		
		
		
	

}
