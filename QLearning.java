import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class QLearning {

    private static  double TransitionCost ;
    static ArrayList<ExitState> Terminals = new ArrayList<>();
    static ArrayList<Boulder> Boulders = new ArrayList<>();
    static int[] RobotStartState = new int[2];
    private static  double alpha;
    static int horizontal;
    static int vertical;
    static int K;
    static int Episodes;
    static double Discount;

    static double Noise;

    static double s;

    public QLearning(String fileName){
        readFile(fileName);
        train(RobotStartState[0], RobotStartState[1]);
    }
    public static Grid train(int row, int col) {
        Grid newGrid = createGrid();
        setGridTerminals(newGrid);
//        grid.printGrid();
        for (int i = 0; i < Episodes; i++) {
           // System.out.println("Episode: "+i);
            trainEpisode(row, col,newGrid);
//           grid.printGrid();
        }
        s = Terminals.get(0).getCurrVal();
//        getPolicy(newGrid);
        QLearningGUI display = new QLearningGUI(newGrid, s );
        return newGrid;
    }
    public static int[] getNextState(int row, int col, int action, Grid grid) {
        // Get the next state (row, col) given the current state and action
        int newRow = row, newCol = col;
        switch (action) {
            case 2: // up
                if (row-1 >= 0 && grid.getGrid()[row-1][col].getCurrVal() != Double.NEGATIVE_INFINITY && grid.isValidCell(row,col))newRow--;
                break;
            case 4: // down
                if (row+1 < vertical && grid.getGrid()[row+1][col].getCurrVal() != Double.NEGATIVE_INFINITY && grid.isValidCell(row,col)) newRow++;
                break;
            case 1: // left
                if (col-1 >= 0 && grid.getGrid()[row][col-1].getCurrVal() != Double.NEGATIVE_INFINITY && grid.isValidCell(row,col)) newCol--;
                break;
            case 3: // right
                if (col+1 < horizontal && grid.getGrid()[row][col+1 ].getCurrVal() != Double.NEGATIVE_INFINITY && grid.isValidCell(row,col)) newCol++;
                break;
        }
        return new int[] {newRow, newCol};
    }
    public static int[] getNextValidCell(int row, int col, Grid grid) {
        int newRow = row;
        int newCol = col;

        double max = Double.NEGATIVE_INFINITY;

        if(grid.isValidCell(row,col-1) && max < grid.getGrid()[row][col].getEast() && !grid.isBoulder(row,col-1) ) {
            newCol = col -1;
            max = grid.getGrid()[row][col].getEast();
        }

        if(grid.isValidCell(row-1,col) && max < grid.getGrid()[row][col].getNorth()&& !grid.isBoulder(row-1,col) )
        {
            newRow = row-1;
            newCol = col;
            max = grid.getGrid()[row][col].getNorth();

        }

        if(grid.isValidCell(row,col+1) && max < grid.getGrid()[row][col].getWest()&& !grid.isBoulder(row,col+1) )
        {
            newCol = col + 1;
            newRow = row;
            max = grid.getGrid()[row][col].getWest();
        }

        if(grid.isValidCell( row+1, col) && max < grid.getGrid()[row][col].getSouth()&& !grid.isBoulder(row+1,col) )
        {
            newRow = row +1;
            newCol = col;

            max = grid.getGrid()[row][col].getSouth();

        }


        return new int[]{newRow,newCol};
    }
    private static void trainEpisode(int startRow, int startCol, Grid grid) {
        int currentRow = startRow;
        int currentCol = startCol;
        int[] newState;
        double sample ;
        double result ;

        while(!grid.isTerminal(currentRow,currentCol)) {
            for (int i = 0; i < 4; i++) {
                newState = getNextState(currentRow,currentCol,i+1,grid);
                sample = TransitionCost+ Discount* grid.getGrid()[newState[0]][newState[1]].getCurrVal() ;
                grid.getGrid()[currentRow][currentCol].setAllAction(i+1,sample);
            }
            result = ((1-alpha) * grid.getGrid()[currentRow][currentCol].getCurrVal() );
            grid.getGrid()[currentRow][currentCol].setMaxQ();
            result += (alpha*grid.getGrid()[currentRow][currentCol].getCurrVal() );
            grid.getGrid()[currentRow][currentCol].setCurrVal(result);
            newState = getNextValidCell(currentRow,currentCol,grid);
            currentRow = newState[0];
            currentCol = newState[1];

        }
    }
    public static void update(int r, int c, double result, Grid grid){
        grid.getGrid()[r][c].setMaxQ();
        grid.getGrid()[r][c].setCurrVal(result);
    }
    public static double getQValue(State state, int action, Grid newGrid2){
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
    public static double getValue(State state, Grid newGrid2){
        return newGrid2.getState(state.getH(),state.getV()).getCurrVal();
    }
    public static void getPolicy(Grid grid){
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
    public static String getDirection(State state, Grid newGrid2){
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
    public static Grid createGrid(){
        Grid newGrid = new Grid(vertical, horizontal);
        newGrid.initialize();
        for (State boulder : Boulders) {
            newGrid.setBoulder(boulder);
        }
        return newGrid;
    }
    public static void setGridTerminals(Grid newGrid){
        for (ExitState terminal : Terminals) {
            newGrid.setTerminal(terminal);
        }
    }
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
    public static void readFile(String filePath){
        try{
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
    public double returnStateValue(int h, int v, int k){
        Grid newGrid2 = createGrid();
        Episodes = k;
        train(RobotStartState[0],RobotStartState[1]);
        return getValue(newGrid2.getState(h,v), newGrid2);
    }
    public String returnBestPolicy(int h, int v, int k) {
        Episodes = k;
        Grid newGrid2 = train(RobotStartState[0],RobotStartState[1]);
        return getDirection(newGrid2.getState(h, v), newGrid2);
    }


}
