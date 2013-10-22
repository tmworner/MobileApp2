package edu.sdsmt.WornerTillma.App2;

import android.widget.ArrayAdapter;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

public interface IContactControlListener {

	public void selectContact(Contact contact);
	public void insertContact();
	public void insertContact(Contact contact);
	public void deleteContact(Contact contact);
	public void updateContact(Contact contact);
	public Contact getContact();
	public ArrayAdapter<Contact> getContactArrayAdapter();
}
