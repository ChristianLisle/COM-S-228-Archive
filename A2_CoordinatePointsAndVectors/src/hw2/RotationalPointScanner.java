package edu.iastate.cs228.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Christian Lisle
 * 
 *         This class sorts all the points in an array by polar angle with
 *         respect to a reference point whose x and y coordinates are
 *         respectively the medians of the x and y coordinates of the original
 *         points.
 * 
 *         It records the employed sorting algorithm as well as the sorting time
 *         for comparison.
 *
 */
public class RotationalPointScanner {
	private Point[] points;

	public Point medianCoordinatePoint; // point whose x and y coordinates are respectively the medians of
										// the x coordinates and y coordinates of those points in the array
										// points[].
	private Algorithm sortingAlgorithm;

	protected String outputFileName; // "select.txt", "insert.txt", "merge.txt", or "quick.txt"

	protected long scanTime; // execution time in nanoseconds.

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[]. Set
	 * outputFileName.
	 * 
	 * @param pts input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public RotationalPointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts == null || pts.length == 0) throw new IllegalArgumentException();
		points = new Point[pts.length];
		for (int i = 0; i < pts.length; i++) {
			points[i] = new Point(pts[i]);
		}
		setFileName(algo);
		sortingAlgorithm = algo;
	}

	/**
	 * This constructor reads points from a file. Set outputFileName.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException if the input file contains an odd number of
	 *             integers
	 */
	protected RotationalPointScanner(String inputFileName, Algorithm algo)
			throws FileNotFoundException, InputMismatchException {
		Scanner input = new Scanner(new File(inputFileName));
		ArrayList<Integer> scannedPoints = new ArrayList<Integer>();
		while (input.hasNextInt()) {
			scannedPoints.add(input.nextInt());
		}
		input.close();
		// Throws exception if the number of elements in ArrayList is odd
		if (scannedPoints.size() % 2 != 0) throw new InputMismatchException();
		points = new Point[scannedPoints.size() / 2];
		int x = 0, y = 1;
		for (int i = 0; i < points.length; x += 2, y = x + 1, i++) {
			points[i] = new Point(scannedPoints.get(x), scannedPoints.get(y));
		}
		setFileName(algo);
		sortingAlgorithm = algo;
	}

	/**
	 * Sets the file output name based on the algorithm given. (Helper method that
	 * reduces redundant code in constructors)
	 * 
	 * @param algo Sorting algorithm type
	 */
	private void setFileName(Algorithm algo) {
		switch (algo) {
		case SelectionSort:
			outputFileName = "select.txt";
			break;
		case InsertionSort:
			outputFileName = "insert.txt";
			break;
		case MergeSort:
			outputFileName = "merge.txt";
			break;
		case QuickSort:
			outputFileName = "quick.txt";
			break;
		}
	}

	/**
	 * Carry out three rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b) Sort
	 * points[] again by the y-coordinate to get the median y-coordinate. c)
	 * Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates. d) Sort points[] again by the polar angle with respect to
	 * medianCoordinatePoint.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter,
	 * InsertionSorter, MergeSorter, or QuickSorter to carry out sorting. Copy the
	 * sorting result back onto the array points[] by calling the method getPoints()
	 * in AbstractSorter.
	 * 
	 * @param algo
	 * @return
	 */
	public void scan() {
		AbstractSorter aSorter = null;
		scanTime = 0;
		switch (sortingAlgorithm) {
		case SelectionSort:
			aSorter = new SelectionSorter(points);
			break;
		case InsertionSort:
			aSorter = new InsertionSorter(points);
			break;
		case MergeSort:
			aSorter = new MergeSorter(points);
			break;
		case QuickSort:
			aSorter = new QuickSorter(points);
			break;
		}
		// Sort points by their x-coordinates
		aSorter.setComparator(0);
		long startTime = System.nanoTime();
		aSorter.sort();
		int medianX = aSorter.getMedian().getX();
		scanTime = System.nanoTime() - startTime;
		// Sort points by their y-coordinates
		aSorter.setComparator(1);
		startTime = System.nanoTime();
		aSorter.sort();
		scanTime += System.nanoTime() - startTime;
		int medianY = aSorter.getMedian().getY();
		// Sort points by their polar angle w.r.t. the newly created
		// medianCoordinatePoint
		medianCoordinatePoint = new Point(medianX, medianY);
		aSorter.setReferencePoint(medianCoordinatePoint);
		aSorter.setComparator(2);
		startTime = System.nanoTime();
		aSorter.sort();
		scanTime += System.nanoTime() - startTime;
		aSorter.getPoints(points);
	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description.
	 */
	public String stats() {
		String algo = null;
		switch (sortingAlgorithm) {
		case SelectionSort:
			algo = "SelectionSort";
			break;
		case InsertionSort:
			algo = "InsertionSort";
			break;
		case MergeSort:
			algo = "MergeSort";
			break;
		case QuickSort:
			algo = "QuickSort";
			break;
		}
		return algo + "\t" + points.length + "\t" + scanTime;
	}

	/**
	 * Write points[] after a call to scan(). When printed, the points will appear
	 * in order of polar angle with respect to medianCoordinatePoint with every
	 * point occupying a separate line. The x and y coordinates of the point are
	 * displayed on the same line with exactly one blank space in between.
	 */
	@Override
	public String toString() {
		String output = "";
		for (int i = 0; i < points.length; i++) {
			String temp = output + points[i].getX() + " " + points[i].getY();
			output = (i == points.length - 1)? temp: temp + "\n";//Adds a new line if not at the end of the array of points
		}
		return output;
	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writePointsToFile() throws FileNotFoundException {
		PrintStream output = new PrintStream(outputFileName);
		output.print(this.toString());
		output.close();
	}

	/**
	 * This method is called after each scan for visually check whether the result
	 * is correct. You just need to generate a list of points and a list of
	 * segments, depending on the value of sortByAngle, as detailed in Section 4.1.
	 * Then create a Plot object to call the method myFrame().
	 */
	public void draw() {
		Point[] consecutive = removeDuplicatePoints(points);
		int numSegs = consecutive.length * 2; // number of segments to draw
		Segment[] segments = new Segment[numSegs];

		int i = 0;
		for (int p1 = 0, p2 = 1; p2 < consecutive.length; p1++, p2++, i++) {
			segments[i] = new Segment(consecutive[p1], consecutive[p2]);// segment between each point
		}
		segments[i++] = new Segment(consecutive[0], consecutive[consecutive.length - 1]);// Segment between first and
																							// last point
		for (int j = 0; j < consecutive.length; j++, i++) {
			segments[i] = new Segment(consecutive[j], medianCoordinatePoint);
		}
		
		String sort = null;
		switch (sortingAlgorithm) {
		case SelectionSort:
			sort = "Selection Sort";
			break;
		case InsertionSort:
			sort = "Insertion Sort";
			break;
		case MergeSort:
			sort = "Mergesort";
			break;
		case QuickSort:
			sort = "Quicksort";
			break;
		default:
			break;
		}

		// The following statement creates a window to display the sorting result.
		Plot.myFrame(consecutive, segments, sort);

	}

	/**
	 * This method returns the array of Points with all duplicates removed. This is
	 * called right before the segments are created.
	 * 
	 * @return result Array without duplicate points
	 */
	private Point[] removeDuplicatePoints(Point[] pts) {
		ArrayList<Point> plist = new ArrayList<Point>();
		for (Point p : pts) {
			plist.add(p);
		}
		for (int i = 0; i + 1 < plist.size(); i++) {
			if (plist.get(i).equals(plist.get(i + 1))) {
				plist.remove(i + 1);
			}
		}
		Point[] result = new Point[plist.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Point(plist.get(i));
		}
		return result;
	}
}
