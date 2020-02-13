package edu.iastate.cs228.hw1;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class represents a Grass square. Grass remains if more than
 *         rabbits in the neighborhood; otherwise, it is eaten.
 */
public class Grass extends Living {
	/**
	 * Creates a Grass object
	 * 
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 */
	public Grass(Plain p, int r, int c) {
		this.plain = p;
		this.row = r;
		this.column = c;
	}

	/*
	 * Grass occupies the square.
	 */
	public State who() {
		return State.GRASS;
	}

	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast
	 * enough to take over Grass.
	 */
	public Living next(Plain pNew) {
		int[] population = new int[5];
		this.census(population);

		if (population[RABBIT] >= (3 * population[GRASS])) {
			pNew.grid[this.row][this.column] = new Empty(pNew, this.row, this.column);
		}
		else if (population[RABBIT] >= 3) {
			pNew.grid[this.row][this.column] = new Rabbit(pNew, this.row, this.column, 0);
		}
		else {
			pNew.grid[this.row][this.column] = new Grass(pNew, this.row, this.column);
		}
		return pNew.grid[this.row][this.column];
	}
}
