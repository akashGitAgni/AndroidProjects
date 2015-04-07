

/**
 * @author Akash Agnihotri
 * Class to Test the application
 */
public class Tester {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Bridge singleLane = new Bridge();

		for (int i = 0; i < 100; i++) {

			if (Math.random() < 0.5) {
				new South(singleLane,i).start();
			} else {
				new North(singleLane,i).start();
			}
			
		}
		try {
			Thread.sleep((int) (Math.random() * 2000));
		} catch (InterruptedException e) {
		}
	}

}
