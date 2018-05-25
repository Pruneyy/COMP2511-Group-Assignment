package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * An implementation of the Puzzle Services interface which will load a new puzzle from one of the
 * pre-made puzzles files.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class FilePuzzleService implements PuzzleService {

	private MoveSet moveSet;	//keeps track of all movements
	private MoveSet solution;
	private int lastRandom;
	private int lastMedRandom;
	private int lastHardRandom;

	public static Integer MED_LOWER_BOUND = 1;
	public static Integer MED_UPPER_BOUND = 5;
	public static Integer HARD_LOWER_BOUND = 6;
	public static Integer HARD_UPPER_BOUND = 10;

	/**
	 * Constructor to create a new object that stores all the moves and solution.
	 */
	public FilePuzzleService() {
		this.moveSet = new MoveSet();
		this.solution = new MoveSet();
	}

	/**
	 * Creates the starting stage for a puzzle which is loaded from a pre-made file. Based on the
	 * difficulty the user has chosen, the appropriate file range is selected from.
	 * @param d The difficulty chosen by the user.
	 * @return A grid for the UI to display.
	 */
	public Grid getNewPuzzle(Difficulty d){

		String input = "src/Starting Board/" + returnRandom(d) + ".txt";
		// System.out.println("SOURCE FILE PATH IS: " + input);
        Grid puzzle = new Grid();
		Scanner sc = null;
	      try
	      { 
	          sc = new Scanner(new File(input));
	          String keyword;
	          int carId;
	          String orientation;
	          int rowNumber;
	          int colNumber;
	          int length;
	          
	          String movementDirection;
	          int movementLength;
	          
	          while(sc.hasNext()) {
	        	  keyword = sc.next();
	        	  if(keyword.equals("Set")) {
	        		  carId = sc.nextInt() + 1;
	        		  orientation = sc.next();
	        		  if(orientation.equals("H")) {
	        			  rowNumber = sc.nextInt() -1;
	        			  colNumber = sc.nextInt() -1;
	        		  }
	        		  else {
	        			  colNumber = sc.nextInt() -1;
	        			  rowNumber = sc.nextInt() -1;
	        		  }
        			  //System.out.println("ORIENTATION = " + orientation);

	        		  length = sc.nextInt() -1;
	        		  
	        		  //create vehicle 
	        		  if(orientation.equals("H")){
	        			  Vehicle v = new Vehicle(carId, Vehicle.Orientation.HORIZONTAL, rowNumber, colNumber,(colNumber +length));
	        			  boolean b = puzzle.addVehicle(v);
	        			  //System.out.println("VEHICLE Horizontal V ADDED (CHECK: " + b);


	        		  }else if(orientation.equals("V")){
	        			  Vehicle v = new Vehicle(carId, Vehicle.Orientation.VERTICAL, colNumber, rowNumber,(rowNumber +length));
	        			  boolean b = puzzle.addVehicle(v);
	        			  //System.out.println("VEHICLE Vertical V ADDED (CHECK:" + b);


	        		  }
	        	  }
	        	  else if(keyword.equals("Solution")) {
	        		  //use to store the solution later (maybe)
	        		  break;
	        	  }
	          }
	      }catch (FileNotFoundException e){
	          System.out.println(e.getMessage());
	      }finally{
	          if (sc != null) sc.close();
	      }
      return puzzle;
	}

	/**
	 * Generates a random file to generate into a game board based on the difficulty the user has chosen.
	 * @param d The difficulty chosen by the user.
	 * @return The random number that has been generated.
	 */
	public int returnRandom(Difficulty d) {
		// Change the upper and lower bounds as the number of pre-made files increases
		int flag = 0;
		Random rand = new Random();
		int randomNum = 0;

		while (flag == 0) {
			if (d == Difficulty.MEDIUM) {
				randomNum = rand.nextInt(MED_UPPER_BOUND) + MED_LOWER_BOUND;
				if(randomNum != getLastMedRandom()) {
					setLastMedRandom(randomNum);
					flag = 1;
				}

			} else if (d == Difficulty.HARD) {
				randomNum = rand.nextInt(HARD_UPPER_BOUND - HARD_LOWER_BOUND + 1) + HARD_LOWER_BOUND;
				if (randomNum != getLastHardRandom()) {
					setLastHardRandom(randomNum);
					flag = 1;
				}
			}
		}

		System.out.println("Returning the random number " + randomNum + " " + d);
		return randomNum;
	}

	/**
	 * Gets the last medium level randomly generated file.
	 * @return The last medium level randomly generated file.
	 */
	public int getLastMedRandom() {
		return lastMedRandom;
	}

	/**
	 * Sets the last medium level randomly generated file.
	 * @param lastMedRandom The last medium level randomly generated file.
	 */
	public void setLastMedRandom(int lastMedRandom) {
		this.lastMedRandom = lastMedRandom;
	}

	/**
	 * Gets the last hard level randomly generated file.
	 * @return The last hard level randomly generated file.
	 */
	public int getLastHardRandom() {
		return lastHardRandom;
	}

	/**
	 * Sets the last hard level randomly generated file.
	 * @param lastHardRandom The last hard level randomly generated file.
	 */
	public void setLastHardRandom(int lastHardRandom) {
		this.lastHardRandom = lastHardRandom;
	}
}
