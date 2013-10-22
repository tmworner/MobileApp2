package edu.sdsmt.WornerTillma.App2;

import android.widget.ArrayAdapter;
import edu.sdsmt.WornerTillma.App2.Model.Contact;

public class IContactControlListener {

	public void selectedContact(Contact contact);
	public void insertContact();
	public void insertContact(Contact contact);
	public void deleteContact(contact contact);
	public Contact getContact();
	public ArrayAdapter<Contact> getContactArrayAdapater();
}
