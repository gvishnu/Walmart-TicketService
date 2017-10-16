package com.walmart.model;

/**
 * 
 * @author vishnukiran
 *
 */

public class SeatNumberGenerator {
	int row;
	int countOnRow;
	
	public SeatNumberGenerator(int row, int countOnRow) {
		
		this.row = row;
		this.countOnRow = countOnRow;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCountOnRow() {
		return countOnRow;
	}

	public void setCountOnRow(int countOnRow) {
		this.countOnRow = countOnRow;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{").append(row).append(",")
				.append(countOnRow).append("}");
		return builder.toString();
	}

}
