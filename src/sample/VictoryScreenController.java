package sample;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main.Theme;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class VictoryScreenController implements Initializable {
		
		private static Double FADE_TIME = 0.2;		// the fade timer

		@FXML private AnchorPane rootPane;
		@FXML private Text title;
		@FXML private Button start;
		@FXML private Button settings;
		@FXML private Button quit;
		@FXML private ImageView background;
		@FXML private Label moves;

	/**
	 * This will initialise all the animations on the Victory Screen.
	 * @param url Location of FXML document.
	 * @param rb Location of resource bundle.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
			
			if (Main.currentTheme == Theme.CARS) {
				setViewModel("Images/SplashBackground.jpg");
			} else if (Main.currentTheme == Theme.PLANE) {
				setViewModel("Images/VictoryPlaneSplash.jpg");
			} else if (Main.currentTheme == Theme.ANIMALS) {
				setViewModel("Images/AnimalVictory.jpg");
			}
			FadeTransition fadeIn = Controller.fadeSet(title, FADE_TIME + 1, 0, 1, 1);
			FadeTransition fadeLabel = Controller.fadeSet(moves, FADE_TIME, 0, 1, 1);
			FadeTransition fadeButtonOne = Controller.fadeSet(start, FADE_TIME, 0, 1, 1);
			FadeTransition fadeButtonTwo = Controller.fadeSet(settings, FADE_TIME, 0, 1, 1);
			FadeTransition fadeButtonThree = Controller.fadeSet(quit, FADE_TIME, 0, 1, 1);

			fadeIn.play();

			fadeIn.setOnFinished((e)->{
				fadeLabel.play();
			});

			fadeLabel.setOnFinished((e)->{
				fadeButtonOne.play();
			});

			fadeButtonOne.setOnFinished((e)->{
				fadeButtonTwo.play();
			});

			fadeButtonTwo.setOnFinished((e)->{
				fadeButtonThree.play();
			});
		}
		public void printMoves(int num) {
			moves.setText("Moves taken: "+num);
		}
	/**
	 * Handles the event of a user pressing the next puzzle (restart) button, by going to the next puzzle.
	 */	@FXML protected void handleRestartGamePress() {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
				Stage primaryStage = (Stage) rootPane.getScene().getWindow();
				primaryStage.setScene(new Scene(root));
			} catch (Exception e) {
				System.out.println("feels bad man");
			}
		}

	/**
	 * Handles the event of a user pressing the menu button, by returning them to the menu.
	 */	@FXML protected void handleMenuPress() {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
				Stage primaryStage = (Stage) rootPane.getScene().getWindow();
				primaryStage.setScene(new Scene(root));
			} catch (Exception e) {
				System.out.println("feels bad man");
			}
		}

	/**
	 * Handles the event of a user pressing the quit button, by quitting the game and ending the code.
	 */	@FXML protected void handleQuitGamePress() {
			System.exit(0);
		}

	/**
	 * Obtains the controller of the input View
	 * @param fxmlPath The FXML needed for the controller.
	 * @return FXML loader configuration for the view.
	 */
	public FXMLLoader loadView(String fxmlPath) {
			return Controller.genericViewLoader(fxmlPath, rootPane);
		}

	/**
	 * Sets the background image.
	 * @param backgroundSrc The source of the background image.
	 */
	public void setViewModel(String backgroundSrc) {
			background.setImage(new Image(backgroundSrc));
		}
}