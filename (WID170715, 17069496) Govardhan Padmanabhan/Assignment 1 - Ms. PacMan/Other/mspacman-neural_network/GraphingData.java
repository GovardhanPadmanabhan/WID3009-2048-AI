package game.controllers.pacman.examples;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class GraphingData extends JPanel {
    ArrayList<Integer> data = new ArrayList<>();
    int size = 0;
    final int PAD = 10;
    int numG = 0;
    int avg = 0;
    
    public GraphingData(ArrayList<Integer> dataD, int numN) {
    	ArrayList<Integer> tmpData = new ArrayList<Integer>();
    	ArrayList<Integer> tmpData2 = new ArrayList<Integer>();
    	numG = numN;
    	size = dataD.size();
    	int tmp1 = 0;
    	for(int n = 0; n<dataD.size(); n++) {
			tmp1 += dataD.get(n);
		}
    	avg = tmp1/dataD.size();
    	int lengthL = dataD.size(); 
    	//Bubble Sort, I know, but for the size of data its fine okay -_-
    	for (int x = 0; x < lengthL; x++) {
   			for (int i = 0; i < lengthL-1; i++) {
   				if (dataD.get(i) > dataD.get(i+1)) {
   					Collections.swap(dataD, i, i+1);
   				}
   			}
   		}
    	for(int i = 0; i < dataD.size(); i++) {
    		if(!tmpData.contains(dataD.get(i))) {
    			tmpData.add(dataD.get(i));
    		}
    	}
    	for(int k = 0; k < tmpData.size(); k++) {
    		int num = 0;
    		for(int i = 0; i < dataD.size(); i++) {
    			if(dataD.get(i) == tmpData.get(k)) {
    				num++;
    			}
    		}
    		tmpData2.add(num);
    	}
    	tmpData.addAll(tmpData2);
		data = tmpData;
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        //Draw X line
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        //Draw Y line
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        double xInc = (double)(w - 2*PAD)/(data.size() - 1)*2;
        double scale = (double)(h - 2*PAD)/size;
        //Make the lines
        g2.setPaint(Color.red);
        for(int i = 0; i < data.size()/2; i++) {
            double y = PAD + i*xInc;
            double x = h - PAD - scale*data.get(data.size()/2 + i);
            g2.draw(new Line2D.Double(y, x, y, h-PAD));
            g2.drawString(String.valueOf(data.get(i) + ", " + data.get((data.size()/2) + i)), (float)y+3, (float)x-3);
        }
        g2.drawString(String.valueOf(numG), h-PAD*2, PAD);
        g2.drawString("TOP = " + String.valueOf(getMax()), PAD, PAD);
        g2.drawString("AVG = " + String.valueOf(avg), PAD*10, PAD);
    }
 
    private int getMax() {
        int max = -Integer.MAX_VALUE;
        for(int i = 0; i < data.size()/2; i++) {
            if(data.get(i) > max)
                max = data.get(i);
        }
        return max;
    }
}