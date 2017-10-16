package com.walmart.serviceImpl;

import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

import com.walmart.model.Customer;
import com.walmart.model.Status;
import com.walmart.model.Seat;
import com.walmart.model.SeatHeldByCustomer;
import com.walmart.model.Theater;
import com.walmart.service.TicketService;
import com.walmart.util.ValidationUtil;

/**
 * This class is the implementation class for TicketService
 * @author vishnukiran
 *
 */

public class TicketServiceImpl implements TicketService {
	private int available;
	private Theater theater;
	private Map<Integer, SeatHeldByCustomer> seatHoldMapper;
	private long seconds = 100L;
	private static ReentrantLock l = new ReentrantLock();

	public TicketServiceImpl(Theater v) {
		super();
		this.theater = v;
		this.available = v.getCapacity();
		seatHoldMapper = new TreeMap<Integer, SeatHeldByCustomer>();
	}
	
	public TicketServiceImpl(Theater v, long secs) {
		this(v);
		this.seconds = secs;
	}
	

	public int numSeatsAvailable() {
		expiryCheck();
		System.out.println(theater.DisplayTheater());
		return available;
	}
	
	/**
	 * Checking the expiry of the seats held
	 */

	private void expiryCheck() {
		for (Iterator<Map.Entry<Integer, SeatHeldByCustomer>> it = seatHoldMapper.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, SeatHeldByCustomer> entry = it.next();
			SeatHeldByCustomer tempSH = entry.getValue();
			long now = Instant.now().getEpochSecond();
			if ((now - tempSH.getCreatedAt().getEpochSecond()) > this.seconds) {
				updateStatus(tempSH.getSeatsHeld(), Status.AVAILABLE);
				this.available += tempSH.getSeatsHeld().size();
				it.remove();
			}
		}
	}

	private void expiryCheck(int seatHoldId) {
		SeatHeldByCustomer tempSH = seatHoldMapper.get(seatHoldId);
		if(tempSH!=null){
			long now = Instant.now().getEpochSecond();
			if((now - tempSH.getCreatedAt().getEpochSecond())> this.seconds){
				updateStatus(tempSH.getSeatsHeld(), Status.AVAILABLE);
				this.available += tempSH.getSeatsHeld().size();
				seatHoldMapper.remove(seatHoldId);
			}
		}
	}
	/**
	* Find and hold the best available seats for a customer
	*
	* @param numSeats the number of seats to find and hold
	* @param customerEmail unique identifier for the customer
	* @return a SeatHold object identifying the specific seats and related
	information
	*/
    
	public SeatHeldByCustomer findAndHoldSeats(int numSeats, String customerEmail) {
		SeatHeldByCustomer hold = null;		
		if(l.tryLock()) {
			System.out.println(Thread.currentThread().getName() + "got the lock of these seats");
			try {
				expiryCheck();
				List<Seat> holdingSeats = findGoodSeats(numSeats);
				updateStatus(holdingSeats, Status.HOLD);
				this.available -= holdingSeats.size();
			    hold = generateSeatHold(holdingSeats, customerEmail);
				if(hold!=null)seatHoldMapper.put(hold.getId(), hold);
				//Thread.sleep(8000);
			}
			
			catch(Exception e) {
				l.unlock();
			}
		}
		

		return hold;
		
	}
	/**
	 * updates the status once the seats are held 
	 * @param seats
	 * @param status
	 */

	private void updateStatus(List<Seat> seats, Status status){
		for(Seat st: seats){
			st.setStatus(status);
		}
	}
	
	/**
	 * return the held seats
	 * @param holdingSeats
	 * @param customerEmail
	 * @return
	 */
	
	private SeatHeldByCustomer generateSeatHold(List<Seat> holdingSeats, String customerEmail){
		if(holdingSeats.size()<1){
			return null;
		}
		SeatHeldByCustomer hold = new SeatHeldByCustomer();
		hold.setCustomer(new Customer(customerEmail));
		hold.setSeatsHeld(holdingSeats);
		hold.setCreatedAt(Instant.now());
		
		return hold;
	}
	/**
	 * finds the available best seats
	 * @param numSeats
	 * @return
	 */
	private List<Seat> findGoodSeats(int numSeats){
		if(this.available < numSeats){
			System.out.println("There are only " + this.available + " seats available now!");
			return new LinkedList<Seat>(); 
		}
		Seat[][] seats = theater.getSeats();
		List<Seat> storeSeats = new LinkedList<Seat>();
		boolean breakFlag = false;
		for(int i=0; i<theater.getRows(); i++){
			if(breakFlag){
				break;
			}
			for(int j=0; j< theater.getCountOnRow(); j++){
				Seat st = seats[i][j];
				if(Status.AVAILABLE == st.getStatus()){
					storeSeats.add(st);
					if(--numSeats == 0){
						breakFlag = true;
						break;
					}
				}
			}
		}
		return storeSeats;
	}
	
	/**
	* Commit seats held for a specific customer
	*
	* @param seatHoldId the seat hold identifier
	* @param customerEmail the email address of the customer to which the
	seat hold is assigned
	* @return a reservation confirmation code
	*/
	public String reserveSeats(int seatHoldId, String customerEmail) {
		expiryCheck(seatHoldId);
		SeatHeldByCustomer seatHold = finder(seatHoldId);
		if(seatHold == null){
			System.out.println("Either seatHoldId is invalid OR is expired! ");
			return null;
		}
		boolean isValidCustomer = ValidationUtil.validateCustomer(customerEmail, seatHold.getCustomer().getEmail());
		if(!isValidCustomer){
			return "cannot verify customer. Please request reservation with correct customer email.";
		}
		updateStatus(seatHold.getSeatsHeld(), Status.RESERVED);
		String result =  ValidationUtil.reservationCode(seatHold);
		seatHoldMapper.remove(seatHoldId);
		return result;
	}
	
	private SeatHeldByCustomer finder(int seatHoldId){
		return seatHoldMapper.get(seatHoldId);
	}
}
