package com.example.android.tabbedroombookingtimetabledisplay;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.android.tabbedroombookingtimetabledisplay.helpers.Converters;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aralzaim on 31/07/15.
 */
public class RoomDetailsActivity extends Fragment {


    ImageView outside1;
    ImageView outside2;
    Converters converters= new Converters();
    Spinner roomSpinner;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) (inflater.inflate(R.layout.activity_details, container, false));

        roomSpinner = (Spinner) rootView.findViewById(R.id.spinner2);
        outside1=(ImageView) rootView.findViewById(R.id.imageView);
        outside2=(ImageView) rootView.findViewById(R.id.imageView2);




     //   Picasso.with(getActivity()).load("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/images/GOPR3414.JPG").into(outside2);
      //  outside2.setImageBitmap(getBitmapFromURL("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/images/GOPR3414.PNG"));
        GetRoomsBooking getRooms= new GetRoomsBooking();
            getRooms.execute();

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(roomSpinner.getSelectedItem().toString().equals("Lab 0"))
                    outside1.setImageResource(R.drawable.image_one);
                else
                    outside1.setImageResource(R.drawable.ic_action_booking);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        return rootView;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    private class GetRoomsBooking extends AsyncTask <String, Void, Boolean>{

        ProgressDialog fetchingDialog;
        HttpClient httpClient=null;
        HttpGet httpGet=null;
        HttpResponse httpResponse=null;
        ArrayList<String> roomNames = new ArrayList<>();

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
