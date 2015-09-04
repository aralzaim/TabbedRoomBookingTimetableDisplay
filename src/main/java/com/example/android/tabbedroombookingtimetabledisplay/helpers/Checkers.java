package com.example.android.tabbedroombookingtimetabledisplay.helpers;

import java.util.Calendar;
import android.widget.TimePicker;

public class Checkers {

	
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
