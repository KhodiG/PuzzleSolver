package a04_Puzzle;

import edu.princeton.cs.algs4.Stack;

public class Board {

	private int size;
	private int hamming;
	private int manhattan;
	private int[] tiles;
	private int zero;

	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		if (blocks == null)
			throw new java.lang.NullPointerException();

		size = blocks.length;

		tiles = new int[size * size];

		int tile = 0;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (blocks[i][j] == 0)
					zero = tile;
				tiles[tile++] = blocks[i][j];
			}
		}
	}
	
	// just added this for the visualizer class to work
	// returns value of tile at [row][col]
	public int getTile(int row, int col) {
		return tiles[row*size + col];
	}

	// board size N
	public int size() {
		return size;
	}

	// number of blocks out of place
	public int hamming() {
		if (hamming > 0)
			return hamming;

		hamming = 0;

		for (int i = 0; i < tiles.length; i++)
			if (tiles[i] != (i + 1) && tiles[i] != 0)
				hamming++;

		return hamming;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		if (manhattan > 0)
			return manhattan;

		manhattan = 0;

		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] == (i + 1) || i == zero)
				continue;
			manhattan += Math.abs((i / size) - ((tiles[i] - 1)) / size);
			manhattan += Math.abs((i % size) - ((tiles[i] - 1)) % size);
		}
		return manhattan;
	}

	// is this board the goal board?
	public boolean isGoal() {
		if (tiles.length - 1 != zero)
			return false;

		for (int i = 0; i < tiles.length - 1; i++)
			if (tiles[i] != (i + 1))
				return false;
		return true;
	}

	// is this board solvable?
	public boolean isSolvable() {
		int inversions = 0;

		for (int i = 0; i < tiles.length - 1; i++) {
			// Check if a larger number exists after the current
			// place in the array, if so increment inversions.
			for (int j = i + 1; j < tiles.length; j++)
				if (tiles[i] > tiles[j])
					inversions++;
			// Determine if the distance of the blank space from the bottom
			// right is even or odd, and increment inversions if it is odd.
			if (tiles[i] == 0 && i % 2 == 1)
				inversions++;
		}

		// If inversions is even, the puzzle is solvable.
		return (inversions % 2 == 0);
	}

	// does this board equal y?
	public boolean equals(Object y) {
		// Might as well check
		if (y == this)
			return true;
		// you ain't nothin', get outta' here
		if (y == null)
			return false;
		// check that they're the same Object
		if (this.getClass() != y.getClass())
			return false;

		// set a Board equal to given Object
		Board other = (Board) y;

		for (int i = 0; i < tiles.length; i++)
			if (this.tiles[i] != other.tiles[i])
				return false;
		return true;
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		Stack<Board> boards = new Stack<>();

		if (zero / size != 0)
			pushToStack(boards, -size);// up neighbor
		if (zero / size != size - 1)
			pushToStack(boards, size);// down neighbor
		if (zero % size != 0)
			pushToStack(boards, -1);// left neighbor
		if (zero % size != size - 1)
			pushToStack(boards, 1);// right neighbor

		return boards;
	}

	// Swap tiles on a board, clone it, add clone to stack then revert to original
	// board
	private void pushToStack(Stack<Board> board, int i) {
		swap(tiles, zero, zero + i);
		board.push(new Board(tiles, size, zero + i));
		swap(tiles, zero, zero + i);
	}

	// Swap two tiles in Array
	private void swap(int[] board, int i, int j) {
		int swap = board[i];
		board[i] = board[j];
		board[j] = swap;
	}

	// Create a new Board Object
	private Board(int[] block, int n, int cero) {
		this.size = n;
		this.zero = cero;
		tiles = new int[n * n];
		System.arraycopy(block, 0, tiles, 0, tiles.length);
	}

	// string representation of this board (in the output format specified
	// below)
        @Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(size + "\n");
		for (int i = 0; i < tiles.length; i++) {
			s.append(String.format("%2d ", tiles[i]));
			if ((i + 1) % size == 0)
				s.append("\n");
		}
		return s.toString();
	}
}