package Snake;

import java.awt.*;
import java.awt.event.*;

public class Mouse implements MouseListener {
    Game theGame; //So game variables can be accessed

    public Mouse(Game game) {
        theGame = game;
    }

    //Whenever the mouse is clicked, if mousing over a button (by checking game variables) then do something
    @Override
    public void mouseClicked(MouseEvent e) {
        //Start the game
        if (theGame.onMenu && theGame.mouseOverPlay) {
            theGame.onMenu = false;
            theGame.gameRunning = true;
            theGame.window.setCursor(Cursor.getDefaultCursor());
        }
        //Submit score
        if(theGame.gameOver && theGame.mouseOverSubmit && !theGame.scoreSubmitted){
            theGame.leaderboard.addScore(theGame.Points, theGame.name);
            theGame.scoreSubmitted = true;
            theGame.name = "Submitted!";
        }
        //Restart game
        if(theGame.gameOver && theGame.mouseOverRestart){
            GameWindow.currentGame.dispose(); //Disposes of current game to prevent memory/usage issues
            GameWindow.currentGame = new Grid(GameWindow.scalingFactor);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //mousePressed
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //mouseReleased
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

class MouseMove implements MouseMotionListener {
    Game theGame; //So game variables can be accessed
    int sf; //To make code easier to read

    public MouseMove(Game game) {
        theGame = game;
        sf = GameWindow.scalingFactor;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //mouseDragged
    }

    //Whenever the mouse is moved, get its x and y value relative to the screen. This is used to determine whether a button is being hovered over or not.
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        //The play button is being moused over on the menu screen
        if (theGame.onMenu && (x > 10 * sf - 100 && y > 10 * sf + 30 && x < 10 * sf + 110 && y < 10 * sf + 70)) {
            theGame.mouseOverPlay = true;
        } else {
            theGame.mouseOverPlay = false;
        }

        //The submit button is being moused over on the game over screen
        if (theGame.gameOver && (x > 10 * sf - 100 && y > 5 * sf + 80 && x < 10 * sf + 110 && y < 5 * sf + 120)) {
            theGame.mouseOverSubmit = true;
        }else{
            theGame.mouseOverSubmit = false;
        }

        //The restart button is being moused over on the game over screen
        if (theGame.gameOver && (x > 10 * sf - 100 && y > 5 * sf + 130 && x < 10 * sf + 110 && y < 5 * sf + 170)) {
            theGame.mouseOverRestart = true;
        }else{
            theGame.mouseOverRestart = false;
        }


    }
}