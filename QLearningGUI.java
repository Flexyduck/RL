import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class QLearningGUI extends JFrame{


    public Grid grid;
    double reward;

    public QLearningGUI( Grid grid, double reward) {
        setTitle("Q Learning with Episodes");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.grid = grid;
        add(new GridQ());
        setVisible(true);
        this.reward = reward;

    }

    private class GridQ extends JPanel
    {


        @Override
        protected void paintComponent(Graphics g) {


            super.paintComponent(g);


            int rows = grid.getRow();
            int cols = grid.getCol();

            int width = getWidth();
            int height = getHeight()- getHeight()/4;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (int i = rows - 1; i >= 0; i--) {
                for (int j = 0; j < cols; j++) {


                    int x = j * width / cols;
                    int y = i * height / rows;


                    if (grid.isTerminal(i,j)) {

                        if (grid.getGrid()[i][j].getCurrVal() > 0) {
                            g.setColor(Color.GREEN);
                        }
                      else
                            g.setColor(Color.RED);

                        g.fillRect(x, y, width / cols  , height / rows  );
                        String text = String.format("%.2f", grid.getGrid()[i][j].getCurrVal()) ;


                        if (text.equals("-Infinity")) {
                            text = "";
                        }
                        FontMetrics fm = g.getFontMetrics();
                        int textWidth = fm.stringWidth(text);
                        int textHeight = fm.getHeight();
                        int textX = x + ((width / cols) - textWidth) / 2;
                        int textY = y + ((height / rows )+ textHeight) / 2;

                        g.setColor(Color.white);
                        g.drawString(text, textX, textY);
                    }
                     else if (grid.getGrid()[i][j].getCurrVal() == Double.NEGATIVE_INFINITY) {
                        g.setColor(Color.GRAY);
                        g.fillRect(x, y, width / cols  , height / rows  );
                    }
                     else {

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

        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        int nPoints = 3;


        double rewardValue = reward;

        int rows = grid.getRow();
        int cols = grid.getCol();

        if(grid.getGrid()[i][j].isTerminal)
        {
            String text = String.format("%.2f", grid.getGrid()[i][j].getCurrVal()) ;


            if (text.equals("-Infinity")) {
                text = "";
            }
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            int textX = x + ((width / cols) - textWidth) / 2;
            int textY = y + ((height / rows )+ textHeight) / 2;

            g.setColor(Color.white);
            g.drawString(text, textX, textY);
        }

        //north
        String text = String.format("%.2f", grid.getGrid()[i][j].getNorth()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = x + ((width / cols - textWidth) ) / 2;
        int textY = y + textHeight ;

        xPoints = new int[]{x,x + (width/cols),x+ (((width/cols))/2)};
        yPoints = new int[]{y,y , y + ((height/rows)/2)};

        if(grid.getGrid()[i][j].getNorth()>=0)
            g.setColor(getGreenGradient(rewardValue, grid.getGrid()[i][j].getNorth()));
        else
            g.setColor(getRedGradient(rewardValue, grid.getGrid()[i][j].getNorth()));

        g.fillPolygon(xPoints, yPoints, nPoints);
        g.setColor(Color.white);
        g.drawString(text, textX, textY);


        //east
        text = String.format("%.2f", grid.getGrid()[i][j].getEast()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }

        textX  = x ;
        textY = y+ (height / rows) / 2;

        xPoints = new int[]{x  ,x,x+ (((width/cols))/2)};
        yPoints = new int[]{y,y + (height/rows) , y + ((height/rows)/2)};

        if(grid.getGrid()[i][j].getEast()>=0)
            g.setColor(getGreenGradient(rewardValue, grid.getGrid()[i][j].getEast()));
        else
            g.setColor(getRedGradient(rewardValue, grid.getGrid()[i][j].getEast()));

        g.fillPolygon(xPoints, yPoints, nPoints);
        g.setColor(Color.white);
        g.drawString(text, textX+5, textY);


        //west
        text = String.format("%.2f  ", grid.getGrid()[i][j].getWest()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(text);
        textX  = x + (width/ cols) - textWidth;
        textY = y + (height /rows)/2 ;


        xPoints = new int[]{x + (width/cols),x + (width/cols),x+ (((width/cols))/2)};
        yPoints = new int[]{y,y + (height/rows) , y + ((height/rows)/2)};

        if(grid.getGrid()[i][j].getWest()>=0)
            g.setColor(getGreenGradient(rewardValue, grid.getGrid()[i][j].getWest()));
        else
            g.setColor(getRedGradient(rewardValue, grid.getGrid()[i][j].getWest()));

        g.fillPolygon(xPoints, yPoints, nPoints);
        g.setColor(Color.white);
        g.drawString(text, textX-5, textY);


        //south
        text = String.format("%.2f", grid.getGrid()[i][j].getSouth()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(text);
        textX = x + (width / cols - textWidth) / 2;
        textY = y + (height/rows - 5) ;

        xPoints = new int[]{x,x + (width/cols),x+ (((width/cols))/2)};
        yPoints = new int[]{y+ (height/rows),y + (height/rows), y + ((height/rows)/2)};

        if(grid.getGrid()[i][j].getSouth()>=0)
            g.setColor(getGreenGradient(rewardValue, grid.getGrid()[i][j].getSouth()));
        else
            g.setColor(getRedGradient(rewardValue, grid.getGrid()[i][j].getSouth()));

        g.fillPolygon(xPoints, yPoints, nPoints);
        g.setColor(Color.white);
        g.drawString(text, textX, textY);


        g.setColor(Color.BLACK);





    }

    public static Color getGreenGradient(double max, double curr) {

        int n = 0;
        // Ensure n is within the range of 0-255
        if (curr != 0) {
            n = (int) Math.max(0, Math.min(255,(255*curr/max)));
        }

        // Create and return the color object
        return new Color(0, n, 0);
    }

    public static Color getRedGradient(double max, double curr) {

        int n = 0;
        // Ensure n is within the range of 0-255
        if (curr != 0) {
            n = (int) Math.max(0, Math.min(255,(255*curr*(-1)/max)));
        }

        // Create and return the color object
        return new Color(n, 0, 0);
    }
}
