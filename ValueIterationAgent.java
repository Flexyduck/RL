import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import static java.lang.String.valueOf;

public class ValueIterationAgent {

    //Instance variables from the file
    static int horizontal,vertical,K;

    static int Episodes;
    static double Discount;

    static double alpha;
    static double Noise;
    static double TransitionCost;
    static ArrayList<ExitState> Terminals = new ArrayList<>();
    static ArrayList<Boulder> Boulders = new ArrayList<>();
    static int[] RobotStartState = new int[2];

    //Constructor
    public ValueIterationAgent(String fileName){
        readFile(fileName);
    }

    /**
     * This function performs the Value Iterating by iterating over the grid K times
     *
     * @param grid the grid to be iterated over
     */
    public void iterateOver(Grid grid){
        ArrayList<State> newState = new ArrayList<>();
        int [] actions = {1,2,3,4};
        double [] qValues = new double[4];
        double [] values;
        int count = 0;
        while (count != K) {
            if(count == 0){
                setGridTerminals(grid);
                count++;
                continue;
            }
            for (int i = 0; i < grid.getRow(); i++) {
                for (int j = 0; j < grid.getCol(); j++) {
                    State currState = grid.getState(i, j);
                    if (grid.isTerminal(i,j)|| grid.isBoulder(i,j)) {
                        continue;
                    }
                    for (int l = 0; l < actions.length; l++) {
                        grid.getState(i, j).setActionTaken(actions[l]);
                        values = grid.getOtherCells(currState);
                        qValues[l] = calculateIterationValue(values[0], values[1], values[2]);
                    }
                    currState.setEast(qValues[0]);
                    currState.setNorth(qValues[1]);
                    currState.setWest(qValues[2]);
                    currState.setSouth(qValues[3]);
                    if (returnMax(qValues) != 0) {
                        State temp = new State(i, j, returnMax(qValues));
                        newState.add(temp);
                    }
                }
            }
            count++;
            updateGrid(newState, grid);
        }


    }


    /**
     * This grid updates the values of the grid
     *
     * @param newStates An arraylist of updated states in the grid
     * @param grid the grid to be updates
     */
    public static void updateGrid(ArrayList<State> newStates, Grid grid){
        for (State state : newStates) {
            grid.getState(state.getH(), state.getV()).setCurrVal(state.getCurrVal());
        }
    }

    /**
     * This function calculates the V(k+1) position using the noise, discount and transition cost
     *
     * @param pos1 The direction the robot is going
     * @param pos2 The other direction the robot can go
     * @param pos3 The other direction the robot can go
     * @return the V(k+1) position
     */
    private static double calculateIterationValue(double pos1, double pos2, double pos3){
        double badProbAlt = (Noise)/2;
        double goodProb = 1-Noise;
        return (goodProb*(TransitionCost + (Discount*pos1)) + badProbAlt*(TransitionCost + (Discount*pos2)) + badProbAlt*(TransitionCost + (Discount*pos3)));
    }

    /**
     * This return the best direction a robot can go from a certain state
     *
     * @param state the state we wish to calculate the value from
     * @return a message indicating what direction is best
     */
    public String computeActionFromValues(State state){
        Grid newGrid2 = createGrid();
        iterateOver(newGrid2);
        int bestAction = newGrid2.findDirectionToGo(state.getH(),state.getV());
        String directionToGo = "";
        if (bestAction == 1){
            directionToGo = "Go East";
        }
        else if (bestAction == 2){
            directionToGo = "Go North";
        }
        else if (bestAction == 3){
            directionToGo = "Go West";
        }
        else if (bestAction == 4){
            directionToGo = "Go South";
        }
        return directionToGo;
    }

    /**
     * This function calculates the Qvalue of a state from a given action
     *
     * @param state The state we wish to get Q value from
     * @param action The action being performed from which we search a Q-Value
     * @return the Qvalue
     */
    public double computeQValueFromValues(State state, int action){
        Grid newGrid2 = createGrid();
        iterateOver(newGrid2);
        double qVal = 0;
        if (action == 1){
            qVal = newGrid2.getState(state.getH(),state.getV()).getEast();
        }
        else if (action == 2){
            qVal = newGrid2.getState(state.getH(),state.getV()).getNorth();
        }
        else if (action == 3){
            qVal = newGrid2.getState(state.getH(),state.getV()).getWest();
        }
        else if (action == 4){
            qVal = newGrid2.getState(state.getH(),state.getV()).getSouth();
        }
        return qVal;
    }

    /**
     * This function calculates the optimal policy a robot should take to
     * get to a reward state
     *
     * @param grid the grid from which we search the optimal policy from
     */
    public void createOptimalPolicy(Grid grid){
        ArrayList<State> optimalPolicy = new ArrayList<>();
        State robot = grid.getState(RobotStartState[0], RobotStartState[1]);
        optimalPolicy.add(robot);
        while(!grid.isTerminal(robot.getH(),robot.getV())){
            robot = grid.optimalPolicy(robot);
            optimalPolicy.add(robot);
        }
        for (int i = 0; i < optimalPolicy.size(); i++) {
             if (i == optimalPolicy.size()-1) {
                System.out.print(optimalPolicy.get(i));
                break;
            }
                System.out.print(optimalPolicy.get(i) + "->");
        }
    }

    /**
     * This function finds the value of a state after K iterations of our algorithm
     *
     * @param h the horizontal coordinate of the states
     * @param v the vertical coordinate of the states
     * @param k the number of iterations to be found
     * @return the value of the state after k iterations
     */
    public double returnStateValue(int h, int v, int k){
        Grid newGrid2 = createGrid();
        K = k;
        iterateOver(newGrid2);

        return newGrid2.getState(h,v).getCurrVal();
    }

    /**
     * This function find the appropriate position for a robot to go from given states
     *
     * @param h h the horizontal coordinate of the states
     * @param v v the vertical coordinate of the states
     * @param k k the number of iterations to be found
     * @return a string indicating the best direction to go
     */
    public String returnBestPolicy(int h, int v, int k) {
        Grid newGrid2 = createGrid();
        K = k;
        iterateOver(newGrid2);
        ValueIterGUI valueIterGUI = new ValueIterGUI(newGrid2);
        return computeActionFromValues(newGrid2.getState(h, v));
    }

    /**
     * This function helps find the best direction a robot should go by
     * getting the highest value from an array of nearby state values
     *
     * @param arr an array which we search the max value from
     * @return the max value from an array of doubles
     */
    public static double returnMax(double[] arr){
        double maxVal = 0.0;
        for (double v : arr) {
            maxVal = Math.max(maxVal, v);
        }
        return maxVal;
    }

    /**
     * This creates and initializes a grid with 0 values and exit states
     * @return the initialized grid
     */
    public Grid createGrid(){
        Grid newGrid = new Grid(vertical, horizontal);
        newGrid.initialize();
        for (State boulder : Boulders) {
            newGrid.setBoulder(boulder);
        }
        return newGrid;
    }

    /**
     * This sets the grid exit states
     * @param newGrid the grid to be modified
     */
    public static void setGridTerminals(Grid newGrid){
        for (ExitState terminal : Terminals) {
            newGrid.setTerminal(terminal);
        }
    }

    /**
     * This file reads a file and processes it line by line
     *
     * @param filePath the file to be read
     */
    public  void readFile(String filePath){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                processLine(line);
            }

            reader.close();
        } catch(Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * This processes each line of a file and assigns the appropriate variables the values
     *
     * @param line the line to be processed
     */
    public static void processLine(String line){
        if(line.charAt(0) == 'H'){
            horizontal = (int)extractNumber(line);;  ;
        }
        else if(line.charAt(0) == 'T'){
            if(line.charAt(1) == 'r'){
                TransitionCost =extractNumber(line);; ;
            } else if (line.charAt(1) == 'e') {
                Terminals = createExitStates(processTerminal(line));
            }

        }
        else if(line.charAt(0) == 'V'){
            vertical = (int)extractNumber(line);;  ;
        }
        else if(line.charAt(0) == 'B'){
            Boulders = createBoulders(processCoordinates(processBoulder(line)));
        }
        else if(line.charAt(0) == 'R'){
            System.arraycopy(processStart(line), 0, RobotStartState, 0, processStart(line).length);

        }
        else if(line.charAt(0) == 'K'){
            K = (int)extractNumber(line);
        }
        else if(line.charAt(0) == 'E'){
            Episodes  = (int) extractNumber(line);;  ;
        }
        else if(line.charAt(0) == 'D'){
            Discount = extractNumber(line);;  ;
        }
        else if(line.charAt(0) == 'N'){
            Noise = extractNumber(line);;  ;

        }
        else if(line.charAt(0) == 'a'){
            alpha  = extractNumber(line);;  ;
        }
    }

    /**
     * This function creates exitStates
     *
     * @param T This creates the ExitState objects from an array of integer co-ordinates
     * @return The list of created ExitStates
     */
    public static ArrayList<ExitState> createExitStates(ArrayList<int[]> T){
        ArrayList<ExitState> exit = new ArrayList<>();
        ExitState E;
        int eROw =0, eCOl = 0, eReward = 0;
        for (int[] ints : T) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    eROw = ints[j];
                } else if (j == 1) {
                    eCOl = ints[j];
                } else {
                    eReward = ints[j];
                }
            }
            E = new ExitState(eROw, eCOl, eReward);
            exit.add(E);
        }
        return exit;
    }
    /**
     * This function creates boulders
     *
     * @param T This creates the Boulder objects from an array of integer co-ordinates
     * @return The list of created Boulders
     */
    public static ArrayList<Boulder> createBoulders(ArrayList<int[]> T){
        ArrayList<Boulder> boulders = new ArrayList<>();
        Boulder B;
        int eROw =0, eCOl = 0;
        for (int i = 0; i < T.size(); i++) {
            for (int j = 0; j < 2; j++) {
                if(j == 0){
                    eROw = T.get(i)[j];
                }
                else {
                    eCOl = T.get(i)[j];
                }
            }
            B = new Boulder(eROw,eCOl);
            boulders.add(B);
        }
        return boulders;
    }

    //Helper functions for file reading
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
    public static int[] processStart(String s){
        int newStart = loopTillEquals(s)+2;
        int[] startStates = new int[2];
        int counter = 0;
        for (int i = newStart; i < s.length(); i++) {
            if(Character.isDigit(s.charAt(i))){
                startStates[counter] = Character.getNumericValue(s.charAt(i));
                counter++;
            }
        }

        return startStates;
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
    public static ArrayList<int[]> processTerminal(String sentence) {
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
        return hm;
    }
}