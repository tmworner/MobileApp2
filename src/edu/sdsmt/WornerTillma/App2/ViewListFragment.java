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

public class ViewListFragment extends ListFragment
{
	private IContactControlListener listener;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
	{
		getActivity().getMenuInflater().inflate(R.menu.menu_list, menu);
		super.onCreateOptionsMenu(menu, menuInflater);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
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
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		this.refreshContactList();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.action_add_course:
			{
				this.listener.insertContact();
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Contact contact = null;
		contact = (Contact) getListAdapter().getItem(position);
		if(contact != null)
		{
			this.listener.selectContact(contact);
		}
	}
	
	private void refreshContactList()
	{
		setListAdapter(this.listener.getContactArrayAdapter());
	}
}
