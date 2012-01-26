package com.simplebus.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




// Singleton
public class StorageManager extends SQLiteOpenHelper {
	private volatile static StorageManager uniqueInstance;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME="SimpleBus";
	private static final String DATABASE_PATH =android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/SimpleBus/BasicInformation.db";

	public static final String EDIT_TRACE="edit_trace";


	private RouteDB routeDB;
	private StopDB stopDB;

	public long handleTid;


	private SQLiteDatabase writedb;
	private Context context;



	public StorageManager(Context c){
		super(c, DATABASE_PATH, null, DATABASE_VERSION);
		routeDB = new RouteDB(c,DATABASE_PATH,DATABASE_NAME,DATABASE_VERSION,10);
		stopDB = new StopDB(c,DATABASE_PATH,DATABASE_NAME,DATABASE_VERSION,10);
		context = c;
		writedb = null;		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		routeDB.onCreate(db);
		stopDB.onCreate(db);

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		routeDB.onUpgrade(db, oldVersion, newVersion);
		stopDB.onUpgrade(db, oldVersion, newVersion);

	}
	public static StorageManager getInstance(Context c){
		if (uniqueInstance == null){
			synchronized (StorageManager.class){
				if(uniqueInstance == null)
				{
					uniqueInstance = new StorageManager(c);
				}
			}
		}
		return uniqueInstance;
	}
	public SQLiteDatabase getDB(){
		if(writedb==null){
			synchronized (this){
				writedb = getWritableDatabase();
			}
		}
		if(!writedb.isOpen()){
			closeDB();
			synchronized (this){
				writedb = getWritableDatabase();
			}
		}
		return writedb;
	}
	public void closeDB(){
		if(writedb!=null){
			synchronized (this){				
				writedb.close();
				writedb = null;						
			}
		}

	}




}
