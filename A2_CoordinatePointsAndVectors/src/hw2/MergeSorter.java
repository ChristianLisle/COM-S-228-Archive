package edu.iastate.cs228.hw2;

/**
 * @author Christian Lisle
 * 
 *         This class implements the mergesort algorithm.
 *
 */
public class MergeSorter extends AbstractSorter {

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts input array of integers
	 */
	public MergeSorter(Point[] pts) {
		super(pts);
		algorithm = "mergesort";
	}

	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		mergeSort(points);
	}

	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of
	 * points. Makes copies of the two halves of pts[], recursively calls mergeSort
	 * on them, and merges the two sorted subarrays into pts[].
	 * 
	 * @param pts point array
	 */
	private void mergeSort(Point[] pts) {
		if (pts.length <= 1) return;
		int mid = pts.length / 2;
		Point[] left = new Point[mid];
		Point[] right = new Point[pts.length - mid];
		int i = 0;
		for (int j = 0; j < left.length; j++) {
			left[j] = pts[i++];
		}
		for (int j = 0; j < right.length; j++) {
			right[j] = pts[i++];
		}
		mergeSort(left);
		mergeSort(right);
		merge(pts, left, right);
	}

	/**
	 * Merges two subarrays. Called by the recursive mergeSortRec method
	 */
	private void merge(Point[] result, Point[] left, Point[] right) {
		int l = 0, r = 0, i = 0;
		while (l < left.length && r < right.length) {
			if (pointComparator.compare(left[l], right[r]) < 0) {
				result[i++] = left[l++];
			}
			else {
				result[i++] = right[r++];
			}
		}
		while (l < left.length) {
			result[i++] = left[l++];
		}
		while (r < right.length) {
			result[i++] = right[r++];
		}
	}
}
