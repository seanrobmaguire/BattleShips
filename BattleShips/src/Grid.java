import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;


public class Grid implements Serializable{
	private int gridSize = 10;
	private int gridMin = 0;
	private Square [][] squareGrid = new Square [gridSize][gridSize];

	public int getMax () {
		return this.gridSize-1;
	}

	public String toString() {
		String output ="Grid max "+this.gridSize;
		output = output +"Grid min "+this.gridMin;

		return output;
	}

	public Ship returnShipFromSquare(int coordX, int coordY) {
		return this.squareGrid[coordX][coordY].getShip();
	}

	public boolean checkSqaureGrid(int x, int y) {
		boolean matched = false;
		if (this.squareGrid[x][y].getContainShip()) {
			matched = true;	
		}
		return matched;
	}

	public String getShipLocations(Ship tempShip) {
		String output="";
		for(int outer = 0; outer < this.gridSize; outer++) {
			for(int inner = 0; inner < this.gridSize; inner++) {
				if(this.squareGrid[outer][inner].getShip() == tempShip) {
					output = output+ "("+outer+","+inner+")";
				}
			}
		}
		return output;
	}//closegetShipLocations

	public void removeShipFromGrid(Ship ship) {
		for(Square [] squareArr : this.squareGrid) {
			for(Square tempSquare: squareArr) {
				if(tempSquare.getShip()==ship) {
					tempSquare.removeShip();
				}
			}
		}
	}//close removeShipFromGrid

	public void clearGrid() {
		for(Square[] tempSqaureArr : this.squareGrid) {
			tempSqaureArr = null;
		}
	}

	public void setShiptoSqaure(int x, int y, Ship tempShip) {
		this.squareGrid[x][y].setContainShip(true);
		this.squareGrid[x][y].setShip(tempShip);
	}

	public void setSquares() {
		for(int counter = 0; counter< this.getGridSize(); counter++) {
			for(int inner = 0; inner < this.getGridSize(); inner++) {
				this.squareGrid[counter][inner] = new Square();
			}
		}

	}//close setSqaure

	public int getGridMin() {
		return gridMin;
	}

	public void setGridMin(int gridMin) {
		this.gridMin = gridMin;
	}

	public Square[][] getSquareGrid() {
		return squareGrid;
	}

	public void setSquareGrid(Square[][] squareGrid) {
		this.squareGrid = squareGrid;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public int getGridSize() {
		return this.gridSize;
	}

	public void printDetails() {
		String output;  
		output = this.toString();
		JOptionPane.showMessageDialog(null, output, "Details of Person", JOptionPane.INFORMATION_MESSAGE);
	}


}//close class
