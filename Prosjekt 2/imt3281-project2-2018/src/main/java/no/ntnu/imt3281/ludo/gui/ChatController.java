package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;


public class ChatController {

    @FXML private ListView<?> joinChatroomList;
    @FXML private Button joinChatButton;
    @FXML private ListView<?> joinedChatroomList;
    @FXML private Button enterChatButton;
    @FXML private TextArea chatroomChatArea;
    @FXML private TextField chatroomTextToSay;
    @FXML private Button chatroomSendTextButton;
    @FXML private Text chatroomName;

    @FXML
    public void enterChatFromChatList(ActionEvent event) {
        System.out.println("asd");

    }

    @FXML
    public void joinChatFromList(ActionEvent event) {
        System.out.println("asd2");
    }

    @FXML
    public void sendChatroomMessage(ActionEvent event) {
        System.out.println("asd3");

    }
}
