package fifteen_puzzle_main;

public class Node
{
	int value = -1;
	int g_cost = 0; // cost to start node
	int h_cost = 0; // estunated cost to goal's location
	int f_cost = 0; // f = g+h
	
	//for locations
	int x , y;
	int goal_x, goal_y;
	
	Node(int x_, int y_)
	{
		goal_x = x = x_; 
		goal_y = y = y_;
	}
	
	boolean atGoal(int y_, int x_)
	{
		if(goal_x == x_ && goal_y ==y)
			return true;
		
		//if(MAP[y][x] == value)
		//		return true;
		
		return false;
	}
}
