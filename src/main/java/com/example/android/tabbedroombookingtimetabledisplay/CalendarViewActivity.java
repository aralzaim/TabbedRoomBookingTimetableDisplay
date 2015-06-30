package com.example.android.tabbedroombookingtimetabledisplay;

import android.app.ProgressDialog;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CalendarViewActivity extends Fragment implements WeekView.MonthChangeListener,
		WeekView.EventClickListener, WeekView.EventLongPressListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    Spinner spinner;
    Calendar today = Calendar.getInstance();
    ArrayList<Booking> bookingsArray=new ArrayList<Booking>();
    List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    DatePicker datepicker;
    Calendar date= Calendar.getInstance();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		RelativeLayout rootView = (RelativeLayout) (inflater.inflate(R.layout.activity_calendar_view, container, false));


        datepicker= (DatePicker) rootView.findViewById(R.id.date_picker);
        spinner= (Spinner) rootView.findViewById(R.id.spinner);
        mWeekView =(WeekView) rootView.findViewById(R.id.weekView);




        mWeekView.goToDate(today);


        datepicker.init(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {



                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.e("DAY", String.valueOf(dayOfMonth));
                Log.e("MONTH", String.valueOf(monthOfYear));
                Log.e("YEAR", String.valueOf(year));


                while ((date.get(Calendar.DAY_OF_WEEK)) != (Calendar.MONDAY)) {
                    date.add(Calendar.DATE, -1);
                }
                
                today=(Calendar) date.clone();
                mWeekView.goToDate(today);


            }
        });


        GetRoomsCalendar getRoomsCalendar= new GetRoomsCalendar();
        getRoomsCalendar.execute();

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);



        final Button previousButton = (Button) rootView.findViewById(R.id.previous_week);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                today.add(Calendar.WEEK_OF_YEAR, -1);
                datepicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                today.set(Calendar.DAY_OF_WEEK, today.getFirstDayOfWeek());
                mWeekView.goToDate(today);
            }
        });

        final Button todayButton = (Button) rootView.findViewById(R.id.today);
        todayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                today.setTime(new Date());
                datepicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                today.set(Calendar.DAY_OF_WEEK, today.getFirstDayOfWeek());
                mWeekView.goToDate(today);
            }
        });
        final Button nextButton = (Button) rootView.findViewById(R.id.next_week);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                today.add(Calendar.WEEK_OF_YEAR, 1);
                datepicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                today.set(Calendar.DAY_OF_WEEK, today.getFirstDayOfWeek());
                mWeekView.goToDate(today);
            }
        });


		return rootView;
	}

    public List<WeekViewEvent> onMonthChange() {


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                mWeekView.notifyDatasetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        if (spinner.getSelectedItemPosition()!=0 && spinner.getSelectedItem()!=null) {

            String roomSelected;
            // Populate the week view with some events.

                roomSelected= spinner.getSelectedItem().toString();

            Log.e("ICERIDE", roomSelected);

             GetBookingsCalendar getBookingsTask=new GetBookingsCalendar();
             getBookingsTask.execute(roomSelected);

            try {
                Boolean str_result= getBookingsTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Calendar startTime=Calendar.getInstance();
            Calendar endTime=Calendar.getInstance();
            WeekViewEvent event= new WeekViewEvent();

            events= new ArrayList<WeekViewEvent>();


            for(int i=0; bookingsArray.size()>i;i++) {

                Log.e("ALOOOO", String.valueOf(i));

                startTime = Calendar.getInstance();
                startTime.setTime(bookingsArray.get(i).getBookingStart());

                Log.e("ALOOOO", String.valueOf(bookingsArray.get(i).getBookingEnd()));

               endTime = (Calendar) startTime.clone();
                endTime.setTime(bookingsArray.get(i).getBookingEnd());


               event = new WeekViewEvent(1, getEventTitle(startTime, endTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.abc_background_cache_hint_selector_material_dark));
                events.add(event);


            }

            return events;
        }
        return null;
        }


	private String getEventTitle(Calendar startTime,Calendar endTime) {
		return String.format("Booked between %02d:%02d - %02d:%02d", startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), endTime.get(Calendar.HOUR_OF_DAY),endTime.get(Calendar.MINUTE));
	}

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getActivity(), "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getActivity(), "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    private class GetRoomsCalendar extends AsyncTask <String, Void, Boolean>{

        ProgressDialog fetchingDialog;
        HttpClient httpClient=null;
        HttpGet httpGet=null;
        HttpResponse httpResponse=null;
        ArrayList<String> roomNames = new ArrayList<>();
        Converters converters =new Converters();

        @Override
        protected void onPreExecute(){
            fetchingDialog = new ProgressDialog(getActivity());
            fetchingDialog.setMessage("Fetching Rooms..");
            fetchingDialog.setCancelable(false);
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
            try {
                roomNames.add("Select a room...");
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
        @Override
        protected void onPostExecute(Boolean result){
            createSpinner(roomNames);
            fetchingDialog.dismiss();

        }

        public void createSpinner(ArrayList<String> roomNames) {

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, roomNames);

            spinnerAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(spinnerAdapter);
        }

    }

    private class GetBookingsCalendar extends AsyncTask <String, Void, Boolean>{

        ProgressDialog fetchingDialog;
        HttpClient httpClient=null;
        HttpGet httpGet=null;
        HttpPost httpPost=null;
        HttpResponse httpResponse=null;
        ArrayList<String> roomNames = new ArrayList<>();
        Converters converters =new Converters();

        @Override
        protected void onPreExecute(){
            fetchingDialog = new ProgressDialog(getActivity());
            fetchingDialog.setMessage("Loading Bookings..");
             fetchingDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {

            httpClient= new DefaultHttpClient();


            try {
                JSONObject bookingJS= new JSONObject();

                bookingJS.put("room_name", params[0]);

                bookingsArray= new ArrayList<Booking>();

                httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getBookings.php");
                httpPost.setEntity(new StringEntity(bookingJS.toString()));

                HttpResponse bookingResponse=httpClient.execute(httpPost);


                jsonRoomParse(bookingResponse);



            } catch (IOException e) {
                e.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;

        }

        public void jsonRoomParse(HttpResponse httpResponse) {
            String jsonResult;
            Booking booking= new Booking();
            try {

                jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();

                JSONObject jsonObj = new JSONObject(jsonResult);

                if (jsonObj != null) {
                    JSONArray bookings = jsonObj.getJSONArray("bookings");

                    for (int i = 0; i < bookings.length(); i++) {
                        JSONObject catObj = (JSONObject) bookings.get(i);


                        booking= new Booking();
                        booking.setBookedBy(catObj.getString("booked_by"));
                        booking.setBookingStart(converters.stringToDate(catObj.getString("booking_start")));
                        booking.setBookingEnd(converters.stringToDate(catObj.getString("booking_end")));

                        bookingsArray.add(booking);
                        Log.e("KOPKOP2", String.valueOf(bookingsArray.get(i).getBookingStart()));
                    }
                }

            }catch (IllegalStateException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        @Override
        protected void onPostExecute(Boolean result){
            fetchingDialog.dismiss();

        }



    }


}