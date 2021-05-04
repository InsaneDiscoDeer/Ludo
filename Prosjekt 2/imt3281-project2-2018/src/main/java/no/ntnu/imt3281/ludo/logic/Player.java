package no.ntnu.imt3281.ludo.logic;

public class Player {
	
	private String userName;
	private String state;
	private int color;
	
	public Player(String userName, String state) {
		this.userName = userName;
		this.state = state;
	}
	
	public void changeState(String newState) {
		if(newState.length() > 0) this.state = newState;
		else System.out.println("Invalid state");
	}
	
	public void changeName(String newName) {
		this.userName = newName;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public String getState() {
		return this.state;
	}

}
