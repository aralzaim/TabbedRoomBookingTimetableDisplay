package com.example.android.tabbedroombookingtimetabledisplay;

import java.util.Calendar;
import android.widget.TimePicker;

public class Checkers {
	
//	public boolean collisionDetectionTwoRooms(Booking parsedBooking, Booking newBooking) {
//		
//        
////      System.out.println("AE"+parsedBooking.getBookingEnd());
////      System.out.println("BE"+newBooking[0].getBookingEnd());
////      System.out.println("BS"+newBooking[0].getBookingStart());
////      System.out.println("AS"+ parsedBooking.getBookingStart());
//    
//		
//    if(newBooking.getRoomName().equals(parsedBooking.getRoomName()))
//    {
//    //	System.out.println("rooms are same");
//      if(0<parsedBooking.getBookingEnd().compareTo(newBooking.getBookingEnd()))
//      {
//    	  if (0< parsedBooking.getBookingStart().compareTo(newBooking.getBookingEnd()))
//    			  {
//    	//	  System.out.println("NO COLLISION IF1");
//    		  return false;
//    			  }
//    	  else
//    	  {
//    	//	  System.out.println("COLLISIONIF1ELSE");
//    		  return true;
//    	  }
//      }
//      else if(0<newBooking.getBookingEnd().compareTo(parsedBooking.getBookingEnd()))
//      {
//    	  if(0<newBooking.getBookingStart().compareTo(parsedBooking.getBookingEnd()))
//    	  {
//    		  
//    	//	  System.out.println("NO COLLISIONIF2");
//    		  return false;
//    	  }
//    	  else{
//    	//	  System.out.println("COLLISIONIF2ELSE");
//    		  return true;
//      }
//    }
//      
//      else
//      {
//    //	  System.out.println("COLLISION LAST ELSE");
//      	return true;
//      	
//      }
//      
//    
//    }
//    return false;
//	}
//
//	
	
	public boolean dateOlder(int year, int monthOfYear, int dayOfMonth){

		if(year >= Calendar.getInstance().get(Calendar.YEAR))
		{
			if((monthOfYear+1) == (Calendar.getInstance().get(Calendar.MONTH)+1))
			{
				
				if(dayOfMonth >= Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
				{
				return false;
				}
				else
					return true;
			}
			
			if((monthOfYear+1)> (Calendar.getInstance().get(Calendar.MONTH)+1))
			{
				return false;
			}
			else return true;
			
		}
		else
			return true;
		
		}	
	
public boolean startTimeValidity(TimePicker startTimePicker){
		
		
		if(startTimePicker.getCurrentHour()<9){
			return false;
        }
		else if(startTimePicker.getCurrentHour()>=21)
		{
			return false;
		}
        else if(startTimePicker.getCurrentHour()>=9)
        {
        	return true;
        }
		return false;
	}
	
	public boolean endTimeValidity(TimePicker startTimePicker, TimePicker endTimePicker){
		if(endTimePicker.getCurrentHour()<startTimePicker.getCurrentHour())
		{
			return false;
		}
		
		else if (endTimePicker.getCurrentHour().equals(startTimePicker.getCurrentHour()) &&endTimePicker.getCurrentMinute()<=startTimePicker.getCurrentMinute())
		{
			return false;
		}
		else if(endTimePicker.getCurrentHour()>=21)
		{
			return false;
		}
		
		else if(startTimePicker.getCurrentHour()>=9)
		{
			return true;
		}
		return false;
	}


	
	
		
	
}
