package com.walmart.model;

import com.walmart.model.SeatNumberGenerator;
import com.walmart.model.Status;

/**
 * This class consists of the seat object information
 * @author vishnukiran
 *
 */

public class Seat {
	
	private SeatNumberGenerator seatNumber;
	private Customer reservedBy;
	private Status status;
	
	public Seat(SeatNumberGenerator seatNo, Status status) {
		this(seatNo);
		this.status = status;
	}
	
	public SeatNumberGenerator getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(SeatNumberGenerator seatNumber) {
		this.seatNumber = seatNumber;
	}

	public Customer getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(Customer reservedBy) {
		this.reservedBy = reservedBy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Seat(SeatNumberGenerator seatNo) {
		this.seatNumber = seatNo;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Seat<");
		if (seatNumber != null)
			builder.append(seatNumber).append(", ");
		if (reservedBy != null)
			builder.append("reservedBy=").append(reservedBy).append(", ");
		if (status != null)
			builder.append("status=").append(status);
		builder.append(">");
		return builder.toString();
	}
	

}
