import java.util.Random;
import java.awt.geom.*;
import java.io.*;  // needed for BufferedReader, InputStreamReader, etc.
import edu.neu.ccs.*;
import edu.neu.ccs.gui.BufferedPanel;
import edu.neu.ccs.gui.JPTFrame;
import edu.neu.ccs.gui.Paintable;
import edu.neu.ccs.gui.ShapePaintable;
import edu.neu.ccs.gui.SimpleAction;
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
public class RubikCube extends TablePanel{
    /** The frame for the animation. */
    private JPTFrame frame = null;
    Cube cube;
    TablePanel mainPanel;
    TablePanel cubePanel;
    TablePanel currFacePanel;
    TablePanel t1, t2, t3, t4;
    private SimpleAction turnTopRowLeft;
    private SimpleAction turnTopRowRight;
    private SimpleAction turnMiddleRowLeft;
    private SimpleAction turnMiddleRowRight;
    private SimpleAction turnBottomRowLeft;
    private SimpleAction turnBottomRowRight;
    private SimpleAction turnFirstColUp;
    private SimpleAction turnFirstColDown;
    private SimpleAction turnSecondColUp;
    private SimpleAction turnSecondColDown;
    private SimpleAction turnThirdColUp;
    private SimpleAction turnThirdColDown;
    private SimpleAction scramble;
    private Face currentFace;
    private RubikCube () {};
    public static RubikCube runRubikCube (Cube c) {
        RubikCube r = new RubikCube();
        r.cube = c;
        r.createFrame();
        r.createActions();
        r.createInterface();
        
        return r;
    }
    
    public void createActions () {
        System.out.println ("Creating Actions");
        
        turnTopRowLeft
        = new SimpleAction("<") {
            public void perform() { 
                cube.turn("left", 0);
                System.out.println ("Turn Top Row Left");
                cube.updateTables();
                
            }
        };
    
        turnTopRowRight
        = new SimpleAction(">") {
            public void perform() { 
                cube.turn("right", 0);
                System.out.println ("Turn Top Row Right");
                cube.updateTables();
            }
        };
        
        turnMiddleRowLeft
        = new SimpleAction("<") {
            public void perform() { 
                cube.turn("left", 1);
                System.out.println ("Turn Top Row Left");
                cube.updateTables();
            }
        };
    
        turnMiddleRowRight
        = new SimpleAction(">") {
            public void perform() { 
                cube.turn("right", 1);
                System.out.println ("Turn Top Row Right");
                cube.updateTables();
            }
        };
        
        turnBottomRowLeft
        = new SimpleAction("<") {
            public void perform() { 
                cube.turn("left", 2);
                System.out.println ("Turn Bottom Row Left");
                cube.updateTables();
            }
        };
    
        turnBottomRowRight
        = new SimpleAction(">") {
            public void perform() { 
                cube.turn("right", 2);
                System.out.println ("Turn Bottom Row Right");
                cube.updateTables();
            }
        };
        
        turnFirstColUp
        = new SimpleAction("/\\") {
            public void perform() { 
                cube.turn("up", 0);
                System.out.println ("Turn First Column Up");
                cube.updateTables();
            }
        };
    
        turnFirstColDown
        = new SimpleAction("\\/") {
            public void perform() { 
                cube.turn("down", 0);
                System.out.println ("Turn First Column Down");
                cube.updateTables();
            }
        };
        turnSecondColUp
        = new SimpleAction("/\\") {
            public void perform() { 
                cube.turn("up", 1);
                System.out.println ("Turn Second Column Up");
                cube.updateTables();
            }
        };
    
        turnSecondColDown
        = new SimpleAction("\\/") {
            public void perform() { 
                cube.turn("down", 1);
                System.out.println ("Turn Second Column Down");
                cube.updateTables();
            }
        };
        
        turnThirdColUp
        = new SimpleAction("/\\") {
            public void perform() { 
                cube.turn("up", 2);
                System.out.println ("Turn Third Column Up");
                cube.updateTables();
            }
        };
    
        turnThirdColDown
        = new SimpleAction("\\/") {
            public void perform() { 
                cube.turn("down", 2);
                System.out.println ("Turn Third Column Down");
                cube.updateTables();
            }
        };
        
        scramble
        = new SimpleAction("Scramble") {
            public void perform() { 
                cube.scramble(100);
                System.out.println ("Scramble Cube");
                cube.updateTables();
            }
        };
        
    }
    
    public void createInterface () {
        System.out.println ("Creating Interface");
        mainPanel = new TablePanel(2, 1, 30, 30, CENTER);
        showCurrentFace();
        showCube();
        mainPanel.addObject(cubePanel, 1, 0);
        add (mainPanel);
        
        repaint();
    }
    
    public void addButtons () {
        System.out.println ("Adding Buttons");
        JButton b1 = new JButton (turnTopRowLeft);        
        JButton b2 = new JButton (turnTopRowRight);
        JButton b3 = new JButton (turnMiddleRowLeft);        
        JButton b4 = new JButton (turnMiddleRowRight);
        JButton b5 = new JButton (turnBottomRowLeft);        
        JButton b6 = new JButton (turnBottomRowRight);
        JButton b7 = new JButton (turnFirstColUp);        
        JButton b8 = new JButton (turnFirstColDown);
        JButton b9 = new JButton (turnSecondColUp);        
        JButton b10 = new JButton (turnSecondColDown);
        JButton b11 = new JButton (turnThirdColUp);        
        JButton b12 = new JButton (turnThirdColDown);
        JButton b13 = new JButton (scramble);
        
        t1 = new TablePanel (3, 1, CENTER);
        t2 = new TablePanel (3, 1, CENTER);
        t3 = new TablePanel (1, 3, CENTER);
        t4 = new TablePanel (1, 3, CENTER);
        
        t1.addObject(b1, 0, 0);
        t1.addObject(b3, 1, 0);        
        t1.addObject(b5, 2, 0);
        currFacePanel.addObject (t1, 1, 0);

        
        t2.addObject(b2, 0, 0);
        t2.addObject(b4, 1, 0);
        t2.addObject(b6, 2, 0);
        currFacePanel.addObject (t2, 1, 2);
        
        
        t3.addObject(b7, 0, 0);
        t3.addObject(b9, 0, 1);
        t3.addObject(b11, 0, 2);
        currFacePanel.addObject (t3, 0, 1);
        
        t4.addObject(b8, 0, 0);
        t4.addObject(b10, 0, 1);
        t4.addObject(b12, 0, 2);
        currFacePanel.addObject (t4, 2, 1);
        
        mainPanel.addObject(b13);
      
    }
    
    public void showCurrentFace () {
        int rows = 4;
        int cols = 4;
        int gap = 10;
        System.out.println ("ShowingCurrentFace");    

        currentFace = cube.getFace (2);
            
        currFacePanel = new TablePanel(rows, cols, gap, gap, CENTER);
        currFacePanel.addObject (currentFace.getTable(), 1, 1);
        addButtons (); 
    }
    
    public void showCube() {
        System.out.println ("ShowingCube");
        int rows = 5;
        int cols = 6;
        int gap = 30;
                
        cubePanel = new TablePanel(rows, cols, gap, gap, CENTER);

        cubePanel.addObject (cube.getFace(0).getTable(), 1, 2);
        cubePanel.addObject (cube.getFace(1).getTable(), 2, 1);
        cubePanel.addObject (currFacePanel, 2, 2);
        cubePanel.addObject (cube.getFace(3).getTable(), 2, 3);
        cubePanel.addObject (cube.getFace(4).getTable(), 2, 4);
        cubePanel.addObject (cube.getFace(5).getTable(), 3, 2);
                    
    }
    private void createFrame() {
        frame = frame("Rubik's Cube");
        frame.maximize();
        frame.setVisible (true);
    }
    
    public static void main (String [] args) {
        Cube c = Cube.makeCube ();
        RubikCube rc =  RubikCube.runRubikCube(c);
    }

}
