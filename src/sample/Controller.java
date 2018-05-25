package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main.Theme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

/**
 * The Controller class looks after a handling a range of events from front end JavaFX events to
 * backend game control events including map generation, button event handling, vehicle tracking
 * and rendering.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class Controller implements Initializable {

	public static int UNIT_LENGTH = 50;			// used to set the height and width of the grid tiles

	private static Integer RED_CAR_ID = 1;		// the ID of the red car
	private static Integer RED_HEAD_COLUMN = 5; // the column location the head of the red car must be to finish the game
	private static Integer RED_TAIL_COLUMN = 4; // the column location the tail of the red car must be to finish the game

	// JavaFX elements to display to the user
	@FXML private BorderPane game;
	@FXML private GridPane gameBoard;
	@FXML private Pane pane;
	@FXML private Pane pane2;
	@FXML private AnchorPane root;
	@FXML private Label moveCounter;
	@FXML private Label move;

	private boolean startFlag = false;	// flag to start the game once all is initialised.
	private Grid grid;
	private ArrayList<Rectangle> vehicleRenders;
	public static PuzzleService.Difficulty currentDifficulty = PuzzleService.Difficulty.EASY;	// Sets the default difficulty (if user does not select difficulty)

	private double mouseClickX;
	private double mouseClickY;
	private int before;
	private int after;
	private int numMoves = 0;

	/**
	 * This will initialise the user interface and set initial instances before a map for te user to play
	 * is made. In particular, it will check if the opening menu splash screen is loaded, determine the type
	 * of map to be loaded based on the difficulty setting, and check that the correct theme is being displayed
	 * for the user.
	 * @param url Location of FXML document.
	 * @param resourceBundle Location of resource bundle.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		moveCounter.setText("MOVES: 0");
		if(!Main.isSplashLoaded) {
			loadSplashScreen();
		}
		if (startFlag == false) {
			this.vehicleRenders = new ArrayList<Rectangle>();

			// Determines whethere to randomly generate the puzzle or to pull from file
			if (currentDifficulty == PuzzleService.Difficulty.EASY) {
				System.out.println("Starting a new puzzle of EASY difficulty (from generator)");
				this.initPuzzle(new GeneratorPuzzleService());
			} else if (currentDifficulty == PuzzleService.Difficulty.MEDIUM) {
				System.out.println("Starting a new puzzle of MEDIUM difficulty (from file)");
				this.initPuzzle(new FilePuzzleService());
			} else if (currentDifficulty == PuzzleService.Difficulty.HARD) {
				System.out.println("Starting a new puzzle of HARD difficulty (from file)");
				this.initPuzzle(new FilePuzzleService());
			}

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

	/**
	 * Looks after the event of a user selecting the button that generates the next puzzle in the
	 * game screen itself. Depending on the difficulty set, the puzzle will be generated using a
	 * random generation algorithm (easy), or from pre-made files (medium and hard).
	 */
	@FXML protected void handleNewGamePress() {
		// System.out.println("CURRENT DIFF IS " + currentDifficulty);
		if (currentDifficulty == PuzzleService.Difficulty.EASY) {
			// System.out.println("Starting a new puzzle of EASY difficulty (from generator)");
			this.initPuzzle(new GeneratorPuzzleService());
		} else if (currentDifficulty == PuzzleService.Difficulty.MEDIUM) {
			// System.out.println("Starting a new puzzle of MEDIUM difficulty (from file)");
			this.initPuzzle(new FilePuzzleService());
		} else if (currentDifficulty == PuzzleService.Difficulty.HARD) {
			// System.out.println("Starting a new puzzle of HARD difficulty (from file)");
			this.initPuzzle(new FilePuzzleService());
		}
	}

	/**
	 * Looks after the event when a user presses the start game button on the main menu. The JavaFX
	 * used to design the main game area is then loaded from Game.fxml so the game screen is accessible.
	 */
	@FXML protected void handleStartMenuPress() {
		AnchorPane parentContent;
		try {
			parentContent = FXMLLoader.load(getClass().getResource(("Game.fxml")));
			game.getChildren().setAll(parentContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will initiate the puzzle by accessing the getNewPuzzle() method in the PuzzleService
	 * interface and generate the grid as well.
	 * @param puzzleService pused to access the interface to access the getNewPuzzle method.
	 */
	private void initPuzzle(PuzzleService puzzleService) {
		this.reset();
		grid = puzzleService.getNewPuzzle(currentDifficulty);
		// System.out.println("THE PUZZLE ASKED FOR IS " + currentDifficulty);
		makeGrid(grid);
	}

	/**
	 * Method is used to reset the gameboard by clearing all vehicles.
	 */
	private void reset() {
		this.gameBoard.getChildren().clear();
		this.pane2.getChildren().removeAll(this.vehicleRenders);
		this.vehicleRenders = new ArrayList<Rectangle>();
	}

	/**
	 * Method is used to handle the event of a user pressing the undo button in game. The undo event will
	 * take the last vehicle that was moved and return it to its position one move ago.
	 */
	@FXML protected void handleUndoPress() {
		Vehicle v = grid.undoLastVehicleMoves();
		if (v != null) {
			snapRectangleToGrid(v, vehicleRenders.get(v.getCarId() - 1));
			numMoves = numMoves - 1 ;
			handleMoveCounter(numMoves);
		}
	}

	/**
	 * Method will handle the event of a user pressing the restart button in game. The restart event will
	 * undo the moves of all vehicles until they have returned to their starting positions for that puzzle.
	 */
	@FXML protected void handleRestartPress() {
		// restart the game board
		while(!grid.getMoves().isEmpty()) {
			Vehicle v = grid.undoLastVehicleMoves();
			if (v != null) {
				snapRectangleToGrid(v, vehicleRenders.get(v.getCarId() - 1));
			}
		}
		numMoves = 0;
		handleMoveCounter(numMoves);
	}

	/**
	 * Method will handle the event of a user pressing the quit button. The quit event will end the game
	 * and close the program by exiting out of the code.
	 */
	@FXML protected void handleQuitPress() {
		// Exit game
		System.exit(0);
	}

	/**
	 * Method will handle the event of a user pressing the menu button in game. The menu event will take
	 * the user back to the main menu by loading SplashScreen.fxml.
	 */
	@FXML protected void handleMenuPress() {
		try {
			Parent rooot = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
			Stage primaryStage = (Stage) root.getScene().getWindow();
			primaryStage.setScene(new Scene(rooot));
		} catch (Exception e) {
			System.out.println("feels bad man");
		}
	}

	/**
	 * Method used to produce grid tiles for the layout of the grid in JavaFX.
	 * @return A rectangle object that will be used as a grid tile.
	 */
	private Rectangle getGridTile() {
		Rectangle rec = new Rectangle();
		rec.setWidth(UNIT_LENGTH);
		rec.setHeight(UNIT_LENGTH);
		return rec;
	}

	/**
	 * Creates the vehicle rectangle, and places it in the grid.
	 * @param v The vehicle that is passed in to be rendered.
	 * @return The grid tile that the vehicles will be rendered on.
	 */
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
			setMovesBefore();
			onMousePress.accept(e, rec);
		});
		rec.setOnMouseDragged(e -> {
			onMouseDrag.accept(e, rec);
		});
		rec.setOnMouseReleased(e -> {
			setMovesAfter();
			onMouseRelease.accept(e, rec);
		});
		return rec;
	}

	/**
	 * Given a vehicle and the rectangle render, snap it to the grid position.
	 * @param v The vehicle that is to be snapped into place.
	 * @param r The rectangle that it has been moved to.
	 */
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

	/**
	 * Method used to determine if the red car has been moved to the correct grid tiles to have solved the puzzle.
	 * @param grid The layout of the vehicles on a representation of the gameboard grid.
	 */
	private void checkForWin(Grid grid) {
		Vehicle redCar = grid.getVehicleById(RED_CAR_ID);
		Coordinate redCarHead = redCar.getOccupiedSpaces().get(1);
		Coordinate redCarTail = redCar.getOccupiedSpaces().get(0);

		if (redCarHead.getColIndex() == RED_HEAD_COLUMN && redCarTail.getColIndex() == RED_TAIL_COLUMN) {
			loadVictoryScreen();
		}

	}

	/**
	 * Gets the pixel location within the grid.
	 * @param e The row or column that is passed in.
	 * @return The grid pixel location of the row or column passed in
	 */
	private int getGridPixelCoord(int e) {
		return e * UNIT_LENGTH + 2*(e);
	}

	/**
	 * Gets the pixel (X, Y) coordinates relative to the grid.
	 * @param pixelCoord The pixel coordinates to be converted into grid coordinates.
	 * @return The position of a set of coordinates on the game grid.
	 */
	private double getGridCoord(double pixelCoord) {
		return pixelCoord / (UNIT_LENGTH + 2.0);
	}

	/**
	 * Computes the next grid position of the rectangle, given it's current position and mouse movement.
	 * @param translationInPixels The file position of a rectangle, in pixel coordinates.
	 * @param delta The change in movement of the mouse.
	 * @return The file position of a rectangle, in grid coordinates.
	 */
	private double getLayoutInGridCoords(double translationInPixels, double delta) {
		if (delta < 0) {
			return Math.floor(getGridCoord(translationInPixels - 1));
		}
		return Math.ceil(getGridCoord(translationInPixels + 1));
	}

	/**
	 * Handles the event when a user releases the mouse to unselect a vehicle.
	 * @param v The vehicle that is to be positioned.
	 * @return Function to be called on mouse release.
	 */
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

	/**
	 * Handles the event when a user presses the mouse to select a vehicle.
	 * @param v The vehicle that is selected by the user.
	 * @return Assign function to the onMouseRelease handler for a vehicle render (rectangle)
	 */
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

	/**
	 * Used to determine the movement of the vehicle as it is being dragged by the user.
	 * @param v The vehicle that is being dragged by the user.
	 * @return Assign function to the onMouseRelease handler for a vehicle render (rectangle)
	 */
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

	/**
	 * Makes the grid that the user will be interacting with.
	 * @param grid The representation of the grid that is used to make the grid the user will interact with.
	 */
	public void makeGrid(Grid grid) {
		Image img = new Image("Images/MrCat.jpg");
		if (Main.currentTheme == Theme.CARS) {
			img = new Image("Images/Parking.png");
		} else if (Main.currentTheme == Theme.PLANE) {
			img = new Image("Images/AirportLandingStrip.png");
		} else if (Main.currentTheme == Theme.ANIMALS) {
			img = new Image("Images/MrCat.jpg");
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

	/**
	 * Used to load the main menu screen and its background imagery and controller.
	 */
	private void loadSplashScreen() {
		Main.isSplashLoaded = true;
		FXMLLoader loader = loadView("SplashScreen.fxml");
		SplashScreenController splashScreenController = loader.getController();
		splashScreenController.setViewModel("Images/SplashBackground.jpg");
	}

	/**
	 * Used to load the victory screen and its controller.
	 */
	private void loadVictoryScreen() {
		FXMLLoader loader = loadView("VictoryScreen.fxml");
		VictoryScreenController victoryScreenController = loader.getController();
		numMoves++;
		victoryScreenController.printMoves(numMoves);
	}
	private void handleMoveCounter(int moves) {
		if(grid.getMoves().size() == 0) {
			moves = 0;
		}
		else if(before < after) {
			moves++;
			before = 0;
			after = 0;
		}
		String s = "MOVES: " + moves;
		moveCounter.setText(s);
		numMoves = moves;
	}
	private void setMovesBefore() {
		before = grid.getMoves().size();
		System.out.println("before = " + before);
	}
	private void setMovesAfter() {
		after = grid.getMoves().size();
		System.out.println("after = " + after);
		handleMoveCounter(numMoves);
	}

	/**
	 * Setting the fade transition parameters to allow for a UI object to fade in.
	 * @param nodeToFade The element to be faded in.
	 * @param time The time of the fade.
	 * @param setFrom The starting fade opacity.
	 * @param setTo The ending fade opacity.
	 * @param setCycle How many times to do the fade.
	 * @return
	 */
	public static FadeTransition fadeSet(Node nodeToFade, double time, int setFrom, int setTo, int setCycle) {
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(time), nodeToFade);
		fadeIn.setFromValue(setFrom);
		fadeIn.setToValue(setTo);
		fadeIn.setCycleCount(setCycle);
		return fadeIn;
	}

	/**
	 * Obtains the controller of the input View
	 * @param fxmlPath The FXML needed for the controller.
	 * @return FXML loader configuration for the view.
	 */
	public FXMLLoader loadView(String fxmlPath) {
		return Controller.genericViewLoader(fxmlPath, game);
	}

	/**
	 * Loads the view that is to be used.
	 * @param fxmlPath The FXML needed for the controller.
	 * @param rootPane The Pane needed for the controller.
	 * @return The null value.
	 */
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
