import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class HighScore implements Serializable{
	private LinkedList <Score> highScoreBoard = new LinkedList <Score>();


	public void sort() {	
		Collections.sort(this.highScoreBoard);
	}

	public int getSize() {
		return this.highScoreBoard.size();
	}

	public Score getLast() {
		return this.highScoreBoard.get(9);
	}


	public void addHighScore(int compareScore) {
		String name;
		String output;

		output = "<html><table><tr><td></td><td><b>NEW HIGH SCORE!</b></td><td></td></tr>"
				+ this.toStringTable()
				+ "<tr><td colspan=2 ><b>Your score: </b></td><td><b>"+compareScore+"</b></td></tr>"
				+ "<tr><td colspan=3 >Please enter your name:</td></tr></table></html>";


		name = JOptionPane.showInputDialog(output);

		Score newScore = new Score(name, compareScore);

		this.addScoreTopOfList(newScore);
	}//addhighScore

	public void addScoreTopOfList(Score score) {
		this.highScoreBoard.addFirst(score);
	}

	public void newScoreEntry(int score){

		this.sort();
		if (score > 0) {
			if(this.getSize()==10) {
				if(score >= highScoreBoard.getLast().getScore() ) {
					this.addHighScore(score);
					this.sort();
					this.removeLast();
					this.viewHighScores();
				}else {
					JOptionPane.showMessageDialog(null, "Your final score is "+score, "Well Done!", JOptionPane.INFORMATION_MESSAGE);
				}
			}else{
				this.addHighScore(score);
				this.viewHighScores();
			}	
		}else {
			this.viewHighScores(score);
		}
	}//close newScoreEntry

	public String toStringTable() {
		this.sort();
		String output ="<tr><td></td><td><u>HIGH SCORE BOARD</u></td><td></td></tr>";
		int counter =1;
		for(Score tempScore : highScoreBoard) {
			output = output + "<tr><td>"+counter+"</td><td>"+tempScore.getName()+"</td>" + "<td>"+tempScore.getScore()+"</td></tr>";
			counter++;
		}
		return output;
	}//close to string

	public void removeLast() {
		this.highScoreBoard.removeLast();
	}

	public void viewHighScores() {
		this.sort();
		String output="<html><table>";
		output = output + this.toStringTable()+"</td></tr></table></html>";
		JOptionPane.showMessageDialog(null, output, "High Score Board", JOptionPane.INFORMATION_MESSAGE);
	}

	public void viewHighScores(int score) {
		this.sort();
		String output ="<html><table>";
		output = output + this.toStringTable();
		output = output + "<tr><td colspan=2 ><b>Your score:</b></td><td><b>"+score+"</b></td></tr></table></html>";

		JOptionPane.showMessageDialog(null, output, "High Score Board", JOptionPane.INFORMATION_MESSAGE);

	}

	public void printDetails() {
		String output;  
		output = this.toString();
		JOptionPane.showMessageDialog(null, output, "Details of Person", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public String toString() {
		return "HighScore [highScoreBoard=" + highScoreBoard + "]";
	}
}//close class
