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
	
	public String dateToString (Date ddate){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(ddate);
	
	
	}
	public Date stringToDate(String sDate) throws ParseException{
		
		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		 return sdf.parse(sDate);
	}
	
	
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
	
	  public String calendarToDateS(Calendar dateCalendar) {

		   
		    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy ");

		    return sdf.format(dateCalendar.getTime());
		   
		    }
	  
	  public String timePickerToTimeS( TimePicker editingCalendar){
		  Calendar calendar= Calendar.getInstance();
		  
		  calendar.set(Calendar.HOUR_OF_DAY, editingCalendar.getCurrentHour());
		  calendar.set(Calendar.MINUTE, editingCalendar.getCurrentMinute());
		  SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
		  
		  	return sdf.format(calendar.getTime());
	  }
}



