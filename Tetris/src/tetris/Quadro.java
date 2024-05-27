package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Quadro extends JPanel implements KeyListener {
	
	private static int FPS = 60;
	private static int delay = FPS / 1000;
	
	public static final int BOARD_WIDHT = 10;
	public static final int BOARD_HEIGHT = 20;
	public static final int BLOCK_SIZE = 30;
	private Timer looper;
	private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDHT];
	
	private Random random;
	
	private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"), 
	        Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
	
	private forma[] shapes = new forma[7];
	private forma courrentForma;
	
	public Quadro () {
		random = new Random();
		
		// create shapes
        shapes[0] = new forma(new int[][]{
            {1, 1, 1, 1} // I shape;
        }, this, colors[0]);

        shapes[1] = new forma(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // T shape;
        }, this, colors[1]);

        shapes[2] = new forma(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // L shape;
        }, this, colors[2]);

        shapes[3] = new forma(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // J shape;
        }, this, colors[3]);

        shapes[4] = new forma(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // S shape;
        }, this, colors[4]);

        shapes[5] = new forma(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // Z shape;
        }, this, colors[5]);

        shapes[6] = new forma(new int[][]{
            {1, 1},
            {1, 1}, // O shape;
        }, this, colors[6]);
        
        courrentForma = shapes[0];
		
		
		
		looper = new Timer(delay, new ActionListener() {
			int n = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				repaint();	
			}
			
		});
		looper.start();
		
	}
	
	private void update() {
		courrentForma.update();
	}
	
	public void setCurrentShape() {
		courrentForma = shapes[random.nextInt(shapes.length)];
		courrentForma.reset();
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		courrentForma.renderizar(g);
		
		for (int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				
				if (board[row][col] != null) {
					g.setColor(board[row][col]);
					g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
		
		//DESENHO DO QUADRO
		g.setColor(Color.white);
		for(int row = 0; row < BOARD_HEIGHT; row++) {
			g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDHT, BLOCK_SIZE * row);
		}
		
		for(int col = 0; col < BOARD_WIDHT + 1; col++) {
			g.drawLine(col * BLOCK_SIZE, 0, col * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
		}	
		
	}
	
	public Color[][] getQuadro() {
		return board;
	}


	@Override
	public void keyTyped(KeyEvent e) {}


	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode () == KeyEvent.VK_DOWN) {
			courrentForma.speedUp();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			courrentForma.moveRight();
		}  else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			courrentForma.moveLeft();
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			courrentForma.rotateShape();
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode () == KeyEvent.VK_DOWN) {
			courrentForma.speedDown();
			
		}
		
	}
	
}
