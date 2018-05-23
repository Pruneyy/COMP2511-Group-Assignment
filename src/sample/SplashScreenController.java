package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class SplashScreenController implements Initializable {

	private static Double FADE_TIME = 0.5;		// the fade timer

	@FXML private AnchorPane rootPane;
	@FXML private Text title;
	@FXML private Button start;
	@FXML private Button settings;
	@FXML private Button quit;
	@FXML private ImageView background;

	@Override
	public void initialize (URL url, ResourceBundle rb) {

		FadeTransition fadeIn = Controller.fadeSet(title, FADE_TIME + 1, 0, 1, 1);
		FadeTransition fadeButtonOne = Controller.fadeSet(start, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonTwo = Controller.fadeSet(settings, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonThree = Controller.fadeSet(quit, FADE_TIME, 0, 1, 1);

		fadeIn.play();

		fadeIn.setOnFinished((e)->{
			fadeButtonOne.play();
		});

		fadeButtonOne.setOnFinished((e)->{
			fadeButtonTwo.play();
		});

		fadeButtonTwo.setOnFinished((e)->{
			fadeButtonThree.play();
		});
	}

	@FXML protected void handleStartGamePress(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
			Stage primaryStage = (Stage) rootPane.getScene().getWindow();
			primaryStage.setScene(new Scene(root));
		} catch (Exception e) {
			System.out.println("feels bad man");
		}
	}

	public FXMLLoader loadView(String fxmlPath) {
		return Controller.genericViewLoader(fxmlPath, rootPane);
	}

//	public class ViewModel {
//		String backgroundSrc;
//
//		public ViewModel(String backgroundSrc) {
//			this.backgroundSrc = backgroundSrc;
//		}
//	}

	public void setViewModel(String backgroundSrc) {
		background.setImage(new Image(backgroundSrc));
	}

}