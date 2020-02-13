package edu.iastate.cs228.hw1;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class represents an Empty square. Empty squares are competed by
 *         various forms of life.
 * 
 */
public class Empty extends Living {
	/**
	 * Creates an Empty object
	 * 
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 */
	public Empty(Plain p, int r, int c) {
		this.plain = p;
		this.row = r;
		this.column = c;
	}

	/*
	 * Nothing (Empty) occupies the square.
	 */
	public State who() {
		return State.EMPTY;
	}

	/**
	 * An empty square will be occupied by a neighboring Badger, Fox, Rabbit, or
	 * Grass, or remain empty.
	 * 
	 * @param pNew plain of the next life cycle.
	 * @return Living life form in the next cycle.
	 */
	public Living next(Plain pNew) {
		int[] population = new int[5];
		this.census(population);

		if (population[RABBIT] > 1) {
			pNew.grid[this.row][this.column] = new Rabbit(pNew, this.row, this.column, 0);
		}
		else if (population[FOX] > 1) {
			pNew.grid[this.row][this.column] = new Fox(pNew, this.row, this.column, 0);
		}
		else if (population[BADGER] > 1) {
			pNew.grid[this.row][this.column] = new Badger(pNew, this.row, this.column, 0);
		}
		else if (population[GRASS] >= 1) {
			pNew.grid[this.row][this.column] = new Grass(pNew, this.row, this.column);
		}
		else {
			pNew.grid[this.row][this.column] = new Empty(pNew, this.row, this.column);
		}
		return pNew.grid[this.row][this.column];
	}
}
