package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FilePuzzleService {
	private MoveSet moveSet;	//keeps track of all movements
	private MoveSet solution;
	
	
	public FilePuzzleService(String difficulty) {
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
	public Grid getNewPuzzle(String input){
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
        			  System.out.println("ORIENTATION = " + orientation);

	        		  length = sc.nextInt() -1;
	        		  
	        		  //create vehicle 
	        		  if(orientation.equals("H")){
	        			  Vehicle v = new Vehicle(carId, Vehicle.Orientation.HORIZONTAL, rowNumber, colNumber,(colNumber +length));
	        			  boolean b = puzzle.addVehicle(v);
	        			  System.out.println("VEHICLE Horizontal V ADDED (CHECK: " + b);


	        		  }else if(orientation.equals("V")){
	        			  Vehicle v = new Vehicle(carId, Vehicle.Orientation.VERTICAL, colNumber, rowNumber,(rowNumber +length));
	        			  boolean b = puzzle.addVehicle(v);
	        			  System.out.println("VEHICLE Vertical V ADDED (CHECK:" + b);


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
}
