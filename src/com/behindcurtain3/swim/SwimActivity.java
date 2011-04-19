package com.behindcurtain3.swim;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class SwimActivity extends ListActivity {
	public static final int CLEAR_LIST = Menu.FIRST;

	// Spinners
	private Spinner mSpEvent;
	private Spinner mSpFrom;
	private Spinner mSpTo;
	
	// Text inputs
	private EditText mEtMinutes;
	private EditText mEtSeconds;
	private EditText mEtHundreths;
	
	// Buttons
	private Button mButtonConvert;
	
	// Database
	private SwimDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Bind our variables to the interface items
        mSpEvent = (Spinner)findViewById(R.id.event);
        mEtMinutes = (EditText)findViewById(R.id.input_minutes);
        mEtSeconds = (EditText)findViewById(R.id.input_seconds);
        mEtHundreths = (EditText)findViewById(R.id.input_hundreths);
        mSpFrom = (Spinner)findViewById(R.id.from);
        mSpTo = (Spinner)findViewById(R.id.to);
        mButtonConvert = (Button)findViewById(R.id.button_convert);
        
        // Load the spinners with the arrays
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpEvent.setAdapter(adapter);
        
        adapter = ArrayAdapter.createFromResource(this, R.array.conversions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpFrom.setAdapter(adapter);
        mSpTo.setAdapter(adapter);
        
        // Restrict the input fields to numbers only
        mEtMinutes.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtSeconds.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtHundreths.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        // Setup the button listeners
        mButtonConvert.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				convertTime(); // Call convertTime() when convert button is clicked
			} 
        });      
        
        // Setup ad's
        AdView ad = new AdView(this, AdSize.BANNER, "a14c718035ad191");
        LinearLayout adLayout = (LinearLayout)findViewById(R.id.ad);
        adLayout.addView(ad);
        
        AdRequest request = new AdRequest();
        //request.setTesting(true); // un-comment this line to enable test ads
        ad.loadAd(request);
        
        // Setup our data base helper
        mDbHelper = new SwimDbAdapter(this);
        mDbHelper.open();        
        
        // Populate the list view with data
        fillListViewWithData();
    }
    
    /*
     * Creates our options menu when the users presses their menu button
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, CLEAR_LIST, 0, R.string.menu_clear);
        return result;
    }

    /*
     * When the user selected CLEAR_LIST, remove all data and refresh the ListView
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case CLEAR_LIST:
        	mDbHelper.deleteAllNotes();	// Delete data
        	fillListViewWithData();		// Refresh ListView
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /*
     * Populates the ListView with data
     */
    private void fillListViewWithData() {
    	Cursor c = mDbHelper.fetchNumNotes(10);
    	startManagingCursor(c);
    	
    	String[] from = new String[] { SwimDbAdapter.KEY_TEXT };
    	int[] to = new int[] { R.id.list_item };
    	
    	SimpleCursorAdapter times = new SimpleCursorAdapter(this, R.layout.list_view, c, from, to);
    	setListAdapter(times);
    }
    
    /*
     * Run when the "convert" button is pushed.
     * Reads the status of the "From" & "To" spinners and calls the appropriate function for the conversion.
     * Takes the response from the conversion function and adds it the results db.
     */
    private void convertTime() {
    	// Make sure some sort of numbers have been entered
    	if(mEtMinutes.getText().length() == 0 && mEtSeconds.getText().length() == 0 && mEtHundreths.getText().length() == 0) {
    		Toast.makeText(this, "Please enter a time.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	String[] choices 	= getResources().getStringArray(R.array.conversions); 	// Possible choices
    	String from 		= (String)mSpFrom.getSelectedItem();					// Convert from choice
    	String to 			= (String)mSpTo.getSelectedItem();						// Convert to choice
    	String currentEvent = (String)mSpEvent.getSelectedItem();    				// Get whatever event the user selected
    	String result; 			// Holds our resulting string
    	String resultFrom;
    	String resultTo;
    	
    	if(from.equals(to)){ // return if the inputs are the same
    		Toast.makeText(this, "The pool lengths you've selected are the same.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	// Setup the time the user input in seconds
    	int minutes;
    	int seconds;
    	int hundreths;
    	
    	if(mEtMinutes.getText().length() == 0)
    		minutes = 0;
    	else 
    		minutes = Integer.parseInt(mEtMinutes.getText().toString(), 10);
    	    	
    	if(mEtSeconds.getText().length() == 0)
    		seconds = 0;
    	else
    		seconds = Integer.parseInt(mEtSeconds.getText().toString(), 10);
    	    	
    	if(mEtHundreths.getText().length() == 0)
    		hundreths = 0;
    	else
    		hundreths = Integer.parseInt(mEtHundreths.getText().toString(), 10);
    	
    	// We need the total seconds
    	double time = (minutes * 60) + (seconds) + ((double)hundreths / 100);
    	double convertedTime = 0;
    	
    	/* Possible calculations:
    	 * LCM -> SCM, SCY
    	 * SCM -> LCM, SCY
    	 * SCY -> LCM, SCM
    	 */
    	if(from.equals(choices[0])){ // from LCM
    		resultFrom = getString(R.string.LCM);
    		
    		if(to.equals(choices[1])){ // to SCM
    			convertedTime = new ConvertLcm().toScm(time, currentEvent); //convertLCMtoSCM(currentEvent, time);
    			resultTo = getString(R.string.SCM);
    		} else if(to.equals(choices[2])){ // to SCY
    			convertedTime = new ConvertLcm().toScy(time, currentEvent); //convertLCMtoSCY(currentEvent, time);
    			resultTo = getString(R.string.SCY);
    		} else
    			return;
    		
    	} else if(from.equals(choices[1])){ // from SCM
    		resultFrom = getString(R.string.SCM);
    		
    		if(to.equals(choices[0])){ // to LCM
    			convertedTime = new ConvertScm().toLcm(time, currentEvent); //convertSCMtoLCM(currentEvent, time);
    			resultTo = getString(R.string.LCM);
    		} else if(to.equals(choices[2])){ // to SCY
    			convertedTime = new ConvertScm().toScy(time, currentEvent); //convertSCMtoSCY(currentEvent, time);
    			resultTo = getString(R.string.SCY);
    		} else
    			return;
    		
    	} else if(from.equals(choices[2])){ // from SCY
    		resultFrom = getString(R.string.SCY);
    		
    		if(to.equals(choices[0])){ // to LCM
    			convertedTime = new ConvertScy().toLcm(time, currentEvent); //convertSCYtoLCM(currentEvent, time);
    			resultTo = getString(R.string.LCM);
    		} else if(to.equals(choices[1])){ // to SCM
    			convertedTime = new ConvertScy().toScm(time, currentEvent); //convertSCYtoSCM(currentEvent, time);
    			resultTo = getString(R.string.SCM);
    		} else
    			return;
    		
    	} else // bad input
    		return;
    	
    	// Reformat the data
    	int newMinutes = (int)(convertedTime / 60);
    	int newSeconds = (int)(convertedTime % 60);
    	int newHundreths = (int)((convertedTime - (newMinutes * 60) - newSeconds) * 100);
    	
    	// Format the data into a readable string
    	result = String.format("%s - %02d:%02d.%02d %s %s %02d:%02d.%02d %s", currentEvent, minutes, seconds, hundreths, resultFrom, getString(R.string.conversion), newMinutes, newSeconds, newHundreths, resultTo);
    	
    	// Store the string
    	mDbHelper.createTime(result);
    	fillListViewWithData();
    	
    }
}