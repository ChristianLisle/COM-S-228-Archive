package edu.iastate.cs228.hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 
 * This class represents a Video Store.
 * 
 * @author Christian Lisle
 */
public class VideoStore {
	protected SplayTree<Video> inventory; // all the videos at the store

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor sets inventory to an empty tree.
	 */
	public VideoStore() {
		inventory = new SplayTree<Video>(); // Empty tree
	}

	/**
	 * Constructor accepts a video file to create its inventory. Refer to Section
	 * 3.2 of the project description for details regarding the format of a video
	 * file.
	 * 
	 * Calls setUpInventory().
	 * 
	 * @param videoFile no format checking on the file
	 * @throws FileNotFoundException
	 */
	public VideoStore(String videoFile) throws FileNotFoundException {
		this();
		setUpInventory(videoFile);
	}

	/**
	 * Accepts a video file to initialize the splay tree inventory. To be efficient,
	 * add videos to the inventory by calling the addBST() method, which does not
	 * splay.
	 * 
	 * Refer to Section 3.2 for the format of video file.
	 * 
	 * @param videoFile correctly formated if exists
	 * @throws FileNotFoundException
	 */
	public void setUpInventory(String videoFile) throws FileNotFoundException {
		bulkImport(videoFile);
	}

	// ------------------
	// Inventory Addition
	// ------------------

	/**
	 * Find a Video object by film title.
	 * 
	 * @param film
	 * @return
	 */
	public Video findVideo(String film) {
		Video v = new Video(film);
		if (inventory.contains(v)) {
			return inventory.getRoot();
		}
		return null;
	}

	/**
	 * Updates the splay tree inventory by adding a number of video copies of the
	 * film. (Splaying is justified as new videos are more likely to be rented.)
	 * 
	 * Calls the add() method of SplayTree to add the video object.
	 * 
	 * a) If true is returned, the film was not on the inventory before, and has
	 * been added. b) If false is returned, the film is already on the inventory.
	 * 
	 * The root of the splay tree must store the corresponding Video object for the
	 * film. Update the number of copies for the film.
	 * 
	 * @param film title of the film
	 * @param n number of video copies
	 */
	public void addVideo(String film, int n) {
		Video v = new Video(film, n);
		if (!inventory.add(v)) {
			inventory.getRoot().addNumCopies(n);
		}
	}

	/**
	 * Add one video copy of the film.
	 * 
	 * @param film title of the film
	 */
	public void addVideo(String film) {
		addVideo(film, 1);
	}

	/**
	 * Update the splay trees inventory by adding videos. Perform binary search
	 * additions by calling addBST() without splaying.
	 * 
	 * The videoFile format is given in Section 3.2 of the project description.
	 * 
	 * @param videoFile correctly formated if exists
	 * @throws FileNotFoundException
	 */
	public void bulkImport(String videoFile) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(videoFile));
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (parseNumCopies(line) > 0)	{
				Video v = new Video(parseFilmName(line), parseNumCopies(line));
				inventory.addBST(v);
			}
		}
		scan.close();
	}

	// ----------------------------
	// Video Query, Rental & Return
	// ----------------------------

	/**
	 * Search the splay tree inventory to determine if a video is available.
	 * 
	 * @param film
	 * @return true if available
	 */
	public boolean available(String film) {
		int availableCopies = 0;
		Video v = new Video(film);
		if (inventory.contains(v))	{
			availableCopies = inventory.getRoot().getNumAvailableCopies();
		}
		return availableCopies > 0;
	}

	/**
	 * Update inventory.
	 * 
	 * Search if the film is in inventory by calling findElement(new Video(film,
	 * 1)).
	 * 
	 * If the film is not in inventory, prints the message "Film <film> is not in
	 * inventory", where <film> shall be replaced with the string that is the value
	 * of the parameter film. If the film is in inventory with no copy left, prints
	 * the message "Film <film> has been rented out".
	 * 
	 * If there is at least one available copy but n is greater than the number of
	 * such copies, rent all available copies. In this case, no
	 * AllCopiesRentedOutException is thrown.
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException if n <= 0 or film == null or film.isEmpty()
	 * @throws FilmNotInInventoryException if film is not in the inventory
	 * @throws AllCopiesRentedOutException if there is zero available copy for the
	 *             film.
	 */
	public void videoRent(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException, AllCopiesRentedOutException {
		if (n <= 0 || film == null || film.isEmpty()) throw new IllegalArgumentException("Film " + film + " has an invalid request");
		Video v = new Video(film, 1);
		if (!inventory.contains(v))	{
			throw new FilmNotInInventoryException("Film " + film + " is not in inventory");
		}
		int availableCopies = inventory.getRoot().getNumAvailableCopies();
		if (availableCopies == 0)	{
			throw new AllCopiesRentedOutException("Film " + film + " has been rented out");
		}
		else inventory.getRoot().rentCopies(n);
	}

	/**
	 * Update inventory.
	 * 
	 * 1. Calls videoRent() repeatedly for every video listed in the file.
	 * 
	 * 2. For each requested video, do the following:
	 * 
	 * 	a) If it is not in inventory or is
	 * rented out, an exception will be thrown from videoRent(). Based on the
	 * exception, prints out the following message: "Film <film> is not in
	 * inventory" or "Film <film> has been rented out." In the message, <film> shall
	 * be replaced with the name of the video.
	 * 
	 * 	b) Otherwise, update the video record in the inventory.
	 * 
	 * For details on handling of multiple exceptions and message printing, please
	 * read Section 3.4 of the project description.
	 * 
	 * @param videoFile correctly formatted if exists
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException if the number of copies of any film is <= 0
	 * @throws FilmNotInInventoryException if any film from the videoFile is not in
	 *             the inventory
	 * @throws AllCopiesRentedOutException if there is zero available copy for some
	 *             film in videoFile
	 */
	public void bulkRent(String videoFile) throws FileNotFoundException, IllegalArgumentException, FilmNotInInventoryException, AllCopiesRentedOutException {
		Scanner scan = new Scanner(new File(videoFile));
		boolean IllegalArgument, NotInInventory, RentedOut;
		IllegalArgument = NotInInventory = RentedOut = false;
		String exceptionMessages = "";
		while (scan.hasNextLine())	{
			String line = scan.nextLine();
			try	{
				videoRent(parseFilmName(line), parseNumCopies(line));
			}
			catch(Exception e)	{
				if (e instanceof IllegalArgumentException) IllegalArgument = true;
				else if (e instanceof FilmNotInInventoryException) NotInInventory = true;
				else if (e instanceof AllCopiesRentedOutException) RentedOut = true;
				exceptionMessages += e.getMessage() + "\n";
			}
		}
		scan.close();
		if (IllegalArgument || NotInInventory || RentedOut)	{
			exceptionMessages = exceptionMessages.trim();
			if (IllegalArgument) throw new IllegalArgumentException(exceptionMessages);
			else if (NotInInventory) throw new FilmNotInInventoryException(exceptionMessages);
			else if (RentedOut) throw new AllCopiesRentedOutException(exceptionMessages);
		}
	}

	/**
	 * Update inventory.
	 * 
	 * If n exceeds the number of rented video copies, accepts up to that number of
	 * rented copies while ignoring the extra copies.
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException if n <= 0 or film == null or film.isEmpty()
	 * @throws FilmNotInInventoryException if film is not in the inventory
	 */
	public void videoReturn(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException {
		if (n <= 0 || film == null || film.isEmpty()) throw new IllegalArgumentException("Film " + film + " has an invalid request");
		if (!inventory.contains(new Video(film, 1))) throw new FilmNotInInventoryException("Film " + film + " is not in inventory");
		else inventory.getRoot().returnCopies(n);
	}

	/**
	 * Update inventory.
	 * 
	 * Handles excessive returned copies of a film in the same way as videoReturn()
	 * does. See Section 3.4 of the project description on how to handle multiple
	 * exceptions.
	 * 
	 * @param videoFile
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException if the number of return copies of any film
	 *             is <= 0
	 * @throws FilmNotInInventoryException if a film from videoFile is not in
	 *             inventory
	 */
	public void bulkReturn(String videoFile) throws FileNotFoundException, IllegalArgumentException, FilmNotInInventoryException {
		Scanner scan = new Scanner(new File(videoFile));
		boolean IllegalArgument, NotInInventory;
		IllegalArgument = NotInInventory = false;
		String exceptionMessages = "";
		while (scan.hasNextLine())	{
			String line = scan.nextLine();
			try	{
				videoReturn(parseFilmName(line), parseNumCopies(line));
			}
			catch(Exception e)	{
				if (e instanceof IllegalArgumentException) IllegalArgument = true;
				else if (e instanceof FilmNotInInventoryException) NotInInventory = true;
				exceptionMessages += e.getMessage() + "\n";
			}
		}
		scan.close();
		if (IllegalArgument || NotInInventory)	{
			exceptionMessages = exceptionMessages.trim();
			if (IllegalArgument) throw new IllegalArgumentException(exceptionMessages);
			else if (NotInInventory) throw new FilmNotInInventoryException(exceptionMessages);
		}
	}

	// ------------------------
	// Methods without Splaying
	// ------------------------

	/**
	 * Performs inorder traversal on the splay tree inventory to list all the videos
	 * by film title, whether rented or not. Below is a sample string if printed
	 * out:
	 * 
	 * 
	 * Films in inventory:
	 * 
	 * A Streetcar Named Desire (1)
	 * Brokeback Mountain (1)
	 * Forrest Gump (1)
	 * Psycho (1)
	 * Singin' in the Rain (2)
	 * Slumdog Millionaire (5)
	 * Taxi Driver (1)
	 * The Godfather (1)
	 * 
	 * 
	 * @return
	 */
	public String inventoryList() {
		Iterator<Video> iter = inventory.iterator();
		String list = "Films in inventory:\n";
		while (iter.hasNext())	{
			Video v = iter.next();
			list += "\n" + v.getFilm() + " (" + v.getNumCopies() + ")";
		}
		return list;
	}

	/**
	 * Calls rentedVideosList() and unrentedVideosList() sequentially. For the
	 * string format, see Transaction 5 in the sample simulation in Section 4 of the
	 * project description.
	 * 
	 * @return
	 */
	public String transactionsSummary() {
		return rentedVideosList() + "\n\n" + unrentedVideosList();
	}

	/**
	 * Performs inorder traversal on the splay tree inventory. Use a splay tree
	 * iterator.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * Rented films:
	 * 
	 * Brokeback Mountain (1)
	 * Forrest Gump (1)
	 * Singin' in the Rain (2)
	 * The Godfather (1)
	 * 
	 * 
	 * @return
	 */
	private String rentedVideosList() {
		Iterator<Video> iter = inventory.iterator();
		String list = "Rented films:\n";
		while (iter.hasNext())	{
			Video v = iter.next();
			if (v.getNumRentedCopies() > 0)	{
				list += "\n" + v.getFilm() + " (" + v.getNumRentedCopies() + ")";
			}
		}
		return list;
	}

	/**
	 * Performs inorder traversal on the splay tree inventory. Use a splay tree
	 * iterator. Prints only the films that have unrented copies.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * 
	 * Films remaining in inventory:
	 * 
	 * A Streetcar Named Desire (1)
	 * Forrest Gump (1)
	 * Psycho (1)
	 * Slumdog Millionaire (4)
	 * Taxi Driver (1)
	 * 
	 * 
	 * @return
	 */
	private String unrentedVideosList() {
		Iterator<Video> iter = inventory.iterator();
		String list = "Films remaining in inventory:\n";
		while (iter.hasNext())	{
			Video v = iter.next();
			if (v.getNumAvailableCopies() > 0)	{
				list += "\n" + v.getFilm() + " (" + v.getNumAvailableCopies() + ")";
			}
		}
		return list;
	}

	/**
	 * Parse the film name from an input line.
	 * 
	 * @param line
	 * @return
	 */
	public static String parseFilmName(String line) {
		String r = line;
		if (r.contains("(")) {
			r = r.substring(0, r.indexOf('('));
		}
		return r.trim();
	}

	/**
	 * Parse the number of copies from an input line.
	 * 
	 * @param line
	 * @return
	 */
	public static int parseNumCopies(String line) {
		String r = line;
		if (r.contains("(")) {
			r = r.substring(r.indexOf('(') + 1, r.indexOf(')'));
			return Integer.parseInt(r);
		}
		else return 1;
	}
}
