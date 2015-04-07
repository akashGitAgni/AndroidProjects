import java.util.concurrent.Semaphore;


public class Bridge {
	
	Semaphore north = new Semaphore(2);
	Semaphore south =  new Semaphore(2);
	
	
	
	
	public void goSouth(int carid)
	{
		System.out.println("Avalaible Permits SOuth" + south.availablePermits());
		try {
			south.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {}
		
		south.release();
		System.out.println("Car exits from south -- " + carid);
	}
	
	
	public void goNorth(int carid)
	{
		System.out.println("Avalaible Permits North" + north.availablePermits());
		try {
			north.acquire();
		
            Thread.sleep(4000);
        } catch (InterruptedException e) {}
		
		north.release();
		System.out.println("Car exits from North -- " + carid);
		
	}

}
