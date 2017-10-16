package main.java.com.ticketService.WalmartTicket;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import com.walmart.model.SeatHeldByCustomer;
import com.walmart.model.Theater;
import com.walmart.service.TicketService;
import com.walmart.serviceImpl.TicketServiceImpl;
import com.walmart.util.ValidationUtil;

public class Start implements Runnable {
	
	private static ReentrantLock l = new ReentrantLock();

	public void run() {
		if(l.tryLock()) {
		Scanner sc = new Scanner(System.in);
		System.out.println("\t\t\t TicketService");
		System.out.println("\t\t\t ====================");
		boolean loop = true;
		String options = "\nOptions: \t1. Available Seats \t2. Request for Hold \t3. Reserve/commit \t4. Exit.";
		Theater v = Theater.getInstance();
		int rows = v.getRows();
		int seatsProw = v.getCountOnRow();
		TicketService service = new TicketServiceImpl(v);
		System.out.println("System started with + rows +  rows &  + seatsProw +  seats per row venue! (Expiration seconds is set to 100 secs.))");
		while(loop) {
			System.out.println(options);
			String str = sc.next();
			boolean isvalidInput = ValidationUtil.isValidNo(str);
			if(!isvalidInput){
				System.out.println("Select only numbers.");
				continue;
			}
			int input = Integer.parseInt(str);
			switch(input){
			case 1:
				System.out.println("\nNo of seats available now: " + service.numSeatsAvailable());
				break;
			case 2:
				System.out.println("How many seats for hold?");
				String xs = sc.next();
				boolean isvalidSeat = ValidationUtil.isValidNo(xs);
				if(!isvalidSeat){
					while(!isvalidSeat){
						System.out.println("Invalid seat no.");
						System.out.println("Enter valid no:");
						xs = sc.next();
						isvalidSeat = ValidationUtil.isValidNo(xs);
					}
				}
				int seats = Integer.parseInt(xs);
				System.out.println("Customer email?");
				String email = sc.next();
				boolean isvalid = ValidationUtil.isValidEmail(email);
				if(!isvalid){
					while(!isvalid){
						System.out.println("Invalid email pattern.");
						System.out.println("Enter valid email:");
						email = sc.next();
						isvalid = ValidationUtil.isValidEmail(email);
					}
				}
				SeatHeldByCustomer hold = service.findAndHoldSeats(seats, email);
				if(hold!=null){
					System.out.println("\n" + seats + " held!\n" + hold);
				}else{
					System.out.println("\nYour request has been failed! Please try again!");
				}
				break;
			case 3:
				System.out.println("SeatHold Id?");
				String x = sc.next();
				boolean isvalidDigit = ValidationUtil.isValidNo(x);
				if(!isvalidDigit){
					while(!isvalidDigit){
						System.out.println("Invalid number.");
						System.out.println("Enter valid number:");
						x = sc.next();
						isvalidDigit = ValidationUtil.isValidNo(x);
					}
				}
				int id = Integer.parseInt(x);
				System.out.println("Please enter the email ID with whcih you registered in?");
				String cust = sc.next();
				boolean isvalidEmail = ValidationUtil.isValidEmail(cust);
				if(!isvalidEmail){
					while(!isvalidEmail){
						System.out.println("Invalid email ID.");
						System.out.println("Enter valid email:");
						cust = sc.next();
						isvalidEmail = ValidationUtil.isValidEmail(cust);
					}
				}
				System.out.println("\n" + service.reserveSeats(id, cust));
				break;
			case 4:
				loop = false;
				System.out.println("\nThank You!");
				break;
			default:
				System.out.println("Invalid option.");
			}
		}
		sc.close();
	
    	
    
		
	}
	
	}

}
