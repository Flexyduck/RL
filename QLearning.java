import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearning {

    private double tc ;
    private double[][] qValues;
    private State[][] state;
    private final double alpha;
    private double gamma;
    private int[][] rewards;
    private boolean[][] terminalStates;
    private Random rand;
    int ROWS = 3;
    int COLS = 4;
    private int prevAction;


    public double[][] getqValues() {
        return qValues;
    }

    public void initialize(){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                state[i][j] = new State(i,j);
            }
        }
    }
    public static void main(String[] args) {
        QLearning q = new QLearning(3,4,0.1,0.9,-0.1);
        q.initialize();
//        q.setReward(2,3,1);
//        q.setReward(1,3,-1);
//        q.setTerminalState(2,3,true);
//        q.setTerminalState(1,3,true);
//
//        q.addTerminal(2,3,1);
//        q.addTerminal(1,3,-1);

        q.addTerminal(2,3,1);
        q.addTerminal(1,3,-1);
        q.addTerminal(1,1,Double.NEGATIVE_INFINITY);
        q.train(3500,0,0);

        QLearningGUI display = new QLearningGUI(q);

    }

    public void print( )
    {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if(state[i][j].getCurrVal() == Double.NEGATIVE_INFINITY)
                    System.out.print("| Boulder|");
                else
                    System.out.printf("| %.2f   |", state[i][j].getCurrVal());

            }
            System.out.println();
        }
    }


    public QLearning(int rows, int cols, double learningRate, double discountFactor, double transitionCost) {
        this.alpha = learningRate;
        this.gamma = discountFactor;
        tc = transitionCost;
        this.rand = new Random();
        state = new State[rows][cols];

    }

    public void addTerminal(int row, int col, double val){
        State newState = new State(row, col);
        newState.setTerminal(true);
        newState.setCurrVal(val);
        state[row][col] = newState;


    }
    public void setReward(int row, int col, int reward) {
        rewards[row][col] = reward;
    }

    public void setTerminalState(int row, int col, boolean isTerminal) {
        terminalStates[row][col] = isTerminal;
    }

    public void train(int episodes,int row, int col) {
        for (int i = 0; i < episodes; i++) {
            System.out.println(i);
            trainEpisode(row, col);

            print();
        }
    }

    public int[] getNextState(int row, int col, int action) {
        // Get the next state (row, col) given the current state and action
        int newRow = row, newCol = col;
        switch (action) {
            case 2: // up
                if (row-1 >= 0 && state[row-1][col].getCurrVal() != Double.NEGATIVE_INFINITY) newRow--;
                break;
            case 4: // down
                if (row+1 < ROWS && state[row+1][col].getCurrVal() != Double.NEGATIVE_INFINITY ) newRow++;
                break;
            case 1: // left
                if (col-1 >= 0 && state[row][col-1].getCurrVal() != Double.NEGATIVE_INFINITY) newCol--;
                break;
            case 3: // right
                if (col+1 < COLS && state[row][col+1 ].getCurrVal() != Double.NEGATIVE_INFINITY ) newCol++;
                break;
        }
        return new int[] {newRow, newCol};
    }



    private void trainEpisode(int startRow, int startCol) {
        int currentRow = startRow;
        int currentCol = startCol;
        double reward = 0;
        int[] newState = new int[2];

        while(!state[currentRow][currentCol].isTerminal){

            int action = getAction();

            newState = getNextState(currentRow,currentCol,action);
            reward = getReward(newState[0], newState[1]);

            double sample = reward + (gamma* getMaxNeighbor(newState[0],newState[1]));

            double result = ((1-alpha) * state[currentRow][currentCol].getCurrVal() )+ alpha*sample;

            if(action == 1)
            {
                state[currentRow][currentCol].setEast(result);
            }

            if(action ==2 )
            {
                state[currentRow][currentCol].setNorth(result);
            }

            if(action ==3){
                state[currentRow][currentCol].setWest(result);
            }

            if(action==4)
            {
                state[currentRow][currentCol].setSouth(result);
            }


            state[currentRow][currentCol].setMaxQ();
            currentRow = newState[0];
            currentCol = newState[1];
        }

    }



    public  double getMaxNeighbor( int row, int col) {
        double max = Double.MIN_VALUE;

        if(state[row][col].isTerminal && state[row][col].getCurrVal() != Double.NEGATIVE_INFINITY)
        {
            return state[row][col].getCurrVal();
        }

        // Check neighbor above
        if (row-1 >= 0 && state[row-1][col].getCurrVal() > max) {
            max = state[row-1][col].getCurrVal();
        }

        // Check neighbor below
        if (row+1 < state.length && state[row+1][col].getCurrVal() > max ) {
            max = state[row+1][col].getCurrVal();
        }

        // Check neighbor to the left
        if (col-1 >= 0 && state[row][col-1].getCurrVal() > max) {
            max = state[row][col-1].getCurrVal();
        }

        // Check neighbor to the right
        if (col+1 < state[0].length && state[row][col+1].getCurrVal() > max) {
            max = state[row][col+1].getCurrVal();
        }

        return max;
    }



    public State[][] getGrid() {
        return state;
    }

    private double getReward(int x, int y) {
        if (x == 1 && y == 3) {
            return -1; // negative reward for exit -1
        } else if (x == 2 && y == 3) {
            return 1; // positive reward for exit +1
        } else {
            return 0; // no reward for all other states
        }
    }

    public int getAction()
    {
        int result = rand.nextInt(4)+1;
//        while(result== prevAction)
//        {
//            result= rand.nextInt(4)+1;
//        }
        return result;
    }


    public double returnMax(double[] arr){
        double maxVal = 0.0;

        for (int i = 0; i < arr.length; i++) {
            maxVal = Math.max(maxVal, arr[i]);
        }

        return maxVal;
    }
}
