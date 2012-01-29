package com.simplebus.data;

import java.util.Calendar;

import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;

public class SimpleBusTime {
	public int year;
	public int month;
	public int day;
	public int hour;
	public int min;
	private TimePicker tp;
	private DatePicker dp;
	public SimpleBusTime(TimePicker timePicker,DatePicker datePicker){
		this.tp = timePicker;
		this.dp = datePicker;
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		this.dp.init(year, month, day, new OnDateChangedListener(){
			public void onDateChanged(DatePicker view, int y,int m, int d) {
				year = y;
				month = m;
				day = d;
			}
		});
		this.tp.setIs24HourView(true);
		this.tp.setCurrentHour(hour);
		this.tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				hour = hourOfDay;
				min = minute;				
			}
		});
	}
}
