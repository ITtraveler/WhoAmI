package com.tc.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 此表封装了对数据库的一些操作
 * 
 * @author hgs
 *
 */
public class DatabaseUtils {
	public static final String DBNAME = "mydb.db";
	public static final String TBNAME = "user";
	private Context context;
	private SQLiteDatabase db;
	public static String SQL_CT = "create table user(id Integer primary key,username,password,behaviourFileName,reportFileName)";
	public static String SQL_Q = "select * from user";
	public DatabaseUtils(Context context) {
		this.context = context;
	}

	public void openDatabase() {
		db = context.openOrCreateDatabase(DBNAME, context.MODE_PRIVATE, null);
	}

	public void create(String sql) {
		db.beginTransaction();
		db.execSQL(sql);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void insert(String sql) {
		db.beginTransaction();
		db.execSQL(sql);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void delete(String sql) {
		db.beginTransaction();
		db.execSQL(sql);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public Cursor query(String sql) {
		db.beginTransaction();
		Cursor cursor = db.rawQuery(sql, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		return cursor;
	}

	/**
	 * 判断数据表是否存在
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean isExsit(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {

			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void closeDB() {
		db.close();
	}
}
