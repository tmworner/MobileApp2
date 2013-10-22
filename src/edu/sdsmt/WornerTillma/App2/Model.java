package edu.sdsmt.WornerTillma.App2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Model extends SQLiteOpenHelper
{
	public static final String KEY_ID = "ContactID";
	public static final String KEY_NAME = "ContactName";
	public static final String KEY_PHONE = "ContactPhone";
	public static final String KEY_EMAIL = "ContactEmail";
	public static final String KEY_STREET = "ContactStreet";
	public static final String KEY_CITY = "ContactCity";
	
	private static final String TAG = "App2";
	
	private static final String DATABASE_NAME = "App2.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_MYCONTACTS = "Contacts";
	
	private static final String TABLE_CREATE_MYCONTACTS = 
			"CREATE TABLE " +
			TABLE_MYCONTACTS + 
			"(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			KEY_NAME + " TEXT, " + 
			KEY_PHONE + " TEXT, " + 
			KEY_EMAIL + " TEXT, " + 
			KEY_STREET + " TEXT, " + 
			KEY_CITY + " TEXT);";
	
	private SQLiteDatabase db;
	private static Model instance;
	
	public Model(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase _db)
	{
		_db.execSQL(TABLE_CREATE_MYCONTACTS);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion)
	{
		if(newVersion == 2)
		{
			// No version 2 upgrade process yet
		}
	}
	
	public static synchronized Model getInstance(Context context)
	{
		if(Model.instance == null)
		{
			Model.instance = new Model(context);
		}
		
		return Model.instance;
	}
	
	public void insertContact(Contact contact)
	{
		ContentValues values = this.populateContentValues(contact);
		this.openDBConnection();
		long id = this.db.insert(TABLE_MYCONTACTS,  null,  values);
		Log.d(TAG, "ContactID inserted = " + String.valueOf(id));
		this.closeDBConnection();
	}
	
	public void updateContact(Contact contact)
	{
		ContentValues values = this.populateContentValues(contact);
		this.openDBConnection();
		int rowsAffected = this.db.update(TABLE_MYCONTACTS,  
										  values,  
										  KEY_ID + " = ?", 
										  new String[] { String.valueOf(contact.ID) });
										  
	    this.closeDBConnection();
	    
	    if(rowsAffected == 0)
	    {
	    	Log.d(TAG, "Contact update failed");
	    }
	}
	
	public void deleteContact(Contact contact)
	{
		this.openDBConnection();
		
		int rowsAffected = this.db.delete(TABLE_MYCONTACTS, 
										  KEY_ID + " = ?",
										  new String[] { String.valueOf(contact.ID) });
										  
	    if(rowsAffected == 0)
	    {
	    	Log.d(TAG, "Contact deletion failed");
	    }
	}
	
	public Contact getContact(long id)
	{
		Contact contact = null;
		this.openDBConnection();
		Cursor cursor = this.db.query(TABLE_MYCONTACTS, 
									  new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_STREET, KEY_CITY },
									  null,
									  null,
									  null,
									  null,
									  KEY_NAME);
	
		if(cursor.moveToFirst())
		{
			contact = this.cursorToContact(cursor);
		}
		
		cursor.close();
		this.closeDBConnection();
		
		return contact;
	}
	
	public List<Contact> getContacts()
	{
		List<Contact> contacts = new ArrayList<Contact>();
		
		this.openDBConnection();
		
		Cursor cursor = this.db.query(TABLE_MYCONTACTS, 
									  new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_STREET, KEY_CITY },
									  null,
									  null,
									  null,
									  null,
									  KEY_NAME);
									  
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false)
		{
			Contact contact = this.cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		
		cursor.close();
		this.closeDBConnection();
		
		return contacts;
	}
	
	private void openDBConnection()
	{
		this.db = getWritableDatabase();
	}
	
	private void closeDBConnection()
	{
		if(this.db != null && this.db.isOpen() == true)
		{
			this.db.close();
		}
	}
	
	private Contact cursorToContact(Cursor cursor)
	{
		Contact contact = new Contact(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
		contact.Name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
		contact.Phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE));
		contact.Email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
		contact.Street = cursor.getString(cursor.getColumnIndex(KEY_STREET));
		contact.City = cursor.getString(cursor.getColumnIndex(KEY_CITY));
		return contact;
	}
	
	private ContentValues populateContentValues(Contact contact)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.Name);
		values.put(KEY_PHONE, contact.Phone);
		values.put(KEY_EMAIL, contact.Email);
		values.put(KEY_STREET, contact.Street);
		values.put(KEY_CITY, contact.City);
		return values;
	}
	
	public static class Contact implements Comparator<Contact>
	{
		public long ID;
		public String Name;
		public String Phone;
		public String Email;
		public String Street;
		public String City;
		
		public Contact()
		{
			this.ID = -1;
		}
		
		public Contact(long id)
		{
			this.ID = id;
		}
		
		@Override
		public String toString()
		{
			return this.Name;
		}
		
		@Override
		public int compare(Contact lhs, Contact rhs)
		{
			return lhs.Name.compareToIgnoreCase(rhs.Name);
		}
	}
}
