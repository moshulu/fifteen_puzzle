package tree_version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * This class represents the board for the fifteen-tile puzzle.
 * 
 * @author andrew (original)
 * modified by Roberto (Rob) and Todd
 *
 */
public class Board extends make_life_easier implements Runnable{

	// use a fixed random number seed, for reproducibility
	private static final Random random = new Random(11023);
	private static final int INITIALIZATION_MOVES = 55;
	public static long counter=0;
	//private final int[] board;
	public int[] board;
	public static int[] solution;// the solution board
	
	public Board parent=null;
	public Board[] children= new Board[4];//public Board left, up, down, right;
	//public ArrayList<Board> children_again = new ArrayList<Board>(); // Program does not like this. Too much for each object?
	public static Board winning_board=null;
	public int current_depth= 1;
	public static final int LEFT=0, UP=1, RIGHT=2, DOWN=3;
	
	public static boolean solved =false;
	public static int solvedDepth=Integer.MAX_VALUE;//arbitrary number
	
	public static ConcurrentLinkedQueue<Board> currDepth = new ConcurrentLinkedQueue<Board>();
	public static ConcurrentLinkedQueue<Board> nextDepth = new ConcurrentLinkedQueue<Board>();
	public static int cDepth= 0;//counter for debug tracking
	public int empty_index= -1;
	//public static Object[][][][][][][] history;
	
	/**
	 * Create a randomized initial board
	 */
	/*public static Board createBoard() {
		Board b = new Board();
		for (int i = 0;i<INITIALIZATION_MOVES;i++) {
			List<Board> nextBoards = b.OLDgenerateSuccessors();
			
			int indx = random.nextInt(nextBoards.size());
			b = nextBoards.get(indx);
		}
		
		return b;		
	}*/
	
	public static Object[] test_list;
	public static HashSet<Integer[]> viewed= new HashSet<Integer[]>(10000000,0.5f);
	
	public static Board createBoard() {
		Board b = new Board();
		for (int i = 0;i<INITIALIZATION_MOVES;i++) {
			List<Board> nextBoards = b.OLDgenerateSuccessors();
			
			int indx = random.nextInt(nextBoards.size());
			b = nextBoards.get(indx);
		}
		b.empty_index= b.getEmptyIndex();
		//b.solution = b.board;
		return b;
	}
	
	public boolean check_hash(int[] b)
	{
		Integer[] x = new Integer[b.length];
		
		for(int a=0; a<b.length;a++)
		{
			x[a]= new Integer(b[a]);
		}
		
		if(!viewed.contains(x))
		{
			/*Integer[] x = new Integer[b.length];
			
			for(int a=0; a<b.length;a++)
			{
				x[a]= new Integer(b[a]);
			}*/
			
			viewed.add(x);
			return true;
		}
		return false;
	}
	
	
	public void test(){
		//test_list= new Object[16];
		Object[][][][][][][] test_me= new Object[16][15][14][13][12][11][10];
		println("Running test: ");
		//long ts = System.currentTimeMillis();
		long counter =0;
		for(int a=0; a< 16;a++)
		{
			//if(test_list[a]==null)
				//test_list[a]= new Object[15];
			
			for(int b=0; b< 15;b++)
			{
				//if(test_list[a][b]==null)
					//test_list[a][b]= new Object[15];
				for(int c=0; c< 14;c++)
				{
					for(int d=0; d< 13;d++)
					{
						for(int e=0; e< 12;e++)
						{
							for(int f=0; f< 11;f++)
							{
								for(int g=0; g< 10;g++)
								{
									test_me[a][b][c][d][e][f][g]= new Boolean(false);
									counter++;
									
									
								}
							}
						}
					}
				}
			}
		}
			
		//long elapsed = System.currentTimeMillis() - ts;
		//sprintln("Elapsed time: " + ((double)elapsed) / 1000.0 + " seconds");
		println("Finished test!: "+ counter);
	}
	
	// initialize a solved board
	public Board () {
		board = new int[16] ;
	
		for (int i=0;i<16;i++)
			board[i]= i;
		
		empty_index = 0;
		//children = new Board[4];
		//counter++;
	}

	private Board (int [] b) {
		this.board = b;
		this.empty_index = this.getEmptyIndex();
		//counter++;
	}
	
	public void set(int depth, Board p) {
		current_depth = depth;
		parent = p;
	}
		
	//this is redundant, but it may help reduce some time when X threads are still creating children while the solution
	// has already been found.
	public void createChildren()
	{
		for(int i=0;i<4; i++)
		{
			// done to prevent looking and creating unnecessary children if a shorter solution is found
			if(!done_yet())//!solved && (current_depth < solvedDepth))
			{
				// x is i%4	(remainder) where x and y ranges from 0-3
				// y is i/4	(quotient)
				//children[i] = null;
				//int empty_index= this.getEmptyIndex();	//(commented out)
				int target = -1;
				
				if(i==LEFT )//&& isValidIndex(i-1))//<--- this called in tryToAddMove()
				{
					//tryToAddMove(int emptyIndex, int targetIndex, Board parent)
					children[i]=tryToAddMove(empty_index, empty_index-1, this);
					if(children[i]!=null)
						children[i].empty_index = this.empty_index-1;
				}
				else if(i==UP )//&& isValidIndex(i-4))
				{
					children[i]=tryToAddMove(empty_index, empty_index-4, this);
					if(children[i]!=null)
						children[i].empty_index = this.empty_index-4;
				}
				else if(i==RIGHT )//&& isValidIndex(i+1))
				{
					children[i]=tryToAddMove(empty_index, empty_index+1, this);
					if(children[i]!=null)
						children[i].empty_index = this.empty_index+1;
				}
				else if(i==DOWN )//&& isValidIndex(i-1))
				{
					children[i]=tryToAddMove(empty_index, empty_index+4, this);
					if(children[i]!=null)
						children[i].empty_index = this.empty_index+4;
				}
				else
				{
					error("Ran into an issue where i is an invalid move!");
					return;
				}
				
				//children[i]=tryToAddMove(empty_index, target, this);
				
				if(children[i]!=null)
				{
					nextDepth.add(children[i]);
				}
					
			}
		}	
	}
	
	public void createChildren(boolean fake)
	{
			// done to prevent looking and creating unnecessary children if a shorter solution is found
		if(!done_yet())//!solved && (current_depth < solvedDepth))
		{
			// x is i%4	(remainder) where x and y ranges from 0-3
			// y is i/4	(quotient)
			//children[i] = null;
			//int empty_index= this.getEmptyIndex();

				//tryToAddMove(int emptyIndex, int targetIndex, Board parent)
				children[LEFT]=tryToAddMove(empty_index, empty_index-1, this);
				if(children[LEFT]!= null)
				{
					//if(check_hash(children[LEFT].board))
						nextDepth.add(children[LEFT]);
						children[LEFT].empty_index = this.empty_index-1;
				}
					

				children[UP]=tryToAddMove(empty_index, empty_index-4, this);
				if(children[UP]!= null)
				{
					//if(check_hash(children[UP].board))
						nextDepth.add(children[UP]);
						children[UP].empty_index = this.empty_index-4;
				}
					
				
				children[RIGHT]=tryToAddMove(empty_index, empty_index+1, this);
				if(children[RIGHT]!= null)
				{
					//if(check_hash(children[RIGHT].board))
						nextDepth.add(children[RIGHT]);
						children[RIGHT].empty_index = this.empty_index+1;
				}
					
				
				children[DOWN]=tryToAddMove(empty_index, empty_index+4, this);
				if(children[DOWN]!= null)
				{
					//if(check_hash(children[DOWN].board))
						nextDepth.add(children[DOWN]);
						children[DOWN].empty_index = this.empty_index+4;
				}
		}
	}
	
	public void createChildren(int i)
	{
		if(!done_yet())
		{
			Board temp= null;
			
			//Left
			temp=tryToAddMove(empty_index, empty_index-1, this);
			if(temp!= null)
			{
				//if(check_hash(children[LEFT].board))
					nextDepth.add(children[LEFT]);
					children[LEFT].empty_index = this.empty_index-1;
			}
				

			children[UP]=tryToAddMove(empty_index, empty_index-4, this);
			if(children[UP]!= null)
			{
				//if(check_hash(children[UP].board))
					nextDepth.add(children[UP]);
					children[UP].empty_index = this.empty_index-4;
			}
				
			
			children[RIGHT]=tryToAddMove(empty_index, empty_index+1, this);
			if(children[RIGHT]!= null)
			{
				//if(check_hash(children[RIGHT].board))
					nextDepth.add(children[RIGHT]);
					children[RIGHT].empty_index = this.empty_index+1;
			}
				
			
			children[DOWN]=tryToAddMove(empty_index, empty_index+4, this);
			if(children[DOWN]!= null)
			{
				//if(check_hash(children[DOWN].board))
					nextDepth.add(children[DOWN]);
					children[DOWN].empty_index = this.empty_index+4;
			}
		}
	}
	
	
	public void run()
	{
		//potential issue when X threads are removing from currDepth, but it's still being created...
		//print("Size of curreDepth: "+ currDepth.size()+"\n");
		if(!currDepth.isEmpty())
		{
			try
			{
				Board temp = currDepth.remove();
				
				//for checking
				if(cDepth >=17)
				{
					//println(Thread.currentThread().getName());
					//print_board();//println(this.board);
				}
				
				
				if(Arrays.equals(temp.board, solution))
				{
					solved = true;
					solvedDepth= temp.current_depth;
					winning_board = temp;
					return;
				}
				
				temp.createChildren(true);
				
			}
			catch(Exception e)
			{
				error("Something bad happened:  "+ Thread.currentThread().getName()+ "\n"+e.toString()+
						"\n\t"+e.getStackTrace());
			}
			
		}
		else
		{
			//going to make the nextDepth as currDepth
			//println("["+cDepth+"]C-Depth: "+ currDepth.size()+"\tNext depth: "+ nextDepth.size());
			
			currDepth= nextDepth;
			cDepth++;
			nextDepth= new ConcurrentLinkedQueue<Board>();
			
			if(cDepth==1)
			{
				int a = Thread.activeCount();
				
				//Main thread counts as one
				if(a>1)
					a--;
				println("Threads: "+ a+"\n");
			}
				
			println("[ "+cDepth+" ]: "+  currDepth.size());
			//println("Depth: "+ cDepth);
		}
	}
	
	public void run_one_thread()
	{
		while(solved==false)//current_depth < 100)
		{
			run();
		}
	}
	
	
	
	public boolean done_yet()
	{
		return (solved || (current_depth >= solvedDepth));
	}
	
	//simply prints what the winning board is
	//n will first start as winning_board
	//uses recursion to print up the 'tree'
	public void winner(Board n)
	{
		if(n.parent == null)
		{
			println("\n==== " + n.current_depth+" ===============");
			n.print_board();
			return;
		}
		
		
		println("\n==== " + n.current_depth+" ===============");
		n.print_board();
		winner(n.parent);
		
		
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
	public List<Board> OLDgenerateSuccessors() {
		List<Board> list = new ArrayList<Board>();
		int emptyIndex = this.getEmptyIndex();
		
		//println("Current Board:");
		//print(toString());
		
		// above
		//println("Above");
		OLDtryToAddMove(list,emptyIndex,emptyIndex - 4);
		
		// below
		//println("Below");
		OLDtryToAddMove(list,emptyIndex,emptyIndex + 4);
		
		// left
		//println("Left");
		if ( (emptyIndex % 4) != 0)
			OLDtryToAddMove(list,emptyIndex,emptyIndex - 1);
		
		// right
		//println("Right");
		if ( (emptyIndex % 4) != 3)
			OLDtryToAddMove(list,emptyIndex,emptyIndex  + 1);
		
		// (Rob) when they call the tryToAddMove(), the list variable gets updated!
		
		return list;
	}

	//my method
	/*
	public void generateSuccessors() {
		int emptyIndex = this.getEmptyIndex();
		
		//println("Current Board:");
		//print(toString());
		
		// above
		//println("Above");
		up= tryToAddMove(emptyIndex,emptyIndex - 4, parent);
		
		// below
		//println("Below");
		down=tryToAddMove(emptyIndex,emptyIndex + 4, parent);
		
		// left
		//println("Left");
		if ( (emptyIndex % 4) != 0)
			left=tryToAddMove(emptyIndex,emptyIndex - 1, parent);
		else
			left=null;
		
		// right
		//println("Right");
		if ( (emptyIndex % 4) != 3)
			right=tryToAddMove(emptyIndex,emptyIndex  + 1, parent);
		else
			right=null;

	}*/
	
	//old method
	private  final void OLDtryToAddMove(List<Board> list, int emptyIndex, int targetIndex) {
		if (! isValidIndex(targetIndex))
			return;
		int [] copy = new int[16];
		System.arraycopy (this.board,0,copy,0,16);
		
		copy[emptyIndex] = copy[targetIndex];
		copy[targetIndex] = 0;
		
		//println(copy.toString()); (Rob) testing!!
		for (int i=0;i<copy.length;i++) {
			//print(copy[i] + "  ");
			
			if ( (i % 4) == 3)
				;
				//print("\n");
		}
		list.add(new Board(copy));
	}
	
	private  final Board tryToAddMove(int emptyIndex, int targetIndex, Board p) {
		if (! isValidIndex(targetIndex))
			return null;
		int [] copy = new int[16];
		System.arraycopy (this.board,0,copy,0,16);
		
		copy[emptyIndex] = copy[targetIndex];
		copy[targetIndex] = 0;
		
		//println(copy.toString()); (Rob) testing!!
		/*for (int i=0;i<copy.length;i++) {
			//print(copy[i] + "  ");
			
			if ( (i % 4) == 3)
				;
				//print("\n");
		}*/
		Board child = new Board(copy);
		child.set(current_depth +1, p); // this is where the depth is incremented!
		
		//basically checks if this child has just been done. 
		//This helps with overall performance
		if(child.parent.parent != null)
		{
			if(Arrays.equals(child.board, child.parent.parent.board))
				return null;
		}
		
		return (child);
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
		String padding = " ";
		for (int i=0;i<board.length;i++) {
			print(board[i]+" ");
			if(board[i]<10)
				print(" ");
			
			if ( (i % 4) == 3)
				print("\n");
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

