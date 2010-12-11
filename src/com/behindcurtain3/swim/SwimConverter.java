package com.behindcurtain3.swim;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SwimConverter extends ListActivity {
	public static final int CLEAR_LIST = Menu.FIRST;
	// Spinners
	private Spinner mSpEvent;
	//private Spinner mSpDistance;
	//private Spinner mSpStroke;
	
	// Text inputs
	private EditText mEtMinutes;
	private EditText mEtSeconds;
	private EditText mEtHundreths;
	
	// Buttons
	private Button mButtonLC;
	private Button mButtonSC;
	
	// Database
	private SwimDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Bind our variables to the interface items
        //mSpDistance = (Spinner)findViewById(R.id.distance);
        //mSpStroke = (Spinner)findViewById(R.id.stroke);
        mSpEvent = (Spinner)findViewById(R.id.event);
        mEtMinutes = (EditText)findViewById(R.id.input_minutes);
        mEtSeconds = (EditText)findViewById(R.id.input_seconds);
        mEtHundreths = (EditText)findViewById(R.id.input_hundreths);
        mButtonLC = (Button)findViewById(R.id.button_lc);
        mButtonSC = (Button)findViewById(R.id.button_sc);
        
        // Load the spinners with the arrays
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpEvent.setAdapter(adapter);
        
        //adapter = ArrayAdapter.createFromResource(this, R.array.strokes, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mSpStroke.setAdapter(adapter);
        
        // Restrict the input fields to numbers only
        mEtMinutes.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtSeconds.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtHundreths.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        // Setup the button listeners
        mButtonLC.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				createTime(false);
			} 
        });
        
        mButtonSC.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				createTime(true);
			} 
        });
        
        // Setup ad's
        //AdManager.setTestDevices(new String[] { AdManager.TEST_EMULATOR });
        AdView ad = (AdView)findViewById(R.id.ad);
        ad.setVisibility(View.VISIBLE);
        
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
     * @args - toSC, true = LC -> SC, false = SC -> LC
     */
    private void createTime(boolean toSC)
    {
    	// Make sure some sort of numbers have been entered
    	if(mEtMinutes.getText().length() == 0 || mEtSeconds.getText().length() == 0 || mEtHundreths.getText().length() == 0)
    	{
    		Toast.makeText(this, "Please enter a complete time.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
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
    	double fourHundred = 0.8925;
    	double eightHundred = 0.8925;
    	double fifteenHundred = 1.02;
    	
    	// Get whatever event the user selected
    	String currentEvent = (String)mSpEvent.getSelectedItem();
    	    	
    	
    	// Setup the time the user input in seconds
    	int minutes = Integer.parseInt(mEtMinutes.getText().toString(), 10);
    	int seconds = Integer.parseInt(mEtSeconds.getText().toString(), 10);
    	int hundreths = Integer.parseInt(mEtHundreths.getText().toString(), 10);
    	
    	// We need the total seconds
    	double time = (minutes * 60) + (seconds) + ((double)hundreths / 100);
    	
    	// switch the calculation based on the event
    	if(currentEvent.equals("50 Fly"))
    	{
    		time = (toSC ? ((time - fiftyFly) / conversionFactor) : ((time * conversionFactor) + fiftyFly));
    	} 
    	else if (currentEvent.equals("50 Back"))
    	{
    		time = (toSC ? ((time - fiftyBack) / conversionFactor) : ((time * conversionFactor) + fiftyBack));
    	}
    	else if (currentEvent.equals("50 Breast"))
    	{
    		time = (toSC ? ((time - fiftyBreast) / conversionFactor) : ((time * conversionFactor) + fiftyBreast));
    	}
    	else if (currentEvent.equals("50 Free"))
    	{
    		time = (toSC ? ((time - fiftyFree) / conversionFactor) : ((time * conversionFactor) + fiftyFree));
    	}
    	else if (currentEvent.equals("100 Fly"))
    	{
    		time = (toSC ? ((time - hundredFly) / conversionFactor) : ((time * conversionFactor) + hundredFly));
    	}
    	else if (currentEvent.equals("100 Back"))
    	{
    		time = (toSC ? ((time - hundredBack) / conversionFactor) : ((time * conversionFactor) + hundredBack));
    	}
    	else if (currentEvent.equals("100 Breast"))
    	{
    		time = (toSC ? ((time - hundredBreast) / conversionFactor) : ((time * conversionFactor) + hundredBreast));
    	}
    	else if (currentEvent.equals("100 Free"))
    	{
    		time = (toSC ? ((time - hundredFree) / conversionFactor) : ((time * conversionFactor) + hundredFree));
    	}
    	else if (currentEvent.equals("200 Fly"))
    	{
    		time = (toSC ? ((time - twoHundredFly) / conversionFactor) : ((time * conversionFactor) + twoHundredFly));
    	}
    	else if (currentEvent.equals("200 Back"))
    	{
    		time = (toSC ? ((time - twoHundredBack) / conversionFactor) : ((time * conversionFactor) + twoHundredBack));
    	}
    	else if (currentEvent.equals("200 Breast"))
    	{
    		time = (toSC ? ((time - twoHundredBreast) / conversionFactor) : ((time * conversionFactor) + twoHundredBreast));
    	}
    	else if (currentEvent.equals("200 Free"))
    	{
    		time = (toSC ? ((time - twoHundredFree) / conversionFactor) : ((time * conversionFactor) + twoHundredFree));
    	}
    	else if (currentEvent.equals("200 IM"))
    	{
    		time = (toSC ? ((time - twoHundredIM) / conversionFactor) : ((time * conversionFactor) + twoHundredIM));
    	}
    	else if (currentEvent.equals("400 IM"))
    	{
    		time = (toSC ? ((time - fourHundredIM) / conversionFactor) : ((time * conversionFactor) + fourHundredIM));
    	}
    	else if (currentEvent.equals("400/500"))
    	{
    		time = (toSC ? (time / fourHundred) : (time * fourHundred));
    	}
    	else if (currentEvent.equals("800/1000"))
    	{
    		time = (toSC ? (time / eightHundred) : (time * eightHundred));
    	}
    	else if (currentEvent.equals("1500/1650"))
    	{
    		time = (toSC ? (time / fifteenHundred) : (time * fifteenHundred));
    	}
    	else // Not a valid selection
    	{
    		return;
    	}
    	
    	// Do the conversion based on direction specified
    	//time = (toSC ? (time/conversionFactor) : (time * conversionFactor));
    	
    	// Reformat the data
    	int newMinutes = (int)(time / 60);
    	int newSeconds = (int)(time % 60);
    	int newHundreths = (int)((time - (newMinutes * 60) - newSeconds) * 100);
    	
    	// Format the data into a readable string
    	String result;
    	if(toSC)
    		result = String.format("%s - %02d:%02d.%02d %s %s %02d:%02d.%02d %s", currentEvent, minutes, seconds, hundreths, getString(R.string.meters), getString(R.string.conversion), newMinutes, newSeconds, newHundreths, getString(R.string.yards));
    	else
    		result = String.format("%s - %02d:%02d.%02d %s %s %02d:%02d.%02d %s", currentEvent, minutes, seconds, hundreths, getString(R.string.yards), getString(R.string.conversion), newMinutes, newSeconds, newHundreths, getString(R.string.meters));
    	
    	// Store the string
    	mDbHelper.createTime(result);
    	fillData();
    }
}