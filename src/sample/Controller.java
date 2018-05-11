package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	@FXML private Pane stackPane;
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
		int length = v.getLength() * unitLength + 2*(v.getLength()-1);
		int width = 1 * unitLength;
		Rectangle rec = new Rectangle(length, width);
		rec.relocate(0,0);
		rec.setOnDragDetected(e -> {
			rec.setFill(Color.YELLOW);
		});
		rec.setOnMousePressed(e -> {
			mouseClickX = e.getSceneX();
			mouseClickY = e.getSceneY();
		});
		rec.setOnMouseDragged(e -> {
			double deltaX = e.getSceneX() - mouseClickX;
			double deltaY = e.getSceneY() - mouseClickY;
			rec.setTranslateX(rec.getTranslateX()+deltaX);
			rec.setTranslateY(rec.getTranslateY()+deltaY);
			mouseClickX = e.getSceneX();
			mouseClickY = e.getSceneY();
		});
		rec.setOnMouseReleased(e -> {
			double deltaX = e.getSceneX();
			double deltaY = e.getSceneY();
			System.out.println("X: " + deltaX + " Y: " + deltaY + " ==== RecX: " + rec.getTranslateX() + " RecY: " + rec.getTranslateY());
			
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
		stackPane.relocate(0, 0);
		Circle circ = new Circle();
		circ.setRadius(20);
		circ.setFill(Color.GREEN);
		stackPane.getChildren().add(circ);
		Rectangle render = getVehicleRender(grid.getVehicleById(1));
		stackPane.getChildren().add(render);
		Rectangle render2 = getVehicleRender(grid.getVehicleById(2));
		stackPane.getChildren().add(render2);
	}
}
