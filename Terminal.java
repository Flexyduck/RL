import java.util.ArrayList;
import java.util.List;

public class Terminal {

    private List<ExitState> terminalList = new ArrayList<>();

    public void addTerminal(int h, int v, double reward)
    {
        terminalList.add(new ExitState(h,v,reward));
    }

    public boolean isExit(int h,int v, double reward )
    {
        return terminalList.contains(new ExitState(h,v,reward));
    }
}
