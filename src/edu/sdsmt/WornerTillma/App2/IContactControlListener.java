package edu.sdsmt.WornerTillma.App2;

import android.widget.ArrayAdapter;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

/**
 * <p>
 * <div style="font-weight:bold">
 * Description:
 * </div>
 * 		<div style="padding-left:3em">
 * 		An interface to be implemented (here by MainActivity).  Ensures whichever class implements
 *  	it has all the functions necessary to communicate with the model and handle contact manipulation.
 * 		</div>
 * </p>
 * 
 * @since October 22, 2013<br>
 * @author Brian Butterfield
 */
public interface IContactControlListener {

	public void selectContact(Contact contact);
	public void insertContact();
	public void insertContact(Contact contact);
	public void deleteContact(Contact contact);
	public void updateContact(Contact contact);
	public Contact getContact();
	public ArrayAdapter<Contact> getContactArrayAdapter();
}
