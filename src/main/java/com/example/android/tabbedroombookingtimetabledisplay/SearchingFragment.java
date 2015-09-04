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

		Calendar mDateCalendar =Calendar.getInstance();
		TimePicker mStartCalendar;
		TimePicker mEndCalendar;

		EditText mDateText;
		EditText mStartText;
		EditText mEndText;

		Button mSearchBtn;
		TextView mResultsTitle;
		ListView mSearchList;

		Converters mConverters = new Converters();
		Checkers mCheckers = new Checkers();

		Booking mSearchBooking = new Booking();

		CheckBox mMoveable;
		CheckBox mProjector;
		CheckBox mMultipleComputers;
		CheckBox mPhone;

		RadioGroup mCapacities;

	    RadioButton mCapacityLow;
		RadioButton mCapacityMedium;
		RadioButton mCapacityHigh;

		int mAvailableBookingStartHour =9;
		int mAvailableBookingStartMinute =00;
		int mSelectedCapacity = 0;

		String mStartDateTimestamp;
		String mEndDateTimestamp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        LinearLayout rootView =(LinearLayout) inflater.inflate(R.layout.activity_searching, container, false);

		//initializations of elements in user interface.
		mSearchBtn =(Button) rootView.findViewById(R.id.submit_button);
		mSearchBtn.setOnClickListener(this);
		
		mSearchList =(ListView) rootView.findViewById(R.id.search_list);
		
		mResultsTitle =(TextView) rootView.findViewById(R.id.results_title);
	   	mResultsTitle.setVisibility(View.GONE);


		mDateCalendar = Calendar.getInstance();
		mStartCalendar = new TimePicker(getActivity());
		mStartCalendar.setCurrentHour(mAvailableBookingStartHour);
		mStartCalendar.setCurrentMinute(mAvailableBookingStartMinute);

		mEndCalendar =new TimePicker(getActivity());
		mEndCalendar.setCurrentHour(mAvailableBookingStartHour + 1);
		mEndCalendar.setCurrentMinute(mAvailableBookingStartMinute);

		mDateText = (EditText) rootView.findViewById(R.id.date_text);
		mStartText = (EditText) rootView.findViewById(R.id.start_text);
		mEndText = (EditText) rootView.findViewById(R.id.end_text);

		mMoveable =(CheckBox) rootView.findViewById(R.id.moveable_box);
		mProjector =(CheckBox) rootView.findViewById(R.id.projector_box);
		mMultipleComputers =(CheckBox) rootView.findViewById(R.id.multiple_box);
		mPhone = (CheckBox) rootView.findViewById(R.id.phone_box);

		mCapacities = (RadioGroup) rootView.findViewById(R.id.capacities);
		mCapacities.clearCheck();

		//default values of text views.
		 mDateText.setText("Date:	" + mConverters.dateShower(mDateCalendar));
		 mDateText.setTextSize(30);
		 mStartText.setText("Time Start:	" + mConverters.timePickerToTimeS(mStartCalendar));
		 mStartText.setTextSize(30);
		 mEndText.setText("Time End:	" + mConverters.timePickerToTimeS(mEndCalendar));
		 mEndText.setTextSize(30);

		//listener that hides result list when moveable is clicked
		mMoveable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);
			}
		});
		//listener that hides result list when projectors is clicked
		mProjector.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);
			}
		});
		//listener that hides result list when multiples computers is clicked
		mMultipleComputers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);
			}
		});
		//listener that hides result list when phones is clicked
		mPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);
			}
		});
		//listener that hides result list when any of capacities is clicked
		mCapacities.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);
			}
		});

	//listener that is responsible for when date is setted
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);

				//checks if date is older than today, give error
				if(mCheckers.dateOlder(year,monthOfYear,dayOfMonth)){

					AlertDialog dateAlert= new AlertDialog.Builder(getActivity()).
							setTitle("Invalid Date!").
							setMessage("Date should not be older than today.")
							.setPositiveButton("Retry!", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {

									Calendar today= Calendar.getInstance();

									mDateCalendar.set(Calendar.YEAR, today.get(Calendar.YEAR));
									mDateCalendar.set(Calendar.MONTH, today.get(Calendar.MONTH));
									mDateCalendar.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
									mDateText.setText("Date: " + mConverters.dateShower(today));
								}
							}).show();
				}
				//if date is not problematic, set selected date to text views
				else
				{
					mDateCalendar.set(Calendar.YEAR, year);
					mDateCalendar.set(Calendar.MONTH, monthOfYear);
					mDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					mDateText.setText("Date:	" + mConverters.dateShower(mDateCalendar));
				}



			}

		};


		//listener that is responsible for when start time is setted
		final TimePickerDialog.OnTimeSetListener startTime= new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				  // TODO Auto-generated method stub
				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);

		        mStartCalendar.setCurrentHour(hourOfDay);
		        mStartCalendar.setCurrentMinute(minute);

				//if start time is bigger than end time, set end time to 1 hour after selected start time.
				if((mEndCalendar.getCurrentHour().equals(hourOfDay)&& mEndCalendar.getCurrentMinute()<=minute) || mEndCalendar.getCurrentHour()<hourOfDay ) {
					mEndCalendar.setCurrentHour(hourOfDay);


					mEndCalendar.setCurrentHour(hourOfDay+1);

					}
				//checks if start time is problematic, if its not, sets text as selected time.
		        if(mCheckers.startTimeValidity(mStartCalendar))
		        {

		        	mStartText.setText("Time Start:	" + mConverters.timePickerToTimeS(mStartCalendar));
				    mEndText.setText("Time End:	" + mConverters.timePickerToTimeS(mEndCalendar));
				    mStartText.setTextColor(Color.BLACK);
				    mEndText.setTextColor(Color.BLACK);

		        }
				//if start time is problematic, sets the start time to 09.00 which is default.
		        else{
		        	AlertDialog startTimeAlert= new AlertDialog.Builder(getActivity()).
		        			setTitle("Invalid Time!").
		        			setMessage("Times should be between 09.00 and 21.00 and end time should be"
		        					+ " after start time.")
		        					.setPositiveButton("Retry!", new DialogInterface.OnClickListener() {
		        				        public void onClick(DialogInterface dialog, int which) {

		        				        	mStartCalendar.setCurrentHour(mAvailableBookingStartHour);
		        				        	mStartCalendar.setCurrentMinute(mAvailableBookingStartMinute);
		        				        	 mStartText.setText("Time Start:	" + mConverters.timePickerToTimeS(mStartCalendar));
		        				        }
		        				     }).show();
		        }

			}
		};

		//listener that is responsible for when end time is setted
		final TimePickerDialog.OnTimeSetListener endTime= new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				  // TODO Auto-generated method stub
		        mEndCalendar.setCurrentHour(hourOfDay);
		        mEndCalendar.setCurrentMinute(minute);

				mSearchList.setVisibility(View.INVISIBLE);
				mResultsTitle.setVisibility(View.INVISIBLE);
				//checks if end time is smaller than star time.
		        if(mCheckers.endTimeValidity(mStartCalendar, mEndCalendar))
		        {

				        mEndText.setText("Time End:	" + mConverters.timePickerToTimeS(mEndCalendar));

		        }
				//if end time is problematic, set it to 1 hour after the start time selected.
		        else{
		        	AlertDialog endTimeAlert= new AlertDialog.Builder(getActivity()).
		        			setTitle("Invalid Time!").
		        			setMessage("Times should be between 09.00 and 21.00 and end time should be"
		        					+ " after start time.")
		        					.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		        				        public void onClick(DialogInterface dialog, int which) {
		        				            mEndCalendar.setCurrentHour(mStartCalendar.getCurrentHour());
												mEndCalendar.setCurrentHour(mStartCalendar.getCurrentHour() + 1);
												mEndText.setText("Time End:	" + mConverters.timePickerToTimeS(mEndCalendar));

		        				        }
		        				     }).show();
		        }
			}


		};

		//listener that opens timepicker when time date textview is clicked.
		mDateText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), date, mDateCalendar
						.get(Calendar.YEAR), mDateCalendar.get(Calendar.MONTH),
						mDateCalendar.get(Calendar.DAY_OF_MONTH));

				dateDialog.getDatePicker().setSpinnersShown(false);
				dateDialog.show();


			}
		});

		//listener that opens timepicker when time start textview is clicked.
		mStartText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TimePickerDialogs startPicker = new TimePickerDialogs(getActivity(), startTime, mStartCalendar.getCurrentHour(), mStartCalendar.getCurrentMinute(), true);
				startPicker.show();
			}
		});

		//listener that opens timepicker when time end textview is clicked.
		mEndText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialogs endPicker = new TimePickerDialogs(getActivity(), endTime, mEndCalendar.getCurrentHour(), mEndCalendar.getCurrentMinute(), true);
				endPicker.show();
			}
		});

		//returns all user interface elements
		return rootView;
	}
//method to reset all dates and user interface elements to default when fragment is continued.
	@Override
	public void onResume() {
		super.onResume();
		mDateText.setText("Date:	" + mConverters.dateShower(mDateCalendar));


		mDateText.setTextSize(30);
		mStartText.setText("Time Start:	" + mConverters.timePickerToTimeS(mStartCalendar));
		mStartText.setTextSize(30);
		mEndText.setText("Time End:	" + mConverters.timePickerToTimeS(mEndCalendar));
		mEndText.setTextSize(30);
	}

	//all implementations which runs after clicking search room button.
	@Override
	public void onClick(View v) {


        String mDateString;
        String mStartString;
        String mEndString;

        Date mStartDate;
      	Date mEndDate;
      	SearchRoom searchTask = new SearchRoom();



		//converting calendar values to string.
        mDateString= mConverters.calendarToDateS(mDateCalendar);

		//converting timepicker values to string.
        mStartString= mConverters.timePickerToTimeS(mStartCalendar);
        mEndString= mConverters.timePickerToTimeS(mEndCalendar);

      	mStartDateTimestamp =(mDateString+ " " + mStartString);
      	mEndDateTimestamp =(mDateString+ " "+ mEndString);
     
      
    
	try {

		//converting string values to Date format.
		mStartDate = mConverters.stringToDate(mStartDateTimestamp);
		mEndDate = mConverters.stringToDate(mEndDateTimestamp);
		
		//assigning start and end time to Booking Object to be used in searchTask.
		   mSearchBooking.setBookingStart(mStartDate);
		   mSearchBooking.setBookingEnd(mEndDate);

		//checking which radio box is selected
		if(mCapacities.getCheckedRadioButtonId()==R.id.capacity_low){
		mSelectedCapacity =1;
		}
		else if(mCapacities.getCheckedRadioButtonId()==R.id.capacity_medium){
			mSelectedCapacity =2;
		}
		else if(mCapacities.getCheckedRadioButtonId()==R.id.capacity_high){
			mSelectedCapacity =3;
		}
		//executing task for searching room.
		   searchTask.execute(mSearchBooking);
		   
		   
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
		
	}
	//class responsible for searching rooms and showing results.
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
		
		//all necessary implementations to be run background
		@Override
		protected Boolean doInBackground(Booking... searchBooking) {
			
			httpClient= new DefaultHttpClient();
			
			String emptyRoom;
			
			
				try {
					
					//converting all information collected from user interface to json object.
				    JSONObject newBookingJS= new JSONObject();
                    newBookingJS.put("booked_room", searchBooking[0].getRoomName());
                    newBookingJS.put("booking_start", mConverters.dateToString(searchBooking[0].getBookingStart()));
                    newBookingJS.put("booking_end", mConverters.dateToString(searchBooking[0].getBookingEnd()));
                    newBookingJS.put("booked_by",searchBooking[0].getBookedBy());
                    newBookingJS.put("moveable_table", mMoveable.isChecked());
                    newBookingJS.put("projector", mProjector.isChecked());
					newBookingJS.put("phone", mPhone.isChecked());
                    newBookingJS.put("multiple_computer", mMultipleComputers.isChecked());
                    newBookingJS.put("capacity", mSelectedCapacity);

                    //sending all information collected from user interface to searchRoom web service.
                    httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/searchRoom.php");
                    httpPost.setEntity(new StringEntity(newBookingJS.toString()));
                    

                 //converting httpResponse to string then to JsonObject
                	HttpResponse httpResponse=httpClient.execute(httpPost);
                	String jsonResult = mConverters.inputStreamToString(httpResponse.getEntity().getContent()).toString();
					JSONObject jsonObj = new JSONObject(jsonResult);

					//converting all results to the string
	                  if (jsonObj != null) {
	                      JSONArray jsonBookings = jsonObj.getJSONArray("resultrooms");
	                      for (int i = 0; i < jsonBookings.length(); i++) {JSONObject catObj = (JSONObject) jsonBookings.get(i);
	                      emptyRoom=(catObj.getString("booked_room"));
	                      //adding all resulting values to  resultList array.
	                      resultList.add(emptyRoom);
	                      }
	                  }
						//if result list having got any values inside return false
	                      if(resultList.size()<=0){
	                    	  return false;
	                      }

						  //if result list has values inside, return true
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
			//giving the results if the list is not empty.
			if(result==true)
			{
				mResultsTitle.setText("Matching Rooms");
				mResultsTitle.setVisibility(View.VISIBLE);
				createList(resultList);

				mSearchList.setVisibility(View.VISIBLE);
				processDialog.dismiss();
			}
			//Giving notification that there are no rooms matching this query..
			else if(result==false)
			{
				
				mResultsTitle.setText("There are no results matching.");
				mResultsTitle.setVisibility(View.VISIBLE);
				mSearchList.setVisibility(View.INVISIBLE);
				
			processDialog.dismiss();
			}
			
		}


		public void createList(ArrayList<String> resultList ) {

		//a custom list with button which has custom array adapter "helpers/ListAdapter"
			final ListAdapter adapter = new ListAdapter(getActivity(), resultList);
			adapter.setCustomButtonListner(new ListAdapter.customButtonListener() {
				@Override
				public void onButtonClickListner(int position, final String room) {


					final EditText namePurposeText = new EditText(getActivity());

					AlertDialog.Builder mPurposeNameDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);

					namePurposeText.setTextColor(Color.WHITE);

					mPurposeNameDialog.setTitle("Book a Room");
					mPurposeNameDialog.setMessage("Please enter purpose or name for your booking.");


					mPurposeNameDialog.setView(namePurposeText);

					//setting max 30 characters for name purpose.
					InputFilter[] mFilterArray = new InputFilter[1];
					mFilterArray[0] = new InputFilter.LengthFilter(30);
					namePurposeText.setFilters(mFilterArray);



					//setting a listener for book button.
					mPurposeNameDialog.setPositiveButton("Book", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface nameDialog, int whichButton) {

							String purposeName = namePurposeText.getText().toString();


							//if purpose is not empt book room
							if (!purposeName.matches("")) {
								Booking newBooking = new Booking();
								BookRoom bookingTask = new BookRoom();

								try {
									//assigning values to booking object
									newBooking.setBookingStart(mConverters.stringToDate(mStartDateTimestamp));
									newBooking.setBookingEnd(mConverters.stringToDate(mEndDateTimestamp));
									newBooking.setRoomName(room);
									newBooking.setBookingName(purposeName);

									bookingTask.execute(newBooking);

									mSearchList.setVisibility(View.INVISIBLE);
									mResultsTitle.setVisibility(View.INVISIBLE);

								} catch (ParseException e) {
									e.printStackTrace();
								}

							}

							//if name purpose is empty, dont book room and give a warning.
							else {

								Toast toast = Toast.makeText(getActivity(), "Please enter name or purpose!", Toast.LENGTH_LONG);
								toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
								toast.show();
							}

						}
					});

					mPurposeNameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface nameDialog, int whichButton) {
						}
					});

					AlertDialog nDialog= mPurposeNameDialog.create();

					nDialog.show();


					//setting background colors for both positive and negative buttons in name purpose dialog.
					Button pButton = (Button) nDialog.getButton(nDialog.BUTTON_POSITIVE);
					if(pButton!=null)
						pButton.setBackgroundColor(Color.LTGRAY);
					pButton.setTextColor(Color.BLACK);


					Button nButton = (Button) nDialog.getButton(nDialog.BUTTON_NEGATIVE);
					if(nButton!=null)
						nButton.setBackgroundColor(Color.LTGRAY);
					nButton.setTextColor(Color.BLACK);



				}
			});

			mSearchList.setAdapter(adapter);
		}

	}

	//class responsible for booking a room from the button.
	private class BookRoom extends AsyncTask <Booking, Boolean, Boolean>{

		ProgressDialog processDialog=new ProgressDialog(getActivity());
		HttpClient httpClient=null;
		HttpPost httpPost=null;

		//progress dialog to be shown untill the progress is finished.
		@Override
		protected void onPreExecute(){
			processDialog.setMessage("Booking room...");
			processDialog.show();

		}

		//things that will run in background while booking room.
		@Override
		protected Boolean doInBackground(Booking... newBooking) {

			httpClient= new DefaultHttpClient();

			//a link to web service responsible for room booking.

			try {
			//all details about booking is assigned to json object to be used in queries in php.
				JSONObject newBookingJS= new JSONObject();
				newBookingJS.put("booked_room", newBooking[0].getRoomName());
				newBookingJS.put("booking_start", mConverters.dateToString(newBooking[0].getBookingStart()));
				newBookingJS.put("booking_end", mConverters.dateToString(newBooking[0].getBookingEnd()));
				newBookingJS.put("booked_by",newBooking[0].getBookedBy());
				newBookingJS.put("name_purpose", newBooking[0].getBookingName());

				//a link assigned to httpPost for booking room.
				httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
				httpPost.setEntity(new StringEntity(newBookingJS.toString()));


				HttpResponse bookingResponse=httpClient.execute(httpPost);

				String bookingResult = mConverters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();

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
				//successfull feedback with pop up dialog.
				AlertDialog.Builder successfulDialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_DARK);
				successfulDialog.setTitle("Successful !");
				successfulDialog.setMessage(R.string.succesful_booking);

				successfulDialog.setPositiveButton("Done !", new DialogInterface.OnClickListener() {


					@Override
					public void onClick(DialogInterface successDialog, int which) {

						successDialog.dismiss();
					}
				});
				AlertDialog sDialog = successfulDialog.create();
				sDialog.show();


				//setting background color of positive button in successfull dialog
				Button sButton = (Button) sDialog.getButton(sDialog.BUTTON_POSITIVE);
				if(sButton!=null)
					sButton.setBackgroundColor(Color.GREEN);
				sButton.setTextColor(Color.BLACK);
			}
		}
	}
}
