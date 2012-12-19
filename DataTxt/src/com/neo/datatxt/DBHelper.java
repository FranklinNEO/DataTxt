package com.neo.datatxt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static DBHelper mInstance = null;
	/** 数据库名称 **/
	public static final String DATABASE_NAME = "info.db";
	/** 数据库版本号 **/
	private static final int DATABASE_VERSION = 1;
	/** DB对象 **/
	SQLiteDatabase mDb = null;

	Context mContext = null;

	public final static String TABLE_NAME = "info_date";
	public final static String ID = "_id";
	public final static String NAMES = "name";
	public final static String ABBR = "abbr";
	public final static String CODE = "code";
	public final static String TIMES = "times";
	// 索引ID
	public final static int ID_INDEX = 0;
	public final static int NAMES_INDEX = 1;
	public final static int ABBR_INDEX = 2;
	public final static int CODE_INDEX = 3;
	public final static int TIMES_INDEX = 4;
	/** 数据库SQL语句 创建表 **/
	public static final String NAME_TABLE_CREATE = "create table info_date("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT NOT NULL,"
			+ "abbr TEXT NOT NULL," + "code TEXT NOT NULL,"
			+ "times INTEGER NOT NULL);";

	/** 单例模式 **/
	public static synchronized DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	}

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// 得到数据库对象
		mDb = getReadableDatabase();
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建数据库
		db.execSQL(NAME_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 
	 * 插入一条数据
	 * 
	 * @param key
	 * @param date
	 */
	public void insert(String tablename, String name, String abbr, String code) {

		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("abbr", abbr);
		values.put("code", code);
		values.put("times", 1);
		mDb.insert(tablename, null, values);
		values.clear();
	}

	public void update(String tablename, String name, String abbr, String code,
			int times) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("abbr", abbr);
		values.put("code", code);
		values.put("times", times + 1);
		mDb.update(tablename, values, "name='" + name + "' AND abbr='" + abbr
				+ "' AND code='" + code + "';", null);
		values.clear();
	}

}
