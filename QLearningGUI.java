import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class QLearningGUI extends JFrame{

    private QLearning qLearning;

    public QLearningGUI( QLearning qLearning)
    {
        setTitle("Grid Drawing");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.qLearning = qLearning;
        add(new GridQ());
        setVisible(true);

    }

    private class GridQ extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            int rows = 3;
            int cols = 4;
            double rewardValue = 1;

            int width = getWidth();
            int height = getHeight()- getHeight()/4;


            for (int i = rows - 1; i >= 0; i--) {
                for (int j = 0; j < cols; j++) {


                    int x = j * width / cols;
                    int y = i * height / rows;


                    if (qLearning.getGrid()[i][j].isTerminal()) {

                        if (qLearning.getGrid()[i][j].getCurrVal() > 0) {
                            g.setColor(Color.GREEN);
                        } else if (qLearning.getGrid()[i][j].getCurrVal() == Double.NEGATIVE_INFINITY) {
                            g.setColor(Color.GRAY);
                        } else
                            g.setColor(Color.RED);

                        g.fillRect(x, y, width / cols  , height / rows  );
                    } else {

                        printQValues(g,width,height,i,j,x,y);

                    }



                    g.setColor(Color.white);
                    g.drawRect(x, y, width / cols, height / rows);


                }
            }

        }
    }

    public void printQValues(Graphics g, int width, int height, int i ,int j, int x, int y)
    {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 10));

        int rows = 3;
        int cols = 4;

        if(qLearning.getGrid()[i][j].isTerminal)
        {
            String text = String.format("%.2f", qLearning.getGrid()[i][j].getCurrVal()) ;
            if (text.equals("-Infinity")) {
                text = "";
            }
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            int textX = x + (width / cols - textWidth) / 2;
            int textY = y + (height / rows + textHeight) / 2;

            g.drawString(text, textX, textY);
        }

        //north
        String text = String.format("%.2f", qLearning.getGrid()[i][j].getNorth()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = x + (width / cols      - textWidth) / 2;
        int textY = y + textHeight ;

        g.drawString(text, textX, textY);

        //west
        text = String.format("%.2f", qLearning.getGrid()[i][j].getWest()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(text);
        textX  = x + (width/ cols) - textWidth;
        textY = y + (height /rows)/2 ;
        g.drawString(text, textX-5, textY);


        //south
        text = String.format("%.2f", qLearning.getGrid()[i][j].getSouth()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(text);
        textX = x + (width / cols - textWidth) / 2;
        textY = y + (height/rows - 5) ;
        g.drawString(text, textX, textY);
        //east
        text = String.format("%.2f", qLearning.getGrid()[i][j].getEast()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }

        textX  = x ;
        textY = y+ (height / rows) / 2;
        g.drawString(text, textX+5, textY);





    }
}
