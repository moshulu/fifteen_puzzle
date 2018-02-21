package prebuilt_package;
import java.io.IOException;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
public class Practice_main
{
	
	public static void main(String args[]) throws IOException
	{
		
		/*
		Thread_practice obj1 = new Thread_practice("obj 1", 1);
		Thread t1 = new Thread(obj1, "1");
		Thread t2 = new Thread(obj1,"5");
		t1.run();
		t2.run();
		*/
		
		ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads  
        for (int i = 0; i < 10; i++) {  
            Runnable worker = new Thread_practice("" + i);  
            executor.execute(worker);//calling execute method of ExecutorService  
          }  
        executor.shutdown();  
        while (!executor.isTerminated()) {   }  
  
        System.out.println("Finished all threads");  
		
	}
	
	

}
