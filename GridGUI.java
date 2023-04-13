import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridGUI extends JFrame {

    public final int h = 3;
    public final int v = 4;

    public Grid grid;


    public GridGUI(Grid grid){
        setTitle("Grid Drawing");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.grid = grid;
        add(new GridPanel());
        setVisible(true);
//        add(new GridQ());
//        setVisible(true);
    }


    private class GridPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {


            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            double rewardValue = 10;

            int width = getWidth();
            int height = getHeight()- getHeight()/4;


                for (int i = grid.getRow() - 1; i >= 0; i--) {
                    for (int j = 0; j < grid.getCol(); j++) {


                        int x = j * width / grid.getCol();
                        int y = i * height / grid.getRow();


                        if (grid.isTerminal(i,j)) {

                            if (grid.getGrid()[i][j].getCurrVal() > 0) {

                                g.setColor(Color.GREEN);
                            } else if (grid.getGrid()[i][j].getCurrVal() == Double.NEGATIVE_INFINITY) {
                                g.setColor(Color.GRAY);
                            } else
                                g.setColor(Color.RED);
                            g.fillRect(x, y, width / grid.getCol(), height / grid.getRow());
                        } else {
                            g.setColor(getGreenGradient(rewardValue, grid.getGrid()[i][j].getCurrVal()));
                            g.fillRect(x, y, width / grid.getCol(), height / grid.getRow());
                            System.out.println(i + " " + j + " action takes is " + grid.findHighestAdjacentValue(i, j));
                            drawArrow(g, x, y, width, height, grid.findHighestAdjacentValue(i,j));

                        }


                        g.drawRect(x, y, width / grid.getCol(), height / grid.getRow());
                        g.setColor(Color.white);
                        g.setFont(new Font("Arial", Font.BOLD, 10));
                        String text = String.format("%.2f", grid.getGrid()[i][j].getCurrVal()) ;
                        if (text.equals("-Infinity")) {
                            text = "";
                        }
                        FontMetrics fm = g.getFontMetrics();
                        int textWidth = fm.stringWidth(text);
                        int textHeight = fm.getHeight();
                        int textX = x + (width / grid.getCol() - textWidth) / 2;
                        int textY = y + (height / grid.getRow() + textHeight) / 2;

                        g.drawString(text, textX, textY);

                    }
                }
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20 ));
            String t  = "Values After " + grid.getK() + " iterations.";
            g.drawString(t, width/6,getHeight() - getHeight()/6);

        }


    }

    private class GridQ extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            double rewardValue = 10;

            int width = getWidth();
            int height = getHeight()- getHeight()/4;


            for (int i = grid.getRow() - 1; i >= 0; i--) {
                for (int j = 0; j < grid.getCol(); j++) {


                    int x = j * width / grid.getCol();
                    int y = i * height / grid.getRow();


                    if (grid.isTerminal(i,j)) {

                        if (grid.getGrid()[i][j].getCurrVal() > 0) {

                            g.setColor(Color.GREEN);
                        } else if (grid.getGrid()[i][j].getCurrVal() == Double.NEGATIVE_INFINITY) {
                            g.setColor(Color.GRAY);
                        } else
                            g.setColor(Color.RED);
                        g.fillRect(x, y, width / grid.getCol(), height / grid.getRow());
                    } else {
                    //    g.setColor(getGreenGradient(rewardValue, grid.getGrid()[i][j].getCurrVal()));
                    //    g.fillRect(x, y, width / grid.getCol(), height / grid.getRow());
                    //    System.out.println(i + " " + j + " action takes is " + grid.findHighestAdjacentValue(i, j));
                       // drawArrow(g, x, y, width, height, grid.findHighestAdjacentValue(i,j));
                        printQValues(g,width,height,i,j,x,y);

                    }



                    g.setColor(Color.white);
                    g.drawRect(x, y, width / grid.getCol(), height / grid.getRow());


                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20 ));
            String t  = "Values After " + grid.getK() + " iterations.";
            g.drawString(t, width/6,getHeight() - getHeight()/6);

        }
    }

    public void printQValues(Graphics g, int width, int height, int i ,int j, int x, int y)
    {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 10));



        //north
        String text = String.format("%.2f", grid.getGrid()[i][j].getNorth()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = x + (width / grid.getCol() - textWidth) / 2;
        int textY = y + textHeight ;

        g.drawString(text, textX, textY);

        //west
        text = String.format("%.2f", grid.getGrid()[i][j].getWest()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(text);
        textX  = x + (width/ grid.getCol()) - textWidth;
        textY = y + (height / grid.getRow())/2 ;
        g.drawString(text, textX-5, textY);


        //south
         text = String.format("%.2f", grid.getGrid()[i][j].getSouth()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }
         fm = g.getFontMetrics();
         textWidth = fm.stringWidth(text);
         textX = x + (width / grid.getCol() - textWidth) / 2;
         textY = y + (height/ grid.getRow() - 5) ;
        g.drawString(text, textX, textY);
        //east
        text = String.format("%.2f", grid.getGrid()[i][j].getEast()) ;
        if (text.equals("-Infinity")) {
            text = "";
        }

        textX  = x ;
        textY = y+ (height / grid.getRow()) / 2;
        g.drawString(text, textX+5, textY);





    }

    public void drawArrow(Graphics g, int x, int y, int width, int height,int action)
    {
        g.setColor(Color.BLACK);

        int x1 = -1,x2=-1,x3=-1,y1=-1,y2=-1,y3=-1;
        //north
        if(action == 2) {
             x1 = x + (width / grid.getCol()) / 2;
             y1 = y;
             x2 = (x + (width / grid.getCol()) / 2) - 5;
             y2 = y + 10;
             x3 = (x + (width / grid.getCol()) / 2) + 5;
             y3 = y + 10;
        }
        // Draw the three sides of the triangle using drawLine() method


        //south
        else if(action == 4) {
            x1 = x + (width / grid.getCol()) / 2;
            y1 = y + height / grid.getRow();
            x2 = (x + (width / grid.getCol()) / 2) - 5;
            y2 = (y + height / grid.getRow()) - 10;
            x3 = (x + (width / grid.getCol()) / 2) + 5;
            y3 = (y + height / grid.getRow()) - 10;
        }
        // Draw the three sides of the triangle using drawLine() method


        //east
        else if(action ==1) {
            x1 = x;
            y1 = y + (height / grid.getRow()) / 2;
            x2 = x + 10;
            y2 = (y + (height / grid.getRow()) / 2) - 5;
            x3 = x + 10;
            y3 = (y + (height / grid.getRow()) / 2) + 5;
        }

        //west
        else if( action == 3) {
        x1 = x + width / grid.getCol();
        y1 = y + (height / grid.getRow()) / 2;
        x2 = (x + width / grid.getCol()) - 10;
        y2 = (y + (height / grid.getRow()) / 2) - 5;
        x3 = (x + width / grid.getCol()) - 10;
        y3 = (y + (height / grid.getRow()) / 2) + 5;

    }
        g.setColor(Color.BLACK);
        g.fillPolygon(new int[]{x1,x2,x3},new int []{y1,y2,y3},3 );

        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);
        g.drawLine(x3, y3, x1, y1);

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




}
