package com.walmart.model;

/**
 * 
 * This class is the blue print for the customer
 * @author vishnukiran
 *
 */

public class Customer {
	
	private String email;
	
	public Customer(String email) {
		this.email = email;
		
	}
	
	public String getEmail() {
		
		return this.email;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		if (email != null)
			builder.append(email);
		return builder.toString();
		
		
	}

}
