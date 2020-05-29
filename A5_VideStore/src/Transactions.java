package edu.iastate.cs228.hw5;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * This class simulates video transactions at a video store.
 *
 * @author Christian Lisle
 *
 */
public class Transactions {

	/**
	 * The main method generates a simulation of rental and return activities.
	 * 1. Construct a VideoStore object.
	 * 2. Simulate transactions as in the example given in Section 4 of
	 *    the project description.
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		VideoStore store = new VideoStore("videoList1.txt"); // Video Store Constructor
		Scanner input = new Scanner(System.in);
		int choice = 5;
		System.out.println("Transactions at a Video Store\nkeys:\t1 (rent)\t2 (bulk rent)");
		System.out.println("\t3 (return)\t4 (bulk return)\n\t5 (summary)\t6 (exit)");

		while (true) {
			System.out.print("\nTransaction: ");
			choice = input.nextInt();
			input.nextLine(); // Skips new line

			if (choice == 1) {
				System.out.print("Film to rent: ");
				String film = input.nextLine();
				try	{
					store.videoRent(VideoStore.parseFilmName(film), VideoStore.parseNumCopies(film));
				}
				catch (Exception e)	{
					System.out.println(e.getMessage());
				}
			}
			else if (choice == 2) {
				System.out.print("Video file (rent): ");
				String file = input.nextLine();
				try {
					store.bulkRent(file);
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			else if (choice == 3) {
				System.out.print("Film to return: ");
				String film = input.nextLine();
				try	{
					store.videoReturn(VideoStore.parseFilmName(film), VideoStore.parseNumCopies(film));
				}
				catch (Exception e)	{
					System.out.println(e.getMessage());
				}
			}
			else if (choice == 4) {
				System.out.print("Video file (return): ");
				String file = input.nextLine();
				try {
					store.bulkReturn(file);
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			else if (choice == 5) {
				System.out.println(store.transactionsSummary());
			}
			else if (choice == 6) break;
		}

		input.close();
	}
}
