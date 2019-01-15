/*
Project:    Program 01
File:       Bombs.java
Purpose:    Builds the main constructors, containers, panels, etc. that function as the core mechanics for the program
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
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Bombs extends JFrame implements ActionListener {
    //main gameplay obj
    private Board mineBoard;

    //labels for the timer and the bomb count
    private JLabel timerLabel, bombLabel;
    private JTextField sizeText, bombText;

    //layout objs: view of the current game board, lables, and menu
    private JPanel boardView, labelView, customPanel;
    private JMenuBar menuBar;   //contains new game, settings, quit, help
    private JMenu new_game, settings, quit, help;    // display a menu with options for 3 presets, and a custom popup
    private JMenuItem beginner, intermediate, expert, custom;
    private Timer timer;
    private int size, bombCount;
    private boolean currgameActive = false;
    private int delay = 1000; // every 1 second
    private int counter = 0;

    private Container c = getContentPane();

    //pre-define the beginner, intermediate, and expert settings
    private int beginGrid = 4;  private int beginBomb = 4;
    private int interGrid = 8;  private int interBomb = 15;
    private int experGrid = 12; private int experBomb = 40;



    public Bombs(){
        super("Minesweeper!");

        timerLabel = new JLabel("Timer: ");

        //set up a timer
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currgameActive){
                    timerLabel.setText("Timer: "+ counter);
                    counter++;
                }
                else{
                    timer.stop();
                    counter = 0;
                }
            }
        });

        menuBar = new JMenuBar();    //holds the menu items

        //sets up the option panel for getting the custom settings
        sizeText = new JTextField(5);
        bombText = new JTextField(5);
        customPanel = new JPanel();
        customPanel.add(new JLabel("Grid Size Between 3 to 12: "));
        customPanel.add(sizeText);
        customPanel.add(Box.createHorizontalStrut(15));     //lil space
        customPanel.add(new JLabel("Bomb Size Between 2 and half the of the area of the Grid: "));
        customPanel.add(bombText);

        new_game = new JMenu("New Game");   //basically restarts the game
        new_game.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {restartGame();}
            @Override
            public void menuDeselected(MenuEvent e) {}
            @Override
            public void menuCanceled(MenuEvent e) {}
        });
        menuBar.add(new_game);
        settings = new JMenu("Settings");   //holds the sub menu
        beginner = new JMenuItem("Beginner: 4x4 Grid, 4 Bombs");
        intermediate = new JMenuItem("Intermediate: 8x8 Grid, 15 Bombs");
        expert = new JMenuItem("Expert: 12x12 Grid, 40 Bombs");
        custom = new JMenuItem("Custom");
        //actionlistener for the presets and custom menu setting
        beginner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                size = beginGrid;
                bombCount = beginBomb;
                restartGame();
            }
        });
        intermediate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                size = interGrid;
                bombCount = interBomb;
                restartGame();
            }
        });
        expert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                size = experGrid;
                bombCount = experBomb;
                restartGame();
            }
        });
        custom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // gets the results from the JOptionPane
                int result = JOptionPane.showConfirmDialog(null, customPanel,
                        "Please Enter Grid and Bomb Values", JOptionPane.OK_CANCEL_OPTION);
                //only change anything when the OK button is submitted
                if(result==JOptionPane.OK_OPTION){
                    //convert to int
                    try{
                        int new_size=Integer.parseInt(sizeText.getText());
                        //check size constraint
                        if (new_size<3 || new_size >12){
                            size = beginGrid;
                        }
                        else {
                            size = new_size;
                        }
                    }
                    catch (NumberFormatException ex){
                        size = beginGrid;
                    }
                    try{
                        int new_bombCount = Integer.parseInt(bombText.getText());
                        //check size constraint by also change size if it fails
                        if (new_bombCount<2 || new_bombCount > ((size*size)/2)){
                            size = beginGrid;
                            bombCount = beginBomb;
                        }
                        else{
                            bombCount = new_bombCount;
                        }
                    }
                    catch (NumberFormatException ex){
                        bombCount = beginBomb;
                    }
                    //remake the board with the new settings
                    restartGame();
                }
            }
        });
        settings.add(beginner);
        settings.add(intermediate);
        settings.add(expert);
        settings.add(custom);
        menuBar.add(settings);

        quit = new JMenu("Quit");           //exits the program
        quit.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {System.exit(0);}
            @Override
            public void menuDeselected(MenuEvent e) {}
            @Override
            public void menuCanceled(MenuEvent e) {}
        });
        menuBar.add(quit);

        help = new JMenu("Help");           //displays a popup with how to play
        help.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                //open a popup message
                JOptionPane.showMessageDialog(labelView,"The classic game Minesweeper. Click a tile to reveal " +
                        "\nthe tile. If the tile is a bomb, you loose. If a tile reveals a number then that represents " +
                        "\nthe number of bombs in an 8 tile radius. The goal of the game is reveal all non-bomb tiles." +
                        "\nThe settings menu will have 3 presets and a custom board.");
            }
            @Override
            public void menuDeselected(MenuEvent e) {}
            @Override
            public void menuCanceled(MenuEvent e) {}
        });
        menuBar.add(help);
        setJMenuBar(menuBar);

        labelView = new JPanel();   //holds the labels
        boardView = new JPanel();   //holds the game board

        //set up the content pane, which everything is added onto
        //using gridbag layout to prevent the tiles from resizing, makes it looks nice (spent too much time on this than anything else...)
        c.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        //setup the game board with the bomb tiles
        setupBoard(size, bombCount);

        //display the curr bombCount, updates in restartGame
        bombLabel = new JLabel("Bombs: " + bombCount);

        // the labels to the labelView
        labelView.setLayout(new GridLayout(1,2,2,2));

        labelView.add(timerLabel);
        labelView.add(bombLabel);

        //add all panels to the container
        //apply the constraints for the board panel
        gc.gridx=0;
        gc.gridy=0;
        gc.anchor = GridBagConstraints.PAGE_START;
        c.add(labelView, gc);

        //apply the constraints for the board panel
        gc.gridx=0;
        gc.gridy=2;
        gc.anchor = GridBagConstraints.PAGE_END;
        c.add(boardView, gc);

        setSize(750,750);       //used 750 so we dont have to resize when choosing the highest grid size
        setVisible(true);

    }

    //store the currently selected tile and store its value
    public void actionPerformed(ActionEvent e) {
        bombReveal currTile = (bombReveal) e.getSource();
        currTile.showFront();
        if (!currgameActive){
            currgameActive=true;
            timer.start();
        }

        //check the bombID value
        if (currTile.get_bombID()==1){
            //flip all tiles over and end the game
            for (int i=0; i < size; i++){
                for (int j=0; j < size; j++){
                    mineBoard.flip_disable(i, j);
                }
            }
            currgameActive = false;
            timer.stop();

            //after flipping all the tiles, display a popup displaying losing the game
            JOptionPane.showMessageDialog(labelView,"BOOM!!! Seems that you set off the bomb.");

        }
        else{
            //get the row and col of the curr tile
            int row = currTile.get_Row();
            int col = currTile.get_Col();
            //check if the nearBomb value is 0 or not
            if (currTile.nearBomb()>0){
                mineBoard.flip_disable(row, col);
            }
            else{
                //call a recursive method that reveals all 0 tiles and their nieghbors based
                // on the initial 0 val tile selected
                mineBoard.revealTiles_helper(row, col, size-1, size-1);
            }
            //after clicking the tile, loop through the array and check if all non-bomb tiles
            //have been disabled
            if (mineBoard.win_check(size)){
                currgameActive = false;
                timer.stop();
                //display a popup window(optional)
                JOptionPane.showMessageDialog(labelView,"Congrats you win!");
                //flip all tiles over and end the game
                for (int i=0; i < size; i++){
                    for (int j=0; j < size; j++){
                        mineBoard.flip_disable(i, j);
                    }
                }
            }
        }

    }

    //sets up the board and adds to the container
    public void setupBoard(int gridSize, int bombs){
        //check if the size value has changed, if not, use default
        if (gridSize > 0){
            mineBoard = new Board(gridSize, bombs, this);
        }
        else{
            //assigns the size and bombCount to prevent issues
            size = beginGrid;
            bombCount = beginBomb;
            gridSize = beginGrid;
            bombs = beginBomb;
            mineBoard = new Board(gridSize, bombs, this);
        }

        //add the game board to the board layout area
        boardView.setLayout(new GridLayout(gridSize,gridSize,0,0));

        //change the dimensions based on size*50, same for frame size
        boardView.setPreferredSize(new Dimension((gridSize*50),(gridSize*50)));
        mineBoard.fillBoardView(boardView);
    }

    //calls the setupBoard function after removing and validating the curr contents
    public void restartGame(){
        //the size and bombCount is set before this function is called
        counter=0;
        currgameActive = false;
        timer.stop();
        bombLabel.setText("Bombs: "+bombCount);
        boardView.removeAll();
        c.revalidate();
        setupBoard(size, bombCount);
    }

    public static void main(String args[]){
        Bombs M = new Bombs();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
