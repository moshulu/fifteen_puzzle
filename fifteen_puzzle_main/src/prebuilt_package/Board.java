package prebuilt_package;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This class represents the board for the fifteen-tile puzzle.
 * 
 * @author andrew (original)
 * modified by Roberto (Rob) and Todd
 *
 */
public class Board extends make_life_easier{

	// use a fixed random number seed, for reproducibility
	private static final Random random = new Random(11023);
	private static final int INITIALIZATION_MOVES = 55;
	
	private final int[] board;
	
	/**
	 * Create a randomized initial board
	 */
	public static Board createBoard() {
		Board b = new Board();
		for (int i = 0;i<INITIALIZATION_MOVES;i++) {
			List<Board> nextBoards = b.generateSuccessors();
			
			int indx = random.nextInt(nextBoards.size());
			b = nextBoards.get(indx);
		}
		
		return b;		
	}
	
	// initialize a solved board
	private Board () {
		board = new int[16] ;
	
		for (int i=0;i<16;i++)
			board[i]= i;
	}

	
	private Board (int [] b) {
		this.board = b;
	}
	
	private static final boolean isValidIndex(int i) {
		return ((i>=0) && (i < 16));
	}
	
	private int getEmptyIndex() {
		// find the empty square
		int emptyIndex = -1;
		for (int i=0;i<16;i++) {
			if (board[i] == 0) {
				emptyIndex = i;
				break;
			}
		}
		
		assert (isValidIndex(emptyIndex)); // (Rob) this is basically a test and/or a simplified version of a try/catch
		return emptyIndex;				// but it is not used for catching exceptions!!
	}
	
	// generate all valid board configurations within one move of this board
	public List<Board> generateSuccessors() {
		List<Board> list = new ArrayList<Board>();
		int emptyIndex = this.getEmptyIndex();
		
		println("Current Board:");
		print(toString());
		
		// above
		println("Above");
		tryToAddMove(list,emptyIndex,emptyIndex - 4);
		
		// below
		println("Below");
		tryToAddMove(list,emptyIndex,emptyIndex + 4);
		
		// left
		println("Left");
		if ( (emptyIndex % 4) != 0)
			tryToAddMove(list,emptyIndex,emptyIndex - 1);
		
		// right
		println("Right");
		if ( (emptyIndex % 4) != 3)
			tryToAddMove(list,emptyIndex,emptyIndex  + 1);
		
		// (Rob) when they call the tryToAddMove(), the list variable gets updated!
		return list;
	}

	private  final void tryToAddMove(List<Board> list, int emptyIndex, int targetIndex) {
		if (! isValidIndex(targetIndex))
			return;
		int [] copy = new int[16];
		System.arraycopy (this.board,0,copy,0,16);
		
		copy[emptyIndex] = copy[targetIndex];
		copy[targetIndex] = 0;
		
		//println(copy.toString()); (Rob) testing!!
		for (int i=0;i<copy.length;i++) {
			print(copy[i] + "  ");
			
			if ( (i % 4) == 3)
				print("\n");
		}
		list.add(new Board(copy));
	}
	
	public boolean isSolved() {
		for (int i=0;i<16;i++) {
			if (board[i] != i)
				return false;
		}
		
		return true;				
	}

	/**
	 * Returns the minimum number of moves that could result in a solution for this board
	 * We use simply "manhattan distance" for this.
	 */
	public int minimumSolutionDepth() {
		int emptyIndex = this.getEmptyIndex();
		int row = emptyIndex / 4;
		int col = emptyIndex % 4;
		
		return row + col;
	}
	
	public void print_board()
	{
		for (int i=0;i<board.length;i++) {
			print(board[i] + " ");
			
			if ( (i % 4) == 3)
				println("\n");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<board.length;i++) {
			sb.append(board[i] + " ");
			
			if ( (i % 4) == 3)
				sb.append("\n");
		}
		
		return sb.toString();
	}
}

