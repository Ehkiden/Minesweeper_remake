/*
Project:    Program 01
File:       Board.java
Purpose:    Create the main panel here which makes the arrays of buttons and assigns the bomb values to them, assigns
            the correct jpgs according to the num of surrounding bombs in the 8 tile radius, and flip and disable the
            required tiles
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

import java.awt.event.*;
import javax.swing.*;

public class Board {

    private bombReveal tiles[][];

    //resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, int bmbCount, ActionListener AL){

       // int bombsLeft=bmbCount;
        //allocate and config the game board: a 2d array of tiles
        tiles = new bombReveal[size][size];
        /*
        go through and make the array and randomly place n bombs
        then loop through the array again, find a bomb, add +1 to the 8 neighboring cell's bombCounter
        when the cell's bomb counter increases, change the front img accordingly
         */
        //initial array
        for (int i=0; i < size; i++){
            for (int j=0; j < size; j++){
                //set default icon to blank
                ImageIcon img = new ImageIcon(loader.getResource("res/tile_0.jpg"));
                bombReveal c = new bombReveal(img);
                c.addActionListener(AL);
                //default the ids to 0
                c.set_nearBomb(0);
                c.setBomb(0);
                c.setRowCol(i,j);   //set the row and col for later use

                //add them to the 2d array
                tiles[i][j]=c;
            }
        }

        //while loop to randomly place the bombs
        int bombsLeft=0;
        while(bombsLeft < bmbCount){
            int randRow=(int)(Math.random()*size);
            int randCol=(int)(Math.random()*size);

            if (tiles[randRow][randCol].get_bombID() != 1){
                //set the current valid cell to the correct bomb values
                tiles[randRow][randCol].setBomb(1);
                tiles[randRow][randCol].set_nearBomb(9);

                //set the tile icon to a bomb
                ImageIcon img = new ImageIcon(loader.getResource("res/tile_bomb.jpg"));
                tiles[randRow][randCol].setFront(img);
                //incr the surrounding neighbors nearBomb count(the num of bombs near that tile's 8 tile radius) by one
                for(int x=-1; x<=1;x++){
                    for(int y=-1; y<=1;y++){
                        isNeighbor(randRow+x, randCol+y, size-1, size-1);
                    }
                }
                bombsLeft++;
            }
        }
    }

    //checks if the the curr tile is a valid neighbor, then inc the nearBomb count
    public void isNeighbor(int i, int j, int xmax, int ymax){
        if ( i>=0 && j>=0 && i<=xmax && j<=ymax){
            //check if the curr tile is also a bomb
            if (tiles[i][j].get_bombID()!=1){
                //get the curr tile nearby bomb count and increase by on
                int curr_nearBomb = tiles[i][j].nearBomb();
                tiles[i][j].set_nearBomb(curr_nearBomb+1);

                //change the icon to reflect the nearby bombs
                ImageIcon img = new ImageIcon(loader.getResource("res/tile_"+(curr_nearBomb+1)+".jpg"));
                tiles[i][j].setFront(img);
            }
        }
    }


    //for each tile in the array, add it to the container
    public void fillBoardView(JPanel view){
        for (bombReveal[] c : tiles) {
            for (bombReveal d : c){
                view.add(d);
            }
        }
    }

    //loops through curr game array and checks for any enabled tiles that is not a bomb
    public boolean win_check(int size){
        for (int i=0; i < size; i++){
            for (int j=0; j < size; j++){
                //check if tile is enabled and not a bomb
                if (tiles[i][j].isEnabled() && tiles[i][j].get_bombID()==0){
                    return false;
                }
            }
        }
        return true;
    }

    //stops the loop when all surrounding neighbors are disables and showing
    //recursive-ish func similar to isNeighbor in the board class
    public void revealTiles(int i, int j, int xmax, int ymax){
        //check if valid and the button is enabled
        if ( i>=0 && j>=0 && i<=xmax && j<=ymax && tiles[i][j].isEnabled()){
            //disable button and call helper func
            flip_disable(i, j);
            //call helper func if tile val is 0
            if (tiles[i][j].nearBomb()==0){
                revealTiles_helper(i, j, xmax, ymax);
            }
        }
    }

    //helper function that is called to then call the next 0 val tile
    public void revealTiles_helper(int i, int j, int xmax, int ymax){
        for(int x=-1; x<=1;x++){        //so many for loops, hungry for frootloops
            for(int y=-1; y<=1;y++){
                revealTiles(i+x, j+y, xmax, ymax);
            }
        }
    }

    //func to call that disables the tile and sets the proper disabled icon
    public void flip_disable(int row, int col){
        int curr_nearBomb = tiles[row][col].nearBomb();
        ImageIcon img;
        if (curr_nearBomb == 9){       //NOTE: could rename tile_bomb.jpg to tile_9.jpg but dont wanna
            img = new ImageIcon(loader.getResource("res/tile_bomb.jpg"));
        }
        else {
            img = new ImageIcon(loader.getResource("res/tile_"+curr_nearBomb+".jpg"));
        }
        tiles[row][col].setDisabledIcon(img);   //prevent icon from being grayed out
        tiles[row][col].setEnabled(false);
    }
}
