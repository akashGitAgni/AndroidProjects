package JavaFun;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

public class Worker {

	Object lock1 = new Object();
	Object lock2 = new Object();
	ArrayList<Integer> list1 = new ArrayList<Integer>();
	ArrayList<Integer> list2 = new ArrayList<Integer>();
	Random random = new Random();
	
	
	public  void stage1()
	{
		synchronized (list1) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list1.add(random.nextInt());
		}
		
	}
	public  void stage2()
	{
		synchronized (list2) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list2.add(random.nextInt());
		}
		
	}
	
	
	public void process()
	{
		for(int i=0 ; i<1000 ;i++)
		{
			stage1();
			stage2();
		}	
		
	}
	
	public Worker()
	{
		System.out.println("Starting - -");
		long time = System.currentTimeMillis();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				process();
				
			}
		});
		t1.start();
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				process();
				
			}
		});
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Size : " + list1.size() +"--"+list2.size());
		System.out.println("Time"+ (time-System.currentTimeMillis()));
	}
	
	
}
