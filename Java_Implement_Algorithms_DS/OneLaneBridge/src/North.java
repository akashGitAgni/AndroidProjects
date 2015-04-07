

/**
 * @author Akash Agnihotri
 * Cars going south from North
 */
public class North extends Thread {
    private Bridge lane;
    int carid;
   
    public North(Bridge bridge, int carid) {
    	this.lane = bridge;
    	this.carid = carid;
    }

    public void run() {
        lane.goSouth(carid);        
    }

}