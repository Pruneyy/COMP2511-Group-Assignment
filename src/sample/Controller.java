package sample;


import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Controller {

	private static final Color[] colors = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.PURPLE, Color.BROWN, Color.AQUAMARINE, Color.SALMON, Color.GRAY};

	@FXML private BorderPane game;
	@FXML private Button start;
	@FXML private GridPane gameBoard;
	@FXML private Pane pane;
	private boolean startFlag = false;

	private double mouseClickX;
	private double mouseClickY;

	@FXML protected void handleButtonPress(ActionEvent event) {
		System.out.println("Start Game");
		if (startFlag == false) {

			//GeneratorPuzzleService gps = new GeneratorPuzzleService();
			//Grid g = gps.getNewPuzzle();
			
			FilePuzzleService gps = new FilePuzzleService("Easy");
			Grid g = gps.getNewPuzzle("3.txt");
			
			System.out.println(g);
			makeGrid(g);
			startFlag = true;
			start.setText("New Game");
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

	private Rectangle getGridTile(int id) {
		Rectangle rec = new Rectangle();
		rec.setWidth(50);
		rec.setHeight(50);
		return rec;
	}

	private Rectangle getVehicleRender(Vehicle v) {
		int unitLength = 50;
		int length;
		int width;
		BiConsumer<MouseEvent, Rectangle> onMouseDrag;
		BiConsumer<MouseEvent, Rectangle> onMousePress;
		BiConsumer<MouseEvent, Rectangle> onMouseRelease;
		
		Vehicle.Orientation orient = v.getOrientation();
		if (orient == Vehicle.Orientation.HORIZONTAL) {
			onMouseDrag = (e,r) -> {
				double deltaX = e.getSceneX() - mouseClickX;
				r.setTranslateX(r.getTranslateX()+deltaX);
				mouseClickX = e.getSceneX();
			};
			onMousePress = (e,r) -> {
				mouseClickX = e.getSceneX();
			};
			
			onMouseRelease = (e,r) -> {
			};
			
			length = v.getLength() * unitLength + 2*(v.getLength()-1);
			width = 1 * unitLength;
		} else {
			onMouseDrag = (e,r) -> {
				double deltaY = e.getSceneY() - mouseClickY;
				r.setTranslateY(r.getTranslateY()+deltaY);
				mouseClickY = e.getSceneY();
			};
			onMousePress = (e,r) -> {
				mouseClickY = e.getSceneY();
			};

			onMouseRelease = (e,r) -> {
			};
			length = 1 * unitLength ;
			width = v.getLength() * unitLength + 2*(v.getLength()-1);
		}
		Rectangle rec = new Rectangle(length, width);
		
		List<Coordinate> coords = v.getOccupiedSpaces();
		int col = coords.get(0).getColIndex();
		int row = coords.get(0).getRowIndex();
		int colCoord = col * unitLength + 2*(col);
		int rowCoord = row * unitLength + 2*(row);
		rec.relocate(colCoord,rowCoord);
		
		/*rec.setOnDragDetected(e -> {
			rec.setFill(Color.YELLOW);
		});*/
		rec.setOnMousePressed(e -> {
			onMousePress.accept(e, rec);
		});
		rec.setOnMouseDragged(e -> {
			onMouseDrag.accept(e, rec);
		});
		rec.setOnMouseReleased(e -> {
			onMouseRelease.accept(e, rec);
			/*double deltaX = e.getSceneX();
			double deltaY = e.getSceneY();
			System.out.println("X: " + deltaX + " Y: " + deltaY + " ==== RecX: " + rec.getTranslateX() + " RecY: " + rec.getTranslateY());*/
		});
		return rec;
	}


	public void makeGrid(Grid grid) {
		int [][] gridObj = grid.getGridObject();
		for (int x = 0; x < grid.GRID_SIZE; x++) {
			for (int y = 0; y < grid.GRID_SIZE; y++) {
				int gridVal = gridObj[y][x];
				Rectangle rec = getGridTile(gridVal);
				rec.setFill(Color.LIGHTGRAY);
				gameBoard.add(rec, y, x);
			}
		}
		
		for (Vehicle v : grid.getVehicles()) {
			Rectangle render = getVehicleRender(v);
			pane.getChildren().add(render);
		}
	}
}
