package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class Controller implements Initializable {

	private static Integer RED_CAR_ID = 1;		// the ID of the red car
	private static Integer RED_HEAD_COLUMN = 5; // the column location the head of the red car must be to finish the game
	private static Integer RED_TAIL_COLUMN = 4; // the column location the tail of the red car must be to finish the game
	private static final Color[] colors = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.PURPLE, Color.BROWN, Color.AQUAMARINE, Color.SALMON, Color.GRAY};

	@FXML private BorderPane game;
	@FXML private Button start;
	@FXML private Button quit;
	@FXML private Button undo;
	@FXML private Button generate;
	@FXML private GridPane gameBoard;
	@FXML private Pane pane;
	@FXML private Pane pane2;
	@FXML private AnchorPane root;
	@FXML private Text title;
	private boolean startFlag = false;

	private Grid grid;
	private ArrayList<Rectangle> vehicleRenders;

	private double mouseClickX;
	private double mouseClickY;
	private int unitLength = 50;

	@FXML public void initialize(URL url, ResourceBundle resourceBundle) {
		if(!Main.isSplashLoaded) {
			loadSplashScreen();
		}
		if (startFlag == false) {
			this.vehicleRenders = new ArrayList<Rectangle>();
			this.initPuzzle(new GeneratorPuzzleService());
//			FilePuzzleService gps = new FilePuzzleService("Easy");
//			grid = gps.getNewPuzzle("3.txt");

			System.out.println(grid);
			startFlag = true;
		}
	}

	@FXML protected void handleNewGamePress(ActionEvent event) {
		// Make new game
	}

	@FXML protected void handleGenerateGamePress(ActionEvent event) {
		// Make new game
		this.initPuzzle(new GeneratorPuzzleService());

	}

	private void initPuzzle(PuzzleService puzzleService) {
		this.reset();
		grid = puzzleService.getNewPuzzle(PuzzleService.Difficulty.EASY);
		makeGrid(grid);
	}

	private void reset() {
		this.gameBoard.getChildren().clear();
		this.pane2.getChildren().removeAll(this.vehicleRenders);
		this.vehicleRenders = new ArrayList<Rectangle>();
	}

	@FXML protected void handleUndoPress(ActionEvent event) {
		System.out.println(grid.getMoves());
		Vehicle v = grid.undoLastVehicleMoves();
		if (v != null) {
			snapRectangleToGrid(v, vehicleRenders.get(v.getCarId() - 1));
		}
		System.out.println(grid.getMoves());
		// undo
	}

	@FXML protected void handleQuitPress(ActionEvent event) {
		// Exit game
		System.exit(0);
	}

	private Rectangle getGridTile() {
		Rectangle rec = new Rectangle();
		rec.setWidth(unitLength);
		rec.setHeight(unitLength);
		return rec;
	}

	private Rectangle createVehicleRender(Vehicle v) {
		BiConsumer<MouseEvent, Rectangle> onMouseDrag;
		BiConsumer<MouseEvent, Rectangle> onMousePress;
		BiConsumer<MouseEvent, Rectangle> onMouseRelease;
		
		Vehicle.Orientation orient = v.getOrientation();
		onMouseDrag = getOnMouseDrag(v);
		onMousePress = getOnMousePress(v);
		onMouseRelease = getOnMouseRelease(v);
		Rectangle rec = getRectangle(v);
		rec.setFill(colors[v.getCarId()]);

		snapRectangleToGrid(v, rec);

//		rec.setOnDragDetected(e -> {
//		});
		rec.setOnMousePressed(e -> {
			onMousePress.accept(e, rec);
		});
		rec.setOnMouseDragged(e -> {
			onMouseDrag.accept(e, rec);
		});
		rec.setOnMouseReleased(e -> {
			onMouseRelease.accept(e, rec);
//			double deltaX = e.getSceneX();
//			double deltaY = e.getSceneY();
//			System.out.println("X: " + deltaX + " Y: " + deltaY + " ==== RecX: " + rec.getTranslateX() + " RecY: " + rec.getTranslateY());
		});
		return rec;
	}

	private void snapRectangleToGrid(Vehicle v, Rectangle r) {
		List<Coordinate> coords = v.getOccupiedSpaces();
		int col = coords.get(0).getColIndex();
		int row = coords.get(0).getRowIndex();
		r.relocate(getGridPixelCoord(col), getGridPixelCoord(row));

		// Will check if the red car is in the winning spaces when it is moved
		if (v.getCarId() == RED_CAR_ID) {
			checkForWin(grid);
		}
	}

	private void checkForWin(Grid grid) {
		Vehicle redCar = grid.getVehicleById(RED_CAR_ID);
		Coordinate redCarHead = redCar.getOccupiedSpaces().get(1);
		Coordinate redCarTail = redCar.getOccupiedSpaces().get(0);

		// ===== DEBUGGING =====
//		System.out.println("HEAD: " + redCarHead);
//		System.out.println("TAIL: " + redCarTail);
		// ===== END DEBUG =====

		if (redCarHead.getColIndex() == RED_HEAD_COLUMN && redCarTail.getColIndex() == RED_TAIL_COLUMN) {
			System.out.println("CONGRATULATIONS YOU WON!!!");
			//ADD CODE FOR VICTORY SCREEN HERE
		}

	}

	private int getGridPixelCoord(int col) {
		return col * unitLength + 2*(col);
	}

	private double getGridCoord(double pixelCoord) {
		return pixelCoord / (unitLength + 2.0);
	}

	/**
	 * Computes the next grid position of the rectangle, given it's current position and mouse movement
	 * @param translationInPixels The file position of a rectangle, in pixel coordinates
	 * @param delta The change in movement of the mouse
	 * @return The file position of a rectangle, in grid coordinates
	 */
	private double getLayoutInGridCoords(double translationInPixels, double delta) {
		if (delta < 0) {
			return Math.floor(getGridCoord(translationInPixels - 1));
		}
		return Math.ceil(getGridCoord(translationInPixels + 1));
	}

	private Rectangle getRectangle(Vehicle v) {
		int length;
		int width;
		Vehicle.Orientation orient = v.getOrientation();
		if (orient == Vehicle.Orientation.HORIZONTAL) {
			length = v.getLength() * unitLength + 2*(v.getLength()-1);
			width = 1 * unitLength;
		} else {
			length = 1 * unitLength ;
			width = v.getLength() * unitLength + 2*(v.getLength()-1);
		}
		return new Rectangle(length, width);
	}

	private BiConsumer<MouseEvent, Rectangle> getOnMouseRelease(Vehicle v) {
		BiConsumer<MouseEvent, Rectangle> onMouseRelease;
		if (v.getOrientation() == Vehicle.Orientation.HORIZONTAL) {
			onMouseRelease = (e,r) -> {
				snapRectangleToGrid(v, r);
			};
		} else {
			onMouseRelease = (e,r) -> {
				snapRectangleToGrid(v, r);
			};
		}

		// This will only enter when the red car was moved as the red car
		// has to be moved to final position last

		return onMouseRelease;
	}

	private BiConsumer<MouseEvent, Rectangle> getOnMousePress(Vehicle v) {
		BiConsumer<MouseEvent, Rectangle> onMousePress;
		if (v.getOrientation() == Vehicle.Orientation.HORIZONTAL) {
			onMousePress = (e,r) -> {
				mouseClickX = e.getSceneX();
			};
		} else {
			onMousePress = (e,r) -> {
				mouseClickY = e.getSceneY();
			};
		}
		return onMousePress;
	}

	private BiConsumer<MouseEvent, Rectangle> getOnMouseDrag(Vehicle v) {
		BiConsumer<MouseEvent, Rectangle> onMouseDrag;
		if (v.getOrientation() == Vehicle.Orientation.HORIZONTAL) {
			onMouseDrag = (e,r) -> {
				double deltaX = e.getSceneX() - mouseClickX;
				mouseClickX = e.getSceneX();
				if (!grid.moveVehicleToIndex(v.getCarId(), (int) getLayoutInGridCoords(r.getLayoutX(), deltaX))) {
					snapRectangleToGrid(v, r);
					return;
				}
				r.setLayoutX(r.getLayoutX() + deltaX);
			};
		} else {
			onMouseDrag = (e,r) -> {
				double deltaY = e.getSceneY() - mouseClickY;
				mouseClickY = e.getSceneY();
				if (!grid.moveVehicleToIndex(v.getCarId(), (int) getLayoutInGridCoords(r.getLayoutY(), deltaY))) {
					snapRectangleToGrid(v, r);
					return;
				}
				r.setLayoutY(r.getLayoutY() + deltaY);
			};
		}
		return onMouseDrag;
	}

	public void makeGrid(Grid grid) {
		for (int x = 0; x < grid.GRID_SIZE; x++) {
			for (int y = 0; y < grid.GRID_SIZE; y++) {
				Rectangle rec = getGridTile();
				rec.setFill(Color.LIGHTGRAY);
				gameBoard.add(rec, y, x);
			}
		}
		
		for (Vehicle v : grid.getVehicles()) {
			Rectangle render = createVehicleRender(v);
			this.vehicleRenders.add(render);
			pane2.getChildren().add(render);
		}
	}
	
	private void loadSplashScreen() {
		try {
			Main.isSplashLoaded = true;
			StackPane sPane = FXMLLoader.load(getClass().getResource(("SplashScreen.fxml")));
			game.getChildren().setAll(sPane);	
			
			FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), sPane.lookup(".tie"));
			fadeIn.setFromValue(0);
			fadeIn.setToValue(1);
			fadeIn.setCycleCount(1);
			
			FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), sPane.lookup(".tie"));
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0);
			fadeOut.setCycleCount(1);
			
			fadeIn.play();
			
			fadeIn.setOnFinished((e)->{
				fadeOut.play();
			});
			
			fadeOut.setOnFinished((e) -> {
				try {
					AnchorPane parentContent = FXMLLoader.load(getClass().getResource(("sample.fxml")));
					game.getChildren().setAll(parentContent);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
