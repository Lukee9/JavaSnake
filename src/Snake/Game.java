package Snake;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

public class Game {
    boolean onMenu = true; //If the game is on the menu screen (default)
    boolean gameRunning = false; //If the game is running (the snake section itself)
    boolean mouseOverPlay = false; //If, while on the menu, the mouse is over the play button
    boolean mouseOverSubmit = false; //If, while on the game over screen, the mouse is over the submit button
    boolean mouseOverRestart = false; //If, while on the game over screen, the mouse is over the restart button
    boolean gameOver = false; //If the game is not currently running (so we are on the menu or the game has ended)
    boolean frameOver = false; //If the current frame has been drawn - stops the snake moving multiple times per frame
    boolean appleExists = false; //If the apple exists - stops multiple apples being drawn and stops the code that calculates the apple being run multiple times
    boolean scoreSubmitted = false; //If the user has submitted their score - stops them being able to submit the same score multiple times in a single game

    Random rndApple = new Random(); //Used to randomly generate the next position of the apple
    Leaderboard leaderboard; //For saving and reading to/from the leaderboard text file
    String[] allBest; //An array containing up to the top 10 scores of all times
    String[] recentBest; //An array containing up to the top 10 scores within the last 24 hours
    Dimension screenSize = new Dimension(0, 0); //The size of the window
    String name = ""; //The name of the player which they can enter at the end
    ArrayList<Point> availablePoints; //All available points for the apple to be placed (i.e. not within the snake or outside the window)
    Point nextApplePoint = new Point(0, 0); //The next point the apple will be placed when it is needed
    Point nextPoint = new Point(0, 0); //Used to store the next point the head will be in
    int nextPointIndex; //The index of the next random point for the apple to appear
    int direction = 3; //N=0,E=1,S=2,W=3 (the direction the snake is moving)
    int nextDirection = 3; //Prevents direction being set before previous direction was set (such as if two keys are pressed too close together, sometimes causing an instant loss erroneously)
    int Points = 0; //The number of points the player has, each apple increments by 1
    int sf; //Scores the scaling factor
    ArrayList<Point> Snake; //Every point that the snake takes up
    Grid window; //The window that is being drawn on

    public Game(Grid Window) {
        window = Window;
        sf = GameWindow.scalingFactor;
        leaderboard = new Leaderboard("snakescore.txt");
        Snake = new ArrayList<Point>();
        //Generates the three initial segments of the snake
        Point InitialPoint;
        InitialPoint = new Point(sf * 10, sf * 10);
        Snake.add(InitialPoint);
        InitialPoint = new Point(sf * 11, sf * 10);
        Snake.add(InitialPoint);
        InitialPoint = new Point(sf * 12, sf * 10);
        Snake.add(InitialPoint);
        setupMenu();
    }

    //Starts the game with the menu
    private void setupMenu() {
        onMenu = true;
        startGame();
    }

    //Starts the timer that runs the game
    private void startGame() {
        Timer t;
        t = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFrame();
            }
        });
        t.start();
    }

    //Each frame of the game
    private void nextFrame() {
        frameOver = false;
        //Removes the game panel if it exists
        if (window.getComponents().length > 1) window.remove(0);
        GamePanel gp = new GamePanel(this);
        //Adds (and therefore paints) the game panel
        window.add(gp);
        //Resizes window if required, otherwise skips this, as this causes a repaint which will cause much more calculations than are necessary
        if (window.getSize() != screenSize) {
            window.pack(); //Keep at correct size
            screenSize = window.getSize(); //Stops redrawing another time
        }
        window.repaint();
    }

    class GamePanel extends JPanel {
        Game theApp;

        GamePanel(Game app) {
            theApp = app;
            //Keeps the window size at the preferred size
            setPreferredSize(new Dimension(20 * sf, 20 * sf));
        }

        //All the panel painting code is here
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            //First draws the black background
            Square background = new Square(0, 0, 20 * sf);
            background.draw(g);
            g.setColor(Color.white);
            //Chooses between drawing the menu, drawing the game or drawing the game over screen
            if (onMenu) menu(g);
            if (gameRunning) frame(g);
            if (gameOver) gameFinished(g);
        }

        //Displaying the menu
        private void menu(Graphics g) {
            //Displays the title SNAKE and my student details
            ScreenString title = new ScreenString(10 * sf - 88, 10 * sf - 100, "SNAKE", 48);
            title.draw(g);

            ScreenString credits = new ScreenString(10 * sf - 100, 10 * sf - 50, "By Luke Dixon (1603069)", 12);
            credits.draw(g);

            //Displays the play button, the cursor and button appearance change when being moused over (using the MouseMotionListener)
            Rectangle outerPlaybtn = new Rectangle(10 * sf - 100, 10 * sf, 200, 40);
            outerPlaybtn.draw(g);
            g.setColor(Color.black);
            if (!mouseOverPlay) {
                Rectangle innerPlaybtn = new Rectangle(10 * sf - 95, 10 * sf + 5, 190, 30);
                innerPlaybtn.draw(g);
                g.setColor(Color.white);
                setCursor(Cursor.getDefaultCursor());
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            ScreenString playLbl = new ScreenString(10 * sf - 30, 10 * sf + 27, "PLAY", 18);
            playLbl.draw(g);
        }

        //Displaying the game
        private void frame(Graphics g) {
            Square snakeSegment;
            Square apple;
            Point lastSegment = Snake.get(Snake.size() - 1); //Last segment saved to add to the snake if an apple is picked up
            Point headSegment;
            //Goes through every snake segment and moves them up one after drawing them
            for (int i = Snake.size() - 1; i >= 0; i--) {
                snakeSegment = new Square(Snake.get(i).x, Snake.get(i).y, sf);
                snakeSegment.draw(g);


                //Generate apple if required
                if (!appleExists) {
                    //Calculates all points on screen
                    availablePoints = new ArrayList<Point>();
                    for (int j = 0; j < sf * 20; j += sf) {
                        for (int k = 0; k < sf * 20; k += sf) {
                            availablePoints.add(new Point(j, k));
                        }
                    }
                    //Removes points that the snake takes up, leaving only available points for the apple
                    availablePoints.removeAll(Snake);
                    nextPointIndex = rndApple.nextInt(availablePoints.size());
                    nextApplePoint = availablePoints.get(nextPointIndex);

                    appleExists = true;
                }
                apple = new Square(nextApplePoint.x, nextApplePoint.y, sf);
                apple.draw(g);


                //Prevent snake being recalculated multiple times even though frame is redrawn multiple times
                if (!frameOver) {
                    //If drawing the head, move in the direction set by the direction variable
                    if (i == 0) {
                        headSegment = Snake.get(0);
                        switch (direction) {
                            case 0:
                                nextPoint = new Point(headSegment.x, headSegment.y - sf);
                                break;
                            case 1:
                                nextPoint = new Point(headSegment.x + sf, headSegment.y);
                                break;
                            case 2:
                                nextPoint = new Point(headSegment.x, headSegment.y + sf);
                                break;
                            case 3:
                                nextPoint = new Point(headSegment.x - sf, headSegment.y);
                                break;
                        }
                        //If snake is out of bounds or contains itself (collided with itself) then set game over and get leaderboard scores
                        if (Snake.contains(nextPoint) || headSegment.x < 0 || headSegment.y < 0 || headSegment.x > 19 * sf || headSegment.y > 19 * sf) {
                            allBest = leaderboard.getAllBest();
                            recentBest = leaderboard.getRecentBest();
                            gameRunning = false;
                            gameOver = true;
                        }
                        Snake.set(0, nextPoint);
                    } else {
                        //Move all segments of the snake up one to where the next segment is
                        Snake.set(i, Snake.get(i - 1));
                    }
                }
            }
            direction = nextDirection; //So you can't change direction twice per frame, sometimes causing an erroneous loss
            //Went over apple, add point
            if (Snake.contains(nextApplePoint)) {
                appleExists = false;
                Snake.add(lastSegment);
                Points++;
            }
            frameOver = true;
        }

        //Game is over, show game over screen
        private void gameFinished(Graphics g) {
            //Shows 'GAME OVER' and the users score and a place for their to type their name
            ScreenString gameOvertxt = new ScreenString(10 * sf - 100, 5 * sf - 100, "GAME OVER", 30);
            gameOvertxt.draw(g);
            ScreenString scoreLbl = new ScreenString(10 * sf - 90, 5 * sf - 50, "Score:", 18);
            scoreLbl.draw(g);
            ScreenString scoreValue = new ScreenString(10 * sf + 50, 5 * sf - 50, Integer.toString(Points), 18);
            scoreValue.draw(g);
            ScreenString nameLbl = new ScreenString(10 * sf - 90, 5 * sf, "Name:", 18);
            nameLbl.draw(g);
            //Draws the user name that they have typed using the KeyListener
            ScreenString playername = new ScreenString(10 * sf + 25, 5 * sf, name, 18);
            playername.draw(g);

            //Similarly to the play button, there is a restart and submit button that change when moused over
            Rectangle outerSubmitbtn = new Rectangle(10 * sf - 100, 5 * sf + 50, 200, 40);
            outerSubmitbtn.draw(g);
            g.setColor(Color.black);
            if (mouseOverSubmit || mouseOverRestart) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }

            if (!mouseOverSubmit) {
                Rectangle innerSubmitbtn = new Rectangle(10 * sf - 95, 5 * sf + 5 + 50, 190, 30);
                innerSubmitbtn.draw(g);
                g.setColor(Color.white);
            }
            ScreenString submitLbl = new ScreenString(10 * sf - 40, 5 * sf + 27 + 50, "SUBMIT", 18);
            submitLbl.draw(g);
            g.setColor(Color.white);
            Rectangle outerRestartbtn = new Rectangle(10 * sf - 100, 5 * sf + 100, 200, 40);
            outerRestartbtn.draw(g);
            g.setColor(Color.black);
            if (!mouseOverRestart) {
                Rectangle innerRestartbtn = new Rectangle(10 * sf - 95, 5 * sf + 5 + 100, 190, 30);
                innerRestartbtn.draw(g);
                g.setColor(Color.white);
            }
            ScreenString restartLbl = new ScreenString(10 * sf - 50, 5 * sf + 27 + 100, "RESTART", 18);
            restartLbl.draw(g);
            g.setColor(Color.white);

            //Draws the titles for all time highscores and recent highscores
            ScreenString allTimelbl = new ScreenString(5 * sf - 100, 12 * sf, "All time highscores", 12);
            allTimelbl.draw(g);
            ScreenString recentlbl = new ScreenString(15 * sf - 85, 12 * sf, "Recent highscores", 12);
            recentlbl.draw(g);

            //Writes the all time high scores and recent high scores under their title
            ScreenString nextScore;
            for (int i = 0; i < allBest.length; i++) {
                nextScore = new ScreenString(5 * sf - 100, 12 * sf + (i + 1) * 15, allBest[i], 10);
                nextScore.draw(g);
            }
            for (int j = 0; j < recentBest.length; j++) {
                nextScore = new ScreenString(15 * sf - 85, 12 * sf + (j + 1) * 15, recentBest[j], 10);
                nextScore.draw(g);
            }

        }

    }
}
