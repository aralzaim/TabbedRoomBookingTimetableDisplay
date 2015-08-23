package com.example.android.tabbedroombookingtimetabledisplay.test;

import com.example.android.tabbedroombookingtimetabledisplay.Booking;
import com.example.android.tabbedroombookingtimetabledisplay.helpers.Converters;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by aralzaim on 21/08/15.
 */
public class AvailableRoomActivityTest {

    @Before
    public void setup(){

    }
    @Test
    public void bookingNumber106(){
        HttpClient httpClient=null;
        HttpGet httpGet=null;
        HttpPost httpPost=null;
        HttpResponse httpResponse=null;
        ArrayList<Booking> bookingsArray;
        Booking booking = new Booking();
        String jsonResult;
        Converters converters= new Converters();
    try
    {
        JSONObject bookingJS = new JSONObject();

        bookingJS.put("room_name", "1.06 Meeting Room");

        bookingsArray = new ArrayList<Booking>();

        httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getBookings.php");
        httpPost.setEntity(new StringEntity(bookingJS.toString()));

        HttpResponse bookingResponse = httpClient.execute(httpPost);


        jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();

        JSONObject jsonObj = new JSONObject(jsonResult);

        if (jsonObj != null) {
            JSONArray bookings = jsonObj.getJSONArray("bookings");

            for (int i = 0; i < bookings.length(); i++) {
                JSONObject catObj = (JSONObject) bookings.get(i);


                booking = new Booking();
                booking.setBookedBy(catObj.getString("booked_by"));
                booking.setBookingStart(converters.stringToDate(catObj.getString("booking_start")));
                booking.setBookingEnd(converters.stringToDate(catObj.getString("booking_end")));
                booking.setBookingName(catObj.getString("name_purpose"));

                bookingsArray.add(booking);

            }
        }
        Assert.assertEquals(bookingsArray.size(),27);
    } catch (JSONException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ParseException e) {
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (RuntimeException e){
        e.printStackTrace();
    }
    }
}
