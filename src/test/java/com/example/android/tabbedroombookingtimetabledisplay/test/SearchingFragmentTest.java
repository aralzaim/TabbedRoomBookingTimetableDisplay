package com.example.android.tabbedroombookingtimetabledisplay.test;

import com.example.android.tabbedroombookingtimetabledisplay.helpers.Converters;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by aralzaim on 21/08/15.
 */
public class SearchingFragmentTest {

    @Before
    public void setup() {

    }

    @Test
    public void searchRoomAllResults() {

        String emptyRoom;
        Converters converters = new Converters();
        HttpPost httpPost = null;
        HttpClient httpClient = new DefaultHttpClient();
        ArrayList<String> resultList = new ArrayList<String>();

        JSONObject newBookingJS = new JSONObject();

        try {
            newBookingJS.put("booking_start", "2015-08-12 09:00:00");
            newBookingJS.put("booking_end", "2015-08-12 09:30:00");
            newBookingJS.put("moveable_table", false);
            newBookingJS.put("projector", false);
            newBookingJS.put("multiple_computer", false);
            newBookingJS.put("capacity", -1);


            httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/searchRoom.php");
            httpPost.setEntity(new StringEntity(newBookingJS.toString()));


            HttpResponse httpResponse = httpClient.execute(httpPost);

            String jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();


            System.out.println(jsonResult);
            JSONObject jsonObj = new JSONObject(jsonResult);

            if (jsonObj != null) {
                JSONArray jsonBookings = jsonObj.getJSONArray("resultrooms");
                for (int i = 0; i < jsonBookings.length(); i++) {
                    JSONObject catObj = (JSONObject) jsonBookings.get(i);

                    emptyRoom = (catObj.getString("booked_room"));

                    resultList.add(emptyRoom);
                }
            }

            Assert.assertEquals(resultList.size(),13);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }




    @Test
    public void searchRoomNoResults() {

        String emptyRoom;
        Converters converters = new Converters();
        HttpPost httpPost = null;
        HttpClient httpClient = new DefaultHttpClient();
        ArrayList<String> resultList = new ArrayList<String>();

        JSONObject newBookingJS = new JSONObject();

        try {
            newBookingJS.put("booking_start", "2015-08-12 09:00:00");
            newBookingJS.put("booking_end", "2015-08-12 09:30:00");
            newBookingJS.put("moveable_table", false);
            newBookingJS.put("projector", 1);
            newBookingJS.put("multiple_computer", 1);
            newBookingJS.put("capacity", -1);


            httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/searchRoom.php");
            httpPost.setEntity(new StringEntity(newBookingJS.toString()));


            HttpResponse httpResponse = httpClient.execute(httpPost);

            String jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();


            System.out.println(jsonResult);
            JSONObject jsonObj = new JSONObject(jsonResult);

            if (jsonObj != null) {
                JSONArray jsonBookings = jsonObj.getJSONArray("resultrooms");
                for (int i = 0; i < jsonBookings.length(); i++) {
                    JSONObject catObj = (JSONObject) jsonBookings.get(i);

                    emptyRoom = (catObj.getString("booked_room"));

                    resultList.add(emptyRoom);
                }
            }

            Assert.assertEquals(resultList.size(),0);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}