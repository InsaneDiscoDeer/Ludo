package no.ntnu.imt3281.ludo.client;

public class User
{
	private String username;
	private String password;
	private String imagePath;
	private int gamesWon;
	private int gamesPlayed;
	
	public User(String username, String password, String imagePath)
	{
		this.username = username;
		this.password = password;
		this.imagePath = imagePath;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getImagePath()
	{
		return imagePath;
	}
	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}
	public int getGamesWon()
	{
		return gamesWon;
	}
	public void setGamesWon(int gamesWon)
	{
		this.gamesWon = gamesWon;
	}
	public int getGamesPlayed()
	{
		return gamesPlayed;
	}
	public void setGamesPlayed(int gamesPlayed)
	{
		this.gamesPlayed = gamesPlayed;
	}
	public String getUsername()
	{
		return username;
	}
	public void increaseGamesWon()
	{
		gamesWon++;
	}
	public void increaseGamesPlayed()
	{
		gamesPlayed++;
	}
	
}
