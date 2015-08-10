package com.example.android.tabbedroombookingtimetabledisplay;

import java.util.Date;

public class Booking {
	
	String bookedBy;
	String roomName;
	Date bookingStart;
	Date bookingEnd;
	String bookingName;

	public String getBookingName() {
		return bookingName;
	}

	public void setBookingName(String bookingName) {
		this.bookingName = bookingName;
	}

	
	public String getBookedBy() {
		return bookedBy;
	}
	public void setBookedBy(String bookedBy) {
		this.bookedBy = bookedBy;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public Date getBookingStart() {
		return bookingStart;
	}
	public void setBookingStart(Date bookingStart) {
		this.bookingStart = bookingStart;
	}
	public Date getBookingEnd() {
		return bookingEnd;
	}
	public void setBookingEnd(Date bookingEnd) {
		this.bookingEnd = bookingEnd;
	}
	
	
	

}
