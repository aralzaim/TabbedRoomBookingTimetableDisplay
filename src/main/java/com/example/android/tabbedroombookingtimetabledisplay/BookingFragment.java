package com.example.android.tabbedroombookingtimetabledisplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.tabbedroombookingtimetabledisplay.helpers.Checkers;
import com.example.android.tabbedroombookingtimetabledisplay.helpers.Converters;


public class BookingFragment extends Fragment implements OnClickListener {

	TimePicker mStartTimePicker;
	TimePicker mEndTimePicker;
	DatePicker mDatePicker;

	Button mSubmitButton;
	Spinner mRoomSpinner;
	EditText mPurposeNameBooking;

    ArrayList<String> mRoomNames = new ArrayList<>();

    TextView mInvalidTimeText;
    TextView mDateOldText;
	TextView mStarWarning;


	Converters mConverters = new Converters();
	Checkers mCheckers = new Checkers();

	private int mAvailableBookingStartHour =9;
	private int mAvailableBookingStartMinute =00;


	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          RelativeLayout rootView = (RelativeLayout)(inflater.inflate(R.layout.activity_booking, container, false));

		//initialization of the user interface elements.
		mRoomSpinner = (Spinner) rootView.findViewById(R.id.room_spinner);

        mInvalidTimeText = (TextView) rootView.findViewById(R.id.time_error_text);
        mInvalidTimeText.setVisibility(View.INVISIBLE);
        mInvalidTimeText.setTextColor(Color.RED);

		mDateOldText = (TextView) rootView.findViewById(R.id.date_error_text);
		mDateOldText.setVisibility(View.INVISIBLE);
		mDateOldText.setTextColor(Color.RED);


		//class responsible for getting room names from database to insert them insde spinner
		GetRoomsBooking getRoomTask=new GetRoomsBooking();

		//Execution for getting values from database.
		getRoomTask.execute();


		//initialization of user interface elements and listeners of them
		mSubmitButton =(Button) rootView.findViewById(R.id.submit_button);
		mSubmitButton.setOnClickListener(this);


		mStartTimePicker = (TimePicker) rootView.findViewById(R.id.time_picker_start);
		mStartTimePicker.setIs24HourView(true);
		mStartTimePicker.setCurrentHour(mAvailableBookingStartHour);
		mStartTimePicker.setCurrentMinute(mAvailableBookingStartMinute);


		mEndTimePicker = (TimePicker) rootView.findViewById(R.id.time_picker_end);
		mEndTimePicker.setIs24HourView(true);
		mEndTimePicker.setCurrentHour((mAvailableBookingStartHour) + 1);
		mEndTimePicker.setCurrentMinute(mAvailableBookingStartMinute);

		mPurposeNameBooking = (EditText) rootView.findViewById(R.id.name_booking);

		mStarWarning =(TextView) rootView.findViewById(R.id.name_warning);
		mStarWarning.setTextColor(Color.RED);
		
		mDatePicker = (DatePicker) rootView.findViewById(R.id.datePicker);

			//listener when a hour or a minute is changed for start time.
		 mStartTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			 @Override
			 public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

				//default 1 hour for booking
				 int newEndHour = (mStartTimePicker.getCurrentHour()) + 1;
				 int newEndMinute = mStartTimePicker.getCurrentMinute();

				//implementing 30 minutes of blocks for minute spinner.
				 final int mTimeBlock = 30;
				 boolean mStopEvent = false;

				 if (mStopEvent)
					 return;

				 if (minute % mTimeBlock != 0) {
					 int mChangeFactor = minute - (minute % mTimeBlock);
					 minute = mChangeFactor + (minute == mChangeFactor + 1 ? mTimeBlock : 0);
					 if (minute == 60)
						 minute = 0;
					 mStopEvent = true;
					 mStartTimePicker.setCurrentMinute(minute);
					 mStopEvent = false;
				 }
				 if (newEndMinute >= 60) {
					 newEndHour++;
				 }


				 //checking if selected time is between 09.00 and 21.00, if start time is before end time and checking if date is older than today.
				 if (mCheckers.startTimeValidity(mStartTimePicker) && mCheckers.endTimeValidity(mStartTimePicker, mEndTimePicker) && !mCheckers.dateOlder(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth())) {
					//if statement above is true date and time warnings will be invisible and button will be enable
					 mInvalidTimeText.setVisibility(View.INVISIBLE);
					 mDateOldText.setVisibility(View.INVISIBLE);
					 mSubmitButton.setEnabled(true);

				 }
				 //one of above statements are wrong
				 else {
					//checking if the time is problematic
					 if (!mCheckers.startTimeValidity(mStartTimePicker) || !mCheckers.endTimeValidity(mStartTimePicker, mEndTimePicker)) {

						 mInvalidTimeText.setVisibility(View.VISIBLE);
						 mDateOldText.setVisibility(View.INVISIBLE);

					 }
					 //checking if date is problematic
					 else if (mCheckers.dateOlder(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth())) {

						 mDateOldText.setVisibility(View.VISIBLE);
						 mInvalidTimeText.setVisibility(View.INVISIBLE);

					 }
					 //if any of date or time is problematic, set the submit button disabled.
					 mSubmitButton.setEnabled(false);
				 }

				//automatically making end time default 1 hour more than start time.
				 if (hourOfDay > mEndTimePicker.getCurrentHour() || (mEndTimePicker.getCurrentHour().equals(hourOfDay) && mEndTimePicker.getCurrentMinute() <= minute)) {
					 mEndTimePicker.setCurrentHour(newEndHour);
					 mEndTimePicker.setCurrentMinute(newEndMinute);
				 }

			 }
		 });

		//listener when a hour or a minute is changed for end time.
		 mEndTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			 @Override
			 public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

				 //implementing 30 minutes of blocks for minute spinner.
				 final int mTimeBlock = 30;
				 boolean mStopEvent = false;
				 if (mStopEvent)
					 return;
				 if (minute % mTimeBlock != 0) {
					 int mChangeFactor = minute - (minute % mTimeBlock);
					 minute = mChangeFactor + (minute == mChangeFactor + 1 ? mTimeBlock : 0);
					 if (minute == 60)
						 minute = 0;
					 mStopEvent = true;
					 mEndTimePicker.setCurrentMinute(minute);
					 mStopEvent = false;
				 }

				 //checking if end time or date older is problematic
				 if (mCheckers.endTimeValidity(mStartTimePicker, mEndTimePicker) && !mCheckers.dateOlder(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth())) {

					 //if they are not problematic, remove warnings and enable button
					 mSubmitButton.setEnabled(true);
					 mInvalidTimeText.setVisibility(View.INVISIBLE);

					 mDateOldText.setVisibility(View.INVISIBLE);
				 }

				 else {
					 //checking if date is problematic
					 if (mCheckers.dateOlder(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth())) {

						 mDateOldText.setVisibility(View.VISIBLE);
						 mInvalidTimeText.setVisibility(View.INVISIBLE);
					 }
					//checking if end time is problematic
					 else if (!mCheckers.endTimeValidity(mStartTimePicker, mEndTimePicker)) {

						 mDateOldText.setVisibility(View.INVISIBLE);
						 mInvalidTimeText.setVisibility(View.VISIBLE);

					 }
					 //if any of date or time is problematic, set the submit button disabled.
					 mSubmitButton.setEnabled(false);
				 }
			 }


		 });

		//listener when date is changed.
		mDatePicker.init(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				
				//checking if date is older than today, end time is problematic, if start time is problematic
				if(mCheckers.dateOlder(year,monthOfYear,dayOfMonth) || !mCheckers.endTimeValidity(mStartTimePicker, mEndTimePicker) || !mCheckers.startTimeValidity(mStartTimePicker))
				{

					//checking if its only date problematic
					if(mCheckers.dateOlder(year,monthOfYear,dayOfMonth)) {

						mDateOldText.setVisibility(View.VISIBLE);
						mInvalidTimeText.setVisibility(View.INVISIBLE);

					}
					//checking if start or end time is problematic
					else if(!mCheckers.endTimeValidity(mStartTimePicker, mEndTimePicker) || !mCheckers.startTimeValidity(mStartTimePicker)){

						mInvalidTimeText.setVisibility(View.VISIBLE);
						mDateOldText.setVisibility(View.INVISIBLE);

					}
					//if any variable is problematic, set the button disabled.
					mSubmitButton.setEnabled(false);

				}
				//all variables are without any problems, so set button enabled.
				else {

					mInvalidTimeText.setVisibility(View.INVISIBLE);
					mDateOldText.setVisibility(View.INVISIBLE);
                    mSubmitButton.setEnabled(true);

                }
                }

		});
		//returning all the view.
		return rootView;
	    

		
	}

	//action which will be taken when the submit button will be clicked.
	@Override
	public void onClick(View v) {

        String mRoomName;
        int mYear=0;
        int mMonth=0;
        int mDay=0;
        int mStartHour = 0;
        int mStartMinute = 0;
        int mEndHour=0;
        int mEndMinute=0;

        String mStartDateTimestamp;
        String mEndDateTimestamp;

        BookRoom bookingTask;

        Date startParsedDateTime=null;
        Date endParsedDateTime=null;

		String mNamePurpose=null;

        Booking mNewBooking= new Booking();

        mRoomName= mRoomSpinner.getSelectedItem().toString();

        mYear= mDatePicker.getYear();
        mMonth= mDatePicker.getMonth()+1;
        mDay= mDatePicker.getDayOfMonth();

        mStartHour= mStartTimePicker.getCurrentHour();
        mStartMinute= mStartTimePicker.getCurrentMinute();

        mEndHour= mEndTimePicker.getCurrentHour();
        mEndMinute= mEndTimePicker.getCurrentMinute();

        mStartDateTimestamp=mYear+"-"+mMonth+"-"+mDay + " " + mStartHour +":"+ mStartMinute;
        mEndDateTimestamp = mYear+"-"+mMonth+"-"+mDay + " " + mEndHour +":"+ mEndMinute;

		//giving error if no room is selected or the purpose is empty
      if(!mRoomName.equalsIgnoreCase("select a room to book..." ) && !mPurposeNameBooking.getText().toString().matches("")) {
		  try {

			  //converting string objects to the to date objects
			  startParsedDateTime = mConverters.stringToDate(mStartDateTimestamp);
			  endParsedDateTime = mConverters.stringToDate(mEndDateTimestamp);

			  //getting purpose/name for booking
			  mNamePurpose= mPurposeNameBooking.getText().toString();

			//setting values to newBooking object
			  mNewBooking.setBookingStart(startParsedDateTime);
			  mNewBooking.setBookingEnd(endParsedDateTime);
			  mNewBooking.setRoomName(mRoomName);
			  mNewBooking.setBookingName(mNamePurpose);


		  } catch (Exception e) {

			  e.printStackTrace();
		  }
		//bookingTask is for connecting to php and make necessary queries with newBooking object.
		  bookingTask = new BookRoom();
		  bookingTask.execute(mNewBooking);



	  }
	  //if room is not selected give warning
		else if (mRoomName.equalsIgnoreCase("select a room to book..." )) {
		  Toast toast = Toast.makeText(getActivity(), "Select a room to book.", Toast.LENGTH_LONG);
		  toast.setGravity(Gravity.CENTER, 0, 0);
		  toast.show();
	  }
	  //if purpose or name is not selected, give warning
		else if(mPurposeNameBooking.getText().toString().matches("")){
		  Toast toast = Toast.makeText(getActivity(), "Enter purpose or name for booking.", Toast.LENGTH_LONG);
		  toast.setGravity(Gravity.CENTER, 0, 0);
		  toast.show();
	  }
	}

	//class responsible for booking a room through
	public class BookRoom extends AsyncTask <Booking, Boolean, Boolean>{

		ProgressDialog processDialog=new ProgressDialog(getActivity());
		HttpClient httpClient=null;
		HttpPost httpPost=null;


		//actions to be taken before execution of task.
		@Override
		protected void onPreExecute(){
			processDialog.setMessage("Booking room...");
			processDialog.show();
			
		}
		
		//method that has all implementations in task.
		@Override
		protected Boolean doInBackground(Booking... newBooking) {

			httpClient= new DefaultHttpClient();

				try {

					//putting all booking information to the json object.
					JSONObject newBookingJS= new JSONObject();
					newBookingJS.put("booked_room", newBooking[0].getRoomName());
					newBookingJS.put("booking_start", mConverters.dateToString(newBooking[0].getBookingStart()));
					newBookingJS.put("booking_end", mConverters.dateToString(newBooking[0].getBookingEnd()));
					newBookingJS.put("booked_by",newBooking[0].getBookedBy());
					newBookingJS.put("name_purpose", newBooking[0].getBookingName());


					//httpPost was assigned as bookingRoom.php adress
					httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
                    httpPost.setEntity(new StringEntity(newBookingJS.toString()));
                    
                    //getting response of the php service
                    HttpResponse bookingResponse=httpClient.execute(httpPost);

					//converting response from php to string to check what is the value.
                	String bookingResult = mConverters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();


					//checking if the response from  php is Collision, or a success message.
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
		
		
		//method responsible to do necessary implementations after execution
		@Override
		protected void onPostExecute(Boolean result){

			//if the result is successful give pop up feedback
			if(result==true)
			{
				processDialog.dismiss();

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


				//changing background and text color of positive button on successfulAlert
				Button sButton = (Button) sDialog.getButton(sDialog.BUTTON_POSITIVE);
				if(sButton!=null)
				sButton.setBackgroundColor(Color.GREEN);
				sButton.setTextColor(Color.BLACK);


			// reseting values after succesful booking
				Calendar today= Calendar.getInstance();
				mPurposeNameBooking.setText("");
				mRoomSpinner.setSelection(0);
				mDatePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
				mStartTimePicker.setCurrentHour(mAvailableBookingStartHour);
				mStartTimePicker.setCurrentMinute(mAvailableBookingStartMinute);
				mEndTimePicker.setCurrentHour(mAvailableBookingStartHour + 1);
				mEndTimePicker.setCurrentMinute(mAvailableBookingStartMinute);
			}

			//if the booking is not successful
			else
			{
				processDialog.dismiss();

				AlertDialog.Builder collisionDialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_DARK);

				collisionDialog.setTitle("Collision !");
				collisionDialog.setMessage(R.string.unsuccesful_booking);
				collisionDialog.setPositiveButton("Retry !", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface colliDialog, int which) {
						colliDialog.dismiss();
					}
				});

				AlertDialog cDialog= collisionDialog.create();

				cDialog.show();

				//changing background and text color of positive button on collisionAlert
				Button fButton= (Button) cDialog.getButton(cDialog.BUTTON_POSITIVE);
				if(fButton!=null)
				fButton.setBackgroundColor(Color.RED);
				fButton.setTextColor(Color.BLACK);
				}
		}

	}
//class responsible for getting rooms into spinner
	private class GetRoomsBooking extends AsyncTask <String, Void, Boolean>{

		ProgressDialog fetchingDialog;
		HttpClient httpClient=null;
		HttpGet httpGet=null;
		HttpResponse httpResponse=null;
	

		@Override
		protected void onPreExecute(){

			fetchingDialog = new ProgressDialog(getActivity());
			fetchingDialog.setMessage("Fetching Rooms..");
			fetchingDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			httpClient= new DefaultHttpClient();
			//getRooms.php has queries to get all bookable rooms from web service.
			httpGet = new HttpGet("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getRooms.php");

			try {

				//getting the response (result) and parsing it to json object
				httpResponse= httpClient.execute(httpGet);
				jsonRoomParse(httpResponse);

			} catch (IOException e) {

				e.toString();
			}
			return true;

		}

		public void jsonRoomParse(HttpResponse httpResponse) {

			String jsonResult;
            if(mRoomNames.size()<=0) {
                try {
					//making first room name Select a room
                    mRoomNames.add("Select a room to book...");
                    jsonResult = mConverters.inputStreamToString(httpResponse.getEntity().getContent()).toString();
                    JSONObject jsonObj = new JSONObject(jsonResult);

                    if (jsonObj != null) {
                        JSONArray rooms = jsonObj.getJSONArray("rooms");

                        for (int i = 0; i < rooms.length(); i++) {
                            JSONObject catObj = (JSONObject) rooms.get(i);
							//adding room names retrieved from database to array
                            mRoomNames.add(catObj.getString("room_name"));
                        }
                    }

                } catch (IllegalStateException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

		}
		@Override
		protected void onPostExecute(Boolean result){
			//creating spinner with room names
			createSpinner(mRoomNames);
			fetchingDialog.dismiss();

		}
		//creating an spinner with room names taken from database.
		public void createSpinner(ArrayList<String> roomNames) {

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, roomNames);
			spinnerAdapter.setDropDownViewResource(R.layout.list_item);
			mRoomSpinner.setAdapter(spinnerAdapter);
		}

	}












}


