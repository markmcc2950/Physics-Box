import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import javax.swing.Timer;


public class MyBox {
    // Set up toolkit things and screen resolution info
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    // Set up JFrame stuff
    JFrame frame = new JFrame("Simple Physics Box");
    JPanel boxPanel = new DrawingPanel();

    // Set specific frame values
    int windowWidth = (int)(screenWidth * 0.8);
    int windowHeight = (int)(screenHeight * 0.8);

    MyBox() {
        frame.setSize(windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(boxPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Custom JPanel for drawing shapes
    static class DrawingPanel extends JPanel {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // Get the current refresh rate
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();
        int refreshRate = dm.getRefreshRate();

        double deltaTime = (refreshRate > 0) ? (1.0 / refreshRate) : (1.0 / 60.0);
        final double baseSpeed = 400;
        double currentSpeedX = baseSpeed;
        double currentSpeedY = baseSpeed;

        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        int screenCenterX = (int)(screenWidth / 2);
        int screenCenterY = (int)(screenHeight / 2);

        int boxDimensions = Math.min((int)(screenWidth * 0.1), (int)(screenHeight * 0.1));
        int boxCenter = boxDimensions / 2;

        int xCoord = screenCenterX - (boxDimensions / 2);
        int yCoord = screenCenterY - (boxDimensions / 2);

        double yFriction = 0.95;
        double xFriction = (1  + (1 - yFriction)) * yFriction * (Math.abs(1 - (screenWidth / screenHeight)));

        double moveSpeed = 0.25;
        //int currentSpeed = 50;
        int currDirX = 1;
        int currDirY = 1;

        Timer timer;

        int boxRed = 0;
        int boxGreen = 0;
        int boxBlue = 0;

        DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    // Set the boolean to dictate which direction we're moving (Horizontal Values)
                    if (mouseX < (xCoord)) {
                        currDirX = 1;
                    }
                    else if (mouseX > (xCoord + boxDimensions)) {
                        currDirX = -1;
                    }
                    else {
                        currDirX = 0;
                    }
                    // Set the boolean to dictate which direction we're moving (Vertical Values)
                    if (mouseY < (yCoord)) {
                        currDirY = 1;
                    }
                    else if (mouseY > (yCoord + boxDimensions)) {
                        currDirY = -1;
                    }
                    else {
                        currDirY = 0;
                    }

                    if (timer != null && timer.isRunning()) {
                        timer.stop();
                    }

                    timer = new Timer(1000 / refreshRate, actionEvent -> {
                        if (currentSpeedX > 0 || currentSpeedY > 0) {
                            // Change x-movement
                            xCoord += (int)(currDirX * currentSpeedX);
                            xCoord = Math.max(0, Math.min(xCoord, getWidth() - boxDimensions));

                            // Change y-movement
                            yCoord += (int)(currDirY * currentSpeedY);
                            yCoord = Math.max(0, Math.min(yCoord, getWidth() - boxDimensions));

                            // If the box hits an edge of the screen, bounce off, remove % of current velocity
                            // Left edge
                            if (xCoord <= 0) {
                                currentSpeedX *= xFriction;
                                currDirX = 1;

                                // Set custom colors to the box
                                boxRed = Math.min(255, boxRed + 10);
                                boxBlue = Math.max(0, boxBlue - 10);
                            }
                            // Right edge
                            if (xCoord >= (getWidth() - boxDimensions)) {
                                currentSpeedX *= xFriction;
                                currDirX = -1;

                                // Set custom colors to the box
                                boxBlue = Math.min(255, boxBlue + 10);
                                boxGreen = Math.max(0, boxGreen - 10);
                            }
                            // Top edge
                            if (yCoord <= 0) {
                                currentSpeedY *= yFriction;
                                currDirY = 1;

                                // Set custom colors to the box
                                boxBlue = Math.min(255, boxBlue + 10);
                                boxRed = Math.max(0, boxRed - 10);
                            }
                            // Bottom edge
                            if (yCoord >= (getHeight() - boxDimensions)) {
                                currentSpeedY *= yFriction;
                                currDirY = -1;

                                // Set custom colors to the box
                                boxGreen = Math.min(255, boxGreen + 10);
                                boxBlue = Math.max(0, boxBlue - 10);
                            }

                            currentSpeedX = Math.max(0, currentSpeedX - moveSpeed);
                            currentSpeedY = Math.max(0, currentSpeedY - moveSpeed);
                            repaint();
                        }
                        else {
                            timer.stop();
                            currentSpeedX = baseSpeed;
                            currentSpeedY = baseSpeed;
                        }
                    });

                    timer.start();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color myColor = new Color(boxRed, boxGreen, boxBlue);
            g.setColor(myColor);
            g.fillRect(xCoord, yCoord, boxDimensions, boxDimensions);
        }

        /*protected void paintComponent(Graphics g) {
            // Set up the box
            super.paintComponent(g);
            g.setColor(Color.black);
            g.drawRect(xCoord, yCoord, boxDimensions, boxDimensions);

            g.setColor(Color.RED);
            g.fillRect(xCoord, yCoord, boxDimensions, boxDimensions);
        }*/
    }
}