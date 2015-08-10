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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.tabbedroombookingtimetabledisplay.helpers.Checkers;
import com.example.android.tabbedroombookingtimetabledisplay.helpers.Converters;


public class BookingActivity extends Fragment implements OnClickListener {

	TimePicker startTimePicker;
	TimePicker endTimePicker;
	DatePicker dateSelected;
	Button submitBtn;
	Spinner roomSpinner;
	Converters converters = new Converters();
	Checkers checkers= new Checkers();
    Button selectBtn;
    ArrayList<String> roomNames= new ArrayList<>();
    TextView invalidTimeText;
    TextView dateOldText;
	TextView roomText;


	
	
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          RelativeLayout rootView = (RelativeLayout)(inflater.inflate(R.layout.activity_booking, container, false));




		
		int availableBookingStartHour=9;
		int availableBookingStartMinute=00;
		
		
		roomSpinner= (Spinner) rootView.findViewById(R.id.room_spinner);

        invalidTimeText = (TextView) rootView.findViewById(R.id.button_invisible_error);
        invalidTimeText.setVisibility(View.INVISIBLE);
        invalidTimeText.setTextColor(Color.RED);

		dateOldText= (TextView) rootView.findViewById(R.id.date_error_text);
		dateOldText.setVisibility(View.INVISIBLE);
		dateOldText.setTextColor(Color.RED);

	//	roomText= (TextView) rootView.findViewById(R.id.room_text);
	//	roomText.setVisibility(View.VISIBLE);
	//	roomText.setTextColor(Color.RED);


		GetRoomsBooking getRoomTask=new GetRoomsBooking();

		getRoomTask.execute();

		submitBtn=(Button) rootView.findViewById(R.id.submit_button);
		submitBtn.setOnClickListener(this);

	//	roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	//		@Override
	//		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
///
	//			if(roomSpinner.getSelectedItemPosition()==0){
	//				roomText.setVisibility(View.VISIBLE);
	//				submitBtn.setEnabled(false);
	//			}
	//			else if(checkers.endTimeValidity(startTimePicker, endTimePicker) || !checkers.dateOlder(dateSelected.getYear(), dateSelected.getMonth(), dateSelected.getDayOfMonth())||roomSpinner.getSelectedItemPosition()!=0){
	//				dateOldText.setVisibility(View.INVISIBLE);
	//				invalidTimeText.setVisibility(View.INVISIBLE);
//
	//				if(roomSpinner.getSelectedItemPosition()!=0) {
	//					roomText.setVisibility(View.INVISIBLE);
	//				}
	//				else if(checkers.dateOlder(dateSelected.getYear(),dateSelected.getMonth(),dateSelected.getDayOfMonth()))
	//				{
	//					dateOldText.setVisibility(View.INVISIBLE);
	//				}
	//				else if(!checkers.endTimeValidity(startTimePicker, endTimePicker)){
	//					dateOldText.setVisibility(View.INVISIBLE);
	//				}
	//				submitBtn.setEnabled(true);
	//			}
	//		}

	//		@Override
	//		public void onNothingSelected(AdapterView<?> parent) {

	//		}
	//		});




			//selectBtn =(Button) rootView.findViewById(R.id.button);
      //  selectBtn.setOnClickListener(new OnClickListener() {
    //        @Override
  //          public void onClick(View v) {
//
    //            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

  //              builderSingle.setIcon(R.drawable.ic_launcher);
//                builderSingle.setTitle("Select a room...");




                //final ListAdapter adapter = new ListAdapter(getActivity(), roomNames);
              //  adapter.setCustomButtonListner(new ListAdapter.customButtonListener() {
                    //@Override
                    //public void onButtonClickListner(int position, String value) {
                  //      Toast.makeText(getActivity(),"Details"+ value,Toast.LENGTH_SHORT).show();
                //    }
              //  });
            //    adapter.setCustomTextListner(new ListAdapter.customTextListener(){
          //          @Override
        //            public void onTextClickListner(int position, String value) {
      //                  Toast.makeText(getActivity(),"Itself"+ value,Toast.LENGTH_SHORT).show();
    //                }
  //              });
//
    //            builderSingle.setNegativeButton("cancel",
  //                      new DialogInterface.OnClickListener() {
//
          //                  @Override
        //                    public void onClick(DialogInterface dialog, int which) {
      //                          dialog.dismiss();
    //                        }
  //                      });
//
    //            builderSingle.setAdapter(adapter,
  //                      new DialogInterface.OnClickListener() {
//
                    //        @Override
                  //          public void onClick(DialogInterface dialog, int which) {
                //                String strName = adapter.getItem(which);
              //                  AlertDialog.Builder builderInner = new AlertDialog.Builder(
            //                            getActivity());
          //                      builderInner.setMessage(strName);
        //                        builderInner.setTitle("Your Selected Item is");
      //                          selectBtn.setText(strName);
    //                            builderInner.setPositiveButton("Ok",
  //                                      new DialogInterface.OnClickListener() {
//
                      //                      @Override
                    //                        public void onClick(
                  //                                  DialogInterface dialog,
                //                                    int which) {
              //                                  dialog.dismiss();
             //                               }
           //                             });
           //                     builderInner.show();
         //                   }
        //                });
         //       builderSingle.show();

        //    }
       // });

		startTimePicker= (TimePicker) rootView.findViewById(R.id.time_picker_start);
		startTimePicker.setIs24HourView(true);
		startTimePicker.setCurrentHour(availableBookingStartHour);
		startTimePicker.setCurrentMinute(availableBookingStartMinute);
		
		endTimePicker= (TimePicker) rootView.findViewById(R.id.time_picker_end);
		endTimePicker.setIs24HourView(true);
		endTimePicker.setCurrentHour(availableBookingStartHour);
		endTimePicker.setCurrentMinute(availableBookingStartMinute+30);
		
		dateSelected = (DatePicker) rootView.findViewById(R.id.datePicker);
		
			
		 startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

	            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
	            	
	            int newEndHour=startTimePicker.getCurrentHour();
	            int newEndMinute=startTimePicker.getCurrentMinute()+30;
	            
	            if(newEndMinute>=60){
	            	newEndHour++;
	            }
	            
           	if(checkers.startTimeValidity(startTimePicker) && checkers.endTimeValidity(startTimePicker, endTimePicker) && !checkers.dateOlder(dateSelected.getYear(), dateSelected.getMonth(), dateSelected.getDayOfMonth()) )/*&& roomSpinner.getSelectedItemPosition()!=0)*/ {
                invalidTimeText.setVisibility(View.INVISIBLE);
				dateOldText.setVisibility(View.INVISIBLE);
                submitBtn.setEnabled(true);
            }
            else {
				if(!checkers.startTimeValidity(startTimePicker) || !checkers.endTimeValidity(startTimePicker, endTimePicker)){

					invalidTimeText.setVisibility(View.VISIBLE);
					dateOldText.setVisibility(View.INVISIBLE);
				}
				else if(checkers.dateOlder(dateSelected.getYear(), dateSelected.getMonth(), dateSelected.getDayOfMonth())){
					dateOldText.setVisibility(View.VISIBLE);
					invalidTimeText.setVisibility(View.INVISIBLE);
				}
		//		else if(roomSpinner.getSelectedItemPosition()==0){
		//			dateOldText.setVisibility(View.INVISIBLE);
		//			invalidTimeText.setVisibility(View.INVISIBLE);
		//			 roomText.setVisibility(View.VISIBLE);
		//		}
                submitBtn.setEnabled(false);
            }
	            
	            
	            	if(hourOfDay>endTimePicker.getCurrentHour()||(endTimePicker.getCurrentHour().equals(hourOfDay) && endTimePicker.getCurrentMinute()<minute)){
						 endTimePicker.setCurrentHour(newEndHour);
						  endTimePicker.setCurrentMinute(newEndMinute);
					}

	            }
	        });
		 
		 endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				
				if(checkers.endTimeValidity(startTimePicker, endTimePicker) && !checkers.dateOlder(dateSelected.getYear(), dateSelected.getMonth(), dateSelected.getDayOfMonth()) /*&& roomSpinner.getSelectedItemPosition()!=0*/) {
                    submitBtn.setEnabled(true);
                    invalidTimeText.setVisibility(View.INVISIBLE);
					dateOldText.setVisibility(View.INVISIBLE);
                }
                else {
					if(checkers.dateOlder(dateSelected.getYear(), dateSelected.getMonth(), dateSelected.getDayOfMonth())){
						dateOldText.setVisibility(View.VISIBLE);
						invalidTimeText.setVisibility(View.INVISIBLE);
					}
					else if(!checkers.endTimeValidity(startTimePicker,endTimePicker)){
						dateOldText.setVisibility(View.INVISIBLE);
						invalidTimeText.setVisibility(View.VISIBLE);

					}
			//		else if(roomSpinner.getSelectedItemPosition()==0){
			//			dateOldText.setVisibility(View.INVISIBLE);
			//			invalidTimeText.setVisibility(View.INVISIBLE);
			//			roomText.setVisibility(View.VISIBLE);
			//		}

					submitBtn.setEnabled(false);
                }
                }



		 });
		 
		dateSelected.init(dateSelected.getYear(), dateSelected.getMonth(), dateSelected.getDayOfMonth(), new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				
				
				if(checkers.dateOlder(year,monthOfYear,dayOfMonth) || !checkers.endTimeValidity(startTimePicker, endTimePicker) || !checkers.startTimeValidity(startTimePicker) /*|| roomSpinner.getSelectedItemPosition()==0*/)
				{
					if(checkers.dateOlder(year,monthOfYear,dayOfMonth)) {
						dateOldText.setVisibility(View.VISIBLE);
						invalidTimeText.setVisibility(View.INVISIBLE);
					}

					else if(!checkers.endTimeValidity(startTimePicker, endTimePicker) || !checkers.startTimeValidity(startTimePicker)){
						invalidTimeText.setVisibility(View.VISIBLE);
						dateOldText.setVisibility(View.INVISIBLE);
					}
				//	else if(roomSpinner.getSelectedItemPosition()==0)
			//		{
		//				dateOldText.setVisibility(View.INVISIBLE);
	//					invalidTimeText.setVisibility(View.INVISIBLE);
	//					roomText.setVisibility(View.VISIBLE);
	//				}

					submitBtn.setEnabled(false);
				}
				else {
					invalidTimeText.setVisibility(View.INVISIBLE);
					dateOldText.setVisibility(View.INVISIBLE);
                    submitBtn.setEnabled(true);
                }
                }

		});
		return rootView;
	    

		
	}


	@Override
	public void onClick(View v) {

        String roomName;
        int year=0;
        int month=0;
        int day=0;
        int startHour = 0;
        int startMinute = 0;
        int endHour=0;
        int endMinute=0;
        String startDateTimestamp;
        String endDateTimestamp;
        BookRoom bookingTask;
        Date startParsedDateTime=null;
        Date endParsedDateTime=null;
        Booking newBooking= new Booking();


         //selectBtn.getText().toString();

        roomName=roomSpinner.getSelectedItem().toString();

        year=dateSelected.getYear();
        month=dateSelected.getMonth()+1;
        day=dateSelected.getDayOfMonth();

        startHour=startTimePicker.getCurrentHour();
        startMinute=startTimePicker.getCurrentMinute();

        endHour=endTimePicker.getCurrentHour();
        endMinute=endTimePicker.getCurrentMinute();

        startDateTimestamp=year+"-"+month+"-"+day + " " + startHour +":"+ startMinute;
        endDateTimestamp = year+"-"+month+"-"+day + " " + endHour +":"+ endMinute;

      if(!roomName.equalsIgnoreCase("select a room to book...")) {
		  try {

			  startParsedDateTime = converters.stringToDate(startDateTimestamp);
			  endParsedDateTime = converters.stringToDate(endDateTimestamp);


			  newBooking.setBookingStart(startParsedDateTime);
			  newBooking.setBookingEnd(endParsedDateTime);
			  newBooking.setRoomName(roomName);


		  } catch (Exception e) {

			  e.printStackTrace();
		  }

		  bookingTask = new BookRoom();
		  bookingTask.execute(newBooking);

	  }
		else {
		  Toast toast = Toast.makeText(getActivity(), "Select a room to book.", Toast.LENGTH_LONG);
		  toast.setGravity(Gravity.CENTER, 0, 0);
		  toast.show();
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
			//	System.out.println("CONNECTED AND CAME BACK AS TRUE");
				processDialog.dismiss();
			//	Toast toast = Toast.makeText(getActivity(), R.string.succesful_booking, Toast.LENGTH_LONG);
			//	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			//	View view = toast.getView();
			//	view.setBackgroundColor(Color.GREEN);
			//	toast.show();


				AlertDialog.Builder successfulDialog = new AlertDialog.Builder(
						                            getActivity(),AlertDialog.THEME_HOLO_DARK);

						                        successfulDialog.setTitle("Successful !");
												successfulDialog.setMessage(R.string.succesful_booking);
						                            successfulDialog.setPositiveButton("Done !", new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.dismiss();
														}
													});
															                     successfulDialog.show();
			}
			else
			{
				
				System.out.println("CONNECTED AND CAME BACK AS FALSE");
				processDialog.dismiss();
				//Toast toast = Toast.makeText(getActivity(), R.string.unsuccesful_booking, Toast.LENGTH_LONG);
			//	toast.setGravity(Gravity.CENTER, 0, 0);
				
			//	View view = toast.getView();
			//	view.setBackgroundColor(Color.RED);
			//	toast.show();


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
				collisionDialog.show();

				}
			
		}



	}

	private class GetRoomsBooking extends AsyncTask <String, Void, Boolean>{

		ProgressDialog fetchingDialog;
		HttpClient httpClient=null;
		HttpGet httpGet=null;
		HttpResponse httpResponse=null;
	//	ArrayList<String> roomNames = new ArrayList<>();

		@Override
		protected void onPreExecute(){

			fetchingDialog = new ProgressDialog(getActivity());
			fetchingDialog.setMessage("Fetching Rooms..");
			fetchingDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			httpClient= new DefaultHttpClient();
			httpGet = new HttpGet("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getRooms.php");

			try {
				httpResponse= httpClient.execute(httpGet);


				jsonRoomParse(httpResponse);

			} catch (IOException e) {

				e.toString();
			}
			return true;

		}

		public void jsonRoomParse(HttpResponse httpResponse) {
			String jsonResult;

            if(roomNames.size()<=0) {
                try {
                    roomNames.add("Select a room to book...");
                    jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();

                    JSONObject jsonObj = new JSONObject(jsonResult);

                    if (jsonObj != null) {
                        JSONArray rooms = jsonObj.getJSONArray("rooms");

                        for (int i = 0; i < rooms.length(); i++) {
                            JSONObject catObj = (JSONObject) rooms.get(i);

                            roomNames.add(catObj.getString("room_name"));
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
			createSpinner(roomNames);
			fetchingDialog.dismiss();

		}

		public void createSpinner(ArrayList<String> roomNames) {

            //MyCustomAdapter adapter = new MyCustomAdapter(roomNames, this);

            //handle listview and assign adapter
            //ListView lView = (ListView)findViewById(R.id.my_listview);
            //lView.setAdapter(adapter);



            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
					R.layout.spinner_item, roomNames);

			spinnerAdapter
					.setDropDownViewResource(R.layout.list_item);

			roomSpinner.setAdapter(spinnerAdapter);
		}

	}












}


