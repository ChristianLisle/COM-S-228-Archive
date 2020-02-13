package edu.iastate.cs228.hw1;

/**
 * 
 * @author Christian Lisle
 * 
 *         This abstract class represents a generic Animal. This class is To be
 *         extended by the Badger, Fox, and Rabbit classes.
 */
public abstract class Animal extends Living implements MyAge {
	protected int age; // Age of the animal

	/**
	 * Constructor for animal type classes.
	 * 
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 * @param a: age
	 */
	public Animal(Plain p, int r, int c, int a) {
		this.plain = p;
		this.row = r;
		this.column = c;
		this.age = a;
	}

	@Override
	/**
	 * @return age The age of the animal
	 */
	public int myAge() {
		return age;
	}
}
