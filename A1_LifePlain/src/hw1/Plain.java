package edu.iastate.cs228.hw1;

/**
 *  
 * @author Christian Lisle
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Random;

/**
 * 
 * The plain is represented as a square grid of size width x width.
 *
 */
public class Plain {
	private int width; // grid size: width X width

	public Living[][] grid; // grid containing living objects

	/**
	 * Default constructor reads from a file. It is assumed that the file is in the
	 * correct format.
	 */
	public Plain(String inputFileName) throws FileNotFoundException {
		File file = new File(inputFileName);
		Scanner input = new Scanner(file);
		Scanner firstLine = new Scanner(file);
		String firstLineContains = firstLine.nextLine();
		for (int i = 0; i < firstLineContains.length(); i++) {
			if (Character.isAlphabetic(firstLineContains.charAt(i))) {
				width++;
			}
		}
		grid = new Living[width][width];

		for (int r = 0; r < grid.length && input.hasNextLine(); r++) {
			for (int c = 0; c < grid[r].length && input.hasNext(); c++) {
				String current = input.next();
				switch (current.charAt(0)) {
				case ' ':
					break;
				case 'B':
					grid[r][c] = new Badger(this, r, c, Character.getNumericValue(current.charAt(1)));
					break;
				case 'F':
					grid[r][c] = new Fox(this, r, c, Character.getNumericValue(current.charAt(1)));
					break;
				case 'R':
					grid[r][c] = new Rabbit(this, r, c, Character.getNumericValue(current.charAt(1)));
					break;
				case 'G':
					grid[r][c] = new Grass(this, r, c);
					break;
				case 'E':
					grid[r][c] = new Empty(this, r, c);
					break;
				}
			}
		}
		firstLine.close();
		input.close();
	}

	/**
	 * Constructor that builds a w x w grid without initializing it.
	 * 
	 * @param width the grid
	 */
	public Plain(int w) {
		this.width = w;
		grid = new Living[width][width];
	}

	public int getWidth() {
		return width;
	}

	/**
	 * Initialize the plain by randomly assigning to every square of the grid one of
	 * BADGER, FOX, RABBIT, GRASS, or EMPTY.
	 * 
	 * Every animal starts at age 0.
	 */
	public void randomInit() {
		Random generator = new Random();
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				switch (generator.nextInt(5)) {
				case 0:
					grid[r][c] = new Empty(this, r, c);
					break;
				case 1:
					grid[r][c] = new Grass(this, r, c);
					break;
				case 2:
					grid[r][c] = new Badger(this, r, c, 0);
					break;
				case 3:
					grid[r][c] = new Fox(this, r, c, 0);
					break;
				case 4:
					grid[r][c] = new Rabbit(this, r, c, 0);
					break;
				}
			}
		}
	}

	/**
	 * Output the plain grid. For each square, output the first letter of the living
	 * form occupying the square. If the living form is an animal, then output the
	 * age of the animal followed by a blank space; otherwise, output two blanks.
	 */
	public String toString() {
		String output = "";
		for (int r = 0; r < width; r++) {
			for (int c = 0; c < width; c++) {
				switch (grid[r][c].who()) {
				case EMPTY:
					output = output + "E ";
					break;
				case GRASS:
					output = output + "G ";
					break;
				case BADGER:
					output = output + "B" + ((Animal) grid[r][c]).myAge();
					break;
				case FOX:
					output = output + "F" + ((Animal) grid[r][c]).myAge();
					break;
				case RABBIT:
					output = output + "R" + ((Animal) grid[r][c]).myAge();
					break;
				}
				output = output + " ";
			}
			output = (r < width - 1) ? output + "\n" : output; // adds a new line unless on the last row
		}
		return output;
	}

	/**
	 * Write the plain grid to an output file. Also useful for saving a randomly
	 * generated plain for debugging purpose.
	 * 
	 * @throws FileNotFoundException
	 */
	public void write(String outputFileName) throws FileNotFoundException {
		File file = new File(outputFileName);
		PrintWriter output = new PrintWriter(file);
		output.println(this.toString());
		output.close();
	}
}
