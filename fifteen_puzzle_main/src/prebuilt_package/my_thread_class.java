package prebuilt_package;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  

public class my_thread_class extends make_life_easier
{
	
	public static void main(String[] args)
	{
		int threadCount = 5;
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		
		FifteenPuzzleSolver fps = new FifteenPuzzleSolver(threadCount);
		Board board = Board.createBoard();
		
		println("Using MY " + threadCount + " threads to solve this board: \n" + board+"\n");
		
		long ts = System.currentTimeMillis();
		List<Board> solution = fps.solve(board);
		long elapsed = System.currentTimeMillis() - ts;
		
		println("Found a solution with " + solution.size() + " moves!");
		println("Elapsed time: " + ((double)elapsed) / 1000.0 + " seconds");
		for (int i=0;i<solution.size();i++) {
			println(""+solution.get(i));
		}
	}
	
}
