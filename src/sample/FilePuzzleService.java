package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import sample.PuzzleService.Difficulty;

public class FilePuzzleService implements PuzzleService {

	private MoveSet moveSet;	//keeps track of all movements
	private MoveSet solution;
	private int lastRandom;
	private int lastMedRandom;
	private int lastHardRandom;
//	public int medIndex;
//	public int hardIndex;

	public static Integer MED_LOWER_BOUND = 1;
	public static Integer MED_UPPER_BOUND = 5;
	public static Integer HARD_LOWER_BOUND = 6;
	public static Integer HARD_UPPER_BOUND = 10;

	public FilePuzzleService() {
		this.moveSet = new MoveSet();
		this.solution = new MoveSet();
	}

	/**
	 * creates the starting stage for a puzzle with a definite known solution
	 * @pre orientation is always either "H" or "V"	
	 * @param input file which provides the initial composition of the puzzle
	 * @return a grid for the UI to display
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

//	public String returnFileNum (Difficulty d) {
//		medLevels = new ArrayList<>();
//		hardLevels = new ArrayList<>();
//		String file = null;
//
//		// Adds all the existing files to the new array lists
//		// This is one the basis they are consecutive numbers from lower to upper bounds
//		// Could improve to scan in .txt files from folder
//		for (int i = 0; i < MED_UPPER_BOUND; i++) {
//			fileName = MED_LOWER_BOUND + i;
//			medLevels.add(fileName);
//		}
//
//		for (int i = 0; i < HARD_UPPER_BOUND - MED_UPPER_BOUND; i++) {
//			fileName = HARD_LOWER_BOUND + i;
//			hardLevels.add(fileName);
//		}
//
//		// Print check
////		for (int currFile : medLevels) {
////			System.out.println(currFile);
////		}
////
////		for (int currFile : hardLevels) {
////			System.out.println(currFile);
////		}
//
//		// Retrieves the file
//		if (d == Difficulty.MEDIUM) {
//			file = Integer.toString(medLevels.get(getMedIndex()));
//			setMedIndex(getMedIndex() + 1);
//
//			 if (getMedIndex() > MED_UPPER_BOUND) {
//				setMedIndex(0);
//			 }
//		} else if (d == Difficulty.HARD) {
//			file = Integer.toString(hardLevels.get(getHardIndex()));
//			setHardIndex(getHardIndex() + 1);
//
//			if (getHardIndex() > HARD_UPPER_BOUND) {
//				setHardIndex(0);
//			}
//		}
//
//		System.out.println("File return is " + d + " " + file);
//
//		return file;
//
//	}

	/**
	 * NOTE FUNCTION CURRENTLY LOOKS LIKE IT'S WORKING BUT IT ISN'T [NEED TO FIX]
	 * @return
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

	public int getLastRandom() {
		return lastRandom;
	}

	public void setLastRandom(int input) {
		lastRandom = input;
		//System.out.println("XX" +lastRandom);
	}

	public int getLastMedRandom() {
		return lastMedRandom;
	}

	public void setLastMedRandom(int lastMedRandom) {
		this.lastMedRandom = lastMedRandom;
	}

	public int getLastHardRandom() {
		return lastHardRandom;
	}

	public void setLastHardRandom(int lastHardRandom) {
		this.lastHardRandom = lastHardRandom;
	}

	// ======================= REMOVE? ================================================

//	public int getMedIndex() {
//		System.out.println("Getting medium index " + medIndex);
//		return medIndex;
//	}
//
//	public void setMedIndex(int input) {
//		System.out.println("Setting medium index " + medIndex + " to " + input);
//		medIndex = input;
//	}
//
//	public int getHardIndex() {
//		System.out.println("Getting hard index " + hardIndex);
//		return hardIndex;
//	}
//
//	public void setHardIndex(int input) {
//		System.out.println("Setting hard index " + hardIndex + " to " + input);
//		hardIndex = input;
//	}
}
