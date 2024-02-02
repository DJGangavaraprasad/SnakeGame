package BackendClasses;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class SnakeGame extends JFrame {
    private final int BOARD_WIDTH = 400;
    private final int BOARD_HEIGHT = 400;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 1600;
    private final int RAND_POS = 40;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int score = 0;
    private int highScore = 0;

    private boolean inGame = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean leftDirection = false;
    private boolean rightDirection = true;

    private Timer timer;

    public SnakeGame() {
        initBoard();
        initGame();
    }

    private void initBoard() {
        setTitle("Snake Game");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addKeyListener(new TAdapter());
    }

    private void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, new GameCycle());
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (inGame) {
            g.setColor(Color.red);
            g.fillOval(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.orange);
                }
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }

            g.setColor(Color.white);
            g.drawString("Score: " + score, 10, 20);
            g.drawString("High Score: " + highScore, 10, 40);

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (BOARD_WIDTH - metrics.stringWidth(message)) / 2, BOARD_HEIGHT / 2);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;

            if (score > highScore) {
                highScore = score;
            }

            locateApple();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= BOARD_HEIGHT || y[0] < 0 || x[0] >= BOARD_WIDTH || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inGame) {
                checkApple();
                checkCollision();
                move();
            }
            repaint();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && !downDirection) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && !upDirection) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_ENTER && !inGame) {
                inGame = true;
                score = 0;
                dots = 3;
                upDirection = false;
                downDirection = false;
                leftDirection = false;
                rightDirection = true;
                initGame();
            }

            if (key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
