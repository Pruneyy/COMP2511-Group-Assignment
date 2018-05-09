package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;

public class Controller {

	private static final Color[] colors = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.PURPLE, Color.BROWN, Color.AQUAMARINE, Color.SALMON, Color.BEIGE};

	@FXML private BorderPane game;
	@FXML private Button start;
	@FXML private GridPane gameBoard;
	private boolean startFlag = false;
	
	@FXML protected void handleButtonPress(ActionEvent event) {
		System.out.println("Start Game");
		if (startFlag == false) {

			GeneratorPuzzleService gps = new GeneratorPuzzleService();
			Grid g = gps.getNewPuzzle();
			System.out.println(g);
			makeGrid(g);
			startFlag = true;
			start.setText("New Game");
		} else {
			
		}
	}
		
	public void makeGrid() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				Rectangle rec = new Rectangle();
				if ((i%2 == 0 && j%2 == 0) || (i%2 != 0 && j%2 != 0)) rec.setFill(Color.AQUAMARINE);
				gameBoard.add(rec, i, j);
			}
		}
	}

	private Rectangle getGridTile() {
		Rectangle rec = new Rectangle();
		rec.setWidth(50);
		rec.setHeight(50);
		return rec;
	}

	private Rectangle getVehicleRender(Vehicle v) {
		int unitLength = 50;
		if (v.getOrientation() == Vehicle.Orientation.HORIZONTAL) {
			return new Rectangle(v.getLength() * unitLength, 1 * unitLength);
		}
		return new Rectangle(1 * unitLength, v.getLength() * unitLength);
	}


	public void makeGrid(Grid grid) {
		int [][] gridObj = grid.getGridObject();
		for (int x = 0; x < grid.GRID_SIZE; x++) {
			for (int y = 0; y < grid.GRID_SIZE; y++) {
				int gridVal = gridObj[y][x];
				Rectangle rec = getGridTile();
				gameBoard.add(rec, y, x);
				if (gridVal != 0) {
					rec.setFill(colors[gridVal]);
				}
			}
		}
	}
}
