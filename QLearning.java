import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.String.valueOf;

public class QLearning {

    private static  double transitionCost ;
    private double[][] qValues;
    private State[][] state;
    private static  double alpha;
    private static  double gamma;
    private int[][] rewards;
    private boolean[][] terminalStates;
    private Random rand;
    private static int ROWS;
    private static int COLS;
    private int prevAction;

    private static int episodes;

    private static int startX,startY;


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
    public static void main(String[] args) throws IOException{
        readFile("gridConf.txt");
        QLearning q = new QLearning(ROWS,COLS,alpha,gamma,transitionCost);
        q.initialize();

        q.addTerminal(1,3,10);
        q.addTerminal(1,4,-10);
        q.addTerminal(0,2,-10);
        q.addTerminal(1,2,Double.NEGATIVE_INFINITY);
        q.addTerminal(4,4,Double.NEGATIVE_INFINITY);
        q.train(episodes,startX,startY);
        QLearningGUI display = new QLearningGUI(q);

    }




    public  int getCOLS() {
        return COLS;
    }

    public  int getEpisodes() {
        return episodes;
    }

    public  int getROWS() {
        return ROWS;
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
        this.transitionCost = transitionCost;
        this.rand = new Random();
        state = new State[rows][cols];

    }

    public void addTerminal(int row, int col, double val){
        State newState = new State(row, col);
        newState.setTerminal(true);
        newState.setCurrVal(val);
        state[row][col] = newState;


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
            return transitionCost; // no reward for all other states
        }
    }

    public int getAction()
    {
        return rand.nextInt(4)+1;
    }


    public double returnMax(double[] arr){
        double maxVal = 0.0;

        for (int i = 0; i < arr.length; i++) {
            maxVal = Math.max(maxVal, arr[i]);
        }

        return maxVal;
    }


    public static void readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            processLine(line);
        }

        reader.close();
    }
    public static void processLine(String line){

        if(line.charAt(0) == 'H'){
            COLS = (int)extractNumber(line);;  ;
        }
        else if(line.charAt(0) == 'T'){
            if(line.charAt(1) == 'r'){
                transitionCost =extractNumber(line);; ;
            } else if (line.charAt(1) == 'e') {
                 //createExitStates(processTerminal(line));
            }

        }
        else if(line.charAt(0) == 'V'){
            ROWS = (int)extractNumber(line);;  ;
        }
        else if(line.charAt(0) == 'B'){

        }
        else if(line.charAt(0) == 'R'){
          processStart(line);

        }
        else if(line.charAt(0) == 'K'){

        }
        else if(line.charAt(0) == 'E'){
            episodes  = (int) extractNumber(line);;  ;
        }
        else if(line.charAt(0) == 'D'){
            gamma = extractNumber(line);;  ;
        }

        else if(line.charAt(0) == 'a'){
            alpha  = extractNumber(line);;  ;
        }
    }
    public static int loopTillEquals(String s){
        int equalsPos = 0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '='){
                equalsPos = i;
                break;
            }
        }
        return equalsPos;
    }
    public static ArrayList<String> processBoulder(String s){
        int newStart = loopTillEquals(s)+2;
        ArrayList<String> idk = new ArrayList<>();
        int commaCounter = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < (s.length()-newStart-1); i++) {
            if(s.charAt(newStart+i) == ','){
                commaCounter++;
            }
            if(commaCounter != 2) {
                stringBuilder.append(s.charAt(newStart + i));
            }
            else{
                idk.add(valueOf(stringBuilder));
                stringBuilder = new StringBuilder();
                commaCounter = 0;
            }
            if(i == (s.length()-newStart-2)){
                idk.add(valueOf(stringBuilder));
            }
        }
        return idk;
    }
    public static void processStart(String s){
        int newStart = loopTillEquals(s)+2;
        int[] startStates = new int[2];
        int counter = 0;
        for (int i = newStart; i < s.length(); i++) {
            if(Character.isDigit(s.charAt(i))){
                startStates[counter] = Character.getNumericValue(s.charAt(i));
                counter++;
            }
        }

        startX = startStates[0];
        startY = startStates[1];

    }
    public static ArrayList<int[]> processCoordinates(ArrayList<String> coordList){
        ArrayList<int[]> idk = new ArrayList<>();
        for (String cList : coordList) {
            int newStart = loopTillEquals(cList) + 1;
            int [] coordinates = new int[2];
            int checker = 0;
            for (int j = newStart; j <= (cList.length() - newStart); j++) {
                if (Character.isDigit(cList.charAt(j))) {
                    coordinates[checker] = Character.getNumericValue(cList.charAt(j));
                    checker++;
                }
            }
            idk.add(coordinates);
        }
        return idk;
    }
    public static double extractNumber(String str) {
        String[] parts = str.split("=");
        String numberStr = parts[1].trim();
        return Double.parseDouble(numberStr);
    }
    public void processTerminal(String sentence) {
        int newStart = loopTillEquals(sentence)+1;
        sentence = sentence.substring(newStart+1);
        ArrayList<int[]> hm = new ArrayList<>();
        for(int i = 0; i< sentence.length(); i++){
            if(sentence.charAt(i)== '{'){
                int [] who = new int[3];
                i++;
                String newSent = sentence.substring(sentence.indexOf('{')+1,sentence.indexOf('}'));
                String [] toBeParsed = newSent.split(",");
                for (int j = 0; j < who.length; j++) {
                    who[j] = Integer.parseInt(toBeParsed[j]);
                }
                sentence =sentence.substring(sentence.indexOf('}')+1);
                i = 0;
                hm.add(who);
            }
        }

    }



}
