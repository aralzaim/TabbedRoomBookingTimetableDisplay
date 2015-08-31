package com.example.android.tabbedroombookingtimetabledisplay;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.tabbedroombookingtimetabledisplay.helpers.Checkers;
import com.example.android.tabbedroombookingtimetabledisplay.helpers.Converters;
import com.example.android.tabbedroombookingtimetabledisplay.helpers.ListAdapter;
import com.example.android.tabbedroombookingtimetabledisplay.helpers.TimePickerDialogs;

public class SearchingFragment extends Fragment implements OnClickListener {

		Calendar dateCalendar=Calendar.getInstance();
		TimePicker startCalendar;
		TimePicker endCalendar;
		EditText dateText;
		EditText startText;
		EditText endText;
		Button searchBtn;
		TextView resultsTitle;
		ListView searchList;

		Converters converters = new Converters();
		Booking searchBooking= new Booking();
		Checkers checkers = new Checkers();
		
		CheckBox moveable;
		CheckBox projector;
		CheckBox multipleComputers;
		CheckBox phone;

		RadioGroup capacities;

	    RadioButton capacityLow;
		RadioButton capacityMedium;
		RadioButton capacityHigh;

		int availableBookingStartHour=9;
		int availableBookingStartMinute=00;
		int selectedCapacity = 0;

		String startDateTimestamp;
		String endDateTimestamp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        LinearLayout rootView =(LinearLayout) inflater.inflate(R.layout.activity_searching, container, false);


		searchBtn=(Button) rootView.findViewById(R.id.submit_button);
		searchBtn.setOnClickListener(this);
		
		searchList=(ListView) rootView.findViewById(R.id.search_list);
		
		resultsTitle=(TextView) rootView.findViewById(R.id.results_title);
	   resultsTitle.setVisibility(View.GONE);


		dateCalendar = Calendar.getInstance();
		startCalendar= new TimePicker(getActivity());
		startCalendar.setCurrentHour(availableBookingStartHour);
		startCalendar.setCurrentMinute(availableBookingStartMinute);

		endCalendar=new TimePicker(getActivity());
		endCalendar.setCurrentHour(availableBookingStartHour+1);
		endCalendar.setCurrentMinute(availableBookingStartMinute);

		dateText= (EditText) rootView.findViewById(R.id.date_text);
		startText= (EditText) rootView.findViewById(R.id.start_text);
		endText= (EditText) rootView.findViewById(R.id.end_text);

		moveable=(CheckBox) rootView.findViewById(R.id.moveable_box);
		projector=(CheckBox) rootView.findViewById(R.id.projector_box);
		multipleComputers=(CheckBox) rootView.findViewById(R.id.multiple_box);
		phone= (CheckBox) rootView.findViewById(R.id.phone_box);

		capacities= (RadioGroup) rootView.findViewById(R.id.capacities);
		capacities.clearCheck();


		 dateText.setText("Date:	" + converters.dateShower(dateCalendar));
		Log.e("DATE",converters.dateShower(dateCalendar));
		 dateText.setTextSize(30);
		 startText.setText("Time Start:	" + converters.timePickerToTimeS(startCalendar));
		 startText.setTextSize(30);
		 endText.setText("Time End:	" + converters.timePickerToTimeS(endCalendar));
		 endText.setTextSize(30);

		moveable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);
			}
		});

		projector.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);
			}
		});

		multipleComputers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);
			}
		});

		phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);
			}
		});
		capacities.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);
			}
		});


		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);


				if(checkers.dateOlder(year,monthOfYear,dayOfMonth)){

					AlertDialog alert= new AlertDialog.Builder(getActivity()).
							setTitle("Invalid Date!").
							setMessage("Date should not be older than today.")
							.setPositiveButton("Retry!", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {

									Calendar today= Calendar.getInstance();

									dateCalendar.set(Calendar.YEAR,today.get(Calendar.YEAR));
									dateCalendar.set(Calendar.MONTH,today.get(Calendar.MONTH));
									dateCalendar.set(Calendar.DAY_OF_MONTH,today.get(Calendar.DAY_OF_MONTH));
									dateText.setText("Date: " + converters.dateShower(today));
								}
							}).show();
				}
				else
				{
					dateCalendar.set(Calendar.YEAR, year);
					dateCalendar.set(Calendar.MONTH, monthOfYear);
					dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					dateText.setText("Date:	" + converters.dateShower(dateCalendar));
				}



			}

		};

		final TimePickerDialog.OnTimeSetListener startTime= new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				  // TODO Auto-generated method stub
				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);

		        startCalendar.setCurrentHour(hourOfDay);
		        startCalendar.setCurrentMinute(minute);

				if((endCalendar.getCurrentHour().equals(hourOfDay)&&endCalendar.getCurrentMinute()<=minute) || endCalendar.getCurrentHour()<hourOfDay ) {
					endCalendar.setCurrentHour(hourOfDay);


					endCalendar.setCurrentHour(hourOfDay+1);

					}

		        if(checkers.startTimeValidity(startCalendar))
		        {

		        	startText.setText("Time Start:	" + converters.timePickerToTimeS(startCalendar));
				    endText.setText("Time End:	" + converters.timePickerToTimeS(endCalendar));
				    startText.setTextColor(Color.BLACK);
				    endText.setTextColor(Color.BLACK);

		        }
		        else{
		        	AlertDialog alert= new AlertDialog.Builder(getActivity()).
		        			setTitle("Invalid Time!").
		        			setMessage("Times should be between 9am and 9pm as well as starting time should"
		        					+ " not be later than ending time.")
		        					.setPositiveButton("Retry!", new DialogInterface.OnClickListener() {
		        				        public void onClick(DialogInterface dialog, int which) {

		        				        	startCalendar.setCurrentHour(availableBookingStartHour);
		        				        	startCalendar.setCurrentMinute(availableBookingStartMinute);
		        				        	 startText.setText("Time Start:	" + converters.timePickerToTimeS(startCalendar));
		        				        }
		        				     }).show();
		        }

			}
		};


		final TimePickerDialog.OnTimeSetListener endTime= new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				  // TODO Auto-generated method stub
		        endCalendar.setCurrentHour(hourOfDay);
		        endCalendar.setCurrentMinute(minute);

				searchList.setVisibility(View.INVISIBLE);
				resultsTitle.setVisibility(View.INVISIBLE);

		        if(checkers.endTimeValidity(startCalendar, endCalendar))
		        {

				        endText.setText("Time End:	" + converters.timePickerToTimeS(endCalendar));

		        }

		        else{
		        	AlertDialog alert= new AlertDialog.Builder(getActivity()).
		        			setTitle("Invalid Time!").
		        			setMessage("Times should be between 09.00 and 21.00 as well as starting time should"
		        					+ " not be later than ending time.")
		        					.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		        				        public void onClick(DialogInterface dialog, int which) {
		        				            endCalendar.setCurrentHour(startCalendar.getCurrentHour());
												endCalendar.setCurrentHour(startCalendar.getCurrentHour()+1);
												endText.setText("Time End:	" + converters.timePickerToTimeS(endCalendar));

		        				        }
		        				     }).show();
		        }
			}


		};

		dateText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dateDialog = new DatePickerDialog(getActivity(),date , dateCalendar
	                    .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
	                    dateCalendar.get(Calendar.DAY_OF_MONTH));

				dateDialog.getDatePicker().setSpinnersShown(false);
				dateDialog.show();


			}
		});

		startText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TimePickerDialogs startPicker = new TimePickerDialogs(getActivity(), startTime, startCalendar.getCurrentHour(), startCalendar.getCurrentMinute(), true);
				startPicker.show();
			}
		});

		endText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialogs endPicker= new TimePickerDialogs(getActivity(), endTime, endCalendar.getCurrentHour(), endCalendar.getCurrentMinute(), true);
				endPicker.show();
			}
		});





		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		dateText.setText("Date:	" + converters.dateShower(dateCalendar));
		Log.e("DATE", converters.dateShower(dateCalendar));
		Log.e("START", converters.timePickerToTimeS(startCalendar));
		Log.e("END", converters.timePickerToTimeS(endCalendar));

		dateText.setTextSize(30);
		startText.setText("Time Start:	" + converters.timePickerToTimeS(startCalendar));
		startText.setTextSize(30);
		endText.setText("Time End:	" + converters.timePickerToTimeS(endCalendar));
		endText.setTextSize(30);
	}

	@Override
	public void onClick(View v) {


        String dateString;
        String startString;
        String endString;

        Date startDate;
      	Date endDate;
      	SearchRoom searchTask = new SearchRoom();




        dateString=converters.calendarToDateS(dateCalendar);
        startString= converters.timePickerToTimeS(startCalendar);
        endString=converters.timePickerToTimeS(endCalendar);

      	startDateTimestamp=(dateString+ " " + startString);
      	endDateTimestamp=(dateString+ " "+ endString);
     
      
    
	try {
		startDate = converters.stringToDate(startDateTimestamp);
		endDate =converters.stringToDate(endDateTimestamp);
		
		
		   searchBooking.setBookingStart(startDate);
		   searchBooking.setBookingEnd(endDate);

		if(capacities.getCheckedRadioButtonId()==R.id.capacity_low){
		selectedCapacity=1;
		}
		else if(capacities.getCheckedRadioButtonId()==R.id.capacity_medium){
			selectedCapacity=2;
		}
		else if(capacities.getCheckedRadioButtonId()==R.id.capacity_high){
			selectedCapacity=3;
		}

		   searchTask.execute(searchBooking);
		   
		   
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
		
	}
	
	private class SearchRoom extends AsyncTask <Booking, Boolean, Boolean>{

		ProgressDialog processDialog=new ProgressDialog(getActivity());
		HttpClient httpClient=null;
		HttpPost httpPost=null;
	
		ArrayList<String> resultList = new ArrayList<String>();
		
		@Override
		protected void onPreExecute(){
			processDialog.setMessage("Searching Rooms...");
			processDialog.show();
			
		}
		
		
		@Override
		protected Boolean doInBackground(Booking... searchBooking) {
			
			httpClient= new DefaultHttpClient();
			
			String emptyRoom;
			
			
				try {
					
					
				    JSONObject newBookingJS= new JSONObject();
                    
                    newBookingJS.put("booked_room", searchBooking[0].getRoomName());
                    newBookingJS.put("booking_start",converters.dateToString(searchBooking[0].getBookingStart()));
                    newBookingJS.put("booking_end",converters.dateToString(searchBooking[0].getBookingEnd()));
                    newBookingJS.put("booked_by",searchBooking[0].getBookedBy());
                    newBookingJS.put("moveable_table",moveable.isChecked());
                    newBookingJS.put("projector",projector.isChecked());
					newBookingJS.put("phone",phone.isChecked());
                    newBookingJS.put("multiple_computer",multipleComputers.isChecked());
                    newBookingJS.put("capacity", selectedCapacity);

                    
                    httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/searchRoom.php");
                    httpPost.setEntity(new StringEntity(newBookingJS.toString()));
                    

                 
                	HttpResponse httpResponse=httpClient.execute(httpPost);
                    
                	String jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();
                
                   
                	System.out.println(jsonResult);
					JSONObject jsonObj = new JSONObject(jsonResult);
					   
	                  if (jsonObj != null) {
	                      JSONArray jsonBookings = jsonObj.getJSONArray("resultrooms");                        

	                      for (int i = 0; i < jsonBookings.length(); i++) {
	                          JSONObject catObj = (JSONObject) jsonBookings.get(i);
	                         
	                      emptyRoom=(catObj.getString("booked_room"));

	                      resultList.add(emptyRoom);
	                      }
	                  } 	
	                      if(resultList.size()<=0){
	                    
	                    	  System.out.println("No booking available");
	                    	  return false;
	                      }
	                      else{
	                    	
	                    	  return true;
	                      }
				}      
	                  
					
					
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
			return true;
		}
		
		

		@Override
		protected void onPostExecute(Boolean result){
			
			if(result==true)
			{
				resultsTitle.setText("Matching Rooms");
				resultsTitle.setVisibility(View.VISIBLE);
				createList(resultList);

				searchList.setVisibility(View.VISIBLE);
				processDialog.dismiss();
			}
			else if(result==false)
			{
				
				resultsTitle.setText("There are no results matching.");
				resultsTitle.setVisibility(View.VISIBLE);
				searchList.setVisibility(View.INVISIBLE);
				
			processDialog.dismiss();
			}
			
		}


		public void createList(ArrayList<String> resultList ) {



			final ListAdapter adapter = new ListAdapter(getActivity(), resultList);
			adapter.setCustomButtonListner(new ListAdapter.customButtonListener() {
				@Override
				public void onButtonClickListner(int position, final String room) {


					final EditText editText = new EditText(getActivity());


					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);

					editText.setTextColor(Color.WHITE);

					alert.setTitle("Book a Room");
					alert.setMessage("Please enter purpose or name for your booking.");


					alert.setView(editText);
					InputFilter[] fArray = new InputFilter[1];
					fArray[0] = new InputFilter.LengthFilter(30);
					editText.setFilters(fArray);


					alert.setPositiveButton("Book", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							String purposeName = editText.getText().toString();

							if (!purposeName.matches("")) {
								Booking newBooking = new Booking();
								BookRoom bookingTask = new BookRoom();

								try {
									newBooking.setBookingStart(converters.stringToDate(startDateTimestamp));
									newBooking.setBookingEnd(converters.stringToDate(endDateTimestamp));
									newBooking.setRoomName(room);
									newBooking.setBookingName(purposeName);

									bookingTask.execute(newBooking);

									searchList.setVisibility(View.INVISIBLE);
									resultsTitle.setVisibility(View.INVISIBLE);

								} catch (ParseException e) {
									e.printStackTrace();
								}

							} else {
								Toast toast = Toast.makeText(getActivity(), "Please enter name or purpose!", Toast.LENGTH_LONG);
								toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
								toast.show();
							}

						}
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// what ever you want to do with No option.
						}
					});

					AlertDialog nameDialog= alert.create();

					nameDialog.show();

					Button pButton = (Button) nameDialog.getButton(nameDialog.BUTTON_POSITIVE);
					if(pButton!=null)
						pButton.setBackgroundColor(Color.LTGRAY);
					pButton.setTextColor(Color.BLACK);

					Button nButton = (Button) nameDialog.getButton(nameDialog.BUTTON_NEGATIVE);
					if(nButton!=null)
						nButton.setBackgroundColor(Color.LTGRAY);
					nButton.setTextColor(Color.BLACK);





				}
			});

			searchList.setAdapter(adapter);
		}


	}

	private class BookRoom extends AsyncTask <Booking, Boolean, Boolean>{

		ProgressDialog processDialog=new ProgressDialog(getActivity());
		HttpClient httpClient=null;
		HttpPost httpPost=null;

		@Override
		protected void onPreExecute(){
			processDialog.setMessage("Booking room...");
			processDialog.show();

		}


		@Override
		protected Boolean doInBackground(Booking... newBooking) {

			httpClient= new DefaultHttpClient();
			httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");


			try {

				JSONObject newBookingJS= new JSONObject();

				newBookingJS.put("booked_room", newBooking[0].getRoomName());
				newBookingJS.put("booking_start",converters.dateToString(newBooking[0].getBookingStart()));
				newBookingJS.put("booking_end",converters.dateToString(newBooking[0].getBookingEnd()));
				newBookingJS.put("booked_by",newBooking[0].getBookedBy());
				newBookingJS.put("name_purpose",newBooking[0].getBookingName());

				System.out.println(newBookingJS.toString());

				httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
				httpPost.setEntity(new StringEntity(newBookingJS.toString()));


				HttpResponse bookingResponse=httpClient.execute(httpPost);

				String bookingResult = converters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();

				System.out.println(bookingResult);

				if(bookingResult.equalsIgnoreCase("collision:"))
				{
					return false;
				}
				else
				{
					return true;
				}


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}



		@Override
		protected void onPostExecute(Boolean result){

			if(result==true)
			{

				processDialog.dismiss();



				AlertDialog.Builder successfulDialog = new AlertDialog.Builder(
						getActivity(),AlertDialog.THEME_HOLO_DARK);

				successfulDialog.setTitle("Successful !");
				successfulDialog.setMessage(R.string.succesful_booking);

				successfulDialog.setPositiveButton("Done !", new DialogInterface.OnClickListener() {


					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
				AlertDialog alertDialog = successfulDialog.create();
				alertDialog.show();



				Button sButton = (Button) alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
				if(sButton!=null)
					sButton.setBackgroundColor(Color.GREEN);
				sButton.setTextColor(Color.BLACK);


			}
			else
			{

				System.out.println("CONNECTED AND CAME BACK AS FALSE");
				processDialog.dismiss();

				AlertDialog.Builder collisionDialog = new AlertDialog.Builder(
						getActivity(),AlertDialog.THEME_HOLO_DARK);

				collisionDialog.setTitle("Collision !");
				collisionDialog.setMessage(R.string.unsuccesful_booking);
				collisionDialog.setPositiveButton("Retry !", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});

				AlertDialog alertDialog= collisionDialog.create();

				alertDialog.show();

				Button fButton= (Button) alertDialog.getButton(alertDialog.BUTTON_POSITIVE);



				if(fButton!=null)
					fButton.setBackgroundColor(Color.RED);

				fButton.setTextColor(Color.BLACK);


			}

		}



	}


}
