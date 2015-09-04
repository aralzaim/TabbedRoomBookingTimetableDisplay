package com.example.android.tabbedroombookingtimetabledisplay.com.alamkanak.weekview.library;

import java.util.Calendar;

/**
 * Created by Raquib on 1/6/2015.
 * Website: http://alamkanak.github.io/
 */
public interface DateTimeInterpreter {
    String interpretDate(Calendar date);
    String interpretTime(int hour);
}
