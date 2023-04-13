import java.util.Queue;

public class State {

    private int h,v;
    private double currVal;
    private int[] possibleActions;
    private int actionTaken;
    private double oldVal;
    private double north, south, east, west;



    public State(int h, int v) {
        this.h = h;
        this.v = v;
        currVal = 0.0;
        oldVal = 0.0;
        possibleActions = new int[3];
        actionTaken = 0;
        //isTerminal = false;
    }
    public State(int h, int v, double currVal) {
        this.h = h;
        this.v = v;
        this. currVal = currVal;
        possibleActions = new int[3];
        actionTaken = 0;
    }

    public double getEast() {
        return east;
    }

    public double getNorth() {
        return north;
    }

    public double getSouth() {
        return south;
    }

    public double getWest() {
        return west;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public void setWest(double west) {
        this.west = west;
    }

    public int getH() {
        return h;
    }

    public int getActionTaken() {
        return actionTaken;
    }
    public void setActionTaken(int actionTaken) {
        this.actionTaken = actionTaken;
        //east
    }
    public int getV() {
        return v;
    }
    public double getCurrVal(){
        return currVal;
    }
    public void setCurrVal(double currVal) {
        this.oldVal = this.currVal;
        this.currVal = currVal;
    }

//    public int findHighestAdjacentValue() {
//        double [] qValues = {east, north, west, south};
//        double max = 0.0;
//        int direction = 0;
//        for (int i = 0; i < qValues.length; i++) {
//            if(qValues[i] > max){
//                max = qValues[i];
//                direction = i+1;
//            }
//        }
//        return direction;
//    }

    @Override
    public String toString() {
        return ("{" + h + "," + v + "}" );
    }
}
