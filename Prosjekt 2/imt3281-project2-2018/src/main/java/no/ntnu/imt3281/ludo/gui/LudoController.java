package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import no.ntnu.imt3281.ludo.client.Connection;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LudoController {

	@FXML private MenuItem random;
    @FXML private MenuItem connect;
	@FXML private TabPane tabbedPane;
	@FXML private PasswordField loginPassword;
	@FXML private TextField loginUsername;
	@FXML private TextField registerUsername;
	@FXML private Text loginError;
	@FXML private Text registerError;
	@FXML private PasswordField registerPassword;
	@FXML private PasswordField registerPassword2;
	@FXML private Button loginButton;
	@FXML private Button registerButton;
	@FXML private TextArea globalChatArea;
    @FXML private TextField globalTextToSay;
    @FXML private Button globalSendTextButton;
    @FXML private ListView<String> globalOnlineUsers;

    private Connection connection;
    private boolean connected = false;
    private String userName;
    private boolean exists=false;

    private final ObservableList<String> globalUserList = FXCollections.observableArrayList();
    ExecutorService executor = Executors.newFixedThreadPool(1);
    
    public LudoController()
    {
    	this.userName = "test";
    }
    
    @FXML void initialize()
    {
    	globalOnlineUsers.setItems(globalUserList);
    }
	
    @FXML public void registerUser()
	{
		//TODO:add some regex to check for hack3rs
		
		if(registerPassword.getText().equals(registerPassword2.getText()))
		{
			try {
				connection.send("user:"+registerUsername.getText());
				System.out.println("lelele2");
				//connection.send(registerPassword.getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("fail to register user"+e.getMessage());
			}
		}
	}
	
	@FXML public void loginUser()
	{
        System.out.println("userName = " + userName);
		
		try {
			connection.send("check:"+loginUsername.getText());
			if(exists)
			{
				loginError.setText("SUCCESSFUL");
				exists = false;
			}
			else
			{
				loginError.setText("NO BUENO :(");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("fail to login user"+e.getMessage());
		}        
	}
	
	@FXML
	public void joinRandomGame(ActionEvent e) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
		loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
		
		GameBoardController controller = loader.getController();
		// Use controller to set up communication for this game.
		// Note, a new game tab would be created due to some communication from the server
		// This is here purely to illustrate how a layout is loaded and added to a tab pane.

		try {
			AnchorPane gameBoard = loader.load();
			Tab tab = new Tab("Game");
			tab.setContent(gameBoard);
			tabbedPane.getTabs().add(tab);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    }

    @FXML
    public void listRooms(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        try {
            AnchorPane chat = loader.load();
            Tab tab = new Tab("Chat");
            tab.setContent(chat);
            tabbedPane.getTabs().add(tab);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @FXML
    public void profile(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserProfile.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        try {
            AnchorPane userProfile = loader.load();
            Tab tab = new Tab(loader.getResources().getString("userProfile.profile"));
            tab.setContent(userProfile);
            tabbedPane.getTabs().add(tab);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @FXML
    public void challengePlayers(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TrialByCombat.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        try {
            AnchorPane challenge = loader.load();
            Tab tab = new Tab(loader.getResources().getString("trial.challenge"));
            tab.setContent(challenge);
            tabbedPane.getTabs().add(tab);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @FXML
    public void privateChat(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendChat.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
        try {
            Stage stage = new Stage();
            stage.setTitle(loader.getResources().getString("friendChat.window"));
            Parent root = (Parent)loader.load();
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * If the client is already connected, disconnect the user
     * else establish a new connection with the server
     */
	@FXML
	public void connect() {
        try {
            if (connected) {
                connected = false;
                connect.setText("Connect");
                connection.close();
                globalUserList.clear();
            } else {
                connection = new Connection();
                connected = true;
                connection.send(userName);
                globalChatArea.setEditable(false);
                executor.execute(this::listen);
                connect.setText("Disconnect");
            }
        } catch (IOException e) {

        }
	}

	// Listen to messages from server
	private void listen() {
	    while (connected) {
	        try {
	            if (connection.input.ready()) {
	                String tmp = connection.input.readLine();
                    Platform.runLater(()->{
                        if (tmp.startsWith("JOIN:")) {
                            globalUserList.add(tmp.substring(5));
                        } else if (tmp.startsWith("DISCONNECTED:")){
                            globalUserList.remove(tmp.substring(13));
                        } else if (tmp.startsWith("yeees")){
                            exists = true;
                        }else {
                            //globalChatArea.setText("But can you see this??!?!?");
                            String who = tmp.substring(4, tmp.indexOf('>', 4));
                            String msg = tmp.substring(tmp.indexOf('>', 4) + 1);
                            globalChatArea.setText(globalChatArea.getText() + who +  " said: " + msg + "\n");
                        }
                    });
                }
                Thread.sleep(50);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Sends messages from the global chatting room to the server
     */
    @FXML public void sendGlobalMessage(ActionEvent event) {
        if (connected) {
            try {
                System.out.println(globalTextToSay.getText());
                connection.send(globalTextToSay.getText());
                globalTextToSay.setText("");
            } catch (IOException e) {
                System.out.println("Could not send text!");
                connection.close();
            }
        }
    }
}



