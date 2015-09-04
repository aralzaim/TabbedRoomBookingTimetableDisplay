package com.example.android.tabbedroombookingtimetabledisplay;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by aralzaim on 31/07/15.
 */
public class RoomDetailsFragment extends Fragment {


    ImageView mOutside1;
    ImageView mOutside2;
    ImageView mInside1;
    ImageView mInside2;
    ImageView mLocation;

    TextView mOutside;
    TextView mInside;
    TextView mDetails;
    TextView mCapacityTitle;
    TextView mCapacityText;
    TextView mResourcesTitle;
    TextView mResource1;
    TextView mResource2;
    TextView mResource3;
    TextView mResource4;
    TextView mCommentsTitle;
    TextView mComment1;
    TextView mComment2;
    TextView mLocationTitle;

    ArrayList<String> mComments = new ArrayList<>();
    ArrayList<String> mResources = new ArrayList<>();

    Converters mConverters = new Converters();
    Spinner mRoomSpinner;

    RoomDetails mRoomDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) (inflater.inflate(R.layout.activity_details, container, false));

      //initialization of all elements in the user interface

        mRoomSpinner = (Spinner) rootView.findViewById(R.id.spinner2);

        mOutside1 =(ImageView) rootView.findViewById(R.id.outside_picture1);
        mOutside2 =(ImageView) rootView.findViewById(R.id.outside_picture2);
        mInside1 =(ImageView) rootView.findViewById(R.id.inside_picture1);
        mInside2 =(ImageView) rootView.findViewById(R.id.inside_picture2);
        mLocation =(ImageView) rootView.findViewById(R.id.location_picture);

        mDetails =(TextView) rootView.findViewById(R.id.details_text);
        mInside = (TextView) rootView.findViewById(R.id.inside_text);
        mOutside =(TextView) rootView.findViewById(R.id.outside_text);
        mCapacityTitle =(TextView) rootView.findViewById(R.id.capacity_title);
        mCapacityText =(TextView) rootView.findViewById(R.id.capacity_text);
        mResourcesTitle =(TextView) rootView.findViewById(R.id.resources_title);
        mResource1 =(TextView) rootView.findViewById(R.id.resource1);
        mResource2 =(TextView) rootView.findViewById(R.id.resource2);
        mResource3 =(TextView) rootView.findViewById(R.id.resource3);
        mResource4 =(TextView) rootView.findViewById(R.id.resource4);
        mCommentsTitle =(TextView) rootView.findViewById(R.id.comment_title);
        mComment1 =(TextView) rootView.findViewById(R.id.comment1);
        mComment2 =(TextView) rootView.findViewById(R.id.comment2);
        mLocationTitle =(TextView) rootView.findViewById(R.id.location_title);


        //setting visibility of all elements, in the beginning they are all invisible.
        mInside.setVisibility(View.INVISIBLE);
        mOutside.setVisibility(View.INVISIBLE);
        mDetails.setVisibility(View.INVISIBLE);
        mOutside1.setVisibility(View.INVISIBLE);
        mInside1.setVisibility(View.INVISIBLE);
        mOutside2.setVisibility(View.INVISIBLE);
        mInside2.setVisibility(View.INVISIBLE);
        mLocation.setVisibility(View.INVISIBLE);
        mLocationTitle.setVisibility(View.INVISIBLE);
        mCapacityTitle.setVisibility(View.INVISIBLE);
        mCapacityText.setVisibility(View.INVISIBLE);
        mResourcesTitle.setVisibility(View.INVISIBLE);
        mResource1.setVisibility(View.INVISIBLE);
        mResource2.setVisibility(View.INVISIBLE);
        mResource3.setVisibility(View.INVISIBLE);
        mResource4.setVisibility(View.INVISIBLE);
        mCommentsTitle.setVisibility(View.INVISIBLE);
        mComment1.setVisibility(View.INVISIBLE);
        mComment2.setVisibility(View.INVISIBLE);


    //getting rooms to put in spinner.
      GetRoomsBooking getRooms= new GetRoomsBooking();
            getRooms.execute();

        mRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //when any value in spinner is selected
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                mRoomDetails = new RoomDetails();
                GetRoomDetails getRoomDetails = new GetRoomDetails();


                //if there is any room selected, make fields visible.
                if (!mRoomSpinner.getSelectedItem().toString().equalsIgnoreCase("Select a room...")) {

                    mInside.setVisibility(View.VISIBLE);
                    mOutside.setVisibility(View.VISIBLE);
                    mDetails.setVisibility(View.VISIBLE);
                    mOutside1.setVisibility(View.VISIBLE);
                    mInside1.setVisibility(View.VISIBLE);
                    mOutside2.setVisibility(View.VISIBLE);
                    mInside2.setVisibility(View.VISIBLE);
                    mLocation.setVisibility(View.VISIBLE);
                    mLocationTitle.setVisibility(View.VISIBLE);
                    mCapacityTitle.setVisibility(View.VISIBLE);

                    mCapacityText.setVisibility(View.INVISIBLE);
                    mResourcesTitle.setVisibility(View.INVISIBLE);
                    mResource1.setVisibility(View.INVISIBLE);
                    mResource2.setVisibility(View.INVISIBLE);
                    mResource3.setVisibility(View.INVISIBLE);
                    mResource4.setVisibility(View.INVISIBLE);
                    mCommentsTitle.setVisibility(View.INVISIBLE);
                    mComment1.setVisibility(View.INVISIBLE);
                    mComment2.setVisibility(View.INVISIBLE);


                    mRoomDetails.setRoomName(mRoomSpinner.getSelectedItem().toString());

                   //getting all details about room from details
                    getRoomDetails.execute();

                    //pooling untill getRooms.execute finishes all implementations
                    try {
                        getRoomDetails.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                //clearing and redeclaring the resources and comments arrays to refresh
                    mResources.clear();
                    mComments.clear();
                    mResources = new ArrayList<>();
                    mComments = new ArrayList<>();


                    //setting Capacity value.
                    mCapacityTitle.setText("Capacity");
                    mCapacityText.setText(Integer.toString(mRoomDetails.getCapacity()));
                    mCapacityText.setVisibility(View.VISIBLE);


                    //setting resources available, necessary implementation is done to show all the resources neatly in user interface.
                    mResourcesTitle.setVisibility(View.VISIBLE);

                    if (!mRoomDetails.getResource1().equalsIgnoreCase("NULL")) {
                        mResources.add(mRoomDetails.getResource1());
                    }
                    if (!mRoomDetails.getResource2().equalsIgnoreCase("NULL")) {
                        mResources.add(mRoomDetails.getResource2());
                    }
                    if (!mRoomDetails.getResource3().equalsIgnoreCase("NULL")) {
                        mResources.add(mRoomDetails.getResource3());
                    }
                    if (!mRoomDetails.getResource4().equalsIgnoreCase("NULL")) {
                        mResources.add(mRoomDetails.getResource4());
                    }


                    for (int i = 0; i < mResources.size(); i++) {

                        if (i == 0) {
                            mResource1.setText(mResources.get(i));
                            mResource1.setVisibility(View.VISIBLE);
                        }
                        if (i == 1) {
                            mResource2.setText(mResources.get(i));
                            mResource2.setVisibility(View.VISIBLE);
                        }
                        if (i == 2) {
                            mResource3.setText(mResources.get(i));
                            mResource3.setVisibility(View.VISIBLE);
                        }
                        if (i == 3) {
                            mResource4.setText(mResources.get(i));
                            mResource4.setVisibility(View.VISIBLE);
                        }

                    }

                    if (mResources.size() <= 0) {
                        mResource1.setText("No resources added.");
                        mResource1.setVisibility(View.VISIBLE);
                        mResource2.setVisibility(View.INVISIBLE);
                        mResource3.setVisibility(View.INVISIBLE);
                        mResource4.setVisibility(View.INVISIBLE);
                    }

                    //setting comments available, necessary implementation is done to show all the comments neatly in user interface.
                    mCommentsTitle.setVisibility(View.VISIBLE);

                    if (!mRoomDetails.getComment1().equalsIgnoreCase("NULL")) {
                        mComments.add(mRoomDetails.getComment1());
                    }

                    if (!mRoomDetails.getComment2().equalsIgnoreCase("NULL")) {
                        mComments.add(mRoomDetails.getComment2());
                    }

                    for (int i = 0; i < mComments.size(); i++) {

                        if (i == 0) {
                            mComment1.setText(mComments.get(i));
                            mComment1.setVisibility(View.VISIBLE);
                        }
                        if (i == 1) {
                            mComment2.setText(mComments.get(i));
                            mComment2.setVisibility(View.VISIBLE);
                        }

                    }

                    if (mComments.size() <= 0) {
                        mComment1.setText("No comments added.");
                        mComment1.setVisibility(View.VISIBLE);
                        mComment2.setVisibility(View.INVISIBLE);
                    }

                //showing all the pictures with the help of Picasso library. http://square.github.io/picasso/
                    Picasso.with(getActivity()).load(mRoomDetails.getOutsidePic1()).resize(350, 250).into(mOutside1);
                    Picasso.with(getActivity()).load(mRoomDetails.getOutsidePic2()).resize(350, 250).into(mOutside2);
                    Picasso.with(getActivity()).load(mRoomDetails.getInsidePic1()).resize(350, 250).into(mInside1);
                    Picasso.with(getActivity()).load(mRoomDetails.getInsidePic2()).resize(350, 250).into(mInside2);
                    Picasso.with(getActivity()).load(mRoomDetails.getLocationPic()).resize(400, 250).into(mLocation);

                //if there is no room selected, make all elements invisible on the user interface.
                } else if (mRoomSpinner.getSelectedItem().toString().equalsIgnoreCase("Select a room...")) {
                    mInside.setVisibility(View.INVISIBLE);
                    mOutside.setVisibility(View.INVISIBLE);
                    mDetails.setVisibility(View.INVISIBLE);
                    mOutside1.setVisibility(View.INVISIBLE);
                    mInside1.setVisibility(View.INVISIBLE);
                    mOutside2.setVisibility(View.INVISIBLE);
                    mInside2.setVisibility(View.INVISIBLE);
                    mLocation.setVisibility(View.INVISIBLE);
                    mLocationTitle.setVisibility(View.INVISIBLE);
                    mCapacityTitle.setVisibility(View.INVISIBLE);
                    mCapacityText.setVisibility(View.INVISIBLE);
                    mResourcesTitle.setVisibility(View.INVISIBLE);
                    mResource1.setVisibility(View.INVISIBLE);
                    mResource2.setVisibility(View.INVISIBLE);
                    mResource3.setVisibility(View.INVISIBLE);
                    mResource4.setVisibility(View.INVISIBLE);
                    mCommentsTitle.setVisibility(View.INVISIBLE);
                    mComment1.setVisibility(View.INVISIBLE);
                    mComment2.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

//returning view with all the elements in it
        return rootView;
    }

    //class for getting rooms to make a spinner
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

            //php service link to get bookable rooms.
            httpGet = new HttpGet("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getRooms.php");

            try {

                //getting response
                httpResponse= httpClient.execute(httpGet);

                //parsing the response to json object.
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
                    jsonResult = mConverters.inputStreamToString(httpResponse.getEntity().getContent()).toString();
                    JSONObject jsonObj = new JSONObject(jsonResult);
                    if (jsonObj != null) {
                        JSONArray rooms = jsonObj.getJSONArray("rooms");

                        for (int i = 0; i < rooms.length(); i++) {
                            JSONObject catObj = (JSONObject) rooms.get(i);
                            //adding retrieved rooms to array.
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
            //creating spinner with all rooms
            createSpinner(roomNames);
            fetchingDialog.dismiss();

        }

        //method to create spinner.
        public void createSpinner(ArrayList<String> roomNames) {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, roomNames);
            spinnerAdapter.setDropDownViewResource(R.layout.list_item);
            mRoomSpinner.setAdapter(spinnerAdapter);
        }

    }


    //class to get all details about selected room.
    private class GetRoomDetails extends AsyncTask <String, Boolean, Boolean>{

        ProgressDialog processDialog=new ProgressDialog(getActivity());
        HttpClient httpClient=null;
        HttpPost httpPost=null;
        ProgressDialog fetchingDialog;

        @Override
        protected void onPreExecute(){
           fetchingDialog = new ProgressDialog(getActivity());
            fetchingDialog.setMessage("Getting Details..");
            fetchingDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {

            httpClient = new DefaultHttpClient();

            try {


                JSONObject newBookingJS = new JSONObject();

                newBookingJS.put("room_name", mRoomDetails.getRoomName());

            //php service link to get details about rooms
                httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getDetails.php");
                httpPost.setEntity(new StringEntity(newBookingJS.toString()));


                HttpResponse httpResponse = httpClient.execute(httpPost);
                String jsonResult = mConverters.inputStreamToString(httpResponse.getEntity().getContent()).toString();
                JSONObject jsonObj = new JSONObject(jsonResult);


                if (jsonObj != null) {
                    JSONArray details = jsonObj.getJSONArray("details");

                    //getting every single details for selected rooms
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject catObj = (JSONObject) details.get(i);
                        mRoomDetails.setCapacity((catObj.getInt("room_capacity")));
                        mRoomDetails.setOutsidePic1((catObj.getString("outside_picture1")));
                        mRoomDetails.setOutsidePic2((catObj.getString("outside_picture2")));
                        mRoomDetails.setInsidePic1((catObj.getString("inside_picture1")));
                        mRoomDetails.setInsidePic2((catObj.getString("inside_picture2")));
                        mRoomDetails.setComment1((catObj.getString("comment1")));
                        mRoomDetails.setComment2((catObj.getString("comment2")));
                        mRoomDetails.setResource1((catObj.getString("resource1")));
                        mRoomDetails.setResource2((catObj.getString("resource2")));
                        mRoomDetails.setResource3((catObj.getString("resource3")));
                        mRoomDetails.setResource4((catObj.getString("resource4")));
                        mRoomDetails.setLocationPic((catObj.getString("location")));
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
                fetchingDialog.dismiss();
        }

    }
}
