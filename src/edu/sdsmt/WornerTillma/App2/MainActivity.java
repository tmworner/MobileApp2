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
 * @since October 22, 2013<br>
 * @author Teresa Worner and James Tillma
 */
public class MainActivity extends Activity implements IContactControlListener
{
	/**
	 * The tag for the contact list fragment
	 */
	private final static String FRAGMENT_LIST_TAG = "ContactListTag";
	/**
	 * The tag for the contact details view
	 */
	private final static String FRAGMENT_DETAIL_TAG = "ContactViewTag";
	
	
	private final static String ID_KEY = "Id";
	private final static String NAME_KEY = "Name";
	private final static String PHONE_KEY = "Phone";
	private final static String EMAIL_KEY = "Email";
	private final static String STREET_KEY = "Street";
	private final static String CITY_KEY = "City";
	
	/**
	 * The fragment manager for the app
	 */
	private FragmentManager fragmentManager;
	/**
	 * The fragment that displays the list of contact in the main activity
	 */
	private ViewListFragment fragmentList;
	/**
	 * The fragment that displays the details of the contact
	 */
	private ViewDetailFragment fragmentDetail;
	/**
	 * Handles the database operations
	 */	
	private Model model;
	/**
	 * Object that holds the current contact information
	 */
	private Contact contact;
	/**
	 * List of contacts
	 */
	private List<Contact> contacts;
	/**
	 * Object for displaying and modifying contact data
	 */
	private ArrayAdapter<Contact> adapter;

	/**
	 * Creates the fragments, refreshes the ArrayAdapter, and preps the contacts for initial display.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(savedInstanceState != null && savedInstanceState.containsKey(ID_KEY))
		{
			this.contact = new Contact();
			this.contact.ID = savedInstanceState.getLong(ID_KEY);
			this.contact.Name = savedInstanceState.getString(NAME_KEY);
			this.contact.Phone = savedInstanceState.getString(PHONE_KEY);
			this.contact.Email = savedInstanceState.getString(EMAIL_KEY);
			this.contact.Street = savedInstanceState.getString(STREET_KEY);
			this.contact.City = savedInstanceState.getString(CITY_KEY);
		}
		
		this.fragmentManager = getFragmentManager();
		
		this.fragmentList = (ViewListFragment) this.fragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
		if(this.fragmentList == null)
		{
			this.fragmentList = new ViewListFragment();
		}
		
		this.fragmentDetail = (ViewDetailFragment) this.fragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
		if(this.fragmentDetail == null)
		{
			this.fragmentDetail = new ViewDetailFragment();
		}
		
		if(savedInstanceState == null)
		{
			this.fragmentManager.beginTransaction()
								.replace(R.id.fragmentContainerFrame,  this.fragmentList, FRAGMENT_LIST_TAG)
								.commit();
		}
		
		this.model = Model.getInstance(this);
		
		this.refreshArrayAdapter();
		this.adapter.sort(new Contact());
		this.adapter.notifyDataSetChanged();
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState)
    {
		if(this.contact != null)
		{
	        outState.putLong(ID_KEY, this.contact.ID);
	        outState.putString(NAME_KEY, this.contact.Name);
	        outState.putString(PHONE_KEY, this.contact.Phone);
	        outState.putString(EMAIL_KEY, this.contact.Email);
	        outState.putString(STREET_KEY, this.contact.Street);
	        outState.putString(CITY_KEY, this.contact.City);
		}
		
        super.onSaveInstanceState(outState);
    }
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
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
	 * Shows the contact in details fragment and determines whether or not the fields are to be active
	 */
	@Override
	public void selectContact(Contact contact)
	{
		this.contact = contact;
		this.fragmentDetail.SetIsEditMode(true);
		this.showDetailFragment();
	}
	
	/**
	 * Creates a new contact object and displays a blank detail fragment.
	 */
	@Override
	public void insertContact()
	{
		this.contact = new Contact();
		this.fragmentDetail.SetIsEditMode(false);
		this.showDetailFragment();
	}
	
	/**
	 * Inserts the new contact into the database and displays it on the list view
	 */
	@Override
	public void insertContact(Contact contact)
	{
		this.adapter.add(contact);
		this.adapter.sort(contact);
		this.adapter.notifyDataSetChanged();
		
		contact.ID = this.model.insertContact(contact);
		this.popBackStack();
	}

	/**
	 * Deletes the contact from the UI and the database
	 */
	@Override
	public void deleteContact(Contact contact)
	{
		this.adapter.remove(contact);
		this.adapter.sort(contact);;
		this.adapter.notifyDataSetChanged();
		
		this.model.deleteContact(contact);
		this.popBackStack();
	}
	
	/**
	 * Updates a contact on the UI and the database (really, removes old and adds new)
	 */
	@Override
	public void updateContact(Contact contact)
	{
		this.adapter.remove(contact);;
		this.adapter.add(contact);
		this.adapter.sort(contact);
		this.adapter.notifyDataSetChanged();
		
		this.model.updateContact(contact);
		this.popBackStack();
	}
	
	/**
	 * Gets the current contact object
	 */
	@Override 
	public Contact getContact()
	{
		return this.contact;
	}
	
	/**
	 * Gets the ArrayAdapter
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
		this.contacts = Model.getInstance(this).getContacts();
		
		this.adapter = new ArrayAdapter<Contact>(this, 
												 android.R.layout.simple_list_item_1, 
												 this.contacts);
	}
	
	/**
	 * Displays the detail fragment
	 */
	private void showDetailFragment()
	{
		this.fragmentManager.beginTransaction()
							.replace(R.id.fragmentContainerFrame,
									 this.fragmentDetail,
									 FRAGMENT_DETAIL_TAG).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
									 					 .addToBackStack(null)
									 					 .commit();
											 								
	}

}
