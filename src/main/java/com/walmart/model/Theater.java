package com.walmart.model;

import java.util.Arrays;

import com.walmart.model.Seat;
import com.walmart.model.SeatNumberGenerator;
import com.walmart.model.Status;

/**
 * This class is the object that represents a theater
 * @author vishnukiran
 *
 */

public class Theater {
	
	private static final int rows = 5;
	private static final int countOnRow = 5;
	private static Seat[][] seats;
	private static int capacity;
	private static Theater instance;
	
	private Theater(	) {

		Theater.capacity = (Theater.rows * Theater.countOnRow);
		InitializeVenue();
	}
	
	 static {
	 instance = new Theater();
	}
	
	
	private void InitializeVenue(){
		seats = new Seat[rows][countOnRow];
		for(int i=0; i<rows; i++){
			for(int j=0; j<countOnRow; j++){
				seats[i][j] = new Seat(new SeatNumberGenerator(i, j), Status.AVAILABLE);
			}
		}
	}
	public static Theater getInstance() {
		return instance;
	}
	
	public int getRows() {
		return rows;
	}

	public int getCountOnRow() {
		return countOnRow;
	}

	public Seat[][] getSeats() {
		return seats;
	}

	public int getCapacity() {
		return capacity;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Venue [rows=").append(rows).append(", seatsPerRow=")
				.append(countOnRow).append(". ");
		if (seats != null)
			builder.append("seats=").append(Arrays.toString(seats));
		builder.append("]");
		return builder.toString();
	}
	
	public String DisplayTheater(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<rows; i++){
			for(int j=0; j<countOnRow; j++){
				String s = seats[i][j].getStatus().name().substring(0, 1);
				sb.append(s); sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString().trim();
	}
}
