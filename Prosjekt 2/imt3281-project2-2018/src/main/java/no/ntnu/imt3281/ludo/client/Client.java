package no.ntnu.imt3281.ludo.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.stage.FileChooser;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * This is the main class for the client. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Client extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
            Locale locale = Locale.getDefault();
            ResourceBundle bundle = ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n", locale);
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../gui/Ludo.fxml"), bundle);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {launch(args);}
}
