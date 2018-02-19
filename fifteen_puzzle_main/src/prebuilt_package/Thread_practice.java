package prebuilt_package;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  

public class Thread_practice implements Runnable
{
	String name="";
	int id= 0;
	String message= "";
	
	Thread_practice(String s, int i)
	{
		name=s; id=i;
	}
	Thread_practice(String s)
	{message = s;}
	
	/*
	public void run()
	{
		
		
		for(int i=1;i<5;i++)
		{  
		    try{Thread.sleep(1000);}catch(InterruptedException e){System.out.println(e);}  
		    System.out.println(""+ i+ " "+Thread.currentThread());  
		} 
		
		
		//System.out.println(name + " is running "+ Thread.currentThread());
	}*/
	

	public void run() {  
        System.out.println(Thread.currentThread().getName()+" (Start) message = "+message);  
        processmessage();//call processmessage method that sleeps the thread for 2 seconds  
        System.out.println(Thread.currentThread().getName()+" (End)");//prints thread name  
    }  
	
    private void processmessage() {  
        try {  Thread.sleep(2000);  } catch (InterruptedException e) { e.printStackTrace(); }  
    }  
	
}

