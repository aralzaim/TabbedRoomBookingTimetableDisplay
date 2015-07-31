package com.example.android.tabbedroombookingtimetabledisplay;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by aralzaim on 31/07/15.
 */
public class RoomDetailsActivity extends Fragment {


    ImageView outside1;
    ImageView outside2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) (inflater.inflate(R.layout.activity_details, container, false));

        outside1=(ImageView) rootView.findViewById(R.id.imageView);
        outside2=(ImageView) rootView.findViewById(R.id.imageView2);


        //Picasso.with(getActivity()).load("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/images/GOPR3417.JPG").into(outside1);

        //Picasso.with(getActivity()).load("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/images/GOPR3414.JPG").into(outside2);
      //  outside2.setImageBitmap(getBitmapFromURL("https://zeno.computing.dundee.ac.uk/2014-msc/aralzaim/images/GOPR3414.PNG"));


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


}
