package edu.iastate.cs228.hw1;

/**
 * 
 * @author Christian Lisle
 *
 *         Living refers to the life form occupying a square in a plain grid. It
 *         is a superclass of Empty, Grass, and Animal, the latter of which is
 *         in turn a superclass of Badger, Fox, and Rabbit. Living has two
 *         abstract methods awaiting implementation.
 *
 */
public abstract class Living {
	protected Plain plain; // the plain in which the life form resides
	protected int row; // location of the square on which
	protected int column; // the life form resides

	// constants to be used as indices.
	protected static final int BADGER = 0;
	protected static final int EMPTY = 1;
	protected static final int FOX = 2;
	protected static final int GRASS = 3;
	protected static final int RABBIT = 4;

	public static final int NUM_LIFE_FORMS = 5;

	// life expectancies
	public static final int BADGER_MAX_AGE = 4;
	public static final int FOX_MAX_AGE = 6;
	public static final int RABBIT_MAX_AGE = 3;

	/**
	 * Censuses all life forms in the 3 X 3 neighborhood in a plain.
	 * 
	 * @param population counts of all life forms
	 */
	protected void census(int population[]) {
		// Set the start and end index for the life search using the ternary operator
		int rStart = (this.row > 0) ? this.row - 1 : this.row;
		int rEnd = (this.row < plain.getWidth() - 1) ? this.row + 1 : this.row;
		int cStart = (this.column > 0) ? this.column - 1 : this.column;
		int cEnd = (this.column < plain.getWidth() - 1) ? this.column + 1 : this.column;

		for (int r = rStart; r <= rEnd; r++) {
			for (int c = cStart; c <= cEnd; c++) {
				switch (plain.grid[r][c].who()) {
				case BADGER:
					population[BADGER]++;
					break;
				case EMPTY:
					population[EMPTY]++;
					break;
				case FOX:
					population[FOX]++;
					break;
				case GRASS:
					population[GRASS]++;
					break;
				case RABBIT:
					population[RABBIT]++;
					break;
				}
			}
		}
	}

	/**
	 * Gets the identity of the life form on the square.
	 * 
	 * @return State
	 */
	public abstract State who();
	// Implemented in each class of Badger, Empty, Fox, Grass, and Rabbit.

	/**
	 * Determines the life form on the square in the next cycle.
	 * 
	 * @param pNew plain of the next cycle
	 * @return Living
	 */
	public abstract Living next(Plain pNew);
	// Implemented in the classes Badger, Empty, Fox, Grass, and Rabbit.
	//
	// Each living object does the following in this method:
	//
	// 1. Obtains counts of life forms in the 3x3 neighborhood of the class object.
	//
	// 2. Applies the survival rules for the life form to determine the life form
	// (on the same square) in the next cycle. These rules are given in the
	// project description.
	//
	// 3. Generate this new life form at the same location in the plain pNew.

}
