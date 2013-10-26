package edu.sdsmt.WornerTillma.App2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

public class ViewDetailFragment extends Fragment implements OnClickListener
{
	private IContactControlListener listener;
	private Contact contact = null;
	private boolean isOrientationChanging = false;
	private boolean isEditMode = false;
	
	View rootView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.Save)
		{
			this.updateContact();
		
			if(this.isEditMode)
			{
				this.listener.updateContact(this.contact);
			}
			else
			{
				((MainActivity) this.getActivity()).insertContact(this.contact);
			}
		}
	}
	
	private boolean checkContact()
	{
		if(this.contact.Name.equals(""))
		{
			return false;
		}
		
		if(this.contact.Phone.equals("") &&
		   this.contact.Email.equals("") &&
		   this.contact.Street.equals("") &&
		   this.contact.City.equals(""))
		{
			
		}
		
		return true;
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.rootView = inflater.inflate(R.layout.detail_fragment, container, false);

		Button saveBtn = (Button) rootView.findViewById(R.id.Save);
		saveBtn.setOnClickListener(this);
		
		this.contact = this.listener.getContact();
		
		if(this.isEditMode)
		{
			this.setEnabled(false);
		}
		else
		{
			this.setEnabled(true);
		}
		
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
				this.setEnabled(true);
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
	
	public boolean GetIsEditMode()
	{
		return this.isEditMode;
	}
	
	public void SetIsEditMode(boolean mode)
	{
		this.isEditMode = mode;
	}
	
	private void setEnabled(boolean enabled)
	{
		this.rootView.findViewById(R.id.Name).setEnabled(enabled);
		this.rootView.findViewById(R.id.Phone).setEnabled(enabled);
		this.rootView.findViewById(R.id.Email).setEnabled(enabled);
		this.rootView.findViewById(R.id.Street).setEnabled(enabled);
		this.rootView.findViewById(R.id.City).setEnabled(enabled);
		
		if(!enabled)
		{
			this.rootView.findViewById(R.id.Save).setVisibility(View.INVISIBLE);
		}
		else
		{
			this.rootView.findViewById(R.id.Save).setVisibility(View.VISIBLE);
		}
	}
	
	private void updateContact()
	{
		this.contact.Name = ((EditText)this.rootView.findViewById(R.id.Name)).getText().toString();
		this.contact.Phone = ((EditText)this.rootView.findViewById(R.id.Phone)).getText().toString();
		this.contact.Email = ((EditText)this.rootView.findViewById(R.id.Email)).getText().toString();
		this.contact.Street = ((EditText)this.rootView.findViewById(R.id.Street)).getText().toString();
		this.contact.City = ((EditText)this.rootView.findViewById(R.id.City)).getText().toString();
	}
	
	private void displayContact()
	{
		if (this.contact.ID > 0)
		{
			((EditText) this.rootView.findViewById(R.id.Name)).setText(this.contact.Name);
			((EditText) this.rootView.findViewById(R.id.Phone)).setText(this.contact.Phone);
			((EditText) this.rootView.findViewById(R.id.Email)).setText(this.contact.Email);
			((EditText) this.rootView.findViewById(R.id.Street)).setText(this.contact.Street);
			((EditText) this.rootView.findViewById(R.id.City)).setText(this.contact.City);
		}
		else
		{
			((EditText)this.rootView.findViewById(R.id.Name)).setText("");
			((EditText)this.rootView.findViewById(R.id.Phone)).setText("");
			((EditText)this.rootView.findViewById(R.id.Email)).setText("");
			((EditText)this.rootView.findViewById(R.id.Street)).setText("");
			((EditText)this.rootView.findViewById(R.id.City)).setText("");
		}
	}
	
}
