import java.io.Serializable;

import javax.swing.JOptionPane;

public class Square implements Serializable{
	private boolean containShip = false;
	private Ship theShip;

	public void setShip(Ship ship) {
		this.theShip = ship;
	}

	public Ship getShip() {
		return this.theShip;
	}

	public void setContainShip(boolean contain) {
		this.containShip = contain;
	}

	public void removeShip() {
		this.setContainShip(false);
		this.theShip = null;
	}

	public boolean getContainShip() {
		return this.containShip;
	}

	@Override
	public String toString() {
		return "Square [containShip=" + containShip + ", theShip=" + theShip + "]";
	}

	public void printDetails() {
		String output;  
		output = this.toString();
		JOptionPane.showMessageDialog(null, output, "Details of Person", JOptionPane.INFORMATION_MESSAGE);
	}



}//close class
