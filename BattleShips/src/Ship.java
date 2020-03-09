import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class Ship implements Serializable{
	private String name;
	private int size; 
	private int points; 

	//constructor
	public Ship(String name, int size, int points) {
		setName(name);
		setSize(size);
		setPoints(points);

	}//close constructor

	public String toString(){
		String output;		
		output = "Shipname: "+this.name;
		output = output + ".\nSize: "+this.size;
		output = output + ".\nPoints: "+this.points;

		return output;
	}//close toString

	public void setName(String name) {
		this.name=name;
	}

	public String getName() {
		return this.name;
	}

	public void setSize(int size) {
		this.size=size;
	}

	public int getSize() {
		return this.size;
	}

	public void setPoints(int points) {
		this.points=points;
	}

	public int getPoints() {
		return this.points;
	}

	public void printDetails() {
		String output;  
		output = this.toString();
		JOptionPane.showMessageDialog(null, output, "Details of Person", JOptionPane.INFORMATION_MESSAGE);
	}


}//close class