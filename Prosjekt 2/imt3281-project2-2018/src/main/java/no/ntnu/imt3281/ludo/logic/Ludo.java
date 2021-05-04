package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingConstants;
import no.ntnu.imt3281.ludo.logic.NoRoomForMorePlayersException;
import no.ntnu.imt3281.ludo.logic.NotEnoughPlayersException;
import org.apache.derby.tools.sysinfo;

/**
 * Size of board image = 724
 * Middle of board = 362
 * Each square is ~48.2px, but need to pad this to fit better (eg. 45px)
 * 
 */

/**
 * The Ludo game class. Holds information about the currently ongoing game relative to the players.
 * @author stignity
 * @version 0.0.1
 */
public class Ludo {
	
	/* Game over if this is true */
	private boolean gameOver;
	/* Current game status (Created, Running...) */
	private String gameStatus;
	/* Maximum amount of players allowed to play */
	private int maxPlayerSize = 4;
	/* Current number of players */
	private int noOfPlayers = 0;	
	/* List of players playing this game */
	private ArrayList<Player> players = new ArrayList<>();
	/* Current active player */
	private int currentActivePlayer;
	/* Assigning the color values */
	public static int RED = 0, BLUE = 1, YELLOW = 2, GREEN = 3;
	/* Player pieces */
	private int[][] pieces;
	/* User grid to player grid conversion function */
	private int[][] userGridToPlayerGrid;
	/* Need to have the number of throws static */
	private static int numOfThrows = 0;
	
	/**
	 * Dummy function to get the first part of the test to work. Need to change this later
	 */
	public Ludo() { createGame(); }
	
	/**
	 * This function takes all the player names and adds them to the list of players current for the game
	 * Minimum number of players should be 2
	 * @param players: Array of players to be added to List
	 */
	public Ludo(String...args) throws NotEnoughPlayersException {
		
		int totalPlayers = args.length;
		int noOfBadPlayers = 0;
		
		/* Not enough players all around, skip the rest of the checks */
		if(totalPlayers < 2) {
			throw new NotEnoughPlayersException("Not enough players. 2 is minimum.");
		}
		/* Enough players, but some might be null, count them and continue */
		else {
			for(int i = 0; i < totalPlayers; i++) {
				if(args[i] == null) noOfBadPlayers++;
			}
		}
		/* As there is no certain rule about handling null players, at least
		 * check if there are at least 2 VALID players (not null) that has registered.
		 * As I figured, as long as there are at least 2 valid players, there can exist other null players?
		*/
		if((totalPlayers - noOfBadPlayers) < 2) {
			throw new NotEnoughPlayersException("Not enough players. 2 is minimum.");
		}
		/* Checks have passed, create game with players */
		else {
			createGame();
			for(int i = 0; i < args.length; i++) {
				addPlayer(args[i]);
			}
		}
	}
	
	/**
	 * add a player to a game
	 * @param name: creates a player with given name. Player only belongs to this game.
	 * @throws NoRoomForMorePlayersException if number of players is 4
	 */
	public void addPlayer(String name) throws NoRoomForMorePlayersException {
		if(noOfPlayers != maxPlayerSize) {
			if(name != null) {
				Player player = new Player(name, "active");			
				this.players.add(player);
				assignPlayerColor(player, noOfPlayers);
				noOfPlayers++;
				/* Players added, initiate game */
				this.gameStatus = "Initiated";
				currentActivePlayer = RED;
			}			
		}
		else {
			throw new NoRoomForMorePlayersException("No room for more players", true);
		}
	}
	
	/**
	 * Assign color to a player
	 * Player1 = RED(0), Player2 = BLUE(1), Player3 = YELLOW(2), Player4 = GREEN(3)
	 * @param player: Which player to assign the color
	 * @param color: what color should the player be assigned
	 */
	public void assignPlayerColor(Player player, int color) {
		player.setColor(color);
	}
	
	/**
	 * Create a new game after players have been added
	 */
	public void createGame() {
		/* Each player has 4 pieces each */
		pieces = new int[4][4];
		/* Setting up the relative grid */
		userGridToPlayerGrid = new int[91][4];
		for(int i = 0; i < 16; i++) {
			userGridToPlayerGrid[i][0] = (int)i/4;
		}
		/* loop though players and set initial values to 0 */
		for(int player = 0; player < 4; player++) {
			/* Game has total of 91 squares, but the first 16 are reserved to player starting positions */
			for(int square = 16; square < 91; square++) {
				userGridToPlayerGrid[square][player] = 0;
			}			
		}
		gameStatus = "Created";
		gameOver = false;
	}
	
	/**
	 * If a player exits the game, update the status. Do not remove from game.
	 * @param name: which player to set inactive
	 */
	public String removePlayer(String name) {
		for(Player player : this.players) {
			if(player.getUserName() == name) {
				player.changeState("Inactive");
				player.changeName(player.getState() + ": " + player.getUserName());
				return player.getUserName();
			}
		}
		return null;
	}
	
	/**
	 * Returns the number of currently active members in the game
	 * @return: number of active players
	 */
	public int activePlayers() {
		int active = 0;
		Iterator<Player> players = this.players.iterator();
		while(players.hasNext()) {
			if(players.next().getState() == "active") active++;
		}
		return active;
	}
	
	/**
	 * Shows whose turn it is
	 * @return: The current active player
	 */
	public int activePlayer() {
		return currentActivePlayer;
	}
	
	/**
	 * Try to get the player name of the given color
	 * 0 = RED, 1 = BLUE, 2 = YELLOW, 3 = GREEN
	 * @param color: integer, the color of the player
	 * @return: Username or null if player is not found
	 */
	public Object getPlayerName(int color) {
		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i).getColor() == color) return this.players.get(i).getUserName();
		}
		return null;
	}
	
	public void deactivate(String player) {
		/* TODO: deactivate player */
	}
	/**
	 * Translate user position to board position
	 * @param player the player of which the location should be returned
	 * @param position board position of player position (translatable)
	 * @return absolute board position (16 - 91) relative to user position (0 - 59)
	 */
	public int userGridToLudoBoardGrid(int player, int relativePiecePosition) {
		int absolutePiecePosition = 0;
		/* position is start position, return current players start position */
		if(relativePiecePosition == 0) return 4 * player;
		/**
		 * Each player is his/hers player number * 13 away from the other players starting positions
		 * E.G: Red 1 = Green 14, Yellow = 27, Blue = 40. 0 - 15 is reserved for starting positions
		 */
		if(relativePiecePosition < 54) {
			absolutePiecePosition =  15 + 13*player + relativePiecePosition;
			if(absolutePiecePosition > 67) {
				/* TODO player is in safe zone, do some mathematics*/
				absolutePiecePosition -= 52;
			}
		}
		/* 68 through 92 is home stretch positions */
		else {
			absolutePiecePosition = 68 + 6*(player);
		}
		/* TODO: actually write this shit (a lot of checks basically....) */
		return absolutePiecePosition;
	}
	/**
	 * Return number of players in the game (even inactive players)
	 * @return total number of players
	 */
	public int nrOfPlayers() {
		return this.noOfPlayers;
	}
	
	public int noOfActivePlayers() {
		int activePlayers = 0;
		for(Player player : this.players) {
			if(!player.getUserName().contains("Inactive")) activePlayers++;
		}
		return activePlayers;
	}
	
	/**
	 * Returns passed piece location
	 * @param piece RED, BLUE, YELLOW, GREEN
	 * @return passed piece location of player on board
	 */
	public int getPosition(int player, int p) {
		return pieces[player][p];
	}
	
	/**
	 * User clicks throw dice button and returns generated value
	 * @return value generated by <CLASS>
	 */
	public int throwDice() {
		Ludo.numOfThrows = 0;
		while(numOfThrows < 2) {
			numOfThrows++;
		}
		return 6;
	}
	
	public int throwDice(int face) {
		if(currentActivePlayer > 3) currentActivePlayer = 0;
		if(face == 6) {
			numOfThrows = 0;
		}
		if(++numOfThrows == 3) {
			currentActivePlayer++;
		}
		return currentActivePlayer;
	}
	
	/**
	 * Move piece to new location and update new position globally
	 * @param player RED, BLUE, YELLOW, GREEN
	 * @param fromLocation previous location
	 * @param toLocation new location
	 * @return true if the location is valid (user is allowed to move there) and the piece has been moved
	 */
	public boolean movePiece(int player, int fromLocation, int toLocation) {
		/* TODO: stack pieces, kill pieces */
		/* Check if the piece was successfully moved to new location */
		boolean moved = false;
		int piece;
		
		/* Previous and next planned location of the players piece */
		int from = userGridToLudoBoardGrid(player, fromLocation);
		System.out.println(from);
		int to   = userGridToLudoBoardGrid(player, toLocation);
		System.out.println(to);
		
		if(moved) checkForWinner();
		
		return true;
	}
	/**
	 * A player can chose to stack pieces on top of eachother
	 */
	public void stackPieces() {
		
	}
	
	/**
	 * If a moving player choses to move a piece to a location where there already
	 * exists a piece from another player, he/she can chose to move on top of it
	 * and killing it in the process (putting the players piece back to start location).
	 */
	public void killPieces() {
		
	}
	
	/**
	 * Returns the currently active player
	 * @return current active player
	 */
	public int activePLayer() {
		return 0;
	}
	
	/**
	 * Returns game status
	 * @return game status
	 */
	public String getStatus() {
		return this.gameStatus;
	}
	
	/**
	 * Function to check if player has won during his/hers last move
	 */
	public void checkForWinner() {
		
	}

}
