package com.example.android.tabbedroombookingtimetabledisplay.test;

import com.example.android.tabbedroombookingtimetabledisplay.BookingActivity;
import com.example.android.tabbedroombookingtimetabledisplay.helpers.Converters;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.example.android.tabbedroombookingtimetabledisplay.BookingActivity.*;


/**
 * Created by aralzaim on 21/08/15.
 */

public class BookingActivityTest {


        @Before
        public void setUp() throws Exception {
        }

        @Test
        public void testCollisionOne() {

            JSONObject newBookingJS= new JSONObject();
            Converters converters= new Converters();
            HttpPost httpPost=null;
            HttpClient httpClient= new DefaultHttpClient();

            try {
                newBookingJS.put("booked_room", "lab0");
                newBookingJS.put("booking_start","2015-06-16 09:00:00");
                newBookingJS.put("booking_end","2015-06-16 09:30:00");

                System.out.println(newBookingJS.toString());

                httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
                httpPost.setEntity(new StringEntity(newBookingJS.toString()));


                HttpResponse bookingResponse=httpClient.execute(httpPost);

                String bookingResult = converters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();

                System.out.println(bookingResult);


                Assert.assertEquals("Collision:", bookingResult);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (RuntimeException e){
        e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }



    @Test
    public void testCollisionTwo() {

        JSONObject newBookingJS= new JSONObject();
        Converters converters= new Converters();
        HttpPost httpPost=null;
        HttpClient httpClient= new DefaultHttpClient();

        try {
            newBookingJS.put("booked_room", "lab0");
            newBookingJS.put("booking_start","2015-06-16 08:00:00");
            newBookingJS.put("booking_end","2015-06-16 09:45:00");

            System.out.println(newBookingJS.toString());

            httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
            httpPost.setEntity(new StringEntity(newBookingJS.toString()));


            HttpResponse bookingResponse=httpClient.execute(httpPost);

            String bookingResult = converters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();

            System.out.println(bookingResult);


            Assert.assertEquals("Collision:", bookingResult);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    @Test
    public void testCollisionThree() {

        JSONObject newBookingJS= new JSONObject();
        Converters converters= new Converters();
        HttpPost httpPost=null;
        HttpClient httpClient= new DefaultHttpClient();

        try {
            newBookingJS.put("booked_room", "lab0");
            newBookingJS.put("booking_start","2015-06-16 09:00:00");
            newBookingJS.put("booking_end","2015-06-16 09:45:00");

            System.out.println(newBookingJS.toString());

            httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
            httpPost.setEntity(new StringEntity(newBookingJS.toString()));


            HttpResponse bookingResponse=httpClient.execute(httpPost);

            String bookingResult = converters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();

            System.out.println(bookingResult);


            Assert.assertEquals("Collision:", bookingResult);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Test
    public void testCollisionFour() {

        JSONObject newBookingJS= new JSONObject();
        Converters converters= new Converters();
        HttpPost httpPost=null;
        HttpClient httpClient= new DefaultHttpClient();

        try {
            newBookingJS.put("booked_room", "lab0");
            newBookingJS.put("booking_start","2015-06-16 09:00:00");
            newBookingJS.put("booking_end","2015-06-16 09:15:00");

            System.out.println(newBookingJS.toString());

            httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
            httpPost.setEntity(new StringEntity(newBookingJS.toString()));


            HttpResponse bookingResponse=httpClient.execute(httpPost);

            String bookingResult = converters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();

            System.out.println(bookingResult);


            Assert.assertEquals("Collision:", bookingResult);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Test
    public void testNoCollision() {

        JSONObject newBookingJS= new JSONObject();
        Converters converters= new Converters();
        HttpPost httpPost=null;
        HttpClient httpClient= new DefaultHttpClient();

        try {
            newBookingJS.put("booked_room", "lab0");
            newBookingJS.put("booking_start","2015-06-16 10:00:00");
            newBookingJS.put("booking_end","2015-06-16 10:45:00");

            System.out.println(newBookingJS.toString());

            httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/bookingRoom.php");
            httpPost.setEntity(new StringEntity(newBookingJS.toString()));


            HttpResponse bookingResponse=httpClient.execute(httpPost);

            String bookingResult = converters.inputStreamToString(bookingResponse.getEntity().getContent()).toString();

            System.out.println(bookingResult);


            Assert.assertNotSame("Collision:", bookingResult);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    }