package edu.iastate.cs228.hw1;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class represents a Fox square. A fox eats rabbits and competes
 *         against a badger.
 * 
 */
public class Fox extends Animal {
	/**
	 * Creates a Fox object
	 * 
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 * @param a: age
	 */
	public Fox(Plain p, int r, int c, int a) {
		super(p, r, c, a);
	}

	/**
	 * A fox occupies the square.
	 */
	public State who() {
		return State.FOX;
	}

	/**
	 * A fox dies of old age or hunger, or from attack by numerically superior
	 * badgers.
	 * 
	 * @param pNew plain of the next cycle
	 * @return Living life form occupying the square in the next cycle.
	 */
	public Living next(Plain pNew) {
		int[] population = new int[5];
		this.census(population);

		if (myAge() == FOX_MAX_AGE) {
			pNew.grid[this.row][this.column] = new Empty(pNew, this.row, this.column);
		}
		else if (population[BADGER] > population[FOX]) {
			pNew.grid[this.row][this.column] = new Badger(pNew, this.row, this.column, 0);
		}
		else if (population[BADGER] + population[FOX] > population[RABBIT]) {
			pNew.grid[this.row][this.column] = new Empty(pNew, this.row, this.column);
		}
		else {
			this.age++;
			pNew.grid[this.row][this.column] = new Fox(pNew, this.row, this.column, myAge());
		}
		return pNew.grid[this.row][this.column];
	}
}
