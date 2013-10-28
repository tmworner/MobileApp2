package edu.sdsmt.WornerTillma.App2;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import edu.sdsmt.WornerTillma.App2.Model.Contact;
/**
 * The fragment which shows details of the contact.
 * 
 * <p>
 * <div style="font-weight:bold">
 * Description:
 * </div>
 * 		<div style="padding-left:3em">
 * 		
 * 		</div>
 * </p>
 * 
 * @since October 22, 2013<br>
 * @author Teresa Worner and James Tillma
 */
public class ViewListFragment extends ListFragment
{
	/**
	 * IContactControlListener object
	 */
	private IContactControlListener listener;
	
	/**
	 * creates list fragment with options menu
	 * @author Teresa Worner and James Tillma
	 * @param savedInstanceState The saved state of a previous runtime
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	/**
	 * Creates options menu
	 * @author Teresa Worner and James Tillma
	 * @param menu The menu to inflate
	 * @param menuInflater The inflater for said menu
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
	{
		getActivity().getMenuInflater().inflate(R.menu.menu_list, menu);
		super.onCreateOptionsMenu(menu, menuInflater);
	}
	
	/**
	 * Attaches the listener to the main activity
	 * @author Teresa Worner and James Tillma
	 * @param activity The activity to attach the fragment
	 */
	@Override
	public void onAttach(Activity activity)
	{
		//try to attach the listener and if it fails throw an exception
		try
		{
			this.listener = (IContactControlListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString());
		}
		
		super.onAttach(activity);
	}
	
	/**
	 * The on resume of the fragment
	 * @author Teresa Worner and James Tillma
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		
		this.refreshContactList();
	}
	
	/**
	 * Handles selection of an options menu item
	 * @author Teresa Worner and James Tillma
	 * @param item The item that was selected on the fragment
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			//if add contact was selected, insert a contact
			case R.id.action_add_contact:
			{
				this.listener.insertContact();
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
	}
	
	/**
	 * Handles the selection of a contact from the list view
	 * @author Teresa Worner and James Tillma
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		//get an item by position in the contact
		Contact contact = null;
		contact = (Contact) getListAdapter().getItem(position);
		//if the getItem succeeded, mark that contact as selected
		if(contact != null)
		{
			this.listener.selectContact(contact);
		}
	}
	
	/**
	 * Sets the list adapter
	 * @author Teresa Worner and James Tillma
	 */
	private void refreshContactList()
	{
		setListAdapter(this.listener.getContactArrayAdapter());
	}
}
