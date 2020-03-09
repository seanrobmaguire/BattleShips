
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePlay implements Serializable{

	private Grid board = new Grid();
	private int turns;
	private int score;
	private String inputXYString;
	private int inputX;
	private int inputY;
	private int playDebug;
	private String sunkShipNames="";
	private ArrayList<Ship> shipArray = new ArrayList<Ship>();	
	private  ArrayList<String> playedCoordsArray = new ArrayList<String>();




	public static void main(String[] args) {

		int playAgain;
		int loadSaveGameResult;
		String output;


		GamePlay game = new GamePlay();
		HighScore highScoreBoard = new HighScore();


		HighScore tableRestored = null;
		GamePlay gameRestored = null;

		try {
			FileInputStream fileIn = new FileInputStream("Game.ser");
			ObjectInputStream gameIn = new ObjectInputStream(fileIn);

			tableRestored = (HighScore) gameIn.readObject();
			gameRestored = (GamePlay) gameIn.readObject();
			highScoreBoard = tableRestored;
			gameIn.close();
			fileIn.close();


		}catch (Exception ex) {

			ex.printStackTrace();
		}
		if(gameRestored != null) {
			output = "Load saved game?";
			loadSaveGameResult = JOptionPane.showConfirmDialog(null, output, "Battleships", JOptionPane.YES_NO_OPTION);
			if (loadSaveGameResult == 0) { //1 means no
				game=gameRestored;
			}else {
				game.startGame();
			}
		}else {
			game.startGame();
		}

		do {
			output = "";
			if ((game.playGame() == -1)) {

				game.saveGame(game, highScoreBoard);
				break;
			}else {
				if(game.getScore() == 0) {
					output = "You sunk no ships!";
				}else {

					output = "<html><table><tr><td>You sunk :</td></tr>"+game.sunkShipNames+ "<tr><td>You scored " +game.score+" points</td></tr></table></html>";
				}

				JOptionPane.showMessageDialog(null, output , "End Game", JOptionPane.INFORMATION_MESSAGE);

				highScoreBoard.newScoreEntry(game.getScore());

				GamePlay gameSave = game;
				gameSave= null;
				game.saveGame(gameSave, highScoreBoard);
			}

			playAgain = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Battleships", JOptionPane.YES_NO_OPTION);
			if(playAgain == 0) {
				game.startGame();
			}
		}while(playAgain == 0);

		JOptionPane.showMessageDialog(null, "Bye.", "Bye", JOptionPane.INFORMATION_MESSAGE);			
	}//close main



	public void startGame() {

		//Reset variables
		this.turns = 10;
		this.score = 0;
		this.board.clearGrid();
		this.board.setSquares();
		this.removeAllPlayed();
		this.shipArray.clear();
		String output;

		Ship ship0 = new Ship("Aircraft Carrier", 5, 2);
		Ship ship1 = new Ship("Battleship", 4, 4);
		Ship ship2 = new Ship("Submarine", 3, 6);
		Ship ship3 = new Ship("Destroyer", 2, 8);
		Ship ship4 = new Ship("Patrol Boat", 1, 10);

		this.shipArray.add(ship0);
		this.shipArray.add(ship1);
		this.shipArray.add(ship2);
		this.shipArray.add(ship3);
		this.shipArray.add(ship4);

		setShipsToSquare();

		output = "Ahoy Captain! \nFind the "+this.shipArray.size()+" ships and sink them. \nYou have "+this.turns+" turns. \nGood luck! \n\n\nDo you want to play in debug mode?";
		this.playDebug = JOptionPane.showConfirmDialog(null, output, "Battleships", JOptionPane.YES_NO_OPTION);	


	}//close startGame

	public int playGame() {
		int resultOfButtonClicked = 0;
		int doYouQuit;
		boolean enterWhileAgain= true;
		int playGameOutput = 0;
		String output = "";
		Ship matchedShip;
		String sunkShip ="";
		
		this.sunkShipNames ="";

		while( ((this.turns > 0) && (this.shipArray.size() > 0)) && (enterWhileAgain) )
		{
			resultOfButtonClicked = input(sunkShip);
			switch (resultOfButtonClicked) {
			case 0: //Ok clicked
				playGameOutput = 0;
				if(this.playedCoordsArray.contains(this.inputXYString)) {
					output = "You've played these coordinates already!";
					JOptionPane.showMessageDialog(null, output , "PLAYED ALREADY!", JOptionPane.INFORMATION_MESSAGE);
					break;
				}else {
					this.addPlayedAlready(this.inputXYString);
					if(!this.board.checkSqaureGrid((this.inputX), (this.inputY))) {
						JOptionPane.showMessageDialog(null, "<html><b>Miss!</b></html>", "MISS", JOptionPane.INFORMATION_MESSAGE);
					}else {
						matchedShip = this.board.returnShipFromSquare(this.inputX, this.inputY);

						output = "<html><table ><tr><td colspan=2><b>HIT!</b></td></tr>"
								+ "<tr><td colspan=2 >You sunk my "+matchedShip.getName()+"</td></tr>"
								+ "<tr><td colspan=2 >Ship coordinates "+board.getShipLocations(matchedShip)+"</td></tr>"
								+ "<tr><td>You scored "+matchedShip.getPoints()+" points!</td><td>Total score: "+(this.score + matchedShip.getPoints())+"</td></tr>"
								+ "</table></html>";

						JOptionPane.showMessageDialog(null, output, "HIT", JOptionPane.INFORMATION_MESSAGE);
						this.score = this.score + matchedShip.getPoints();

						sunkShip += "<tr><td>"+matchedShip.getName()+"</td><td>"+board.getShipLocations(matchedShip)+"</td></tr>";
						this.sunkShipNames += "<tr><td>"+matchedShip.getName()+"</td></tr>";
						this.board.removeShipFromGrid(matchedShip);
						this.removeShipFromShipArray(matchedShip);

					}//close if else
					this.turns--;
				}
				break;
			case 2://cancel clicked on input
				playGameOutput = -1;
				enterWhileAgain = false;
				doYouQuit = JOptionPane.showConfirmDialog(null, "Do you want to quit?", "Battleships", JOptionPane.YES_NO_OPTION);	

				if(doYouQuit == 1)  {
					enterWhileAgain =true;
					break;
				}else {
					enterWhileAgain = false;
					break;
				}
			case -1: //clicked x
				playGameOutput=-1;
				enterWhileAgain = false;
				doYouQuit = JOptionPane.showConfirmDialog(null, "Do you want to quit?", "Battleships", JOptionPane.YES_NO_OPTION);	
				//quit OK == 0
				//No == 1
				//x == -1

				if(doYouQuit == 1)  {
					enterWhileAgain =true;
					break;
				}else {
					enterWhileAgain = false;
					break;
				}
			}
		}//close While
		return playGameOutput;
	}//close playGame

	public int input(String sunkShip) {


		String inputXString;
		String inputYString;
		String output;
		int inputButtonClicked;
		String debugMessage="";

		if(this.playDebug == 0) {
			debugMessage = "<tr><td>Ships can be found at: </td></tr>"+this.getLocationsOfShip();
		}else {
			debugMessage = "<tr><td>Ships remaining: </td></tr>";
			for(Ship tempShip : this.shipArray) {
				debugMessage = debugMessage+ "<tr><td>"+tempShip.getName()+"</td></tr>";
			}
		}

		output = "<html><table><tr><td>Turns remaining: "+this.turns+".</td><td>"
				+ "Ships remaining: "+this.shipArray.size()+".</td></tr>"
				+ "<tr><td colspan=2 >Coordinates tried: "+this.getPlayedAlreadyString()+"</td></tr>"
				+"<tr><td>Ships sunk: </td></tr>"
				+ sunkShip
				+ debugMessage
				+"<tr><td></td><td><b>Score: "+this.score + "</b></td></tr>"
				+ "<tr><td>Enter 2 coordnates between 0 and 9</td></tr></table></html>";

		do{
			inputXString = null;
			inputYString = null;
			this.inputXYString = null;

			JTextField xField = new JTextField(2);
			JTextField yField = new JTextField(2);

			JPanel myPanel = new JPanel();
			JPanel inputPanel = new JPanel();
			JPanel addPanel = new JPanel();

			addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

			myPanel.add(new JLabel(output));

			inputPanel.add(new JLabel("Enter an X coordinate:"));
			inputPanel.add(xField);
			inputPanel.add(Box.createHorizontalStrut(15)); // a spacer
			inputPanel.add(new JLabel("Enter a Y coordinate:"));
			inputPanel.add(yField);

			addPanel.add(myPanel);
			addPanel.add(inputPanel);

			inputButtonClicked = JOptionPane.showConfirmDialog(null, addPanel,
					"Enter", JOptionPane.OK_CANCEL_OPTION);


			if (inputButtonClicked == JOptionPane.OK_OPTION) {
				inputXString = xField.getText();
				inputYString = yField.getText();

				try {
					this.inputX = Integer.parseInt(inputXString);
					this.inputY = Integer.parseInt(inputYString);
					this.inputXYString = this.inputX+","+this.inputY;
					System.out.print("parsed "+this.inputX +""+this.inputY);
				} catch (Exception ex) {

				}//close catch

				if( (!(this.inputX >= this.board.getGridMin() && this.inputX <= this.board.getMax())  || !(this.inputY >= this.board.getGridMin() && this.inputY <= this.board.getMax())  || this.inputXYString == null)){
					JOptionPane.showMessageDialog(null, "<html><b>Enter coordinates between 0 and 9!</b></html>", "ERROR", JOptionPane.INFORMATION_MESSAGE);
				}
			}else {
				break;
			}
		} while  (!(this.inputX >= this.board.getGridMin() && this.inputX <= this.board.getMax())  || !(this.inputY >= this.board.getGridMin() && this.inputY <= this.board.getMax())  || inputXYString == null);
		return inputButtonClicked;
	}//close method

	public String getLocationsOfShip() {
		String output="";
		for(Ship tempShip : this.shipArray) {
			output = output+ "<tr><td>"+tempShip.getName()+"</td><td>"+board.getShipLocations(tempShip)+"</td></tr>";
		}
		return output;
	}


	//SHIP ARRAY METHODS
	public void addShip(Ship ship) {
		this.shipArray.add(ship);
	}

	public void removeShipFromShipArray(Ship ship) {
		this.shipArray.remove(ship); 
	}



	//PLAYED ALREADY ARRAY METHODS
	public void addPlayedAlready(String inputXYString) {
		this.playedCoordsArray.add(inputXYString);
	}

	public String getPlayedAlreadyString() {
		String playedAlreadyString="";
		for(String played : playedCoordsArray) {
			playedAlreadyString=playedAlreadyString+"("+played+"),";
		}
		return playedAlreadyString;
	}

	public void removeAllPlayed() {
		playedCoordsArray.clear();
	}


	//Start game methods

	public void setShipsToSquare() {
		for(Ship tempShip : this.shipArray) {
			int[][] candidateArray;
			do {	
				candidateArray = this.generateCandidateCoords(tempShip.getSize());
			}while(!this.areSquareCoordsAvailable(candidateArray));
			this.setSquarePosition(candidateArray, tempShip);				
		}
	}//close setShipToSqaure

	public int[][] generateCandidateCoords(int size) {	
		int horOrVer;
		int coordX;
		int coordY;
		int candidateArray1[][]=new int[size][2];

		Random numGenerator = new Random();
		horOrVer = numGenerator.nextInt(2);

		if(horOrVer == 1) {
			coordX = numGenerator.nextInt(size);
			coordY = numGenerator.nextInt(board.getGridSize());
			for(int outer = 0; outer < size; outer++) {
				candidateArray1[outer][0]= coordX=coordX+1;
				candidateArray1[outer][1]=coordY;
			}
		}else {
			coordY = numGenerator.nextInt(size);
			coordX = numGenerator.nextInt(board.getGridSize());
			for(int outer = 0; outer < size; outer++) {
				candidateArray1[outer][0]= coordX;
				candidateArray1[outer][1]=coordY=coordY+1;
			}//close for
		}
		for(int outerLoop = 0; outerLoop < candidateArray1.length; outerLoop++) {
			for(int innerLoop = 0; innerLoop < 2; innerLoop++) {
			}
		}
		return candidateArray1;	
	}//close generate candidate coords

	public boolean areSquareCoordsAvailable(int[][] candidateArray) {
		boolean coordsAvailable =true; 
		for(int outer = 0; outer < candidateArray.length; outer++) {
			int X = candidateArray[outer][0];
			int Y = candidateArray[outer][1];	
			if(board.checkSqaureGrid(X, Y)) {
				coordsAvailable =false;
				break;
			}else {
				coordsAvailable =true;
			}
		}
		return coordsAvailable;
	}//close areSquareCoordsAvailable

	public void setSquarePosition(int[][] candidateArray, Ship tempShip) {
		for(int outer = 0; outer < candidateArray.length; outer++) {
			int x = candidateArray[outer][0];
			int y = candidateArray[outer][1];
			board.setShiptoSqaure(x, y, tempShip);			
		}
	}//close setSquarePosition


	//END GAME METHODS
	public void saveGame(GamePlay gameSave, HighScore highScoreBoard) {

		try {
			FileOutputStream fileOut = new FileOutputStream("Game.ser");
			ObjectOutputStream gameOut = new ObjectOutputStream(fileOut);

			gameOut.writeObject(highScoreBoard);
			gameOut.writeObject(gameSave);

			gameOut.close();
			fileOut.close();

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public int getScore() {
		return this.score;
	}



	public Grid getBoard() {
		return board;
	}



	public void setBoard(Grid board) {
		this.board = board;
	}



	public int getTurns() {
		return turns;
	}



	public void setTurns(int turns) {
		this.turns = turns;
	}



	public String getInputXYString() {
		return inputXYString;
	}



	public void setInputXYString(String inputXYString) {
		this.inputXYString = inputXYString;
	}



	public int getInputX() {
		return inputX;
	}



	public void setInputX(int inputX) {
		this.inputX = inputX;
	}



	public int getInputY() {
		return inputY;
	}



	public void setInputY(int inputY) {
		this.inputY = inputY;
	}



	public int getPlayDebug() {
		return playDebug;
	}



	public void setPlayDebug(int playDebug) {
		this.playDebug = playDebug;
	}



	public String getSunkShipNames() {
		return sunkShipNames;
	}



	public void setSunkShipNames(String sunkShipNames) {
		this.sunkShipNames = sunkShipNames;
	}



	public ArrayList<Ship> getShipArray() {
		return shipArray;
	}



	public void setShipArray(ArrayList<Ship> shipArray) {
		this.shipArray = shipArray;
	}



	public ArrayList<String> getPlayedCoordsArray() {
		return playedCoordsArray;
	}



	public void setPlayedCoordsArray(ArrayList<String> playedCoordsArray) {
		this.playedCoordsArray = playedCoordsArray;
	}



	public void setScore(int score) {
		this.score = score;
	}



	@Override
	public String toString() {
		return "GamePlay [board=" + board + ", turns=" + turns + ", score=" + score + ", inputXYString=" + inputXYString
				+ ", inputX=" + inputX + ", inputY=" + inputY + ", playDebug=" + playDebug + ", sunkShipNames="
				+ sunkShipNames + ", shipArray=" + shipArray + ", playedCoordsArray=" + playedCoordsArray + "]";
	}

	public void printDetails() {
		String output;  
		output = this.toString();
		JOptionPane.showMessageDialog(null, output, "Details of Person", JOptionPane.INFORMATION_MESSAGE);
	}





}//close class
