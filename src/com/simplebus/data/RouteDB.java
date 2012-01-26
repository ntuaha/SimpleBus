package com.simplebus.data;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RouteDB{
    public String DATABASE_PATH;
	public static final String TABLE_NAME = "Route";
	private int MAX_TEMP_MEMORY;
	private ArrayList<DataPackage> array;
	
    public RouteDB(Context c,String path, String database_name, int database_version,int max_temp_memory) {
		MAX_TEMP_MEMORY = max_temp_memory;
		DATABASE_PATH = path;
		array = new  ArrayList<DataPackage>();
	}
    
	public void onCreate(SQLiteDatabase db) {
    	StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
    	sql.append(TABLE_NAME);
    	sql.append(" (id integer primary key autoincrement, tid INTEGER, ax REAL,ay REAL,az REAL,ox REAL,oy REAL,oz REAL, current_T REAL,score REAL)");
        db.execSQL(sql.toString());
	}
	
	private void insert(SQLiteDatabase db,long tid,ArrayList<DataPackage> a){
		int array_size = a.size();		
		ContentValues values = new ContentValues();
	    db.beginTransaction();
	    try {
	  		for (int i=0;i<array_size;i++)
			{
	  			DataPackage d = a.get(i);
				values.put("tid", tid);
				values.put("ax", d.acceleration[0]);
				values.put("ay", d.acceleration[1]);
				values.put("az", d.acceleration[2]);
				values.put("ox", d.outer_acceleration[0]);
				values.put("oy", d.outer_acceleration[1]);
				values.put("oz", d.outer_acceleration[2]);
				values.put("score", d.score);
				values.put("current_T", d.current_time);
				db.insert(TABLE_NAME, null, values);
				values.clear();
			}
	  		
		    db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	    
	}

	public void update(long tid,float current_time,float score){
		
	}
	
	public ArrayList<DataPackage> readTrace(SQLiteDatabase db,long tid)
	{
		ArrayList<DataPackage> array = new ArrayList<DataPackage>();
		Cursor cursor = db.query(TABLE_NAME, new String[]{"ax,ay,az,ox,oy,oz,current_T,score"}, "tid=?", new String[]{Long.toString(tid)}, null, null, null);
		while(cursor.moveToNext())
		{
			DataPackage data = new DataPackage();
			data.acceleration[0] = cursor.getDouble(0);
			data.acceleration[1] = cursor.getDouble(1);
			data.acceleration[2] = cursor.getDouble(2);
			data.outer_acceleration[0] = cursor.getDouble(3);
			data.outer_acceleration[1] = cursor.getDouble(4);
			data.outer_acceleration[2] = cursor.getDouble(5);
			data.current_time = cursor.getDouble(6);
			data.score = cursor.getDouble(7);
			array.add(data);
		}
		cursor.close();

		return array;
	}
	public int getDataNumber(SQLiteDatabase db,long tid)
	{
		if(array.size()>0)
		{
			insert(db,tid,array);
			array.clear();
		}
		int number=0;
		Cursor cursor = db.query(TABLE_NAME, new String[]{"ax"}, "tid=?", new String[]{Long.toString(tid)}, null, null, null);
		number = cursor.getCount();
		cursor.close();
		return number;
	}
	
	public void reset(){
		array.clear();
	}
	public void write(SQLiteDatabase db,long tid,double[] a,double[] o,double score,double current_time)
	{
		DataPackage data = new DataPackage();
		for(int i=0; i<3;i++)
		{
			data.acceleration[i] = a[i];
			data.outer_acceleration[i] = o[i];
		}
		data.score = score;
		data.current_time = current_time;
		array.add(data);
		if (array.size()>=MAX_TEMP_MEMORY)
		{
			insert(db,tid,array);
			array.clear();
		}
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);		
	}
	public void deleteTrace(SQLiteDatabase db,long tid){
		db.delete(TABLE_NAME, "tid=?", new String[]{Long.toString(tid)});	
	}
	private class DataPackage{
		double[] acceleration = new double[3];
		double[] outer_acceleration = new double[3];
		double score;
		double current_time;
	}
	public JSONArray getData(SQLiteDatabase db, long tid, int size,DecimalFormat df) {
		Cursor cursor = db.query(TABLE_NAME, new String[]{"ax","ay","az","ox","oy","oz","current_T","score","id"}, "tid=?", new String[]{Long.toString(tid)}, null, null, "id ASC LIMIT "+size);
		if (cursor.getCount()==0)
			return null;
		JSONArray array = new JSONArray();
		while(cursor.moveToNext())
		{
			JSONObject object = new JSONObject();
			try {
				object.put("ax",df.format(cursor.getDouble(0)));
				object.put("ay",df.format(cursor.getDouble(1)));
				object.put("az",df.format(cursor.getDouble(2)));
				object.put( "ox",df.format(cursor.getDouble(3)));
				object.put("oy",df.format(cursor.getDouble(4)));
				object.put("oz",df.format(cursor.getDouble(5)));
				object.put("current_time",df.format(cursor.getDouble(6)));
				object.put("score",cursor.getDouble(7));
				object.put("id",cursor.getLong(8));
				array.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		return array;
	}

	public void deleteData(SQLiteDatabase db, long tid, long start,long end) {
		db.delete(TABLE_NAME, "id BETWEEN ? AND ?", new String[]{Long.toString(start),Long.toString(end)});		
	}

}
