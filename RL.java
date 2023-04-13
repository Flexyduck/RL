public class RL {




    public static void main(String[] args) {


    }


//    public void rlQval(State[][] grid,int startX, int startY,float gamma, float alpha, float reward)
//    {
//
//        double [] values = new double[4];
//        int [] actions = {1,2,3,4};
//
//        int actionTaken = 0;
//
//        double sample = 0;
//        int x = startX;
//        int y = startY;
//        for (int i = 0; i < 3500; i++) {
//            while(!grid[x][y].isTerminal) {
//
//                for (int j = 0; j <actions.length ; j++) {
//                    values[j] = getActionVal(grid,x,y,actions[j]);
//                }
//
//                double max = returnMax(values);
//                for (int j = 0; j < values.length; j++) {
//                    if(max == values[j])
//                    {
//                        actionTaken = j+1;
//                    }
//                }
//                sample = reward + gamma* returnMax(values);
//                double qVal = (1-alpha)* grid[x][y].getCurrVal() + (alpha* sample);
//                grid[x][y].setCurrVal(qVal);
//
//                if(actionTaken == 1 && isValidCell(x,y-1,grid.length,grid[0].length))
//                {
//                    y = y-1;
//                }
//                else if(actionTaken == 2 && isValidCell(x-1,y,grid.length,grid[0].length))
//                {
//                    x = x-1;
//                }
//                else if(actionTaken == 3 && isValidCell(x+1,y,grid.length,grid[0].length)){
//                    x= x+1;
//                }
//                else if(actionTaken == 4 && isValidCell(x,y+1,grid.length,grid[0].length)){
//                    y = y+1;
//                }
//            }
//            x = startX;
//            y = startY;
//        }
//    }




    public double getActionVal(State[][]grid, int x, int y, int action)
    {
        double result = 0;

        //east
        if(action == 1 && isValidCell(x,y-1,grid.length,grid[0].length))
        {
                result = grid[x][y-1].getCurrVal();
        }
        //north
        else if(action == 2 && isValidCell(x-1,y,grid.length,grid[0].length))
        {
                result = grid[x-1][y].getCurrVal();
        }
        //west
        else if(action == 3 && isValidCell(x,y+1,grid.length,grid[0].length))
        {
                result = grid[x][y+1].getCurrVal();
        }
        else if( action == 4 && isValidCell(x+1,y,grid.length,grid[0].length))
        {
            result = grid[x+1][y].getCurrVal();
        }
        return result;
    }

    public double[] getOtherCell(int row, int col,State s,State[][]grid){

        double[] retArr = new double[3];
        double pos1 = 0;
        double pos2 = 0;
        double pos3 = 0;
        if(s.getActionTaken() == 1){
            pos1 = getEast(s,grid);
            pos2 = getNorth(s,grid);
            pos3 = getSouth(s,grid);
        }
        else if(s.getActionTaken() == 2){
            pos1 = getNorth(s,grid);
            pos2 = getEast(s,grid);
            pos3 = getWest(s,grid);
        }
        else if(s.getActionTaken() == 3){
            pos1 = getWest(s,grid);
            pos2 = getNorth(s,grid);
            pos3 = getSouth(s,grid);
        }
        else if(s.getActionTaken() == 4){
            pos1 = getSouth(s,grid);
            pos2 = getEast(s,grid);
            pos3 = getWest(s,grid);
        }
        retArr[0] = pos1;
        retArr[1] = pos2;
        retArr[2] = pos3;

        return retArr;
    }

    public double getNorth(State s,State[][] grid){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH()-1, s.getV(), grid.length, grid[0].length) && !isBlockCell(grid,s.getH()-1, s.getV())){
            retState = grid[s.getH()-1][s.getV()].getCurrVal();
        }
        return retState;
    }
    public double getSouth(State s,State[][] grid){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH()+1, s.getV(), grid.length, grid[0].length) && !isBlockCell(grid,s.getH()+1, s.getV())){
            retState = grid[s.getH()+1][s.getV()].getCurrVal();
        }
        return retState;
    }
    public double getEast(State s,State[][] grid){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH(), s.getV()+1, grid.length, grid[0].length) && !isBlockCell(grid,s.getH(), s.getV()+1)){
            retState = grid[s.getH()][s.getV()+1].getCurrVal();
        }
        return retState;
    }

    public boolean isValidCell(int row, int col, int xLen, int yLen){
        boolean isValid = false;
        if(row >=0 && row <= xLen-1){
            if(col >=0 && col<= yLen-1){
                isValid = true;
            }
        }
        return isValid;
    }
    public  double getWest(State s,State[][] grid){
        double retState = s.getCurrVal();
        if (isValidCell(s.getH(), s.getV()-1, grid.length, grid[0].length) && !isBlockCell(grid,s.getH(), s.getV()-1)){
            retState = grid[s.getH()][s.getV()-1].getCurrVal();
        }
        return retState;
    }
    public double returnMax(double[] arr){
        double maxVal = 0.0;

        for (int i = 0; i < arr.length; i++) {
            maxVal = Math.max(maxVal, arr[i]);
        }

        return maxVal;
    }

    public State goNorth(State[][] grid, int x, int y)
    {
        if(x-1 >=0){
            return grid[x-1][y];
        }
        return null;
    }
    public boolean isBlockCell(State [][]grid,int row, int col){
        return grid[row][col].getCurrVal() == Double.NEGATIVE_INFINITY;
    }





}
