import java.util.Random;
import java.awt.geom.*;
import java.io.*;  // needed for BufferedReader, InputStreamReader, etc.
import edu.neu.ccs.*;
import edu.neu.ccs.gui.JPTFrame;
import edu.neu.ccs.gui.PaintableSequence;
import edu.neu.ccs.gui.TablePanel;
import edu.neu.ccs.*;
import edu.neu.ccs.gui.*;
import edu.neu.ccs.codec.*;
import edu.neu.ccs.console.*;
import edu.neu.ccs.filter.*;
import edu.neu.ccs.jpf.*;
import edu.neu.ccs.parser.*;
import edu.neu.ccs.pedagogy.*;
import edu.neu.ccs.quick.*;
import edu.neu.ccs.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.beans.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.regex.*;
import java.text.ParseException;
public class Face {
    private String [][] arr = new String [3][3];
    int rows = 3;
    int cols = 3;
    int gap = 3;
    int size = 35;

    JPTFrame frame;
    TablePanel mainPanel;
    
    protected Face (){};
    
   
    public static Face makeFace(String[][] newArr) {
        Face f = new Face();
        f.arr = newArr;
        f.createTable();
        return f;
    }
    
    public String getValue (int row, int column) {
        return arr[row][column];
    }
    
    public void changeValue (String newVal, int row, int col) {
        arr[row][col] = newVal;
    }
    
    public TablePanel getTable() {
        return mainPanel;
    }
    
    public void createTable () {
        
        mainPanel = new TablePanel(rows, cols, gap, gap);
        mainPanel.setVisible(true);
        updateTable();
        
    }
   
    public void updateTable () {
        mainPanel.removeAll();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < rows; j++) {
                BufferedPanel b = new BufferedPanel (size, size, Colors.getColorFromName(arr[i][j]));
                //System.out.println (Colors.getColorFromName(arr[i][j]));
                mainPanel.addObject(b, i, j);
            }
        mainPanel.repaint();
        System.out.println (toString());

    }
    
    public String toString (String blankSpace) {
        String str = "Face: ";
        for (int i = 0; i < 3; i++) {
            str = str + "\n";
            for (int j = 0; j < 3; j++){
                str += blankSpace;
                str = str + arr[i][j].charAt(0) + " ";
            }
        }
        return str;
    }
    
    public String toString () {
        String str = "Face: ";
        for (int i = 0; i < 3; i++) {
            str = str + "\n";
            for (int j = 0; j < 3; j++)
                str = str + arr[i][j].charAt(0) + " ";
        }
        return str;
    }  
   
    public static void main (String [] args) {
        Cube c = Cube.makeCube();        
    }

}
