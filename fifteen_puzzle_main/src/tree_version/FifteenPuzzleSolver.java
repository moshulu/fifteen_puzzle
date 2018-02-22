package tree_version;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
/**
 * An implementation of a solver for the 15-puzzle.  This implementation uses an A* search strategy.
 * 
 * Unlike some pictures, I define a "solved" puzzle to have the empty slot in the upper-left corner.
 * 
 * TODO: concurrency support
 * 
 * @author andrew (orginal)
 * modified by Roberto (Rob) and Todd
 */
public class FifteenPuzzleSolver extends make_life_easier implements Runnable{

	public static void OLDmain(String [] args) {
		int threadCount = 1;
		
		//This is for calling the java file in the terminal (Rob)
		if (args.length > 0)
			threadCount = Integer.parseInt(args[0]);
		
		FifteenPuzzleSolver fps = new FifteenPuzzleSolver(threadCount);
		Board board = Board.createBoard();
		
		println("Using " + threadCount + " threads to solve this board: \n" + board+"\n");
		
		long ts = System.currentTimeMillis();
		List<Board> solution = fps.solve(board);
		long elapsed = System.currentTimeMillis() - ts;
		
		println("Found a solution with " + solution.size() + " moves!");
		println("Elapsed time: " + ((double)elapsed) / 1000.0 + " seconds");
		for (int i=0;i<solution.size();i++) {
			println(""+solution.get(i));
		}
		
	}
	
	@SuppressWarnings("static-access")
	public static void main(String [] args) {
		int threadCount = -1;
		int arbirtary_range = 10;
		
		//This is for calling the java file in the terminal (Rob)
		if (args.length > 0)
			threadCount = Integer.parseInt(args[0]);
		else
		{
			Scanner input = new Scanner(System.in);
			do {
				try {
					println("How many threads would you like to run? (1-"+arbirtary_range+")");
					int temp = input.nextInt();
					
					if(temp >=1 && temp<=arbirtary_range)
					{
						threadCount = temp;
						break;
					}
				}
				catch(Exception e)
				{
					error("\nInvalid input!\tPlease try again.");
					input = new Scanner(System.in);
				}
			}
			while(true);
		}
		
		FifteenPuzzleSolver fps = new FifteenPuzzleSolver(threadCount);
		Board board = Board.createBoard();
		board.solution=board.board;
		//Board start = new Board();
		board.currDepth.add(new Board());
		//board.check_hash(board.board);
		
		println("Using " + threadCount + " threads to solve this board: \n" + board+"\n");
		
		long ts = System.currentTimeMillis();
		//List<Board> solution = fps.solve(board);
		//board.run();
		board.run_one_thread();
		//board.test();
		long elapsed = System.currentTimeMillis() - ts;
		
		//println("Found a solution with " + solution.size() + " moves!");
		println("Found a solution with " + board.solvedDepth + " moves!");
		println("Elapsed time: " + ((double)elapsed) / 1000.0 + " seconds");
		
		if(board.winning_board != null)
			board.winner(board.winning_board);
		else
			println("No solution");
		
		/*
		for (int i=0;i<solution.size();i++) {
			println(""+solution.get(i));
		}*/
		
	}
	
	
	
	public FifteenPuzzleSolver(int threadCount) {
		if (threadCount > 1)
		{
			// this will change!!!
			//throw new RuntimeException("No support for multiple threads :(");	
			
			
			
			
			
		}
				
	}
	
	public void run()
	{
		//solve();
	}

	
	public List<Board> solve (Board board) {
				
		int maxDepth = board.minimumSolutionDepth();
		
		// note: program searches forever.  At each iteration, it searchers for solutions that
		// have an increasing number of maximum moves (as reflected in maxDepth).
		while (true) {
			List<Board> solution = doSolve(board,0,maxDepth);
			
			if (solution != null) {
				//println("counter: "+board.counter);
				return solution;
			}
			else {
				maxDepth++; // search again, with a larger maxDepth
			}
		}
	}
	
	public static void quick_print(String s, int c, int m)
	{
		println(s+" cDepth: "+ c+ "\tmaxDepth: "+ m);
	}
	
	
	/**
	 * Look for solution with up to maxDepth moves
	 * 
	 * @param board: The board to be solved
	 * @param solution: The list of moves so far
	 * @param currentDepth: The number of moves so far
	 * @param maxDepth: The maximum number of moves before we quit.
	 * 
	 * @return A valid solution (sequence of boards) or null to indicate failure
	 */
	private List<Board> doSolve(Board board, int currentDepth, int maxDepth) {
		if (board.isSolved()) {
			
			List<Board> list = new LinkedList<Board>();
			list.add(board);
			return list;
		}
		
		// stop searching if we can't solve the puzzle within our maximum depth allotment
		if ((currentDepth + board.minimumSolutionDepth()) > maxDepth)
		{
			//quick_print("Returning null. ", currentDepth, maxDepth);
			return null;
		}
			
		
		// search for neighboring moves...
		List<Board> nextMoves = board.OLDgenerateSuccessors();
		for (Board nextBoard : nextMoves) {
			List<Board> solution = doSolve(nextBoard,currentDepth+1,maxDepth);
			if (solution != null) {
				// prepend this board to the solution, and return
				solution.add(0,board);
				//quick_print("Found solution! @ cDepth: ", currentDepth, maxDepth);
				return solution;
			}
		}
		
		// no successor moves were fruitful
		return null;
	}
}

