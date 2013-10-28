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

/**
 * Holds the data and handles database interaction.
 * 
 * <p>
 * <div style="font-weight:bold">
 * Description:
 * </div>
 * 		<div style="padding-left:3em">
 * 		This is the model for the project.  It controls access to the database and
 * 		</div>
 * </p>
 * 
 * @since October 22, 2013
 * @author James Tillma and Teresa Worner
 */
public class Model extends SQLiteOpenHelper
{
	/**
	 * Key for the contact ID (used in storing data in database)
	 */
	public static final String KEY_ID = "ContactID";
	/**
	 * Key for the contact name (used in storing data in database)
	 */
	public static final String KEY_NAME = "ContactName";
	/**
	 * Key for the contact phone number (used in storing data in database)
	 */
	public static final String KEY_PHONE = "ContactPhone";
	/**
	 * Key for the contact email (used in storing data in database)
	 */
	public static final String KEY_EMAIL = "ContactEmail";
	/**
	 * Key for the contact street (used in storing data in database)
	 */
	public static final String KEY_STREET = "ContactStreet";
	/**
	 * Key for the contact city (used in storing data in database)
	 */
	public static final String KEY_CITY = "ContactCity";
	
	//* The tag for the app, used in Logcat
	private static final String TAG = "App2";
	// The name of the database
	private static final String DATABASE_NAME = "App2.db";
	// The version of the database
	private static final int DATABASE_VERSION = 1;
	// Used in insert into database for a contact
	private static final String TABLE_MYCONTACTS = "Contacts";
	
	// String for creating database
	private static final String TABLE_CREATE_MYCONTACTS = 
			"CREATE TABLE " +
			TABLE_MYCONTACTS + 
			"(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			KEY_NAME + " TEXT, " + 
			KEY_PHONE + " TEXT, " + 
			KEY_EMAIL + " TEXT, " + 
			KEY_STREET + " TEXT, " + 
			KEY_CITY + " TEXT);";
			
	// The database
	private SQLiteDatabase db;
	// An instance of the model
	private static Model instance;
	
	/**
	 * Constructor for the class
	 * @param context
	 */
	public Model(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * Handles creation of database
	 * @param _db The object to use to create the table
	 */
	@Override
	public void onCreate(SQLiteDatabase _db)
	{
		_db.execSQL(TABLE_CREATE_MYCONTACTS);
	}
	
	/**
	 * Handles upgrade of database (note: at this point it does nothing)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion)
	{
		if(newVersion > 1)
		{
			// No version 2 upgrade process yet
		}
	}
	
	/**
	 * Gets and instance of the model
	 * @param context
	 * @return Model.instance An instance of the model for the database
	 */
	public static synchronized Model getInstance(Context context)
	{
		if(Model.instance == null)
		{
			Model.instance = new Model(context);
		}
		
		return Model.instance;
	}
	
	/**
	 * Handles insertion of contact into the database
	 * @param contact The contact to be inserted into the database
	 * @return id The id of the contact
	 */
	public long insertContact(Contact contact)
	{
		ContentValues values = this.populateContentValues(contact);
		this.openDBConnection();
		long id = this.db.insert(TABLE_MYCONTACTS,  null,  values);
		Log.d(TAG, "ContactID inserted = " + String.valueOf(id));
		this.closeDBConnection();
		return id;
	}
	
	/**
	 * Handles the update of a contact in the database
	 * @param contact The contact to be updated in the database
	 */
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
	
	/**
	 * Handles removal of a contact from the database
	 * @param contact The contact to be removed from the database
	 */
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
	
	/**
	 * Returns a contact with the given ID
	 * @param id The id of the contact to be returned
	 * @return contact The contact with the matching id
	 */
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
									  
		// move cursor to first instance of positive result of query
		if(cursor.moveToFirst())
		{
			contact = this.cursorToContact(cursor);
		}
		
		cursor.close();
		this.closeDBConnection();
		
		return contact;
	}
	
	/**
	 * Creates and sets a list of contacts
	 * @return The list of contacts
	 */
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
		
		// loop that adds all contacts from the general query to the list of contacts
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
	
	/**
	 * Opens a connection to the database of contacts
	 */
	private void openDBConnection()
	{
		this.db = getWritableDatabase();
	}
	
	/**
	 * Closes a connection to the database of contacts
	 */
	private void closeDBConnection()
	{
		if(this.db != null && this.db.isOpen() == true)
		{
			this.db.close();
		}
	}
	
	/**
	 * Takes a cursor and gets the contact the cursor references
	 * @param cursor the cursor linked to a contact
	 * @return contact A new contact based on the information found at the cursor's contact
	 */
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
	
	/**
	 * Puts contact information in a ContentValues object and returns it
	 * @param contact the contact to populate values from
	 * @return values The ContentValues object based on the input parameter contact
	 */
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
	
	/**
	 * Inner contact class. Implements Comparator so contacts can be sorted.
	 * 
	 * <p>
	 * <div style="font-weight:bold">
	 * Description:
	 * </div>
	 * 		<div style="padding-left:3em">
	 * 		Holds contact data and handles comparison of contacts by name.
	 * 		</div>
	 * </p>
	 * 
	 * @since October 22, 2013
	 */
	public static class Contact implements Comparator<Contact>
	{
		/**
		 * ID of the contact
		 */
		public long ID;
		/**
		 * Name of the contact
		 */
		public String Name;
		/**
		 * Phone number of the contact
		 */
		public String Phone;
		/**
		 * Email of the contact
		 */
		public String Email;
		/**
		 * Street of the contact
		 */
		public String Street;
		/**
		 * City of the contact
		 */
		public String City;
		
		/**
		 * Constructor if an ID isn't given
		 */
		public Contact()
		{
			this.ID = -1;
		}
		
		/**
		 * Constructor if an ID is given
		 * @param id The id to use in the class's ID
		 */
		public Contact(long id)
		{
			this.ID = id;
		}
		
		/**
		 * Overrides the toString function to return the name of the contact
		 */
		@Override
		public String toString()
		{
			return this.Name;
		}
		
		/**
		 * Override to compare the names of the contacts
		 * @param lhs The contact "on the left" of the compare
		 * @param rhs The contact "on the right" of the compare
		 */
		@Override
		public int compare(Contact lhs, Contact rhs)
		{
			return lhs.Name.compareToIgnoreCase(rhs.Name);
		}
	}
}
