package com.example.android.tabbedroombookingtimetabledisplay;

import java.util.Date;

public class Booking {
	
	String mBookedBy;
	String mRoomName;
	Date mBookingStart;
	Date mBookingEnd;
	String mBookingName;

	public String getBookingName() {
		return mBookingName;
	}

	public void setBookingName(String bookingName) {
		this.mBookingName = bookingName;
	}

	
	public String getBookedBy() {

		return mBookedBy;
	}
	public void setBookedBy(String bookedBy) {

		this.mBookedBy = bookedBy;
	}

	public String getRoomName() {

		return mRoomName;
	}
	public void setRoomName(String roomName) {

		this.mRoomName = roomName;
	}
	public Date getBookingStart() {

		return mBookingStart;
	}
	public void setBookingStart(Date bookingStart) {

		this.mBookingStart = bookingStart;
	}
	public Date getBookingEnd() {

		return mBookingEnd;
	}
	public void setBookingEnd(Date bookingEnd) {

		this.mBookingEnd = bookingEnd;
	}
	
	
	

}
