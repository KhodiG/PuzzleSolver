package a04_Puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdOut;

public class Solver {

	private Stack<Board> solutionBoard;
	private int moves;

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		if (initial.isSolvable() == false)
			throw new java.lang.IllegalArgumentException();
		solutionBoard = new Stack<>();

		MinPQ<SearchNode> q = new MinPQ<>();
		q.insert(new SearchNode(initial, 0, null));
		while (true) {
			SearchNode move = q.delMin();
			// goal has been reached, populate fields of interest and return
			if (move.board.isGoal()) {
				this.moves = move.moves;
				do {
					solutionBoard.push(move.board);
					move = move.previous;
				} while (move != null);
				// done solving
				return;
			}
			for (Board next : move.board.neighbors()) {
				// look back one move to prevent useless looping
				if (move.previous == null || !next.equals(move.previous.board))
					q.insert(new SearchNode(next, move.moves + 1, move));
			}
		}
	}

	private class SearchNode implements Comparable<SearchNode> {

		private Board board;
		private int moves;
		private SearchNode previous;

		public SearchNode(Board b, int m, SearchNode p) {
			this.board = b;
			this.moves = m;
			this.previous = p;
		}

		@Override
		public int compareTo(SearchNode arg0) {
			int difference = this.board.manhattan() + this.moves - arg0.board.manhattan() - arg0.moves;
			if (difference != 0)
				// return normal difference of priority functions
				return difference;
			if (this.moves > arg0.moves)
				// if priority is the same give preference to the one with more moves
				return -1;
			return 1;
		}

	}

	// min number of moves to solve initial board
	public int moves() {
		return moves;
	}

	// sequence of boards in a shortest solution
	public Iterable<Board> solution() {
		return solutionBoard;
	}

	// Made this into a method so I could test all the puzzles easier
	public static void solve(String file) {
		// create initial board from file
		In in = new In(file);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// check if puzzle is solvable; if so, solve it and output solution
		if (initial.isSolvable()) {
			Solver solver = new Solver(initial);
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}

		// if not, report unsolvable
		else {
			StdOut.println("Unsolvable puzzle");
		}
	}

	// TESTING
//	public static void main(String[] args) {
//		// Loops through and solves puzzles 11-50 from the ftp directory
//		// for (int i = 11; i <= 50; i++) {
//		// solve("src/a04_Puzzle.Puzzles/puzzle" + i + ".txt");
//		// }
//		solve("src/puzzle11.txt");
//	}
}