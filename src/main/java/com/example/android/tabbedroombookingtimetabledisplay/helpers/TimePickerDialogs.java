package com.example.android.tabbedroombookingtimetabledisplay.helpers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class TimePickerDialogs extends TimePickerDialog {

    public TimePickerDialogs(Context arg0, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(arg0, callBack, hourOfDay, minute, is24HourView);
        //not used
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

        //programatically changing the minutes by 30.
        if (stopEvent)
            return;
        if (minute% mMinuteBlock !=0){
            int mChaningFactor=minute-(minute% mMinuteBlock);
            minute=mChaningFactor + (minute==mChaningFactor+1 ? mMinuteBlock : 0);
            if (minute==60)
                minute=0;
            stopEvent =true;
            view.setCurrentMinute(minute);
            stopEvent =false;
        }
    }

    private final int mMinuteBlock =30;
    private boolean stopEvent = false;
}
 