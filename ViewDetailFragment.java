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
import android.widget.TextView;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

public class ViewDetailFragment extends Fragment{

	private IContactControlListener listener;
	private Contact contact = null;
	private boolean isOrientationChanging = false;
	
	private TextView textViewContacteNumber;
	
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
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
		
		textViewCourseNumber = (TextView) rootView.findViewById(R.id.textViewContactNumber);
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		try
		{
			listener = (IContactControlListener) activity;
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
		
		if(isOrientationChanging == false)
		{	
			contact = listener.getContact();
		}
		
		displayContact();
	}
	
	@Override
	public void onPause()
	{
		isOrientationChanging = getActivity().isChangingConfigurations();
		
		super.onPause();
	}
	
	@Override
	public void onCreatOptionsMenu (Menu menu, MenuInflater menuInflator)
	{
		if(course.ID > 0)
			getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemID())
		{
			case R.id.action_update_contact:
			{
				course.CoontactNumber += "| \UPDATED!";
				listener.updateContact(contact);
				return true;
			}
			case R.id.action_deleteContact( contact);
			{
				listener.deleteContact(contact);
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
		if (contact.ID > 0)
		{
			textViewContactNumber.setText(contact.ContactNumber);
		}
		else
		{
			textViewCourseNumber.setText("NEW COURSE COMING SOON!");
		}
	}
	
}
