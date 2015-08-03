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
import com.squareup.picasso.Picasso;

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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by aralzaim on 31/07/15.
 */
public class RoomDetailsActivity extends Fragment {


    ImageView outside1;
    ImageView outside2;
    ImageView inside1;
    ImageView inside2;
    ImageView location;
    Converters converters= new Converters();
    Spinner roomSpinner;
    RoomDetails selectedRoomDetails;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) (inflater.inflate(R.layout.activity_details, container, false));

        roomSpinner = (Spinner) rootView.findViewById(R.id.spinner2);
        outside1=(ImageView) rootView.findViewById(R.id.outside_picture1);
        outside2=(ImageView) rootView.findViewById(R.id.outside_picture2);
        inside1=(ImageView) rootView.findViewById(R.id.inside_picture1);
        inside2=(ImageView) rootView.findViewById(R.id.inside_picture2);
        location=(ImageView) rootView.findViewById(R.id.location_picture);


        outside1.setVisibility(View.INVISIBLE);
        outside2.setVisibility(View.INVISIBLE);
        inside1.setVisibility(View.INVISIBLE);
        inside2.setVisibility(View.INVISIBLE);

        //  outside2.setImageBitmap(getBitmapFromURL("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/images/11824241_10153410531706206_1007425622_n.jpg"));
        GetRoomsBooking getRooms= new GetRoomsBooking();
            getRooms.execute();

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedRoomDetails=new RoomDetails();
                GetRoomDetails getRoomDetails= new GetRoomDetails();

                if(roomSpinner.getSelectedItem().toString().equals("Lab 0")){

                    selectedRoomDetails.setRoomName(roomSpinner.getSelectedItem().toString());
                    getRoomDetails.execute();

                    try {
                        getRoomDetails.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Picasso.with(getActivity()).load(selectedRoomDetails.getOutsidePic1()).resize(350, 250).into(outside1);
                    Picasso.with(getActivity()).load(selectedRoomDetails.getOutsidePic2()).resize(350,250).into(outside2);
                    Picasso.with(getActivity()).load(selectedRoomDetails.getInsidePic1()).resize(350, 250).into(inside1);
                    Picasso.with(getActivity()).load(selectedRoomDetails.getInsidePic2()).resize(350, 250).into(inside2);


                    outside1.setVisibility(View.VISIBLE);
                    outside2.setVisibility(View.VISIBLE);
                    inside1.setVisibility(View.VISIBLE);
                    inside2.setVisibility(View.VISIBLE);
                }
                else if(roomSpinner.getSelectedItem().toString().equals("Lab 1"))
                {

                }

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

    private class GetRoomDetails extends AsyncTask <String, Boolean, Boolean>{

        ProgressDialog processDialog=new ProgressDialog(getActivity());
        HttpClient httpClient=null;
        HttpPost httpPost=null;

        ArrayList<String> resultList = new ArrayList<String>();

        @Override
        protected void onPreExecute(){

        }


        @Override
        protected Boolean doInBackground(String... params) {

            httpClient = new DefaultHttpClient();

            try {


                JSONObject newBookingJS = new JSONObject();

                newBookingJS.put("room_name", selectedRoomDetails.getRoomName());


                httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getDetails.php");
                httpPost.setEntity(new StringEntity(newBookingJS.toString()));


                HttpResponse httpResponse = httpClient.execute(httpPost);

                String jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();


                Log.e("JSONRESULT", jsonResult);

                JSONObject jsonObj = new JSONObject(jsonResult);


                if (jsonObj != null) {
                    JSONArray details = jsonObj.getJSONArray("details");

                    for (int i = 0; i < details.length(); i++) {
                        JSONObject catObj = (JSONObject) details.get(i);


                        selectedRoomDetails.setCapacity((catObj.getInt("room_capacity")));
                        selectedRoomDetails.setOutsidePic1((catObj.getString("outside_picture1")));
                        selectedRoomDetails.setOutsidePic2((catObj.getString("outside_picture2")));
                        selectedRoomDetails.setInsidePic1((catObj.getString("inside_picture1")));
                        selectedRoomDetails.setInsidePic2((catObj.getString("inside_picture2")));
                        selectedRoomDetails.setComment1((catObj.getString("comment1")));
                        selectedRoomDetails.setComment2((catObj.getString("comment2")));
                        selectedRoomDetails.setResource1((catObj.getString("resource1")));
                        selectedRoomDetails.setResource2((catObj.getString("resource2")));
                        selectedRoomDetails.setResource3((catObj.getString("resource3")));
                        selectedRoomDetails.setResource4((catObj.getString("resource4")));
                        selectedRoomDetails.setLocationPic((catObj.getString("location")));

                    }


                    return true;

                } else {

                    return false;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();}

            return true;
        }

            @Override
            protected void onPostExecute(Boolean result){


        }



    }
}
