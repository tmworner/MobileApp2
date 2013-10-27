package edu.sdsmt.WornerTillma.App2;

import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

public class MainActivity extends Activity implements IContactControlListener
{
	private final static String FRAGMENT_LIST_TAG = "ContactListTag";
	private final static String FRAGMENT_DETAIL_TAG = "ContactViewTag";
	
	private final static String ID_KEY = "Id";
	private final static String NAME_KEY = "Name";
	private final static String PHONE_KEY = "Phone";
	private final static String EMAIL_KEY = "Email";
	private final static String STREET_KEY = "Street";
	private final static String CITY_KEY = "City";
	
	private FragmentManager fragmentManager;
	private ViewListFragment fragmentList;
	private ViewDetailFragment fragmentDetail;
	
	private Model model;
	private Contact contact;
	private List<Contact> contacts;
	private ArrayAdapter<Contact> adapter;

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
	
	@Override
	public void selectContact(Contact contact)
	{
		this.contact = contact;
		this.fragmentDetail.SetIsEditMode(true);
		this.showDetailFragment();
	}
	
	@Override
	public void insertContact()
	{
		this.contact = new Contact();
		this.fragmentDetail.SetIsEditMode(false);
		this.showDetailFragment();
	}
	
	@Override
	public void insertContact(Contact contact)
	{
		this.adapter.add(contact);
		this.adapter.sort(contact);
		this.adapter.notifyDataSetChanged();
		
		contact.ID = this.model.insertContact(contact);
		this.popBackStack();
	}
	
	@Override
	public void deleteContact(Contact contact)
	{
		this.adapter.remove(contact);
		this.adapter.sort(contact);;
		this.adapter.notifyDataSetChanged();
		
		this.model.deleteContact(contact);
		this.popBackStack();
	}
	
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
	
	@Override 
	public Contact getContact()
	{
		return this.contact;
	}
	
	@Override 
	public ArrayAdapter<Contact> getContactArrayAdapter()
	{
		return this.adapter;
	}
	
	public void popBackStack()
	{
		this.fragmentManager.popBackStackImmediate();
	}
	
	private void refreshArrayAdapter()
	{
		this.contacts = Model.getInstance(this).getContacts();
		
		this.adapter = new ArrayAdapter<Contact>(this, 
												 android.R.layout.simple_list_item_1, 
												 this.contacts);
	}
	
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
