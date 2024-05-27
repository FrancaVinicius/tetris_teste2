package tetris;

import java.awt.Color;
import java.awt.Graphics;
import static tetris.Quadro.BLOCK_SIZE;
import static tetris.Quadro.BOARD_HEIGHT;

public class forma {
	private int x = 4, y = 0;
	private int normal = 600;
	private int fast = 50;
	private int delayTimeForMovement = normal;
	private long beginTime;
	
	private int deltaX = 0;
	private boolean collision = false;
	
	private int [][] cordenadas;
	private Quadro quadro;
	private Color color;
	
	public forma(int[][] cordenadas, Quadro quadro, Color color) {
		this.cordenadas = cordenadas;
		this.quadro = quadro;
		this.color = color;
	}
	
	public void setX(int x) {
		this.x = x;
		
	}
	
	public void setY(int y) {
		this.y = y;
		
	}
	
	public void reset() {
		this.x = 4;
		this.y = 0;
		collision = false;
	}
	
	public void update() {
		if(collision) {
			// PREENCHE A COR DO QUADRO
			for(int row = 0; row < cordenadas.length; row++) {
				for(int col = 0; col < cordenadas[0].length; col++) {
					if(cordenadas[row][col] != 0) {
						quadro.getQuadro()[y + row][x + col] = color;
					}
				}
			}
			checkLine();
			// define a forma atual
			quadro.setCurrentShape();
			
			return;
		}
		
		// Movimento horizontal
		boolean moveX = true;
		if(!(x + deltaX + cordenadas[0].length > 10) && !(x + deltaX < 0)) {
			for(int row = 0; row < cordenadas.length; row++) {
				for(int col = 0; col < cordenadas[row].length; col++) {
					if(cordenadas[row][col] !=0) {
						if(quadro.getQuadro()[y + row][x + deltaX + col] != null) {
							moveX = false;
						}	
					}
				}
			}
			if(moveX) {
				x += deltaX;
			}	
		}
		deltaX = 0;
		
		
		
		if(System.currentTimeMillis() - beginTime > delayTimeForMovement) {
			// Movimento vertical
			if(!(y + 1 + cordenadas.length > BOARD_HEIGHT)) {
				for(int row = 0; row < cordenadas.length; row++) {
					for(int col = 0; col < cordenadas[row].length; col++) {
						if(cordenadas[row][col] != 0) {
							if(quadro.getQuadro()[y + 1 + row][x + deltaX + col] != null) {
								collision = true;
							}
						}
					}
				}
				if(!collision) {
					y++;
				}
			} else {
				collision = true;
			}
			beginTime = System.currentTimeMillis();
		}
	}
	
	
	private void checkLine() {
		int bottomLine = quadro.getQuadro().length - 1;
		for(int topLine = quadro.getQuadro().length - 1; topLine > 0; topLine --) {
			int count = 0;
			for(int col = 0; col < quadro.getQuadro()[0].length; col++) {
				if(quadro.getQuadro()[topLine][col] != null) {
					count++;
				}
				quadro.getQuadro()[bottomLine][col] = quadro.getQuadro()[topLine][col];
			}
			if(count < quadro.getQuadro()[0].length) {
				bottomLine--;
			}
		}
	}
	
	
	public void rotateShape() {
		int[][] rotatedShape = transposeMatrix(cordenadas);
		reverseRows(rotatedShape);
		// verifica o lado direito e inferior
		if((x + rotatedShape[0].length > Quadro.BOARD_WIDHT) || (y + rotatedShape.length > 20)) {
			return;
		}
		
		
		// verificar se há colisão com outras formas antes de fazer
		for(int row = 0; row < rotatedShape.length; row++) {
			for(int col = 0; col < rotatedShape[row].length; col++) {
				if(rotatedShape[row][col] != 0) {
					if(quadro.getQuadro()[y + row][x + col] != null) {
						return;
					}
				}
			}
		}
		cordenadas = rotatedShape;
	}
	
	
	private int [] [] transposeMatrix(int [][] matrix) {
		int [][] temp = new int [matrix[0].length][matrix.length];
		for(int row = 0; row < matrix.length; row++) {
			for(int col = 0; col < matrix[0].length; col ++) {
				temp[col][row] = matrix[row][col];
			}
		}
		return temp;
	}
	
	private void reverseRows(int [][] matrix) {
		int middle = matrix.length / 2;
		for(int row = 0; row < middle; row++) {
			int[] temp = matrix[row];
			matrix[row] = matrix[matrix.length - row - 1];
			matrix[matrix.length - row - 1] = temp;
		}
	}
	
	public void renderizar(Graphics g) {
		//DESENHO DA FORMA
		for(int row = 0; row < cordenadas.length; row ++){
			for(int col = 0; col <cordenadas[0].length; col++) {
				if(cordenadas[row][col] != 0) {
				g.setColor(color);
				g.fillRect(col * BLOCK_SIZE + x * BLOCK_SIZE, row * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
		
	}
	
	public void speedUp() {
		delayTimeForMovement = fast;
	}
	
	public void speedDown() {
		delayTimeForMovement = normal;
	}
	
	public void moveRight() {
		deltaX = 1;
	}
	
	public void moveLeft() {
		deltaX = -1;
	}

}
