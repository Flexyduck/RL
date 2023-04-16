import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private final State [][] grid;
    private final int row;
    private final int col;

    private final int k;
    public Grid(int vertical, int horizontal) {
        row = vertical;
        col = horizontal;
        grid = new State [row][col];
         k= 0;
    }
    public void initialize(){
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = new State(i,j);
            }
        }
    }
    public int getK() {
        return k;
    }
    public State[][] getGrid() {
        return grid;
    }
    public void add(int row, int col, double val){
        State newState = new State(row, col);
        newState.setCurrVal(val);
        grid[row][col] = newState;
    }
    public double[] getOtherCells(State s){
        double[] retArr = new double[3];
        double pos1 = 0;
        double pos2 = 0;
        double pos3 = 0;
        //east
        if(s.getActionTaken() == 1){
            pos1 = getEast(s);
            pos2 = getNorth(s);
            pos3 = getSouth(s);
        }
        //north
        else if(s.getActionTaken() == 2){
            pos1 = getNorth(s);
            pos2 = getEast(s);
            pos3 = getWest(s);
        }
        //west
       else if(s.getActionTaken() == 3){
            pos1 = getWest(s);
            pos2 = getNorth(s);
            pos3 = getSouth(s);
        }
       //south
        else if(s.getActionTaken() == 4){
            pos1 = getSouth(s);
            pos2 = getEast(s);
            pos3 = getWest(s);
        }
        retArr[0] = pos1;
        retArr[1] = pos2;
        retArr[2] = pos3;

        return retArr;
    }
    public boolean isValidCell(int row, int col){
        boolean isValid = false;
        if(row >=0 && row <= this.row-1){
            if(col >=0 && col<= this.col-1){
                isValid = true;
            }
        }
        return isValid;
    }
    public boolean isBoulder(int row, int col){
        return grid[row][col].getClass().equals(Boulder.class);
    }
    public boolean isTerminal(int row, int col){
        return grid[row][col].getClass().equals(ExitState.class);
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public double getNorth(State s){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH()+1, s.getV()) && !isBoulder(s.getH()+1, s.getV())){
            retState = grid[s.getH()+1][s.getV()].getCurrVal();
        }
        return retState;
    }
    public double getSouth(State s){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH()-1, s.getV()) && !isBoulder(s.getH()-1, s.getV())){
            retState = grid[s.getH()-1][s.getV()].getCurrVal();
        }
        return retState;
    }
    public double getEast(State s){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH(), s.getV()+1) && !isBoulder(s.getH(), s.getV()+1)){
            retState = grid[s.getH()][s.getV()+1].getCurrVal();
        }
        return retState;
    }
    public  double getWest(State s){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH(), s.getV()-1) && !isBoulder(s.getH(), s.getV()-1)){
            retState = grid[s.getH()][s.getV()-1].getCurrVal();
        }
        return retState;
    }
    public State optimalPolicy(State s){
        State nextState = new State(s.getH(),s.getV());

        double [] values = {getEast(s), getNorth(s), getWest(s), getSouth(s)};

        double max = 0.0;
        int bestWayToGo = 0;
        for (int i = 0; i < values.length; i++) {
            if(values[i] > max){
                max = values[i];
                bestWayToGo = i+1;
            } else if (values[i] == max) {
                double [] randm = {max, values[i]};
                Random rand = new Random();
                max = (rand.nextInt(randm.length));
            }
        }
        //east
        if(bestWayToGo == 1){
            nextState = getState(s.getH(), s.getV()+1);
        }
        //north
        else if(bestWayToGo == 2){
            nextState = getState(s.getH()+1, s.getV());
        }
        //west
        else if(bestWayToGo == 3){
            nextState = getState(s.getH(), s.getV()-1);
        }
        //south
        else if(bestWayToGo == 4){
            nextState = getState(s.getH()-1, s.getV());
        }

        return nextState;
    }
    public void printGrid() {
        System.out.print("+");
        for (int i = 0; i < col; i++) {
            System.out.print("--------+");
        }
        System.out.println();
// loop through the grid and set the values
        for (int i = row - 1; i >= 0; i--) {
            System.out.print("|");
            for (int j = 0; j < col; j++) {
                if(isBoulder(i,j)){
                    System.out.printf("        |");
                }
                else{
                    if(isTerminal(i,j)){
                        if(grid[i][j].getCurrVal() > 0) {
                            System.out.printf(" %.2f  |", grid[i][j].getCurrVal());
                        }
                        else {
                            System.out.printf(" %.2f |", grid[i][j].getCurrVal());
                        }
                    }
                    else{
                        System.out.printf(" %.2f   |", grid[i][j].getCurrVal());
                    }
                }

            }
            System.out.println();

            System.out.print("+");
            for (int j = 0; j < col; j++) {
                System.out.print("--------+");
            }
            System.out.println();
        }
    }
    public int findHighestAdjacentValue(int row, int col) {
        double max = Double.MIN_VALUE;
        int result = -1;

        // Check north
        if (row > 0 && grid[row-1][col].getCurrVal() > max) {
            max = grid[row-1][col].getCurrVal();
            result = 2;
        }

        // Check south
        if (row < grid.length-1 && grid[row+1][col].getCurrVal() > max) {
            max = grid[row+1][col].getCurrVal();
            result = 4;
        }

        // Check east
        if (col > 0 && grid[row][col-1].getCurrVal() > max) {
            max = grid[row][col-1].getCurrVal();
            result = 1;

        }

        // Check west
        if (col < grid[0].length-1 && grid[row][col+1].getCurrVal() > max) {
            result = 3;

        }


        return result;
    }
    public int findDirectionToGo(int row, int col) {
        double max = Double.MIN_VALUE;
        State checkState = getState(row,col);
        int bestWayToGo = 0;

        double [] values = {getEast(checkState), getNorth(checkState), getWest(checkState), getSouth(checkState)};
        for (int i = 0; i < values.length; i++) {
            if(values[i] > max){
                max = values[i];
                bestWayToGo = i+1;
            }
        }
        return bestWayToGo;
    }
    public State getState(int row, int col) {
        return grid[row][col];
    }
    public void setTerminal(ExitState endState){
        grid[endState.getH()][endState.getV()] = endState;
    }
    public void setBoulder(State boulder){
        grid[boulder.getH()][boulder.getV()] = boulder;
    }

}
