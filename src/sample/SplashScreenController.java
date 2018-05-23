package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class SplashScreenController implements Initializable {
		
		@FXML private AnchorPane rootPane;
		@FXML private Text title;
		@FXML private Button start;
		
		@Override
		public void initialize (URL url, ResourceBundle rb) {
			
		}

		@FXML protected void handleStartGamePress(ActionEvent event) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
				Stage primaryStage = (Stage) rootPane.getScene().getWindow();
				primaryStage.setScene(new Scene(root));
			} catch (Exception e) {
				System.out.println("feels bad man");
			}
		}
	}