package no.ntnu.imt3281.ludo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class FriendChatController {

    @FXML private Text friendChatFriendName;
    @FXML private TextArea friendChatChatArea;
    @FXML private TextField friendChatTextToSay;
    @FXML private Button friendChatSend;
    
    
    @FXML
    void sendFriendChatMessage(ActionEvent event) {

    }

}
