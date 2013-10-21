import java.util.Random;
import java.awt.geom.*;
import java.io.*;  // needed for BufferedReader, InputStreamReader, etc.
import edu.neu.ccs.*;
import edu.neu.ccs.gui.JPTFrame;
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

public class Cube{
    
    private String[] ar = new String [faceNames.length * squaresPerFace];
    private static String[] colors = {"White", "Red", "Green", "Blue", "Orange", "Yellow"};
    public static String[] faceNames = {"Top", "Left", "Front", "Right", "Back", "Bottom"};
    public static String[] directions = {"up", "down", "left", "right"};
    private static int numOfAxes = 6; // 0 = first row, 1 = second row, third row, first column, second column, third column
    private static int numPerAxis = 12;
    public static int squaresPerFace = 9;
    private static int numOfRows = (int)Math.sqrt(squaresPerFace), numOfColumns = numOfRows;
    private static int [] rotateCornerRightOrder = {2, 8, 6, 0}; 
    private static int [] rotateCornerLeftOrder = {6, 8, 2, 0}; 
    private static int [] rotateCrossRightOrder = {5, 7, 3, 1}; 
    private static int [] rotateCrossLeftOrder = {3, 7, 5, 1}; 
    private static int [] turnLeftOrder = {1, 4, 3, 2}; //this is backwards from my intuitive 
    private static int [] turnRightOrder = {3, 4, 1, 2}; // this is backwards
    private static int [] turnDownOrder = {5, 4, 0, 2}; // this is backwards from my intuitive
    private static int [] turnUpOrder = {0, 4, 5, 2};
    private Face[] faces = new Face[faceNames.length];
    
    private static BufferedReader stdin = 
        new BufferedReader( new InputStreamReader( System.in ) );
    private int currFace = 2;
    private Face currentFace;
    
    private Cube (){};
    
    public static Cube makeCube () {
        Cube c = new Cube();
        c.createFaces();        
        return c;
    }
    
    public void createFaces () {    
        for (int z = 0; z < faceNames.length; z++){
            String[][] newArr = new String[3][3];
            for (int i = 0; i < numOfRows; i++)
                for (int j = 0; j < numOfColumns; j++){
                    newArr[i][j] = colors[z];
                }
            faces[z] = Face.makeFace(newArr);
        }
        currentFace = faces[currFace];
    }
    
    public void updateTables () {
        for (int i = 0; i < faceNames.length; i++) {
            faces[i].updateTable ();
        }
        currentFace = getFace(currFace);
    }
    
    public Face getCurrentFace() {
        return currentFace;
    }
    
    public String getNum (int i) {
        return ar[i];
    }
    
    //row 0-2, column 0-2
    public int getSquare (String s, int row, int column) throws Error{
        if (row < 0 || row > 2 || column < 0 || column > 2)
            throw new Error ("getSquare did not work");
        for (int i = 0; i < faceNames.length; i++)
            if (s == faceNames[i])
                return i*squaresPerFace + numOfRows * row + column;
        throw new Error ("getSquare did not work");
 
    }
    // return the given face (top, left, front, right, back, bottom)
    /*
     * public Face face (int f) {
        
    }
    */
    
    // change face 2 of row to face 3, face 3 of row to face 4, to 5, to 2
    // if top or bottom, must turn orientation of top/bottom squares
    // row = 0-2
    // if dir == 0, turn right
    public void turn (String dir, int i) {
        
        if (dir.equals("Right") || dir.equals("right"))
            turnHorizontal (dir,turnRightOrder, i);
        else if (dir.equals("Left") || dir.equals ("left"))
            turnHorizontal (dir,turnLeftOrder, i);
        else if (dir.equals("Up") || dir.equals ("up"))
            turnVertical (dir,turnUpOrder, i);
        else if (dir.equals("Down") || dir.equals ("down"))
            turnVertical (dir,turnDownOrder, i);
        else
            throw new Error ("Must turn up down left or right");
    }
    
    public void handleRotate (String dir, int row) {
        if (row == 0)
            if (dir.equals("Right") || dir.equals ("right"))
                rotateFace (0,1);
            else if (dir.equals("Left") || dir.equals ("left"))
                rotateFace (0,0);
            else if (dir.equals("Up") || dir.equals("up"))
                rotateFace (1,1);
            else
                rotateFace (1,0);
        if (row == 2)
            if (dir.equals("Left") || dir.equals ("left"))
                rotateFace (5,1);
            else if (dir.equals("Right") || dir.equals ("right"))
                rotateFace (5,0);
            else if (dir.equals("Down") || dir.equals ("down"))
                rotateFace (3,1);
            else
                rotateFace (3,0);
     
    }
    
    public void turnHorizontal (String dir, int[] turnOrder, int row) {
        int startingFace = 2;
        System.out.println ("turnHorizontal (String "+ dir+ ",int[] turnOrder, int "+row + ");");
        for (int j = 0; j < numOfColumns; j++){
            String next1 = getFace(startingFace).getValue (row, j);
            for (int i = 0; i < turnOrder.length; i++) {
                //System.out.println ("place (" + next1 + ", " + nexti + ");");
                next1 = place (next1, turnOrder[i], row, j);
                //System.out.println ("New next1: " + next1);
            }
        }
        handleRotate(dir, row);
    }
     
     public void turnVertical (String dir,int[] turnOrder, int column) {
         int startingFace = 2;        
         //System.out.println ("turnVertical (String "+ dir+ ",int[] turnOrder, int "+column + ");");
        
         for (int j = 0; j < numOfRows; j++) {
             String next1 = getFace (startingFace).getValue(j, column);
             for (int i = 0; i < turnOrder.length; i++) {
                 //System.out.println ("place (" + next1 + ", " + nexti + ");");
                 next1 = place (next1, turnOrder[i], j, column);
                 //System.out.println ("New next1: " + next1);
             }
         }
         handleRotate(dir, column);
           
     }    
     public String place (String stringFrom, int faceTo, int rowTo, int colTo){
        String temp = getFace(faceTo).getValue(rowTo, colTo);
        getFace (faceTo).changeValue(stringFrom, rowTo, colTo);
        return temp;
        
     }
    
    // clockwise = 0, counterclockwise = 1
    public void rotateFace (int f, int dir) throws Error{
        //System.out.println ("rotateFace (int "+ f + ",int " + dir + ");");
        // # of squares that move, center doesnt, so skips
        //System.out.println ("TopLeftNum: " + topLeftNum);
        int[] turnOrder1, turnOrder2;
        if (dir == 0) {
            turnOrder1 = rotateCornerRightOrder;
            turnOrder2 = rotateCrossRightOrder;
        }
        else if (dir == 1){
            turnOrder1 = rotateCornerLeftOrder;
            turnOrder2 = rotateCrossLeftOrder;
        }
        else
            throw new Error ("Must rotate face either 0 or 1");
        
        String next1 = getFace (f).getValue(0, 0);
        String next2 = getFace (f).getValue(0, 1);
        
            for (int i = 0; i < turnOrder1.length; i++) {
                //System.out.println ("place (" + next1 + ", " + nexti + ");");
                next1 = place (next1, f, turnOrder1[i]/3, turnOrder1[i] % 3);
                //System.out.println ("New next1: " + next1);
            }
            for (int i = 0; i < turnOrder2.length; i++) {
                next2 = place (next2, f, turnOrder2[i]/3, turnOrder2[i] % 3);
            }
    }
    
    public static int topLeftNum (int f) {
        return f*squaresPerFace;
    }
    
    public void printFace (int f) {
        System.out.println (faces[f].toString());
    }
    
    public void printFaces () {
        for (int i = 0; i < faces.length; i++) {
            printFace(i);
            System.out.println();
        }
    }
    
    public Face getFace (int i) {
        return faces[i];
    }
    
    public void printCurrentFace () {
        System.out.println (faces[currFace].toString());
    }
    
    public void scramble (int num) {
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            int dir = r.nextInt (4);
            int rowcol = r.nextInt (3);
            turn (directions[dir], rowcol);
        }
    }
    
    public void run () {
            // Create a single shared BufferedReader for keyboard input
            try {
            String input = "";
            // Program execution starts here
                while (!input.equals ("quit")) {
                    //printCurrentFace();
                    printFaces();
                    System.out.print("Enter Command (turn, rotate): ");
                    input = stdin.readLine();
                                       
                    if (input.equals("rotate cube")) {
                        System.out.print("Direction? ");
                        String dir = stdin.readLine();
                        for (int i = 0; i < numOfColumns; i++){
                            //System.out.println (i);
                            turn (dir, i);       
                        }
                    }
                    else if (input.equals ("turn")) {
                        System.out.print("Direction? ");
                        String dir = stdin.readLine();
                        System.out.print("Row/Column? ");
                        String rc = stdin.readLine();
                        int rowcolumn = Integer.parseInt(rc);
                                               
                        turn (dir, rowcolumn);
                    }
                }
           }
            catch (IOException e) {
                System.out.println ("Execution failed.");
            }
    }
    public static void main (String [] args) {
        Cube c = Cube.makeCube();
        c.printFaces();
        c.run();
        }
}

    

 