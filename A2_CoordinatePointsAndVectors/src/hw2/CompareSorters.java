package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Christian Lisle
 * 
 *         This class executes four sorting algorithms: selection sort,
 *         insertion sort, mergesort, and quicksort, over randomly generated
 *         integers as well integers from a file input. It compares the
 *         execution times of these algorithms on the same input.
 *
 */
public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Use them as coordinates to construct points. Scan these points with
	 * respect to their median coordinate point four times, each time using a
	 * different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		int choice = 1, trial = 1;
		Scanner input = new Scanner(System.in);
		System.out.println(
				"Performances of Four Sorting ALgorithms in Point Scanning\nkeys: 1 (random integers) 2 (file input) 3 (exit)");
		while (choice == 1 || choice == 2 || (choice == 0 && trial == 1)) {
			RotationalPointScanner[] scanners = new RotationalPointScanner[4];
			System.out.print("Trial " + trial + ": ");
			choice = input.nextInt();
			if (choice == 1) {
				System.out.print("Enter the number of random points: ");
				int count = input.nextInt();
				Point[] points = generateRandomPoints(count, new Random());
				scanners[0] = new RotationalPointScanner(points, Algorithm.SelectionSort);
				scanners[1] = new RotationalPointScanner(points, Algorithm.InsertionSort);
				scanners[2] = new RotationalPointScanner(points, Algorithm.MergeSort);
				scanners[3] = new RotationalPointScanner(points, Algorithm.QuickSort);
			}
			else if (choice == 2) {
				System.out.print("Points from a file\nFile name: ");
				String fileName = input.next();
				scanners[0] = new RotationalPointScanner(fileName, Algorithm.SelectionSort);
				scanners[1] = new RotationalPointScanner(fileName, Algorithm.InsertionSort);
				scanners[2] = new RotationalPointScanner(fileName, Algorithm.MergeSort);
				scanners[3] = new RotationalPointScanner(fileName, Algorithm.QuickSort);
			}
			else break; // Break out of the loop if choice is not equal to 1 or 2. Important for the
						// first iteration

			System.out.println("algorithm\tsize\ttime (ns)\n----------------------------------");
			for (int i = 0; i < scanners.length; i++) {
				scanners[i].scan();
				System.out.println(scanners[i].stats());
				scanners[i].draw();
			}
			System.out.println("----------------------------------");
			trial++;
		}
		input.close();
	}

	/**
	 * This method generates a given number of random points. The coordinates of
	 * these points are pseudo-random numbers within the range [-50,50] × [-50,50].
	 * Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts number of points
	 * @param rand Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		Point[] output = new Point[numPts];
		if (numPts < 1) throw new IllegalArgumentException();
		for (int i = 0; i < output.length; i++) {
			int xCoord = rand.nextInt(101) - 50;
			int yCoord = rand.nextInt(101) - 50;
			output[i] = new Point(xCoord, yCoord);
		}
		return output;
	}

}
