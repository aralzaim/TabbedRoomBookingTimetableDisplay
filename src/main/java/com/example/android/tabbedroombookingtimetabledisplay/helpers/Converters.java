package com.example.android.tabbedroombookingtimetabledisplay.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.TimePicker;

public class Converters {

	//method to convert date object into string object
	public String dateToString (Date ddate){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(ddate);
	
	
	}

	//method to convert string object to date object.
	public Date stringToDate(String sDate) throws ParseException{
		
		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		 return sdf.parse(sDate);
	}
	
	//method to convert input stream object to string.
	public StringBuilder inputStreamToString(InputStream is) {
	    String rLine = "";
	    StringBuilder answer = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	    try {
	     while ((rLine = rd.readLine()) != null) {
	      answer.append(rLine);
	       }
	    }

	    catch (IOException e) {
	        e.printStackTrace();
	     }
	    return answer;
	   }

	//method to converting calendar object to String
	  public String calendarToDateS(Calendar dateCalendar) {
		   
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		    return sdf.format(dateCalendar.getTime());
		   
		    }
	//method to convert Date object to string but in EEE, d MMM yyyy which is Sat, 28 Aug 2015
	public String dateShower(Calendar dateCalendar) {

		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");

		return sdf.format(dateCalendar.getTime());

	}
	  //method to convert timepicker object to string
	  public String timePickerToTimeS( TimePicker editingCalendar){
		  Calendar calendar= Calendar.getInstance();
		  
		  calendar.set(Calendar.HOUR_OF_DAY, editingCalendar.getCurrentHour());
		  calendar.set(Calendar.MINUTE, editingCalendar.getCurrentMinute());
		  SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
		  
		  	return sdf.format(calendar.getTime());
	  }
}



