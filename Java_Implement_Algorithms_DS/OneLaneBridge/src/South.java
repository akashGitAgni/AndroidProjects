

/**
 * @author Akash Agnihotri
 * Cars going North from south
 */
public class South extends Thread {
    private Bridge lane;
    int carid;
    public South(Bridge bridge,int carid) {
    	this.lane = bridge;
    	this.carid = carid;
    }

    public void run() {
        lane.goNorth(carid);        
    }

}