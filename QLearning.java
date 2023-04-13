import java.util.Random;

public class QLearning {

    private double[][] qValues;
    private double learningRate;
    private double discountFactor;
    private int[][] rewards;
    private boolean[][] terminalStates;
    private Random rand;
    int ROWS = 3;
    int COLS = 4;


    public static void main(String[] args) {
        QLearning q = new QLearning(3,4,0.1,0.9);
        q.setReward(2,3,1);
        q.setReward(1,3,-1);
        q.setTerminalState(2,3,true);
        q.setTerminalState(1,3,true);

        q.train(3500);
        q.print();
    }

    public void print( )
    {
        for (int i = 0; i < qValues.length; i++) {
            for (int j = 0; j < qValues[i].length; j++) {

                System.out.printf("| %.2f   |", qValues[i][j]);

            }
            System.out.println();
        }
    }
    public QLearning(int rows, int cols, double learningRate, double discountFactor) {
        this.qValues = new double[rows][cols];
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.rewards = new int[rows][cols];
        this.terminalStates = new boolean[rows][cols];
        this.rand = new Random();
    }

    public void setReward(int row, int col, int reward) {
        rewards[row][col] = reward;
    }

    public void setTerminalState(int row, int col, boolean isTerminal) {
        terminalStates[row][col] = isTerminal;
    }

    public void train(int episodes) {
        for (int i = 0; i < episodes; i++) {
            int row = rand.nextInt(qValues.length);
            int col = rand.nextInt(qValues[0].length);
            while (terminalStates[row][col]) {
                row = rand.nextInt(qValues.length);
                col = rand.nextInt(qValues[0].length);
            }
            trainEpisode(row, col);
        }
    }

    public int[] getNextState(int row, int col, int action) {
        // Get the next state (row, col) given the current state and action
        int newRow = row, newCol = col;
        switch (action) {
            case 0: // up
                if (row > 0) newRow--;
                break;
            case 1: // down
                if (row < ROWS - 1) newRow++;
                break;
            case 2: // left
                if (col > 0) newCol--;
                break;
            case 3: // right
                if (col < COLS - 1) newCol++;
                break;
        }
        return new int[] {newRow, newCol};
    }

    private void trainEpisode(int startRow, int startCol) {
        int currentRow = startRow;
        int currentCol = startCol;
        while (!terminalStates[currentRow][currentCol]) {
            int action = chooseAction(currentRow, currentCol);
            int[] newState = getNextState(currentRow, currentCol, action);
            double reward = getReward(newState[0], newState[1]);
            qValues[currentRow][currentCol] = (1-learningRate)* qValues[currentRow][currentCol] + learningRate *
                    (reward + discountFactor * getMaxQValue(newState[0], newState[1]) - qValues[currentRow][currentCol]);
            currentRow = newState[0];
            currentCol = newState[1];
        }
    }

    private double getMaxQValue(int x, int y) {
        double maxQ = -Double.MAX_VALUE;
        for (int a = 0; a < 4; a++) {
            double q = qValues[x][y];
            if (q > maxQ) {
                maxQ = q;
            }
        }
        return maxQ;
    }


    private int getReward(int x, int y) {
        if (x == 1 && y == 3) {
            return -1; // negative reward for exit -1
        } else if (x == 2 && y == 3) {
            return 1; // positive reward for exit +1
        } else {
            return 0; // no reward for all other states
        }
    }


    private int chooseAction(int row, int col) {
        int[] maxActionIndices = getMaxActionIndices(row, col);
        if (maxActionIndices.length == 1) {
            return maxActionIndices[0];
        } else {
            return maxActionIndices[rand.nextInt(maxActionIndices.length)];
        }
    }
    private double getQValue(int x, int y, int a) {
        return qValues[x][y];
//        double result = 0;
//        //north
//        if(a == 1 && y-1 >= 0)
//        {
//            result = qValues[x][y-1];
//        }
//
//        else if (a == 2 && x-1 >=0)
//        {
//            result = qValues[x-1][y];
//        }
//
//
//        else if (a == 3 && y +1 < COLS)
//        {
//            result = qValues[x][y+1];
//        }
//
//        else if (a == 4 && x+1 < ROWS)
//        {
//            result = qValues[x+1][y];
//        }
//        return result;
    }


    private int[] getMaxActionIndices(int row, int col) {
        int[] maxActionIndices = new int[4];
        double max = Double.NEGATIVE_INFINITY;
        int numMaxActions = 0;
        double[] values = new double[4];
        values[0] = getQValue(row, col, 1);
        values[1] = getQValue(row, col, 2);
        values[2] = getQValue(row, col, 3);
        values[3] = getQValue(row, col, 4);
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
                maxActionIndices[0] = i;
                numMaxActions = 1;
            } else if (values[i] == max) {
                maxActionIndices[numMaxActions] = i;
                numMaxActions++;
            }
        }
        int[] result = new int[numMaxActions];
        for (int i = 0; i < numMaxActions; i++) {
            result[i] = maxActionIndices[i];
        }
        return result;
    }


}
