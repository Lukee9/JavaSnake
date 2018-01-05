package Snake;

import javax.swing.*;

public class GameWindow {
    final static int scalingFactor = 30; //Used to change the size of the window, everything drawn looks at this variable to determine its size/position
    static Grid currentGame; //Used to save the current game window so we can restart at the end of the game
    //Generates the initial game window to begin the first game
    public static void main(String[] args) {
       generateWindow();
    }
    //Generates the game window and saves it in currentGame
    private static void generateWindow() {
        currentGame = new Grid(scalingFactor);
    }

}

class Grid extends JFrame {
    int size; //For the size of the panel and frame

    //Generates the window and adds the listeners to it. Also starts the game code itself.
    public Grid(int scalingFactor) {
        size = scalingFactor * 20;//Width+Height=20
        setSize(size, size);
        setTitle("Snake");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //Starting the game
        Game g = new Game(this);

        //Adding the listeners
        addKeyListener(new Keyboard(g));
        addMouseListener(new Mouse(g));
        addMouseMotionListener(new MouseMove(g));

        setVisible(true);
    }
}
