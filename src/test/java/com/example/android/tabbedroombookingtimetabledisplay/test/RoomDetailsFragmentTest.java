package com.example.android.tabbedroombookingtimetabledisplay.test;

import android.util.Log;

import com.example.android.tabbedroombookingtimetabledisplay.RoomDetails;
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

/**
 * Created by aralzaim on 31/08/15.
 */
public class RoomDetailsFragmentTest {

    RoomDetails selectedRoomDetails;

    @Before
    public void setUp() throws Exception {


    }

            @Test
            public void testDetailsCapacity () {

                    HttpPost httpPost;
                    Converters converters = new Converters();
                    HttpClient httpClient = new DefaultHttpClient();
                    JSONObject newBookingJS = new JSONObject();
                    selectedRoomDetails = new RoomDetails();


                    try {


                        newBookingJS.put("room_name", "Seminar Room");
                        httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getDetails.php");
                        httpPost.setEntity(new StringEntity(newBookingJS.toString()));


                        HttpResponse httpResponse = httpClient.execute(httpPost);

                        String jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();

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
                        }

                    Assert.assertEquals(30, selectedRoomDetails.getCapacity());
                }
                catch (RuntimeException e){
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

    @Test
    public void testDetailsLocation () {
        HttpPost httpPost;
        Converters converters = new Converters();
        HttpClient httpClient = new DefaultHttpClient();
        JSONObject newBookingJS = new JSONObject();
        selectedRoomDetails = new RoomDetails();


        try {


            newBookingJS.put("room_name", "Seminar Room");
            httpPost = new HttpPost("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/getDetails.php");
            httpPost.setEntity(new StringEntity(newBookingJS.toString()));


            HttpResponse httpResponse = httpClient.execute(httpPost);

            String jsonResult = converters.inputStreamToString(httpResponse.getEntity().getContent()).toString();

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
            }

            Assert.assertEquals("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/images/seminar_room_location.jpeg", selectedRoomDetails.getLocationPic());
        }
        catch (RuntimeException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}