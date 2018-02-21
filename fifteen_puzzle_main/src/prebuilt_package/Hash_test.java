package prebuilt_package;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Hash_test extends make_life_easier
{
	
	public static void main(String[] args)
	{
		//Hashtable<String, Integer> x = new Hashtable<String, Integer>(); 
		ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
		map.put("5-12-0-14", 4);
		map.put("5-12-1-14", 3);
		//x.computeIfAbsent//("5-12-0-14", 1);
		if(!map.containsKey("5-12-0-14"))
			map.put("5-12-0-14", 1);
		
		print(map.toString());
		
		
	}
	
}
