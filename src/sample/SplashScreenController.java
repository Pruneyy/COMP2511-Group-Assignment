package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main.Theme;


public class SplashScreenController implements Initializable {

	private static Double FADE_TIME = 0.5;		// the fade timer
	
	@FXML private AnchorPane navBar;
	@FXML private AnchorPane rootPane;
	@FXML private Text title;
	@FXML private Button start;
	@FXML private Button themes;
	@FXML private Button quit;
	@FXML private ImageView background;
	@FXML private ToggleGroup theme;

	@Override
	public void initialize (URL url, ResourceBundle rb) {

		FadeTransition fadeIn = Controller.fadeSet(title, FADE_TIME + 1, 0, 1, 1);
		FadeTransition fadeButtonOne = Controller.fadeSet(start, FADE_TIME, 0, 1, 1);
		FadeTransition fadeButtonTwo = Controller.fadeSet(themes, FADE_TIME, 0, 1, 1);
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
		
		slideMenu();
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
	
	@FXML protected void handleQuitGamePress(ActionEvent event) {
		System.exit(0);
	}

	@FXML protected void handleCarSelect(ActionEvent event) {
		Main.currentTheme = Theme.CARS;
		setViewModel("Images/SplashBackground.jpg");
	}
	
	@FXML protected void handleAnimalSelect(ActionEvent event) {
		Main.currentTheme = Theme.ANIMALS;
		setViewModel("Images/AnimalSplash.jpg");
	}

	@FXML protected void handleFoodSelect(ActionEvent event) {
		Main.currentTheme = Theme.FOOD;
		setViewModel("Images/FoodSplash.jpg");
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
	
    private void slideMenu() {
        TranslateTransition openNav=new TranslateTransition(new Duration(350), navBar);
        openNav.setToX(0);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), navBar);
        themes.setOnAction((ActionEvent evt)->{
            if(navBar.getTranslateX()!=0){
                openNav.play();
            }else{
                closeNav.setToX(-(navBar.getWidth()));
                closeNav.play();
            }
        });
    }

}