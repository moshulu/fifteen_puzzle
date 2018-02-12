package fifteen_puzzle_main;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Illustrates how to solve the fifteen puzzle using Dijkstra's algorithm and A*.
 * Haven't really written any java in at least 5 years, so apologies for sloppiness.
 * from https://gist.github.com/leopd/5992493 
 * @author leodirac
 *
 */
public class damn {

  private class TilePos {
		public int x;
		public int y;
		
		public TilePos(int x, int y) {
			this.x=x;
			this.y=y;
		}
		
	}
	
	public final static int DIMS=4;
	private int[][] tiles;
	private int display_width;
	private TilePos blank;
	
	public damn() {
		tiles = new int[DIMS][DIMS];
		int cnt=1;
		for(int i=0; i<DIMS; i++) {
			for(int j=0; j<DIMS; j++) {
				tiles[i][j]=cnt;
				cnt++;
			}
		}
		display_width=Integer.toString(cnt).length();
		
		// init blank
		blank = new TilePos(DIMS-1,DIMS-1);
		tiles[blank.x][blank.y]=0;
	}
	
	public final static damn SOLVED=new damn();
	
	
	public damn(damn toClone) {
		this();  // chain to basic init
		for(TilePos p: allTilePos()) { 
			tiles[p.x][p.y] = toClone.tile(p);
		}
		blank = toClone.getBlank();
	}

	public List<TilePos> allTilePos() {
		ArrayList<TilePos> out = new ArrayList<TilePos>();
		for(int i=0; i<DIMS; i++) {
			for(int j=0; j<DIMS; j++) {
				out.add(new TilePos(i,j));
			}
		}
		return out;
	}

	
	public int tile(TilePos p) {
		return tiles[p.x][p.y];
	}
	
	
	public TilePos getBlank() {
		return blank;
	}
	
	
	public TilePos whereIs(int x) {
		for(TilePos p: allTilePos()) { 
			if( tile(p) == x ) {
				return p;
			}
		}
		return null;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof damn) {
			for(TilePos p: allTilePos()) { 
				if( this.tile(p) != ((damn) o).tile(p)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	
	@Override 
	public int hashCode() {
		int out=0;
		for(TilePos p: allTilePos()) {
			out= (out*DIMS*DIMS) + this.tile(p);
		}
		return out;
	}
	
	
	public void show() {
		System.out.println("-----------------");
		for(int i=0; i<DIMS; i++) {
			System.out.print("| ");
			for(int j=0; j<DIMS; j++) {
				int n = tiles[i][j];
				String s;
				if( n>0) {
					s = Integer.toString(n);	
				} else {
					s = "";
				}
				while( s.length() < display_width ) {
					s += " ";
				}
				System.out.print(s + "| ");
			}
			System.out.print("\n");
		}
		System.out.print("-----------------\n\n");
	}
	
	
	public List<TilePos> allValidMoves() {
		ArrayList<TilePos> out = new ArrayList<TilePos>();
		for(int dx=-1; dx<2; dx++) {
			for(int dy=-1; dy<2; dy++) {
				TilePos tp = new TilePos(blank.x + dx, blank.y + dy);
				if( isValidMove(tp) ) {
					out.add(tp);
				}
			}
		}
		return out;
	}
	
	
	public boolean isValidMove(TilePos p) {
		if( ( p.x < 0) || (p.x >= DIMS) ) {
			return false;
		}
		if( ( p.y < 0) || (p.y >= DIMS) ) {
			return false;
		}
		int dx = blank.x - p.x;
		int dy = blank.y - p.y;
		if( (Math.abs(dx) + Math.abs(dy) != 1 ) || (dx*dy != 0) ) {
			return false;
		}
		return true;
	}
	
	
	public void move(TilePos p) {
		if( !isValidMove(p) ) {
			throw new RuntimeException("Invalid move");
		}
		assert tiles[blank.x][blank.y]==0;
		tiles[blank.x][blank.y] = tiles[p.x][p.y];
		tiles[p.x][p.y]=0;
		blank = p;
	}
	
	
	/**
	 * returns a new puzzle with the move applied
	 * @param p
	 * @return
	 */
	public damn moveClone(TilePos p) {
		damn out = new damn(this);
		out.move(p);
		return out;
	}

	
	public void shuffle(int howmany) {
		for(int i=0; i<howmany; i++) {
			List<TilePos> possible = allValidMoves();
			int which =  (int) (Math.random() * possible.size());
			TilePos move = possible.get(which);
			this.move(move);
		}
	}

	
	public void shuffle() {
		shuffle(DIMS*DIMS*DIMS*DIMS*DIMS);
	}

	
	public int numberMisplacedTiles() {
		int wrong=0;
		for(int i=0; i<DIMS; i++) {
			for(int j=0; j<DIMS; j++) {
				if( (tiles[i][j] >0) && ( tiles[i][j] != SOLVED.tiles[i][j] ) ){
					wrong++;
				}
			}
		}
		return wrong;
	}
	
	
	public boolean isSolved() {
		return numberMisplacedTiles() == 0;
	}
	
	
	/**
	 * another A* heuristic.
	 * Total manhattan distance (L1 norm) from each non-blank tile to its correct position
	 * @return
	 */
	public int manhattanDistance() {
		int sum=0;
		for(TilePos p: allTilePos()) {
			int val = tile(p);
			if( val > 0 ) {
				TilePos correct = SOLVED.whereIs(val);
				sum += Math.abs( correct.x = p.x );
				sum += Math.abs( correct.y = p.y );
			}
		}
		return sum;
	}

	/**
	 * distance heuristic for A*
	 * @return
	 */
	public int estimateError() {
		return this.numberMisplacedTiles();
		//return 5*this.numberMisplacedTiles(); // finds a non-optimal solution faster
		//return this.manhattanDistance();
	}
	
	
	public List<damn> allAdjacentPuzzles() {
		ArrayList<damn> out = new ArrayList<damn>();
		for( TilePos move: allValidMoves() ) {
			out.add( moveClone(move) );
		}
		return out;
	}
	
	/**
	 * returns a list of boards if it was able to solve it, or else null
	 * @return
	 */
	public List<damn> dijkstraSolve() {
	  	Queue<damn> toVisit = new LinkedList<damn>();
	  	HashMap<damn,damn> predecessor = new HashMap<damn,damn>();
	  	toVisit.add(this);
	  	predecessor.put(this, null);
	  	int cnt=0;
	  	while( toVisit.size() > 0) {
	  		damn candidate = toVisit.remove();
	  		cnt++;
	  		if( cnt % 10000 == 0) {
	  			System.out.printf("Considered %,d positions. Queue = %,d\n", cnt, toVisit.size());
	  		}
	  		if( candidate.isSolved() ) {
	  			System.out.printf("Solution considered %d boards\n", cnt);
	  			LinkedList<damn> solution = new LinkedList<damn>();
	  			damn backtrace=candidate;
	  			while( backtrace != null ) {
	  				solution.addFirst(backtrace);
	  				backtrace = predecessor.get(backtrace);
	  			}
	  			return solution;
	  		}
	  		for(damn fp: candidate.allAdjacentPuzzles()) {
	  			if( !predecessor.containsKey(fp) ) {
	  				predecessor.put(fp,candidate);
	  				toVisit.add(fp);
	  			}
	  		}
	  	}
	  	return null;
	}
	
	
	
	/**
	 * returns a list of boards if it was able to solve it, or else null
	 * @return
	 */
	public List<damn> aStarSolve() {
	  	HashMap<damn,damn> predecessor = new HashMap<damn,damn>();
	  	HashMap<damn,Integer> depth = new HashMap<damn,Integer>();
	  	final HashMap<damn,Integer> score = new HashMap<damn,Integer>();
	  	Comparator<damn> comparator = new Comparator<damn>() {
	  		@Override
	  		public int compare(damn a, damn b) {
	  			return score.get(a) - score.get(b);
	  		}
	  	};
	  	PriorityQueue<damn> toVisit = new PriorityQueue<damn>(10000,comparator);

	  	predecessor.put(this, null);
	  	depth.put(this,0);
	  	score.put(this, this.estimateError());
	  	toVisit.add(this);
	  	int cnt=0;
	  	while( toVisit.size() > 0) {
	  		damn candidate = toVisit.remove();
	  		cnt++;
	  		if( cnt % 10000 == 0) {
	  			System.out.printf("Considered %,d positions. Queue = %,d\n", cnt, toVisit.size());
	  		}
	  		if( candidate.isSolved() ) {
	  			System.out.printf("Solution considered %d boards\n", cnt);
	  			LinkedList<damn> solution = new LinkedList<damn>();
	  			damn backtrace=candidate;
	  			while( backtrace != null ) {
	  				solution.addFirst(backtrace);
	  				backtrace = predecessor.get(backtrace);
	  			}
	  			return solution;
	  		}
	  		for(damn fp: candidate.allAdjacentPuzzles()) {
	  			if( !predecessor.containsKey(fp) ) {
	  				predecessor.put(fp,candidate);
	  				depth.put(fp, depth.get(candidate)+1);
	  				int estimate = fp.estimateError();
					score.put(fp, depth.get(candidate)+1 + estimate);
	  				// dont' add to p-queue until the metadata is in place that the comparator needs
	  				toVisit.add(fp);
	  			}
	  		}
	  	}
	  	return null;
	}
	
	private static void showSolution(List<damn> solution) {
		if (solution != null ) {
			System.out.printf("Success!  Solution with %d moves:\n", solution.size());
			for( damn sp: solution) {
				sp.show();
			}
		} else {
			System.out.println("Did not solve. :(");			
		}
	}
	
	
	public static void main(String[] args) {
		damn p = new damn();
		p.shuffle(70);  // Number of shuffles is critical -- large numbers (100+) and 4x4 puzzle is hard even for A*.
		System.out.println("Shuffled board:");
		p.show();
		
		List<damn> solution;

		System.out.println("Solving with A*");
		solution = p.aStarSolve();
		showSolution(solution);

		System.out.println("Solving with Dijkstra");
		solution = p.dijkstraSolve();
		showSolution(solution);
	}

}