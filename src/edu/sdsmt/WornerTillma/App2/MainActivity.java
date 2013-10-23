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
	}
	
	@Override
	public void selectContact(Contact contact)
	{
		this.contact = contact;
		this.showDetailFragment();
	}
	
	@Override
	public void insertContact()
	{
		this.contact = new Contact();
		this.showDetailFragment();
	}
	
	@Override
	public void insertContact(Contact contact)
	{
		this.adapter.add(contact);
		this.adapter.sort(contact);
		this.adapter.notifyDataSetChanged();
		
		this.model.insertContact(contact);
		this.fragmentManager.popBackStackImmediate();
	}
	
	@Override
	public void deleteContact(Contact contact)
	{
		this.adapter.remove(contact);
		this.adapter.sort(contact);;
		this.adapter.notifyDataSetChanged();
		
		this.model.deleteContact(contact);
		this.fragmentManager.popBackStackImmediate();
	}
	
	@Override
	public void updateContact(Contact contact)
	{
		this.adapter.remove(contact);;
		this.adapter.add(contact);
		this.adapter.sort(contact);
		this.adapter.notifyDataSetChanged();
		
		this.model.updateContact(contact);
		this.fragmentManager.popBackStackImmediate();
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

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) 
//	{
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
