package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

	public class VictoryScreenController implements Initializable {
		
		@FXML private StackPane rootPane;
		@FXML private Text title;
		
		@Override
		public void initialize (URL url, ResourceBundle rb) {

			FadeTransition fadeIn = Controller.fadeSet(title, 1.5, 0, 1, 1);
			FadeTransition fadeOut = Controller.fadeSet(title, 1.5, 1, 0, 1);

			fadeIn.play();

			fadeIn.setOnFinished((e)->{
				fadeOut.play();
			});

			fadeOut.setOnFinished((e) -> {
				loadView("Game.fxml");
			});
		}

		public FXMLLoader loadView(String fxmlPath) {
			return Controller.genericViewLoader(fxmlPath, rootPane);
		}

		public void setViewModel() {

		}
	}