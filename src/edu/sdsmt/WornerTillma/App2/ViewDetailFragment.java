package edu.sdsmt.WornerTillma.App2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

public class ViewDetailFragment extends Fragment{

	private IContactControlListener listener;
	private Contact contact = null;
	private boolean isOrientationChanging = false;
	
	private EditText EditTextContactName;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
		
		this.EditTextContactName = (EditText) rootView.findViewById(R.id.Name);
		
		return rootView;
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
			throw new ClassCastException (activity.toString());
		}
		
		super.onAttach(activity);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if(this.isOrientationChanging == false)
		{	
			this.contact = this.listener.getContact();
		}
		
		displayContact();
	}
	
	@Override
	public void onPause()
	{
		this.isOrientationChanging = getActivity().isChangingConfigurations();
		
		super.onPause();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator)
	{
		if(this.contact.ID > 0)
			getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_update_contact:
			{
				this.contact.Name += "| UPDATED!";
				this.listener.updateContact(this.contact);
				return true;
			}
			case R.id.action_delete_contact:
			{
				this.listener.deleteContact(this.contact);
				return true;
			}
			default:
			{
					return super.onOptionsItemSelected(item);
			}
		}
	}
	
	private void displayContact()
	{
/*		if (this.contact.ID > 0)
		{
			this.EditTextContactName.setText(this.contact.Name);
		}
		else
		{
			this.EditTextContactName.setHint("New Contact");
		}*/
	}
	
}
