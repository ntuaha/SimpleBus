package com.simplebus.data;

import java.util.Calendar;

import com.simplebus.Data;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TimePicker;

public class Information {
	public int year;
	public int month;
	public int day;
	public int hour;
	public int min;
	public String busid;
	private Data[] data = new Data[3];
	public Information(){
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		busid = "";
		data[0] = new Data("公車車號",busid);
		data[1] = new Data("日期",year+"-"+(month+1)+"-"+day);
		data[2] = new Data("時間",hour+":"+min);

	}
	public boolean setDate(Context context,final BaseAdapter adapter){
		new DatePickerDialog(context,new OnDateSetListener() {			
			public void onDateSet(DatePicker view, int y, int monthOfYear,int dayOfMonth) {
				year = y;
				month = monthOfYear;
				day = dayOfMonth;
				data[1].content = year+"-"+(month+1)+"-"+day;
				adapter.notifyDataSetChanged();
			}
		},year,month,day).show();
		return true;
	}
	public boolean setTime(Context context,final BaseAdapter adapter){
		new TimePickerDialog(context,new OnTimeSetListener() {			
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				hour = hourOfDay;
				min = minute;
				data[2].content = hour+":"+min;
				adapter.notifyDataSetChanged();

			}
		},hour,min,true).show();
		return true;
	}
	public boolean setBusId(Context context,final BaseAdapter adapter){
		AlertDialog.Builder editDialog = new AlertDialog.Builder(context);
		editDialog.setTitle("--- Tell us which bus you want to know ---");		    
		final EditText editText = new EditText(context);
		editText.setText(busid);
		editDialog.setView(editText);	    
		editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				busid = editText.getText().toString();
				data[0].content = busid;
				adapter.notifyDataSetChanged();
			}
		});
		editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// Do nothing
			}
		});
		editDialog.show();
		return true;
	}
	public Data[] getData(){
		return data;
	}
}
