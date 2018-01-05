package Snake;

import java.awt.event.*;

public class Keyboard implements KeyListener {
    Game theGame; //So game variables can be accessed
    boolean keyDown = false; //Prevents multiple keys being pressed simultaneously

    public Keyboard(Game game) {
        theGame = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //keyTyped
    }

    //When the user presses a key, do something depending on which stage of the game we are at
    @Override
    public void keyPressed(KeyEvent e) {
        keyDown = true;
        //If the game is over, we want the user to type their name in to save
        if (theGame.gameOver && !theGame.scoreSubmitted) {
            if (Character.isLetterOrDigit(e.getKeyChar())) {
                //Adds the new letter to their name (only if it is alphanumeric)
                theGame.name += e.getKeyChar();
            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                //Removes the last letter from their name if exists if the user presses backspace
                if (theGame.name.length() > 0) {
                    theGame.name = theGame.name.substring(0, theGame.name.length() - 1);
                }
            }
        }

        //Allows the user to change direction based on what arrow key they press
        switch (e.getKeyCode()) {
            //Up arrow, move up
            case KeyEvent.VK_UP:
                //If statements prevent moving snake back in opposite direction (instant loss)
                if (theGame.direction != 2) {
                    theGame.nextDirection = 0;
                }
                break;
            //Right arrow, move right
            case KeyEvent.VK_RIGHT:
                if (theGame.direction != 3) {
                    theGame.nextDirection = 1;
                }
                break;
            //Down arrow, move down
            case KeyEvent.VK_DOWN:
                if (theGame.direction != 0) {
                    theGame.nextDirection = 2;
                }
                break;
            //Left arrow, move left
            case KeyEvent.VK_LEFT:
                if (theGame.direction != 1) {
                    theGame.nextDirection = 3;
                }
                break;
        }
    }

    @Override
    //Prevents multiple keys being pressed simultaneously
    public void keyReleased(KeyEvent e) {
        keyDown = false;
    }
}
