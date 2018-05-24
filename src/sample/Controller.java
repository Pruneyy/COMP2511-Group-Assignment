package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.Main.Theme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class Controller implements Initializable {

	public static int UNIT_LENGTH = 50;

	private static Integer RED_CAR_ID = 1;		// the ID of the red car
	private static Integer RED_HEAD_COLUMN = 5; // the column location the head of the red car must be to finish the game
	private static Integer RED_TAIL_COLUMN = 4; // the column location the tail of the red car must be to finish the game

	@FXML private BorderPane game;
	@FXML private GridPane gameBoard;
	@FXML private Pane pane;
	@FXML private Pane pane2;
	@FXML private AnchorPane root;
	private boolean startFlag = false;

	private Grid grid;
	private ArrayList<Rectangle> vehicleRenders;

	private double mouseClickX;
	private double mouseClickY;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		if(!Main.isSplashLoaded) {
			loadSplashScreen();
		}
		if (startFlag == false) {
			this.vehicleRenders = new ArrayList<Rectangle>();
			this.initPuzzle(new FilePuzzleService());
			startFlag = true;
		}
		if (Main.currentTheme == Theme.CARS) {
			root.setStyle("-fx-background-image: url('Images/MainGameBackground.jpg'); -fx-background-size: 20%;");
		} else if (Main.currentTheme == Theme.PLANE) {
			root.setStyle("-fx-background-image: url('Images/PlaneBackground.png');");
		} else if (Main.currentTheme == Theme.ANIMALS) {
			root.setStyle("-fx-background-image: url('Images/TreeBackground.jpg'); -fx-background-size: 20%;");
		}
		pane.setStyle("-fx-background-image: url('Images/GridBorderBackground.jpg');");
	}

	@FXML protected void handleNewGamePress(ActionEvent event) {
		// Make new game from the bank of pre-loaded puzzles
		this.initPuzzle(new FilePuzzleService());
	}

	@FXML protected void handleGenerateGamePress(ActionEvent event) {
		// Make new game using the generator algorithm
		this.initPuzzle(new GeneratorPuzzleService());

	}

	@FXML protected void handleStartMenuPress(ActionEvent event) {
		AnchorPane parentContent;
		try {
			parentContent = FXMLLoader.load(getClass().getResource(("Game.fxml")));
			game.getChildren().setAll(parentContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Vehicle v = grid.undoLastVehicleMoves();
		if (v != null) {
			snapRectangleToGrid(v, vehicleRenders.get(v.getCarId() - 1));
		}
	}
	@FXML protected void handleRestartPress(ActionEvent event) {
		// restart the game board
		while(!grid.getMoves().isEmpty()) {
		Vehicle v = grid.undoLastVehicleMoves();
		if (v != null) {
			snapRectangleToGrid(v, vehicleRenders.get(v.getCarId() - 1));
		}
		}
	}
	@FXML protected void handleQuitPress(ActionEvent event) {
		// Exit game
		System.exit(0);
	}

	private Rectangle getGridTile() {
		Rectangle rec = new Rectangle();
		rec.setWidth(UNIT_LENGTH);
		rec.setHeight(UNIT_LENGTH);
		return rec;
	}

	private Rectangle createVehicleRender(Vehicle v) {
		BiConsumer<MouseEvent, Rectangle> onMouseDrag;
		BiConsumer<MouseEvent, Rectangle> onMousePress;
		BiConsumer<MouseEvent, Rectangle> onMouseRelease;
		
		onMouseDrag = getOnMouseDrag(v);
		onMousePress = getOnMousePress(v);
		onMouseRelease = getOnMouseRelease(v);
		Rectangle rec = new VehicleView(v).getRec();

		snapRectangleToGrid(v, rec);

		rec.setOnMousePressed(e -> {
			onMousePress.accept(e, rec);
		});
		rec.setOnMouseDragged(e -> {
			onMouseDrag.accept(e, rec);
		});
		rec.setOnMouseReleased(e -> {
			onMouseRelease.accept(e, rec);
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

		if (redCarHead.getColIndex() == RED_HEAD_COLUMN && redCarTail.getColIndex() == RED_TAIL_COLUMN) {
			loadVictoryScreen();
		}

	}

	private int getGridPixelCoord(int col) {
		return col * UNIT_LENGTH + 2*(col);
	}

	private double getGridCoord(double pixelCoord) {
		return pixelCoord / (UNIT_LENGTH + 2.0);
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
		Image img = new Image("Images/MrCat.jpg");
		if (Main.currentTheme == Theme.CARS) {
			img = new Image("Images/Parking.png");
		} else if (Main.currentTheme == Theme.PLANE) {
			img = new Image("Images/AirportLandingStrip.png");
		} else if (Main.currentTheme == Theme.ANIMALS) {
			img = new Image("Images/MrCat.png");
		}
		for (int x = 0; x < grid.GRID_SIZE; x++) {
			for (int y = 0; y < grid.GRID_SIZE; y++) {
				Rectangle rec = getGridTile();
				if (x == 2 && (y == 4 || y == 5)) {
					rec.setStrokeType(StrokeType.INSIDE);
					rec.setStroke(Color.BLACK);
					rec.setFill(new ImagePattern(img));
				} else {
					rec.setFill(Color.LIGHTGRAY);
				}
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
		Main.isSplashLoaded = true;
		FXMLLoader loader = loadView("SplashScreen.fxml");
		SplashScreenController splashScreenController = loader.getController();
		splashScreenController.setViewModel("Images/SplashBackground.jpg"); // TODO(pranav) change this
	}
	
	private void loadVictoryScreen() {
		FXMLLoader loader = loadView("VictoryScreen.fxml");
		VictoryScreenController victoryScreenController = loader.getController();
	}
	
	public static FadeTransition fadeSet(Node nodeToFade, double time, int setFrom, int setTo, int setCycle) {
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(time), nodeToFade);
		fadeIn.setFromValue(setFrom);
		fadeIn.setToValue(setTo);
		fadeIn.setCycleCount(setCycle);
		return fadeIn;
	}

	public FXMLLoader loadView(String fxmlPath) {
		return Controller.genericViewLoader(fxmlPath, game);
	}

	public static FXMLLoader genericViewLoader(String fxmlPath, Pane rootPane) {
		try {
			FXMLLoader loader = new FXMLLoader(Controller.class.getResource(fxmlPath));
			AnchorPane sPane = loader.load();
			rootPane.getChildren().setAll(sPane);
			return loader;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
