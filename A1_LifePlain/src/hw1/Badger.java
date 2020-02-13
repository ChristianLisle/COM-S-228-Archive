package edu.iastate.cs228.hw1;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class represents a Badger square. A badger eats a rabbit and
 *         competes against a fox.
 *
 */
public class Badger extends Animal {
	/**
	 * Creates a Badger object
	 * 
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 * @param a: age
	 */
	public Badger(Plain p, int r, int c, int a) {
		super(p, r, c, a);
	}

	/**
	 * A badger occupies the square.
	 */
	public State who() {
		return State.BADGER;
	}

	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group
	 * of foxes.
	 * 
	 * @param pNew plain of the next cycle
	 * @return Living life form occupying the square in the next cycle.
	 */
	public Living next(Plain pNew) {
		int[] population = new int[5];
		this.census(population);

		if (myAge() == BADGER_MAX_AGE) {
			pNew.grid[this.row][this.column] = new Empty(pNew, this.row, this.column);
		}
		else if (population[BADGER] == 1 && population[FOX] > 1) {
			pNew.grid[this.row][this.column] = new Fox(pNew, this.row, this.column, 0);
		}
		else if (population[BADGER] + population[FOX] > population[RABBIT]) {
			pNew.grid[this.row][this.column] = new Empty(pNew, this.row, this.column);
		}
		else {
			this.age++;
			pNew.grid[this.row][this.column] = new Badger(pNew, this.row, this.column, myAge());
		}
		return pNew.grid[this.row][this.column];
	}
}
