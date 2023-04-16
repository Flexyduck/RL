import java.io.BufferedReader;
import java.io.FileReader;
public class Main {
    static ValueIterationAgent VA = new ValueIterationAgent("gridConf.txt");
    static QLearning QL = new QLearning("gridConf.txt");
    static int h;
    static int v;
    static int step;
    static String method;
    static String query;
    public static void main(String[] args) {
        readResultFile("result.txt");
    }

    /**
     * This function reads the results.txt file and does the appropriate action per line
     *
     * @param fileName the name of the file to be read
     */
    public static void readResultFile(String fileName){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
     * This function processes each line in the file
     *
     * @param line the line in the file to be processed
     */
    public static void processLine(String line){
        String [] contents = line.split(",");
        h = Integer.parseInt(contents[0]);
        v = Integer.parseInt(contents[1]);
        step = Integer.parseInt(contents[2]);
        method = contents[3];
        query = contents[4];

        if(method.equals("MDP")){
            if(query.equals("stateValue")){
                System.out.println(line + ": " + VA.returnStateValue(h,v,step));
            } else if (query.equals("bestPolicy")) {
                System.out.println(line + ": " + VA.returnBestPolicy(h,v,step));
            }
        } else if (method.equals("RL")) {
            if(query.equals("bestQValue")){
                System.out.println(line + ": " + QL.returnStateValue(h,v,step));
            } else if (query.equals("bestPolicy")) {
                System.out.println(line + ": " + QL.returnBestPolicy(h,v,step));
            }
        }
    }
}
