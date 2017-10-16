package main.java.com.ticketService.WalmartTicket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)  
    {
    	
    	Start st = new Start();
    	Thread t = new Thread(st);
    	t.start();	
 }
}
