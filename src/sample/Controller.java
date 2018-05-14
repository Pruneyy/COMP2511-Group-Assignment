package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller implements Initializable {

	private static final Color[] colors = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.PURPLE, Color.BROWN, Color.AQUAMARINE, Color.SALMON, Color.GRAY};

	@FXML private BorderPane game;
	@FXML private Button start;
	@FXML private Button quit;
	@FXML private Button undo;
	@FXML private GridPane gameBoard;
	@FXML private Pane pane;
	@FXML private AnchorPane root;
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
		grid.getMoves().clear();
		makeGrid(grid);
	}

	private void reset() {
		this.gameBoard.getChildren().clear();
		this.pane.getChildren().removeAll(this.vehicleRenders);
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
			pane.getChildren().add(render);
		}
	}
	
	private void loadSplashScreen() {
		try {
			Main.isSplashLoaded = true;
			StackPane sPane = FXMLLoader.load(getClass().getResource(("SplashScreen.fxml")));
			game.getChildren().setAll(sPane);	
			
			FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), sPane);
			fadeIn.setFromValue(1);
			fadeIn.setToValue(0);
			fadeIn.setCycleCount(1);
			
			FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), sPane);
			fadeOut.setFromValue(0);
			fadeOut.setToValue(1);
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
