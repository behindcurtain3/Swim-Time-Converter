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

public class SwimConverter extends ListActivity {
	public static final int CLEAR_LIST = Menu.FIRST;
	
	// Conversion factors
	double conversionFactor = 1.11;
	
	// 50's
	double fiftyBreast = 1.0;
	double fiftyBack = 0.6;
	double fiftyFly = 0.7;
	double fiftyFree = 0.8;
	
	// 100's
	double hundredFly = 1.4;
	double hundredBack = 1.2;
	double hundredBreast = 2.0;
	double hundredFree = 1.6;
	
	// 200's
	double twoHundredFly = 2.8;
	double twoHundredBack = 2.4;
	double twoHundredBreast = 4.0;
	double twoHundredFree = 3.2;
	double twoHundredIM = 3.2;
	
	// 400
	double fourHundredIM = 6.4;
	
	// Distance, uses different calculation
	double fourHundredEq = 0.8925;
	double eightHundredEq = 0.8925;
	double fifteenHundredEq = 1.02;
	double fourHundred = 6.4;
	double eightHundred = 12.8;
	double fifteenHundred = 24.0;
	
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
				convertTime();
			} 
        });      
        
        // Setup ad's
        AdView ad = new AdView(this, AdSize.BANNER, "a14c718035ad191");
        LinearLayout adLayout = (LinearLayout)findViewById(R.id.ad);
        adLayout.addView(ad);
        
        AdRequest request = new AdRequest();
        //request.setTesting(true);
        ad.loadAd(request);
        
        mDbHelper = new SwimDbAdapter(this);
        mDbHelper.open();        
        fillData();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, CLEAR_LIST, 0, R.string.menu_clear);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case CLEAR_LIST:
        	mDbHelper.deleteAllNotes();
        	fillData();
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void fillData() {
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
    	
    	String[] choices = getResources().getStringArray(R.array.conversions); 	// Possible choices
    	String from = (String)mSpFrom.getSelectedItem();
    	String to = (String)mSpTo.getSelectedItem();
    	String currentEvent = (String)mSpEvent.getSelectedItem();    			// Get whatever event the user selected
    	String result; 
    	String resultFrom;
    	String resultTo;
    	
    	if(from.equals(to)){ // return if the inputs are the same
    		Toast.makeText(this, "The pool lengths you've selected are the same.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	// Setup the time the user input in seconds
    	int minutes;
    	if(mEtMinutes.getText().length() == 0)
    		minutes = 0;
    	else 
    		minutes = Integer.parseInt(mEtMinutes.getText().toString(), 10);
    	
    	int seconds;
    	if(mEtSeconds.getText().length() == 0)
    		seconds = 0;
    	else
    		seconds = Integer.parseInt(mEtSeconds.getText().toString(), 10);
    	
    	int hundreths;
    	if(mEtHundreths.getText().length() == 0)
    		hundreths = 0;
    	else
    		hundreths = Integer.parseInt(mEtHundreths.getText().toString(), 10);
    	
    	// We need the total seconds
    	double time = (minutes * 60) + (seconds) + ((double)hundreths / 100);
    	double convertedTime = 0;
    	
    	/* Possible calculations:
    	 * LCM -> SCM
    	 * LCM -> SCY
    	 * SCM -> LCM
    	 * SCM -> SCY
    	 * SCY -> LCM
    	 * SCY -> SCM
    	 */
    	if(from.equals(choices[0])){ // from LCM
    		resultFrom = getString(R.string.LCM);
    		
    		if(to.equals(choices[1])){ // to SCM
    			convertedTime = convertLCMtoSCM(currentEvent, time);
    			resultTo = getString(R.string.SCM);
    		} else if(to.equals(choices[2])){ // to SCY
    			convertedTime = convertLCMtoSCY(currentEvent, time);
    			resultTo = getString(R.string.SCY);
    		} else
    			return;
    		
    	} else if(from.equals(choices[1])){ // from SCM
    		resultFrom = getString(R.string.SCM);
    		
    		if(to.equals(choices[0])){ // to LCM
    			convertedTime = convertSCMtoLCM(currentEvent, time);
    			resultTo = getString(R.string.LCM);
    		} else if(to.equals(choices[2])){ // to SCY
    			convertedTime = convertSCMtoSCY(currentEvent, time);
    			resultTo = getString(R.string.SCY);
    		} else
    			return;
    		
    	} else if(from.equals(choices[2])){ // from SCY
    		resultFrom = getString(R.string.SCY);
    		
    		if(to.equals(choices[0])){ // to LCM
    			convertedTime = convertSCYtoLCM(currentEvent, time);
    			resultTo = getString(R.string.LCM);
    		} else if(to.equals(choices[1])){ // to SCM
    			convertedTime = convertSCYtoSCM(currentEvent, time);
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
    	fillData();
    	
    }
    
    private double convertLCMtoSCY(String currentEvent, double time) {
    	if(currentEvent.equals("50 Fly"))
    		time = (time - fiftyFly) / conversionFactor;
    	else if (currentEvent.equals("50 Back"))
    		time = (time - fiftyBack) / conversionFactor;
    	else if (currentEvent.equals("50 Breast"))
    		time = (time - fiftyBreast) / conversionFactor;
    	else if (currentEvent.equals("50 Free"))
    		time = (time - fiftyFree) / conversionFactor;
    	else if (currentEvent.equals("100 Fly"))
    		time = (time - hundredFly) / conversionFactor;
    	else if (currentEvent.equals("100 Back"))
    		time = (time - hundredBack) / conversionFactor;
    	else if (currentEvent.equals("100 Breast"))
    		time = (time - hundredBreast) / conversionFactor;
    	else if (currentEvent.equals("100 Free"))
    		time = (time - hundredFree) / conversionFactor;
    	else if (currentEvent.equals("200 Fly"))
    		time = (time - twoHundredFly) / conversionFactor;
    	else if (currentEvent.equals("200 Back"))
    		time = (time - twoHundredBack) / conversionFactor;
    	else if (currentEvent.equals("200 Breast"))
    		time = (time - twoHundredBreast) / conversionFactor;
    	else if (currentEvent.equals("200 Free"))
    		time = (time - twoHundredFree) / conversionFactor;
    	else if (currentEvent.equals("200 IM"))
    		time = (time - twoHundredIM) / conversionFactor;
    	else if (currentEvent.equals("400 IM"))
    		time = (time - fourHundredIM) / conversionFactor;
    	else if (currentEvent.equals("400/500"))
    		time /= fourHundredEq;
    	else if (currentEvent.equals("800/1000"))
    		time /= eightHundredEq;
    	else if (currentEvent.equals("1500/1650"))
    		time /= fifteenHundredEq;
    	
    	return time;
	}
    
    private double convertLCMtoSCM(String currentEvent, double time) {
    	if(currentEvent.equals("50 Fly"))
    		time -= fiftyFly;
    	else if (currentEvent.equals("50 Back"))
    		time -= fiftyBack;
    	else if (currentEvent.equals("50 Breast"))
    		time -= fiftyBreast;
    	else if (currentEvent.equals("50 Free"))
    		time -= fiftyFree;
    	else if (currentEvent.equals("100 Fly"))
    		time -= hundredFly;
    	else if (currentEvent.equals("100 Back"))
    		time -= hundredBack;
    	else if (currentEvent.equals("100 Breast"))
    		time -= hundredBreast;
    	else if (currentEvent.equals("100 Free"))
    		time -= hundredFree;
    	else if (currentEvent.equals("200 Fly"))
    		time -= twoHundredFly;
    	else if (currentEvent.equals("200 Back"))
    		time -= twoHundredBack;
    	else if (currentEvent.equals("200 Breast"))
    		time -= twoHundredBreast;
    	else if (currentEvent.equals("200 Free"))
    		time -= twoHundredFree;
    	else if (currentEvent.equals("200 IM"))
    		time -= twoHundredIM;
    	else if (currentEvent.equals("400 IM"))
    		time -= fourHundredIM;
    	else if (currentEvent.equals("400/500"))
    		time -= fourHundred;
    	else if (currentEvent.equals("800/1000"))
    		time -= eightHundred;
    	else if (currentEvent.equals("1500/1650"))
    		time -= fifteenHundred;
    	
    	return time;
	}

    private double convertSCMtoLCM(String currentEvent, double time) {
    	if(currentEvent.equals("50 Fly"))
    		time += fiftyFly;
    	else if (currentEvent.equals("50 Back"))
    		time += fiftyBack;
    	else if (currentEvent.equals("50 Breast"))
    		time += fiftyBreast;
    	else if (currentEvent.equals("50 Free"))
    		time += fiftyFree;
    	else if (currentEvent.equals("100 Fly"))
    		time += hundredFly;
    	else if (currentEvent.equals("100 Back"))
    		time += hundredBack;
    	else if (currentEvent.equals("100 Breast"))
    		time += hundredBreast;
    	else if (currentEvent.equals("100 Free"))
    		time += hundredFree;
    	else if (currentEvent.equals("200 Fly"))
    		time += twoHundredFly;
    	else if (currentEvent.equals("200 Back"))
    		time += twoHundredBack;
    	else if (currentEvent.equals("200 Breast"))
    		time += twoHundredBreast;
    	else if (currentEvent.equals("200 Free"))
    		time += twoHundredFree;
    	else if (currentEvent.equals("200 IM"))
    		time += twoHundredIM;
    	else if (currentEvent.equals("400 IM"))
    		time += fourHundredIM;
    	else if (currentEvent.equals("400/500"))
    		time += fourHundred;
    	else if (currentEvent.equals("800/1000"))
    		time += eightHundred;
    	else if (currentEvent.equals("1500/1650"))
    		time += fifteenHundred;
    	
    	return time;
	}

    private double convertSCMtoSCY(String currentEvent, double time) {
    	if (currentEvent.equals("400/500"))
    		time = (time + fourHundred) / fourHundredEq;
    	else if (currentEvent.equals("800/1000"))
    		time = (time + eightHundred) / eightHundredEq;
    	else if (currentEvent.equals("1500/1650"))
    		time = (time + fifteenHundred) / fifteenHundredEq;
    	else
    		time /= conversionFactor;
    	
    	return time;
	}
    
    private double convertSCYtoLCM(String currentEvent, double time) {
    	if(currentEvent.equals("50 Fly"))
    		time = (time * conversionFactor) + fiftyFly;
    	else if (currentEvent.equals("50 Back"))
    		time = (time * conversionFactor) + fiftyBack;
    	else if (currentEvent.equals("50 Breast"))
    		time = (time * conversionFactor) + fiftyBreast;
    	else if (currentEvent.equals("50 Free"))
    		time = (time * conversionFactor) + fiftyFree;
    	else if (currentEvent.equals("100 Fly"))
    		time = (time * conversionFactor) + hundredFly;
    	else if (currentEvent.equals("100 Back"))
    		time = (time * conversionFactor) + hundredBack;
    	else if (currentEvent.equals("100 Breast"))
    		time = (time * conversionFactor) + hundredBreast;
    	else if (currentEvent.equals("100 Free"))
    		time = (time * conversionFactor) + hundredFree;
    	else if (currentEvent.equals("200 Fly"))
    		time = (time * conversionFactor) + twoHundredFly;
    	else if (currentEvent.equals("200 Back"))
    		time = (time * conversionFactor) + twoHundredBack;
    	else if (currentEvent.equals("200 Breast"))
    		time = (time * conversionFactor) + twoHundredBreast;
    	else if (currentEvent.equals("200 Free"))
    		time = (time * conversionFactor) + twoHundredFree;
    	else if (currentEvent.equals("200 IM"))
    		time = (time * conversionFactor) + twoHundredIM;
    	else if (currentEvent.equals("400 IM"))
    		time = (time * conversionFactor) + fourHundredIM;
    	else if (currentEvent.equals("400/500"))
    		time *= fourHundredEq;
    	else if (currentEvent.equals("800/1000"))
    		time *= eightHundredEq;
    	else if (currentEvent.equals("1500/1650"))
    		time *= fifteenHundredEq;
    	
    	return time;
	}
    
    private double convertSCYtoSCM(String currentEvent, double time) {
    	if (currentEvent.equals("400/500"))
    		time = (time * fourHundredEq) - fourHundred;
    	else if (currentEvent.equals("800/1000"))
    		time = (time * eightHundredEq) - eightHundred;
    	else if (currentEvent.equals("1500/1650"))
    		time = (time * fifteenHundredEq) - fifteenHundred;
    	else
    		time *= conversionFactor;
    	
    	return time;
	}
    
}