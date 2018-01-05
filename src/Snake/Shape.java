package Snake;

import javax.swing.*;
import java.awt.*;

//All shapes have an x and y position
abstract class Shape {
    int posX;
    int posY;
}

class Square extends Shape {
    int sideLength;
    //New square shape, which takes coordinates x and y and a single length of a side
    public Square(int X, int Y, int Side) {
        sideLength = Side;
        posX = X;
        posY = Y;
    }

    //Used to draw this shape during paintComponent
    public void draw(Graphics g) {
        g.fillRect(posX, posY, sideLength, sideLength);
    }

    //Used to create a panel containing this shape (is not used in this application, only used as example)
    private class DrawSquare extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            draw(g);
        }
    }
}

class Rectangle extends Shape {
    int width;
    int height;
    //New rectangle shape, which takes coordinates x and y and lengths of both sides of the rectangle
    public Rectangle(int X, int Y, int Width, int Height) {
        width = Width;
        height = Height;
        posX = X;
        posY = Y;
    }
    //Used to draw this shape during paintComponent
    public void draw(Graphics g) {
        g.fillRect(posX, posY, width, height);
    }
    //Used to create a panel containing this shape (is not used in this application, only used as example)
    private class DrawRectangle extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            draw(g);
        }
    }
}

class Circle extends Shape {
    int radius;
    //New circle shape, which takes coordinates x and y and the radius
    public Circle(int X, int Y, int Radius) {
        radius = Radius;
        posX = X;
        posY = Y;
    }
    //Used to draw this shape during paintComponent - radius is used to recalculate the actual position, and used to calculate diameter of the circle
    public void draw(Graphics g) {
        g.fillOval(posX - radius, posY - radius, radius * 2, radius * 2);
    }
    //Used to create a panel containing this shape (is not used in this application, only used as example)
    private class DrawCircle extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            draw(g);
        }
    }
}

class Triangle extends Shape {
    int posX2;
    int posY2;
    int posX3;
    int posY3;
    //New triangle shape, which takes three sets of coordinates of x and y representing the three points of the triangle
    public Triangle(int X, int Y, int X2, int Y2, int X3, int Y3) {
        posX = X;
        posY = Y;
        posX2 = X2;
        posY2 = Y2;
        posX3 = X3;
        posY3 = Y3;
    }
    //Used to draw this shape during paintComponent - a polygon of 3 sides
    public void draw(Graphics g) {
        g.fillPolygon(new int[]{posX, posX2, posX3}, new int[]{posY, posY2, posY3}, 3);
    }
    //Used to create a panel containing this shape (is not used in this application, only used as example)
    private class DrawTriangle extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            draw(g);
        }
    }
}

class Pie extends Shape {
    int radius;
    int startAngle;
    int angle;
    //New pie shape, which takes coordinates x and y and a start angle, as well as pieces and total pieces
    public Pie(int X, int Y, int Radius, int StartAngle, int Pieces, int TotalPieces) {
        posX = X;
        posY = Y;
        radius = Radius;
        startAngle = StartAngle;
        angle = (int) (((double) Pieces / (double) TotalPieces) * 360); //The end angle is calculated by working out how many slices of the pie are missing and then multiplying by 360 degrees for a full circle
    }
    //Used to draw this shape during paintComponent - same as filling a circle but only between the given start angle and the calculated angle
    public void draw(Graphics g) {
        g.fillArc(posX - radius, posY - radius, radius * 2, radius * 2, startAngle, angle);
    }
    //Used to create a panel containing this shape (is not used in this application, only used as example)
    private class DrawPie extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            draw(g);
        }
    }
}

class ScreenString extends Shape {
    String s;
    int fontSize;
    //New string shape, which takes coordinates x and y and the string and font size
    public ScreenString(int X, int Y, String string, int FontSize) {
        posX = X;
        posY = Y;
        s = string;
        fontSize = FontSize;
    }
    //Used to draw this shape during paintComponent
    public void draw(Graphics g) {
        //Gets all available fonts on the system
        String[] AvailableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        boolean hasOCR = false;
        for (String s : AvailableFonts) {
            //Checks if our desired font is on the system (OCR A Std)
            if (s.equals("OCR A Std")) hasOCR = true;
        }
        //If our desired font is on the system use it
        if (hasOCR) g.setFont(new Font("OCR A Std", Font.BOLD, fontSize));
        //otherwise use the default font
        else g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, fontSize));
        g.drawString(s, posX, posY);
    }

    //Used to create a panel containing this shape (is not used in this application, only used as example)
    private class DrawString extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            draw(g);
        }
    }
}