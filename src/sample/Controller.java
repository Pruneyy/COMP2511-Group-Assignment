package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Controller {
	@FXML private GridPane game;
	@FXML private Button start;
	private boolean startFlag = false;
	
	@FXML protected void handleButtonPress(ActionEvent event) {
		System.out.println("Start Game");
		if (startFlag == false) {
			makeGrid();
			startFlag = true;
			start.setText("New Game");
		} else {
			
		}
	}
		
	public void makeGrid() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				game.add(new Text("Wow"), i, j);
			}
		}
	}
}
