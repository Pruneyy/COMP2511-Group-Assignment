package sample;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main.Theme;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class handles the JavaFX splash screens which are animated during UI interactions.
 */
public class SplashScreenController implements Initializable {

	private static Double FADE_TIME = 0.5;		// the fade timer
	private static Integer NAVBAR_THEME_HIDE = -150;		// x value to hide the theme navigation bar
	private static Integer NAVBAR_THEME_SHOW = 0;			// x value to show the theme navigation bar
	private static Integer NAVBAR_DIFF_HIDE = 670;			// x value to hide the difficulty navigation bar
	private static Integer NAVBAR_DIFF_SHOW = 370;			// x value to show the difficulty navigation bar

	@FXML private AnchorPane navBarTheme;
	@FXML private AnchorPane navBarDiff;
	@FXML private AnchorPane rootPane;
	@FXML private Text title;
	@FXML private Text instructions;
	@FXML private Button start;
	@FXML private Button difficulties;
	@FXML private Button themes;
	@FXML private Button quit;
	@FXML private ImageView background;
	@FXML private ToggleGroup theme;
	@FXML private ToggleGroup difficulty;

	/**
	 * This will initialise all the
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {

		FadeTransition fadeIn = Controller.fadeSet(title, FADE_TIME + 1, 0, 1, 1);
		FadeTransition fadeButtonOne = Controller.fadeSet(start, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonTwo = Controller.fadeSet(difficulties, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonThree = Controller.fadeSet(themes, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonFour = Controller.fadeSet(quit, FADE_TIME, 0, 1, 1);
		FadeTransition fadeIns = Controller.fadeSet(instructions, FADE_TIME, 0, 1, 1);

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

		fadeButtonThree.setOnFinished((e)->{
			fadeButtonFour.play();
		});

		fadeButtonFour.setOnFinished((e)->{
			fadeIns.play();
		});

		slideMenu();
	}

	@FXML protected void handleStartGamePress(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
			Stage primaryStage = (Stage) rootPane.getScene().getWindow();
			primaryStage.setScene(new Scene(root));
		} catch (Exception e) {
			System.out.println("feels bad mang");
		}
	}

	@FXML protected void handleDifficultiesPress(ActionEvent event) {
		System.out.println("Cool story bro");
	}

	@FXML protected void handleQuitGamePress(ActionEvent event) {
		System.exit(0);
	}

	@FXML protected void handleCarSelect(ActionEvent event) {
		Main.currentTheme = Theme.CARS;
		setViewModel("Images/SplashBackground.jpg");
	}
	
	@FXML protected void handlePlaneSelect(ActionEvent event) {
		Main.currentTheme = Theme.PLANE;
		setViewModel("Images/PlaneSplash.jpg");
	}

	@FXML protected void handleAnimalSelect(ActionEvent event) {
		Main.currentTheme = Theme.ANIMALS;
		setViewModel("Images/AnimalSplash.jpg");
	}

	@FXML protected void handleEasySelect(ActionEvent event) {
		Controller.currentDifficulty = PuzzleService.Difficulty.EASY;
	}

	@FXML protected void handleMediumSelect(ActionEvent event) {
		Controller.currentDifficulty = PuzzleService.Difficulty.MEDIUM;
	}

	@FXML protected void handleHardSelect(ActionEvent event) {
		Controller.currentDifficulty = PuzzleService.Difficulty.HARD;
	}

	public FXMLLoader loadView(String fxmlPath) {
		return Controller.genericViewLoader(fxmlPath, rootPane);
	}
	
	public void setViewModel(String backgroundSrc) {
		background.setImage(new Image(backgroundSrc));
	}
	
    private void slideMenu() {
        TranslateTransition openNavTheme=new TranslateTransition(new Duration(350), navBarTheme);
		TranslateTransition openNavDiff=new TranslateTransition(new Duration(350), navBarDiff);
		openNavTheme.setToX(NAVBAR_THEME_SHOW);
		openNavDiff.setToX(NAVBAR_DIFF_SHOW);

		TranslateTransition closeNavTheme=new TranslateTransition(new Duration(350), navBarTheme);
		TranslateTransition closeNavDiff=new TranslateTransition(new Duration(350), navBarDiff);

		themes.setOnAction((ActionEvent evt)->{
            if(navBarTheme.getTranslateX()!=NAVBAR_THEME_SHOW){
                openNavTheme.play();
				closeMenu(closeNavDiff, NAVBAR_DIFF_HIDE);
            }else{
            	closeMenu(closeNavTheme, NAVBAR_THEME_HIDE);
            }
        });

		difficulties.setOnAction((ActionEvent evt)->{
			if(navBarDiff.getTranslateX()!=NAVBAR_DIFF_SHOW){
				openNavDiff.play();
				closeMenu(closeNavTheme, NAVBAR_THEME_HIDE);
			}else{
				closeMenu(closeNavDiff, NAVBAR_DIFF_HIDE);
			}
		});
    }

	private void closeMenu(TranslateTransition closeNav, Integer xValue) {
		closeNav.setToX(xValue);
		closeNav.play();
	}

}