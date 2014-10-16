package com.testapp.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ArraySQLSavedList<E extends Serializable> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3202773106484459652L;
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE %1$s "
			+ FeedEntry.COLUMN_STRING;
	private static final String WHERE_CLAUSE = FeedEntry.COLUMN_DataIndex
			+ " LIKE ?";

	
	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS %1$s";
	private static final String TAG = "ArraySQLSavedList";

	private String mTableName;

	protected Context mContext;
	private FeedReaderDbHelper mDbHelper;

	public class FeedReaderDbHelper extends SQLiteOpenHelper {
		public static final String DATABASE_NAME = "WMSData.db";

 		public FeedReaderDbHelper(Context context) {
			super(context, DATABASE_NAME, null, FeedEntry.DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			String sql = String.format(SQL_CREATE_ENTRIES, mTableName);

			db.execSQL(sql);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql = String.format(SQL_DELETE_ENTRIES, mTableName);
			db.execSQL(sql);
			onCreate(db);

		}

		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
			onUpgrade(db, oldVersion, newVersion);
		}
	}

	public static abstract class FeedEntry implements BaseColumns {
		// If you change the database schema, you must increment the database
		// version.
		public static final int DATABASE_VERSION = 3;

		public static final String COLUMN_DATA = "Data";
		public static final String COLUMN_DataIndex = "CellNum";
		public static final String COLUMN_STRING = " (" + _ID
				+ " INTEGER PRIMARY KEY" + ", " + COLUMN_DataIndex + " INTEGER"
				+ ", " + COLUMN_DATA + " BLOB" + ") ";
	}

	@Override
	public boolean add(E object) {

		boolean result = super.add(object);
		save(size() - 1);
		return result;
	}

	@Override
	public void add(int index, E object) {
		super.add(index, object);
		save(index);

	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		// TODO Auto-generated method stub
		boolean addAll = super.addAll(collection);
		SaveAll();
		return addAll;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		boolean addAll = super.addAll(index, collection);
		SaveAll();
		return addAll;
	}

	@Override
	public E remove(int index) {
		RemoveSQL(index + 1);
		FetchSQL(index);
		return super.remove(index);
	}

	@Override
	public boolean remove(Object object) {
		int ind = indexOf(object);
		if (ind != -1) {
			RemoveSQL(ind + 1);
			FetchSQL(ind);
		}

		return super.remove(object);
	}

	public ArraySQLSavedList(String TableName, Context context) {
		super();

		mTableName = TableName;
		this.mContext = context;
		mDbHelper = new FeedReaderDbHelper(mContext);
		CheckExist(TableName);

		Restore();
	}

	private void CheckExist(String TableName) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query("sqlite_master", new String[] { "name" },
				"type = 'table'", null, null, null, null);

		if (cursor.moveToFirst())
			do {
				String name = cursor.getString(0);
				if (name.equals(TableName)) {
					db.close();
					return;
				}
			} while (cursor.moveToNext());
		db.close();

		db = mDbHelper.getWritableDatabase();

		String sql = String.format(SQL_CREATE_ENTRIES, mTableName);

		db.execSQL(sql);
		db.close();
	}

	public boolean save(E object) {
		int ind = indexOf(object);
		if (ind != -1) {
			save(ind);
			return true;
		}

		return false;
	}

	private long save(int index) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutStream = new ObjectOutputStream(
					byteArrayOutputStream);

			objectOutStream.writeObject(this.get(index));
			objectOutStream.flush();
			objectOutStream.close();
			byteArrayOutputStream.close();

			byte[] data = byteArrayOutputStream.toByteArray();

			return SaveData(index, data);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;
	}

	private void SaveAll() {

		SQLClear();

		for (int fori = 0; fori < this.size(); fori++)
			save(fori);
	}

	@SuppressWarnings("unchecked")
	private void Restore() {
		try {
			super.clear();

			SQLiteDatabase db = mDbHelper.getReadableDatabase();

			String[] projection = { FeedEntry._ID, FeedEntry.COLUMN_DataIndex,
					FeedEntry.COLUMN_DATA };

			String sortOrder = FeedEntry.COLUMN_DataIndex + " asc";

			Cursor cursor = db.query(mTableName, projection, null, null, null,
					null, sortOrder);

			if (cursor.moveToFirst())
				do {
					byte[] data = cursor.getBlob(cursor
							.getColumnIndexOrThrow(FeedEntry.COLUMN_DATA));
					int ind = cursor.getInt(cursor
							.getColumnIndexOrThrow(FeedEntry.COLUMN_DataIndex));

					try {
						ByteArrayInputStream inStream = new ByteArrayInputStream(
								data);

						ObjectInputStream objectInStream = new ObjectInputStream(
								inStream);
						super.add((E) objectInStream.readObject());
					} catch (StreamCorruptedException e) {
						e.printStackTrace();
					}
				} while (cursor.moveToNext());

		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLiteException e) {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();

			String sql = String.format(SQL_DELETE_ENTRIES, mTableName);
			db.execSQL(sql);

			sql = String.format(SQL_CREATE_ENTRIES, mTableName);
			db.execSQL(sql);

		}

	}

	private long SaveData(int id, byte[] data) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		int realId = id;
		values.put(FeedEntry.COLUMN_DataIndex, realId);
		values.put(FeedEntry.COLUMN_DATA, data);

		RemoveSQL(realId);

		long insert = db.insert(mTableName, null, values);
		return insert;
	}

	private void RemoveSQL(int id) {
		int realId = id;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(mTableName, WHERE_CLAUSE,
				new String[] { String.valueOf(realId) });
	}

	private static final String FetchSelection = FeedEntry.COLUMN_DataIndex
			+ " LIKE %1$s";

	private void FetchSQL(int id) {
		int real = id;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		for (int i = real + 1; i < this.size() + 1; i++) {
			values.put(FeedEntry.COLUMN_DataIndex, i - 1);
			String selection = String.format(FetchSelection, i);
			db.update(mTableName, values, selection, null);
		}
	}

	@Override
	public void clear() {
		SQLClear();
		super.clear();
	}

	private void SQLClear() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(mTableName, null, null);
	}

}
