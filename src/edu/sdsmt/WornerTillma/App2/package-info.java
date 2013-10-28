/**
 * App2 (Address Book) saves contacts with phone number, email, and street address.
 * 
 * <p>
 * <div style="font-weight:bold">
 * Description:
 * </div>
 * 		<div style="padding-left:3em">
 * 		App Name: Address Book<br>
 * 		Package Name: edu.sdsmt.WornerTillma.App2<br>
 * 		Class: Mobile Computing, CSC 492<br>
 * 		Professor: Dr. Logar, Brian Butterfield<br>
 * 		Due: October 28, 2013<br><br>
 * 
 * 		This app allows the user to store contacts.  The main page lists contact names and
 * 		selecting a contact displays detailed information, including phone number,
 * 		email address, street address, and city.  When a user adds a new contact or edits 
 *  	an existing contact, they are required to enter a name, but all other data is optional.
 *  	Duplicate names are permitted.  A user can edit or delete a contact from the options menu on the
 *  	detail page which is reached by selecting a contact on the app's home screen.
 *  	<br><br>
 *  
 *  	A large portion of the code for this app comes from Brian Butterfield's examples, modified
 *  	to suit the requirements of this app.
 * 		</div>
 * </p>
 *
 * <p>
 * <div style="font-weight:bold">
 * Code Files:
 * </div>
 * 		<div style="padding-left:3em">
 * 		MainActivity.java<br>
 * 		IContactControlListener.java<br>
 * 		Model.java<br>
 * 		ViewDetailFragment.java<br>
 * 		ViewListFragment.java
 * 		</div>
 * </p>
 * 
 * <p>
 * <div style="font-weight:bold">
 * Custom xml Files:
 * </div>
 * 		<div style="padding-left:3em; font-style:italic">
 * 		Layouts:
 * 		<div style="padding-left:3em; font-style:normal">
 * 		activity_main.xml<br>
 * 		detail_fragment.xml
 * 		</div>
 * 
 * 		Drawables:
 * 		<div style="padding-left:3em; font-style:normal">
 * 		editborder_focused.xml<br>
 * 		edittext_border.xml<br>
 * 		selector.xml
 * 		</div>
 * 
 * 		Other:
 * 		<div style="padding-left:3em; font-style:normal">
 * 		strings.xml<br>
 * 		styles.xml
 * 		</div>
 * 
 * 		</div>
 * </p>
 * 
 * <p>
 * <div style="font-weight:bold">
 * Rough Development Timeline:
 * </div>
 * 		<div style="padding-left:3em">
 * 		October 22: Created the UI. Got contacts to save and sort on the main activity.<br>
 * 		October 25: Filled fields when contact selected. Forced the name field to be required. Made update option functional. 
 * 					Made editTexts inactive until update is chosen. Set text colors for active/inactive.
 * 					Did general testing.<br>
 * 		October 28: More general testing. Forced a white fill on the editText fields. Solved null exception in the add_contact option.
 * 		</div>
 * </p>
 * 
 * @author Teresa Worner and James Tillma
 */
package edu.sdsmt.WornerTillma.App2;