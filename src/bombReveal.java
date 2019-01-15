/*
Project:    Program 01
File:       bombReveal.java
Purpose:    Implements current button to have a top and a bottom img and indicates if it is a bomb or not, and assigns
            the proper values
Class:      CS 335
Author:     Jared Rigdon
Date:       9/21/2018
Purpose:    Build a stand-alone Java program that will allow a user to play the game Minesweeper. The graphical user
            interface should support the basic elements of the game:  new game, settings(beginner, intermediate, expert,
            and custom), play of the game itself, display a popup msg with instructions on how to play,
             and detecting the termination state.

            NOTE: The jpgs are from the original minesweeper game,

References: Used previous assignment Program 00 as a refernce. Which itself is based on the template
            "memory-game-template" provided by (at least last modified by) Kristina Gessel found in the
            CS 335 Canvas files.
 */

import javax.swing.*;

public class bombReveal extends JButton{
    //resource loader
    private ClassLoader loader = getClass().getClassLoader();

    //Tile front icon
    private Icon front;
    //Tile back icon
    private Icon back = new ImageIcon(loader.getResource("res/tile_back.jpg"));

    private int nearbyID, bombID, row, col;

    public bombReveal() {super();}

    public bombReveal(ImageIcon frontImg){
        super();
        front = frontImg;
        super.setIcon(front);
        hideFront();
    }

    public void showFront(){
        super.setIcon(front);
    }
    //sets the front image, mainly used for updating buttons
    public void setFront(ImageIcon frontimg){
        front=frontimg;
        super.setIcon(front);
        hideFront();
    }

    public void hideFront(){ super.setIcon(back);}

    //use nearBomb to identify how many bombs are near the tile, since 8 is the max,
    //set the tile that is a bomb to 9
    public int nearBomb() { return nearbyID; }
    public void set_nearBomb(int i) { nearbyID = i; }

    //set the bomb id, either 1 for bomb or 0 for not a bomb
    public int get_bombID(){ return bombID;}
    public void setBomb(int i) { bombID=i;}

    //could condense but might make it annoying to deal with
    public int get_Row(){ return row;}
    public int get_Col(){ return col;}
    public void setRowCol(int i, int j){row=i; col=j;}

}
