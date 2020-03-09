import java.io.Serializable;

import javax.swing.JOptionPane;

public class Score implements Comparable<Score>, Serializable{

	private String name;
	private int scoreNum;

	public Score (String name, int score) {
		this.addName(name);
		this.setScore(score);
	}

	//getter setters

	@Override
	public String toString() {
		return "Score [name=" + name + ", scoreNum=" + scoreNum + "]";
	}

	public void printDetails() {
		String output;  
		output = this.toString();
		JOptionPane.showMessageDialog(null, output, "Details of Person", JOptionPane.INFORMATION_MESSAGE);
	}

	public void addName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setScore(int scoreNum) {
		this.scoreNum = scoreNum;
	}

	public int getScore() {
		return this.scoreNum;
	}

	public int compareTo(Score score) {
		return this.scoreNum < score.scoreNum ? 1 : this.scoreNum > score.scoreNum ? -1 : 0;
	}


}
