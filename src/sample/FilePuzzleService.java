package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import sample.PuzzleService.Difficulty;

public class FilePuzzleService implements PuzzleService {
	private MoveSet moveSet;	//keeps track of all movements
	private MoveSet solution;
	private int lastRandom;
	

	
	
	public FilePuzzleService() {
		// TODO Auto-generated constructor stub
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
		String input = "src/Starting Board/" + returnRandom() + ".txt";
		//System.out.print(input +"\n");
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
	 * NOTE FUNCTION CURRENTLY LOOKS LIKE IT'S WORKING BUT IT ISN'T [NEED TO FIX]
	 * @return
	 */
	public int returnRandom() {
		//(edbert) increase this number as more preloaded puzzles are created
		int flag = 0;
		Random rand = new Random();
		int randomNum = 0; 
		while (flag == 0) {
			randomNum = rand.nextInt(10) + 1;
			//System.out.println("Curr Random = " + randomNum + " Last Random = " + getLastRandom());
			if(randomNum != getLastRandom()) {
				setLastRandom(randomNum);
				flag = 1;
			}
		}		
		return lastRandom;
	}
	public int getLastRandom() {
		return lastRandom;
	}
	public void setLastRandom(int input) {
		lastRandom = input;
		//System.out.println("XX" +lastRandom);
	}
}
