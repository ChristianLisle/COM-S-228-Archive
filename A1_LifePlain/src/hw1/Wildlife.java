package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * @author Christian Lisle
 * 
 *         The Wildlife class performs a simulation of a grid plain with squares
 *         inhabited by badgers, foxes, rabbits, grass, or none.
 *
 */
public class Wildlife {
	/**
	 * Update the new plain from the old plain in one cycle.
	 * 
	 * @param pOld old plain
	 * @param pNew new plain
	 */
	public static void updatePlain(Plain pOld, Plain pNew) {
		for (int r = 0; r < pNew.grid.length; r++) {
			for (int c = 0; c < pNew.grid[r].length; c++) {
				pNew.grid[r][c] = pOld.grid[r][c].next(pNew);
			}
		}
	}

	/**
	 * Repeatedly generates plains either randomly or from reading files. Over each
	 * plain, carries out an input number of cycles of evolution.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner input = new Scanner(System.in);
		int choice = 0;
		int trial = 1;
		Plain even, odd;
		System.out.println("Simulation of Wildlife of the Plain\nkeys: 1 (random plain) 2 (file input) 3 (exit)");

		while (choice == 1 || choice == 2 || (choice == 0 && trial == 1)) {
			int cycles = 0;
			System.out.print("Trial " + trial + ": ");
			choice = input.nextInt();

			if (choice == 1) {
				System.out.print("Random plain\nEnter grid width: ");
				int gridWidth = input.nextInt();
				even = new Plain(gridWidth);
				odd = new Plain(gridWidth);
				even.randomInit();
				while (cycles <= 0) {
					System.out.print("Enter the number of cycles: ");
					cycles = input.nextInt();
				}
			}
			else if (choice == 2) {
				System.out.print("Plain input from a file\nFile name: ");
				String fileName = input.next();
				even = new Plain(fileName);
				odd = new Plain(even.getWidth());
				while (cycles <= 0) {
					System.out.print("Enter the number of cycles: ");
					cycles = input.nextInt();
				}
			}
			else break; // break out of loop if choice is not equal to 1 or 2. Important for the first iteration

			System.out.println("Initial plain:\n" + even.toString() + "\n");
			for (int i = 1; i <= cycles; i++) {
				if (i % 2 == 0) {
					updatePlain(odd, even);
				}
				else {
					updatePlain(even, odd);
				}
			}
			if (cycles % 2 == 0) {
				System.out.println("Final plain:\n" + even.toString() + "\n");
			}
			else {
				System.out.println("Final plain:\n" + odd.toString() + "\n");
			}
			trial++;
		}
		input.close();
	}
}
