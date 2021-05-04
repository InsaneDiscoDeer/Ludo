package no.ntnu.imt3281.ludo.gui;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Sample Skeleton for 'GameBoard.fxml' Controller Class
 */

import javafx.fxml.FXML; 
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameBoardController {

    @FXML private Label player1Name;
    @FXML private ImageView player1Active;
    @FXML private Label player2Name;
    @FXML private ImageView player2Active;
    @FXML private Label player3Name;
    @FXML private ImageView player3Active;
    @FXML private Label player4Name;
    @FXML private ImageView player4Active;
    @FXML private ImageView diceThrown;
    @FXML private Button throwTheDice;
    @FXML private TextArea chatArea;
    @FXML private TextField textToSay;
    @FXML private Button sendTextButton;
    private ArrayList<Image> images;
    
    public GameBoardController()
    {
    	images = new ArrayList<>();
    	for(int i = 1; i <= 6; i++)
    	{
    		Image img = new Image(getClass().getResource("../../../../../images/dice"+i+".png").toExternalForm());
    		images.add(img);
    	}
    }
    
    @FXML
    /**
     * Animates the dice
     */
    public int animateDice()
    {    	
    	long t0 = System.currentTimeMillis();
    	Timer t = new Timer();
    	Random rnd = new Random();
    	int answer = rnd.nextInt(6);
    	
    	t.schedule(new TimerTask() {
    	    @Override
    	    public void run() {
    	    	if (System.currentTimeMillis() - t0 > 3000)
    	    		cancel();
    	    	Random temp = new Random();
    	        int tempAns = temp.nextInt(6);
    			diceThrown.setImage(images.get(tempAns));
    	    }
    	}, 0, 100);
    	diceThrown.setImage(images.get(answer));
    	System.out.println(answer);
    	return answer;
    }

}