package fifteen_puzzle_main;
import java.util.Random;


/*
 * MAP Dimensions
 *   0  1  2  3 (x)
 * 0
 * 1
 * 2
 * 3 
 * (y)
 * 
 * */

public class main_class
{
	final static int MAX_SIZE =4;
	final static int MAP[][] = {	//use for now
			{1,2,3,4},
			{5,6,7,8},
			{9,10,11,12},
			{13,14,15,0}
	};
	static Node map[][] = new Node[MAX_SIZE][MAX_SIZE];
	static Random rand = new Random();
	static boolean dev_mode = true;
	
	
	private static class Node
	{
		int value = -1;
		
		//for locations
		int x , y;
		int goal_x, goal_y;
		
		Node(int x_, int y_)
		{
			goal_x = x = x_; 
			goal_y = y = y_;
		}
	}
	
	public static void print(String s) {
		System.out.print(s);
	}
	public static void println(String s) {
		System.out.println(s);
	}
	
	public static void print_map()
	{
		boolean first_time =true;
		print("  ");
		
		for(int y=0;y< MAX_SIZE;y++)
		{
			if(!first_time)
			{
				print(y+ " ");
			}
			

			for(int x=0; x< MAX_SIZE; x++)
			{
				if(y==0 && first_time)
				{
					String pad = "  ";
					
					if(x>=10 && x <100)
						pad = " ";
					else if (x >=100 )
						pad="";
					
					print(pad+ x + " ");
					//here
					if(x== MAX_SIZE-1)
					{ 
						x=0; y= -1; first_time = false;
						break;
					}
				}
				else
				{
					int val= map[y][x].value;
					String pad = "  ";
					
					if(val>=10 && val <100)
						pad = " ";
					else if (val >=100 )
						pad="";
					print(pad+map[y][x].value+" ");
				}
			}
			println("");
			
			/*
			if(first_time)
			{ 
				 y= -1; first_time = false;
				print("");
			}*/
		}
	}
	
	public static void main(String[] args)
	{
		// Creating an array of nodes
		
		Node end; //the last node
		// [y][x]

		//creating the MAP with Nodes
		int val = 1;
		for(int y=0;y< MAX_SIZE;y++)
		{
			for(int x=0; x< MAX_SIZE; x++)
			{
				map[y][x]= new Node(x,y);//constructor assigns the locations and goals
				map[y][x].value = val%(MAX_SIZE*MAX_SIZE); //goes from 1-15 then 0
				//println("val: "+ val+ "\t"+ map[y][x].value);
				val++;
			}
		}
		end = map[MAX_SIZE-1][MAX_SIZE-1];
		//print("end is: "+ end.x + ", "+ end.y);
		if(dev_mode)
			print_map();
		
		// random placement
		for(int y=0;y< MAX_SIZE;y++)
		{
			for(int x=0; x< MAX_SIZE; x++)
			{
				int r_y = rand.nextInt(MAX_SIZE);
				int r_x = rand.nextInt(MAX_SIZE);
				
				//swaps 2 nodes
				if(true)//y== r_y && x==r_x)
				{
					//map[y][x].y = r_y;
					//map[y][x].x = r_x;
					//map[map[y][x].y][map[y][x].x];
					
					//swaps the values
					//int v= map[r_y][r_x].value;
					//map[r_y][r_x].value = map[y][x].value;
					//map[y][x].value = v;
					
					//swaps the nodes
					Node temp= map[r_y][r_x];
					map[r_y][r_x]= map[y][x];
					map[y][x] = temp;
					
					//map[r_y][r_x].y = y;
					//map[r_y][r_x].x = x;
				}
			}
		}
		if(dev_mode)
		{
			println("\n===================");
			print_map();
		}

	}
	
}
