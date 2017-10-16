

package main.java.com.ticketService.WalmartTicket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.walmart.model.SeatHeldByCustomer;
import com.walmart.model.Theater;
import com.walmart.serviceImpl.TicketServiceImpl;
import com.walmart.model.SeatHeldByCustomer;
import com.walmart.model.Theater;
/**
 * @author gvishnukiran
 *
 */
public class TicketServiceImplTest {
	private TicketServiceImpl service;
	private int second = 3;
	private int wait = 4000;
	
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}

	@Before
	public void setUp() throws Exception {
		service = new TicketServiceImpl(Theater.getInstance(), second);
	}

//	@After
//	public void tearDown() throws Exception {
//	}
	
	@Test
	public void numSeatsAvailable() throws InterruptedException{
		int no = service.numSeatsAvailable();
		assert(no == 1);
		service = new TicketServiceImpl(Theater.getInstance(), second);
		no = service.numSeatsAvailable();
		assert(no == (2*3));
		service.findAndHoldSeats(2, "gvk@gmail.com");
		no = service.numSeatsAvailable();
		assert(no == ((2*3)-2));
		Thread.sleep(wait); 
		System.out.println("After waiting: " + service.numSeatsAvailable());
		assert((2*3) == service.numSeatsAvailable());
		service.findAndHoldSeats((2*3), "gvk@gmail.com");
		assert(0 == service.numSeatsAvailable());
		Thread.sleep(wait); 
		assert((2*3) == service.numSeatsAvailable());
		SeatHeldByCustomer sh = service.findAndHoldSeats((2*3), "gvk@gmail.com");
		no = service.numSeatsAvailable();
		service.reserveSeats(sh.getId(), "gvk@gmail.com");
		assert(no == service.numSeatsAvailable()); 
		System.gc();
		service = new TicketServiceImpl(Theater.getInstance(), second);
		sh = service.findAndHoldSeats((2*3), "gvk@gmail.com");
		no = service.numSeatsAvailable();
		Thread.sleep(wait);
		service.reserveSeats(sh.getId(), "gvk@gmail.com");
		assert((no + sh.getSeatsHeld().size()) == service.numSeatsAvailable()); 
	}
	
	@Test
	public void findAndHoldSeats() throws InterruptedException{
		SeatHeldByCustomer s1 = service.findAndHoldSeats(1, "gvk1@gmail.com");
		assertNotNull(s1);
		assert(1 == s1.getSeatsHeld().size());
		s1 = service.findAndHoldSeats(1, "gvk1@gmail.com");
		assert(null == s1);
		Thread.sleep(wait);
		s1 = service.findAndHoldSeats(1, "gvk1@gmail.com");
		assertNotNull(s1);
		assert(1 == s1.getSeatsHeld().size());
		Thread.sleep(wait);
		s1 = service.findAndHoldSeats(2, "gvk1@gmail.com");
		assert(null == s1);
	}
	
	@Test
	public void reserveSeats() throws InterruptedException{
		SeatHeldByCustomer s1 = service.findAndHoldSeats(1, "gvk1@gmail.com");
		String conf = service.reserveSeats(s1.getId(), "gvk1@gmail.com");
		assertNotNull(conf);
		assertTrue(conf.contains("reserved!"));
		conf = service.reserveSeats(0, "gvk1@gmail.com");
		assert(null == conf);
	}
	
	
}
