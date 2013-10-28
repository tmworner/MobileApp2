package edu.sdsmt.WornerTillma.App2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
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

/**
 * The fragment which shows details of the contact.
 * 
 * <p>
 * <div style="font-weight:bold">
 * Description:
 * </div>
 * 		<div style="padding-left:3em">
 * 		This class is the detail fragment. It gets displayed when the user chooses to add
 * 		a new contact or selects a contact. When the contact is selected, the fields remain
 * 		inactive until they user chooses to update the contact or deletes it.
 * 		</div>
 * </p>
 * 
 * @since October 22, 2013<br>
 * @author Teresa Worner and James Tillma
 */
public class ViewDetailFragment extends Fragment implements OnClickListener
{
	/**
	 * Listener for the delete option in the dialog fragment
	 */
	public DialogInterface.OnClickListener delete = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			listener.deleteContact(contact);
		}
	};
	/**
	 * Listener object that allows us to call contact control functions
	 */
	private IContactControlListener listener;
	/**
	 * A contact object for use in this fragment
	 */
	private Contact contact = null;
	/**
	 * A contact object that allows for restoration after state changes
	 */
	private Contact restoreContact = null;
	/**
	 * Stores whether or not the orientation is changing 
	 */
	private boolean isOrientationChanging = false;
	/**
	 * Defines whether or not the update contact has been selected
	 */
	private boolean isEditMode = false;
	/**
	 * Defines whether or not the fields in the fragment are active
	 */
	private boolean editing = false;
	/**
	 * Defines whether or not there is temp data in the text fields to restore
	 */
	private boolean restoreData = false;
	
	View rootView = null;
	/**
	 * Sets the retain instance and that it has an options menu
	 * @author Teresa Worner and James Tillma
	 * @param savedInstanceState The saved state of a previous runtime
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//Call the parent's on create, set retain instance and options menu
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
	}
	/**
	 * Detects the save click event and saves if the name field isn't empty
	 * @author Teresa Worner and James Tillma
	 * @param v The view that has the "clickables"...in this case only a save button
	 */
	@Override
	public void onClick(View v)
	{
		//If the save button was selected
		if(v.getId() == R.id.Save)
		{
			//save the updated contact
			this.updateContact();
			
			EditText nameField = (EditText)this.rootView.findViewById(R.id.Name);
			//if the name field was empty, redisplay the fragment with an "error" message in the name field
			if(nameField.getText().toString().equals(""))
			{
				if(nameField.getHint().toString().equals("Name"))
					nameField.setHint(Html.fromHtml("<i>Name is Required</i>"));
				else
					nameField.setHint(Html.fromHtml("<i>" + nameField.getHint().toString() + "!</i>"));
			}
			//Otherwise if it is in edit mode, update the contact
			else if(this.isEditMode)
			{
				this.listener.updateContact(this.contact);
			}
			//Else insert the new contact
			else
			{
				((MainActivity) this.getActivity()).insertContact(this.contact);
			}
		}
	}
	/**
	 * Saves the state of the fragment on orientation change
	 * @author Teresa Worner and James Tillma
	 * @param outState The state to be saved
	 */
	@Override
    public void onSaveInstanceState(Bundle outState)
    {
		//If contact isn't null, save the contact information
		if(this.contact != null)
		{
			this.restoreContact = new Contact();
			this.restoreContact.Name = ((EditText)this.rootView.findViewById(R.id.Name)).getText().toString();
			this.restoreContact.Phone = ((EditText)this.rootView.findViewById(R.id.Phone)).getText().toString();
			this.restoreContact.Email = ((EditText)this.rootView.findViewById(R.id.Email)).getText().toString();
			this.restoreContact.Street = ((EditText)this.rootView.findViewById(R.id.Street)).getText().toString();
			this.restoreContact.City = ((EditText)this.rootView.findViewById(R.id.City)).getText().toString();
	        this.restoreData = true;
		}
		//call the parent's save instance
        super.onSaveInstanceState(outState);
    }
	/**
	 * Creates the root view and determines whether the fields should be enabled
	 * @author Teresa Worner and James Tillma
	 * @param inflater The inflater for the detail fragment
	 * @param container The container to be given to the inflater
	 * @param savedInstanceState The bundle containing the saved state of the app
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//get the inflater for the fragment root view
		this.rootView = inflater.inflate(R.layout.detail_fragment, container, false);
		//set a listener for the save button
		Button saveBtn = (Button) rootView.findViewById(R.id.Save);
		saveBtn.setOnClickListener(this);
		//set the listener for the contact
		this.contact = this.listener.getContact();
		//if it is in editMode but it is not being edited, disable the fields and change the text colors
		if(this.isEditMode && !this.editing)
		{
			this.setEnabled(false);
			this.changeColors(R.color.darkGray);
		}
		//otherwise, change the text colors to black and enable the fields
		else
		{
			this.changeColors(R.color.black);
			this.setEnabled(true);
		}
		
		return rootView;
	}
	/**
	 * Attaches a listener to the main activity
	 * @author Teresa Worner and James Tillma
	 * @param activity The activity to be attached to the listener
	 */
	@Override
	public void onAttach(Activity activity)
	{
		//try to attach the listener to the activity, throw an exception if it fails
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
	/**
	 * Handles the resume of the fragment.
	 * @author Teresa Worner and James Tillma
	 */
	@Override
	public void onResume()
	{
		//call super class's on resume
		super.onResume();
		//if the orientation isn't changing then set the listener and display contact
		if(this.isOrientationChanging == false)
		{	
			this.contact = this.listener.getContact();
		}
		
		displayContact();
	}
	/**
	 * Handles how the fragment pauses
	 * @author Teresa Worner and James Tillma
	 */
	@Override
	public void onPause()
	{
		//Decect if the orientation is changing
		this.isOrientationChanging = getActivity().isChangingConfigurations();
		//if it isn't set not to restore data and set that it isn't being edited
		if(!this.isOrientationChanging)
		{
			this.restoreData = false;
			this.editing = false;
		}
		
		super.onPause();
	}
	/**
	 * Creates the options menu for the fragment
	 * @author Teresa Worner and James Tillma
	 * @param menu The menu to inflate
	 * @param menuInflater The inflater for said menu
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator)
	{
		//if the contact is empty and it has an ID, inflate the detail menu
		if(this.contact != null && this.contact.ID > 0)
			getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
	}
	/**
	 * Detaches the fragment from the activity
	 * @author Teresa Worner and James Tillma
	 */
	@Override
	public void onDetach() {
		super.onDetach();
	}
	/**
	 * Determines which menu action was selected and acts on it
	 * @author Teresa Worner and James Tillma
	 * @param item The selected item on the menu
	 * @return true The delete or update option has been chosen
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			//if the update action is chosen, enable the fields, change the color, and editing
			case R.id.action_update_contact:
			{
				this.setEnabled(true);
				this.changeColors(R.color.black);
				this.editing = true;
				return true;
			}
			//if delete action is chosen, show the dialog
			case R.id.action_delete_contact:
			{
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				DialogFragment fragment = DeleteDialog.newInstance(delete);
				fragment.show(ft, "dialog");
				return true;
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
	}
	/**
	 * Returns the current state of isEditMode
	 * @author Teresa Worner and James Tillma
	 * @return isEditMode The boolean representing whether or not the fragment is in EditMode
	 */
	public boolean GetIsEditMode()
	{
		return this.isEditMode;
	}
	/**
	 * Sets isEditMode
	 * @author Teresa Worner and James Tillma
	 * @param mode Boolean showing whether or not the gragment is in edit mode
	 */
	public void SetIsEditMode(boolean mode)
	{
		this.isEditMode = mode;
	}
	/**
	 * Sets the fields to be enabled or disabled based on boolean value
	 * @author Teresa Worner and James Tillma
	 * @param enabled A boolean representing what to set the enabled status of the fields to
	 */
	private void setEnabled(boolean enabled)
	{
		//set the edit text fields to be enabled or disabled
		this.rootView.findViewById(R.id.Name).setEnabled(enabled);
		this.rootView.findViewById(R.id.Phone).setEnabled(enabled);
		this.rootView.findViewById(R.id.Email).setEnabled(enabled);
		this.rootView.findViewById(R.id.Street).setEnabled(enabled);
		this.rootView.findViewById(R.id.City).setEnabled(enabled);
		//enabled is false make the save invisible
		if(!enabled)
		{
			this.rootView.findViewById(R.id.Save).setVisibility(View.INVISIBLE);
		}
		//otherwise, set the save button to be visible
		else
		{
			this.rootView.findViewById(R.id.Save).setVisibility(View.VISIBLE);
		}
	}
	/**
	 * Updates the contact object
	 * @author Teresa Worner and James Tillma
	 */
	private void updateContact()
	{
		//updates the class's contact based on what is in the editTExt fields
		this.contact.Name = ((EditText)this.rootView.findViewById(R.id.Name)).getText().toString();
		this.contact.Phone = ((EditText)this.rootView.findViewById(R.id.Phone)).getText().toString();
		this.contact.Email = ((EditText)this.rootView.findViewById(R.id.Email)).getText().toString();
		this.contact.Street = ((EditText)this.rootView.findViewById(R.id.Street)).getText().toString();
		this.contact.City = ((EditText)this.rootView.findViewById(R.id.City)).getText().toString();
	}
	/**
	 * Displays details of the contact object in the fragment
	 * @author Teresa Worner and James Tillma
	 */
	private void displayContact()
	{
		//if the contact is valid, displays the text in the editText fields
		if (this.contact.ID > 0)
		{
			((EditText) this.rootView.findViewById(R.id.Name)).setText(this.contact.Name);
			((EditText) this.rootView.findViewById(R.id.Phone)).setText(this.contact.Phone);
			((EditText) this.rootView.findViewById(R.id.Email)).setText(this.contact.Email);
			((EditText) this.rootView.findViewById(R.id.Street)).setText(this.contact.Street);
			((EditText) this.rootView.findViewById(R.id.City)).setText(this.contact.City);
		}
		//otherwise, it sets the text in the editTexts to be empty
		else
		{
			((EditText)this.rootView.findViewById(R.id.Name)).setText("");
			((EditText)this.rootView.findViewById(R.id.Phone)).setText("");
			((EditText)this.rootView.findViewById(R.id.Email)).setText("");
			((EditText)this.rootView.findViewById(R.id.Street)).setText("");
			((EditText)this.rootView.findViewById(R.id.City)).setText("");
		}
		//calls restore data
		this.restoreData();
	}
	/**
	 * Changes the text color in the editText fields
	 * @author Teresa Worner and James Tillma
	 * @param color The color to change the values to, it is an integer based on an ID
	 */
	private void changeColors(int color)
	{
		color = getResources().getColor(color);
		((EditText) this.rootView.findViewById(R.id.Name)).setTextColor(color);
		((EditText) this.rootView.findViewById(R.id.Phone)).setTextColor(color);
		((EditText) this.rootView.findViewById(R.id.Email)).setTextColor(color);
		((EditText) this.rootView.findViewById(R.id.Street)).setTextColor(color);
		((EditText) this.rootView.findViewById(R.id.City)).setTextColor(color);
	}
	/**
	 * Restores any saved data from previous fragment
	 * @author Teresa Worner and James Tillma
	 */
	private void restoreData()
	{
		//if there is data to be restored, the contact and restoreContact and the ID's match
		if(this.restoreData &&
		   this.contact != null &&
		   this.restoreContact != null &&
		   this.contact.ID == this.restoreContact.ID)
		{
			((EditText) this.rootView.findViewById(R.id.Name)).setText(this.restoreContact.Name);
			((EditText) this.rootView.findViewById(R.id.Phone)).setText(this.restoreContact.Phone);
			((EditText) this.rootView.findViewById(R.id.Email)).setText(this.restoreContact.Email);
			((EditText) this.rootView.findViewById(R.id.Street)).setText(this.restoreContact.Street);
			((EditText) this.rootView.findViewById(R.id.City)).setText(this.restoreContact.City);
		}
	}
	/**
	 * Inner class that controls the delete dialog
	 * 
	 * <p>
	 * <div style="font-weight:bold">
	 * Description:
	 * </div>
	 * 		<div style="padding-left:3em">
	 * 		This controls a delete dialog. It creates a listener and calls it
	 * 		on positive action. On negative action, it does nothing.
	 * 		</div>
	 * </p>
	 * 
	 * @since October 22, 2013<br>
	 * @author Teresa Worner and James Tillma
	 */
	public static class DeleteDialog extends DialogFragment
	{
		/**
		 * Dialog box listener
		 */
		DialogInterface.OnClickListener listener;
		
		/**
		 * Returns new instance of the delete dialog box
		 * @author Teresa Worner and James Tillma
		 * @param listener The listener for the dialog box
		 */
		static DeleteDialog newInstance(DialogInterface.OnClickListener listener) 
		{
			DeleteDialog d = new DeleteDialog();
			d.listener = listener;
			return d;
		}
		/**
		 * Creates the dialog box
		 * @author Teresa Worner and James Tillma
		 * @param savedInstanceState The saved state from the Bundle
		 */
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) 
	    {
	        return new AlertDialog.Builder(this.getActivity())
	                .setTitle(R.string.confirm)
	                .setMessage(R.string.confirmMessage)
	                .setPositiveButton(R.string.ok, listener)
	                .setNegativeButton(R.string.cancel,
	                    new DialogInterface.OnClickListener() 
	                    {
	                		@Override
	                        public void onClick(DialogInterface dialog, int whichButton) 
	                        {
	                            ((MainActivity)getActivity()).popBackStack();
	                        }
	                    })
	                .create();
	    }
	}
	
}
