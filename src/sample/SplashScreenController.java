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
import javafx.scene.control.RadioButton;
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
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class SplashScreenController implements Initializable {

	private static Double FADE_TIME = 0.5;					// the fade timer
	private static Integer NAVBAR_THEME_HIDE = -150;		// x value to hide the theme navigation bar
	private static Integer NAVBAR_THEME_SHOW = 0;			// x value to show the theme navigation bar
	private static Integer NAVBAR_DIFF_HIDE = 670;			// x value to hide the difficulty navigation bar
	private static Integer NAVBAR_DIFF_SHOW = 370;			// x value to show the difficulty navigation bar

	@FXML private AnchorPane navBarTheme;
	@FXML private AnchorPane navBarDiff;
	@FXML private AnchorPane rootPane;
	@FXML private Text title;
	@FXML private Text instructions;
	@FXML private Text instructionsTwo;
	@FXML private Button start;
	@FXML private Button difficulties;
	@FXML private Button themes;
	@FXML private Button quit;
	@FXML private ImageView background;
	@FXML private ToggleGroup theme;
	@FXML private ToggleGroup difficulty;
    @FXML private RadioButton carThemeRadioButton;
    @FXML private RadioButton planeThemeRadioButton;
    @FXML private RadioButton animalThemeRadioButton;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;

	/**
	 * This will initialise all the animations on the Splash Screen (main menu).
	 * @param url Location of FXML document.
	 * @param rb Location of resource bundle.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {

		FadeTransition fadeIn = Controller.fadeSet(title, FADE_TIME + 1, 0, 1, 1);
		FadeTransition fadeButtonOne = Controller.fadeSet(start, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonTwo = Controller.fadeSet(difficulties, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonThree = Controller.fadeSet(themes, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonFour = Controller.fadeSet(quit, FADE_TIME, 0, 1, 1);
		FadeTransition fadeIns = Controller.fadeSet(instructions, FADE_TIME, 0, 1, 1);
		FadeTransition fadeInsTwo = Controller.fadeSet(instructionsTwo, FADE_TIME, 0, 1, 1);

		fadeIn.play();

		fadeIn.setOnFinished((e)->{
			fadeIns.play();
		});
		
		fadeIns.setOnFinished((e)->{
			fadeInsTwo.play();
		});
		
		fadeInsTwo.setOnFinished((e)->{
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

		slideMenu();

        if (Main.currentTheme == Theme.CARS) {
            carThemeRadioButton.fire();
        } else if (Main.currentTheme == Theme.PLANE) {
            planeThemeRadioButton.fire();
        } else if (Main.currentTheme == Theme.ANIMALS) {
            animalThemeRadioButton.fire();
        }

        if (Controller.currentDifficulty == PuzzleService.Difficulty.EASY) {
            easyRadioButton.fire();
        } else if (Controller.currentDifficulty == PuzzleService.Difficulty.MEDIUM) {
            mediumRadioButton.fire();
        } else if (Controller.currentDifficulty == PuzzleService.Difficulty.HARD) {
            hardRadioButton.fire();
        }
    }

	/**
	 * Handles the event of the user selecting the start game button.
	 */
	@FXML protected void handleStartGamePress() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
			Stage primaryStage = (Stage) rootPane.getScene().getWindow();
			primaryStage.setScene(new Scene(root));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Handles the event of the user pressing the quit button.
	 */
	@FXML protected void handleQuitGamePress() {
		System.exit(0);
	}

	/**
	 * Handles the event of the user selecting the car theme.
	 */
	@FXML protected void handleCarSelect() {
		Main.currentTheme = Theme.CARS;
		setViewModel("Images/SplashBackground.jpg");
	}

	/**
	 * Handles the event of the user selecting the plane theme.
	 */
	@FXML protected void handlePlaneSelect() {
		Main.currentTheme = Theme.PLANE;
		setViewModel("Images/PlaneSplash.jpg");
	}

	/**
	 * Handles the event of the user selecting the animal theme.
	 */
	@FXML protected void handleAnimalSelect() {
		Main.currentTheme = Theme.ANIMALS;
		setViewModel("Images/AnimalSplash.jpg");
	}

	/**
	 * Handles the event of the user selecting the easy option.
	 */
	@FXML protected void handleEasySelect() {
		Controller.currentDifficulty = PuzzleService.Difficulty.EASY;
	}

	/**
	 * Handles the event of the user selecting the medium option.
	 */
	@FXML protected void handleMediumSelect() {
		Controller.currentDifficulty = PuzzleService.Difficulty.MEDIUM;
	}

	/**
	 * Handles the event of the user selecting the hard option.
	 */
	@FXML protected void handleHardSelect() {
		Controller.currentDifficulty = PuzzleService.Difficulty.HARD;
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

	/**
	 * Contains all of the transitions and movements for the theme and difficulty menus.
 	 */
	private void slideMenu() {
        TranslateTransition openNavTheme=new TranslateTransition(new Duration(350), navBarTheme);
		TranslateTransition openNavDiff=new TranslateTransition(new Duration(525), navBarDiff);
		openNavTheme.setToX(NAVBAR_THEME_SHOW);
		openNavDiff.setToX(NAVBAR_DIFF_SHOW);

		TranslateTransition closeNavTheme=new TranslateTransition(new Duration(350), navBarTheme);
		TranslateTransition closeNavDiff=new TranslateTransition(new Duration(700), navBarDiff);

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

	/**
	 * Used to close the difficulty or theme menus when the other one is opened.
	 * @param closeNav The navigation menu to be closed.
	 * @param xValue The X value of where it should slide to.
	 */
	private void closeMenu(TranslateTransition closeNav, Integer xValue) {
		closeNav.setToX(xValue);
		closeNav.play();
	}

}