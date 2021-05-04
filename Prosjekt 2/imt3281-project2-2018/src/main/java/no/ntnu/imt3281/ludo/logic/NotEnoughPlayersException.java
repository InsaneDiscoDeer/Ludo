package no.ntnu.imt3281.ludo.logic;

public class NotEnoughPlayersException extends RuntimeException {
	private int badNumber;

    public NotEnoughPlayersException(int noOfPlayers) {
        super("NotEnoughPlayersException");
        badNumber = noOfPlayers;
    }
    public NotEnoughPlayersException( )
    {
        super("No exception should be thrown");
    }
    public NotEnoughPlayersException(String message)
    {
        super(message);
    }
    public int getBadNumber( )
    {
        return badNumber;
    }
}
