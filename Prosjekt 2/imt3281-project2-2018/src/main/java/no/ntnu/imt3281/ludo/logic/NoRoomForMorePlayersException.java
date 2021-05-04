package no.ntnu.imt3281.ludo.logic;

public class NoRoomForMorePlayersException extends RuntimeException {
	private int badNumber;
	
	public NoRoomForMorePlayersException(int noOfPlayers) {
		super("NoRoomForMorePlayersException");
		badNumber = noOfPlayers;
	}
	
	public NoRoomForMorePlayersException() {
		super("NoRoomForMorePlayersException");
	}
	
	public NoRoomForMorePlayersException(String msg, boolean enableSuppression) {
		super(msg);
	}
}
