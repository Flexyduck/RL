import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static ValueIterationAgent VA = new ValueIterationAgent("gridConf.txt");
    static int h;
    static int v;
    static int step;
    static String method;
    static String query;
    public static void main(String[] args) {
        readResultFile("result.txt");
    }
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
                System.out.println("Do a RL");
            } else if (query.equals("bestPolicy")) {
                System.out.println("Do a RL2");
            }
        }
    }
}
