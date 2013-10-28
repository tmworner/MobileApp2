package edu.sdsmt.WornerTillma.App2;

import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

/**
 * The activity from which the entire app runs.
 * 
 * <p>
 * <div style="font-weight:bold">
 * Description:
 * </div>
 * 		<div style="padding-left:3em">
 * 		This class handles most of the app. It controls the detail and list fragments, the saved states, and the database.
 * 		It does not outright handle the updating or adding of a contact (as seen by the user), but it does control refreshing
 * 		of the UI and the actual storing of the update/insert.
 * 		</div>
 * </p>
 * 
 * @since October 22, 2013
 * @author James Tillma and Teresa Worner
 */
public class MainActivity extends Activity implements IContactControlListener
{
	// The tag for the contact list fragment
	private final static String FRAGMENT_LIST_TAG = "ContactListTag";
	// The tag for the contact details view
	private final static String FRAGMENT_DETAIL_TAG = "ContactViewTag";
	
	// The key used to store the ID of the contact in onSaveInstanceState
	private final static String ID_KEY = "Id";
	// The key used to store the name of the contact in onSaveInstanceState
	private final static String NAME_KEY = "Name";
	// The key used to store the phone number of the contact in onSaveInstanceState
	private final static String PHONE_KEY = "Phone";
	// The key used to store the email address of the contact in onSaveInstanceState
	private final static String EMAIL_KEY = "Email";
	// The key used to store the street address of the contact in onSaveInstanceState
	private final static String STREET_KEY = "Street";
	// The key used to store the city/state/zip of the contact in onSaveInstanceState
	private final static String CITY_KEY = "City";
	
	// The fragment manager for the app
	private FragmentManager fragmentManager;
	// The fragment that displays the list of contact in the main activity
	private ViewListFragment fragmentList;
	// The fragment that displays the details of the contact
	private ViewDetailFragment fragmentDetail;
	// Handles the database operations\
	private Model model;
	// Object that holds the current contact information
	private Contact contact;
	// List of contacts
	private List<Contact> contacts;
	// Object for displaying and modifying contact data
	private ArrayAdapter<Contact> adapter;

	/**
	 * Creates the fragments, refreshes the ArrayAdapter, and preps the contacts for initial display.
	 * @param savedInstanceState The saved state of a previous runtime
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get the fragment manager
		this.fragmentManager = getFragmentManager();
		
		// Fill the fragment list and if it's empty, recreate it.
		this.fragmentList = (ViewListFragment) this.fragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
		if(this.fragmentList == null)
		{
			this.fragmentList = new ViewListFragment();
		}
		
		// Fill the detail fragment and if it's empty, recreate it.
		this.fragmentDetail = (ViewDetailFragment) this.fragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
		if(this.fragmentDetail == null)
		{
			this.fragmentDetail = new ViewDetailFragment();
		}
		
		// If there is no saved instance state, reset the fragmentManager
		if(savedInstanceState == null)
		{
			this.fragmentManager.beginTransaction()
								.replace(R.id.fragmentContainerFrame,  this.fragmentList, FRAGMENT_LIST_TAG)
								.commit();
		}
		else if(savedInstanceState.containsKey(ID_KEY))
		{
			// Create a new contact and get information from saved states to repopulate it
			this.contact = new Contact();
			this.contact.ID = savedInstanceState.getLong(ID_KEY);
			this.contact.Name = savedInstanceState.getString(NAME_KEY);
			this.contact.Phone = savedInstanceState.getString(PHONE_KEY);
			this.contact.Email = savedInstanceState.getString(EMAIL_KEY);
			this.contact.Street = savedInstanceState.getString(STREET_KEY);
			this.contact.City = savedInstanceState.getString(CITY_KEY);
		}
		
		// Get an instance of the model
		this.model = Model.getInstance(this);
		
		// Get the ArrayAdapter, sort it, and update the contents of the UI
		this.refreshArrayAdapter();
		this.adapter.sort(new Contact());
		this.adapter.notifyDataSetChanged();
	}
	
	/**
	 * Saves state of the app
	 * @param outState The state of the program to be saved
	 */
	@Override
    public void onSaveInstanceState(Bundle outState)
    {
		// If the contact contains values, put them in the outState bundle
		if(this.contact != null)
		{
	        outState.putLong(ID_KEY, this.contact.ID);
	        outState.putString(NAME_KEY, this.contact.Name);
	        outState.putString(PHONE_KEY, this.contact.Phone);
	        outState.putString(EMAIL_KEY, this.contact.Email);
	        outState.putString(STREET_KEY, this.contact.Street);
	        outState.putString(CITY_KEY, this.contact.City);
		}
		
		// save the outState
        super.onSaveInstanceState(outState);
    }
    
	/**
	 * Gets the saved state of the app
	 * @param savedInstanceState The saved state to be gotten from the bundle
	 */
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// if there is a saved state that contains an ID, get the contact's values
		if(savedInstanceState.containsKey(ID_KEY))
		{
			this.contact = new Contact();
			this.contact.ID = savedInstanceState.getLong(ID_KEY);
			this.contact.Name = savedInstanceState.getString(NAME_KEY);
			this.contact.Phone = savedInstanceState.getString(PHONE_KEY);
			this.contact.Email = savedInstanceState.getString(EMAIL_KEY);
			this.contact.Street = savedInstanceState.getString(STREET_KEY);
			this.contact.City = savedInstanceState.getString(CITY_KEY);
		}
	}
	
	/**
	 * Shows the contact in details fragment and ensures EditText fields are disabled and save button is invisible.
	 * @param contact The contact that has been selected
	 */
	@Override
	public void selectContact(Contact contact)
	{
		// sets class contact to be selected contact, updates IsEditMode, shows detail fragment
		this.contact = contact;
		this.fragmentDetail.SetIsEditMode(true);
		this.showDetailFragment();
	}
	
	/**
	 * Creates a new contact object and displays a blank detail fragment with EditText fields enabled and save button visible.
	 */
	@Override
	public void insertContact()
	{
		// sets class contact to be new contact, updates isEditMode, shows detail fragment
		this.contact = new Contact();
		this.fragmentDetail.SetIsEditMode(false);
		this.showDetailFragment();
	}
	
	/**
	 * Inserts the new contact into the database and displays it on the list view
	 * @param contact The contact to be added to the database
	 */
	@Override
	public void insertContact(Contact contact)
	{
		// add contact to the adapter and update the UI
		this.adapter.add(contact);
		this.adapter.sort(contact);
		this.adapter.notifyDataSetChanged();
		
		// set the contact ID to insert to the list, pop view off backStack
		contact.ID = this.model.insertContact(contact);
		this.popBackStack();
	}

	/**
	 * Deletes the contact from the UI and the database
	 * @param contact The contact to be removed from the database
	 */
	@Override
	public void deleteContact(Contact contact)
	{
		// Remove the contact and update the UI
		this.adapter.remove(contact);
		this.adapter.sort(contact);;
		this.adapter.notifyDataSetChanged();
		
		// Remove the contact from the database
		this.model.deleteContact(contact);
		this.popBackStack();
	}
	
	/**
	 * Updates a contact on the UI and the database (really, removes old and adds new)
	 * @param contact The contact to be updated
	 */
	@Override
	public void updateContact(Contact contact)
	{
		// Update (that is, remove old and add new) the contact and update the UI
		this.adapter.remove(contact);
		this.adapter.add(contact);
		this.adapter.sort(contact);
		
		// Update the contact in the database
		this.model.updateContact(contact);
		
		// refresh array adapter and pop detail fragment off back stack
		this.refreshArrayAdapter();
		this.adapter.notifyDataSetChanged();
		this.popBackStack();
	}
	
	/**
	 * Gets the current contact object
	 * @return contact The classes contact
	 */
	@Override 
	public Contact getContact()
	{
		return this.contact;
	}
	
	/**
	 * Gets the ArrayAdapter
	 * @return The classes ArrayAddapter
	 */ 
	@Override
	public ArrayAdapter<Contact> getContactArrayAdapter()
	{
		return this.adapter;
	}
	
	/**
	 * Does an immediate remove from the top of the backStack
	 */
	public void popBackStack()
	{
		this.fragmentManager.popBackStackImmediate();
	}
	
	/**
	 * Creates a new ArrayAdapter at the beginning of the app's runtime
	 */
	private void refreshArrayAdapter()
	{
		// Get the contacts from the model
		this.contacts = Model.getInstance(this).getContacts();
		
		// set to array adapter
		this.adapter = new ArrayAdapter<Contact>(this, 
												 android.R.layout.simple_list_item_1, 
												 this.contacts);
	}
	
	/**
	 * Displays the detail fragment
	 */
	private void showDetailFragment()
	{
		// set the fragmentManager
		this.fragmentManager.beginTransaction()
							.replace(R.id.fragmentContainerFrame,
									 this.fragmentDetail,
									 FRAGMENT_DETAIL_TAG).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
									 					 .addToBackStack(null)
									 					 .commit();
											 								
	}

}
