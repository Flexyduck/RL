import java.util.Queue;

public class State {

    private int h,v;
    private double currVal;
    private int[] possibleActions;
    private int actionTaken;


    public boolean isTerminal;
    private double north, south, east, west;



    public State(int h, int v) {
        this.h = h;
        this.v = v;
        currVal = 0.0;
        possibleActions = new int[3];
        actionTaken = 0;
        isTerminal = false;
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


    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
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
        this.currVal = currVal;
    }



public void  setMaxQ()
{

    double max = getEast();
    if( max < getNorth()) {
        max = getNorth();

    }
     if( max < getWest()) {
        max = getWest();

    }
     if (max < getSouth()) {
        max = getSouth();
    }


    setCurrVal(max);

}



    public void setAllAction(int action, double val)
    {
        if( action == 1)
            setEast(val);
        else if ( action == 2)
            setNorth(val);
        else if( action == 3)
            setWest(val);
        else if (action == 4)
            setSouth(val);
    }
    @Override
    public String toString() {
        return ("{" + h + "," + v + "}" );
    }
}
