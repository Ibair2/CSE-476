package edu.msu.bhushanj.arewethereyetbhushanj;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActiveListener activeListener = new ActiveListener();

    private LocationManager locationManager = null;

    private double latitude = 0;
    private double longitude = 0;
    private boolean valid = false;

    private double toLatitude = 0;
    private double toLongitude = 0;
    private String to = "";
    private SharedPreferences settings = null;


    private final static String TO = "to";
    private final static String TOLAT = "tolat";
    private final static String TOLONG = "tolong";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handle an options menu selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemSparty:
                newTo("Sparty", 42.731138, -84.487508);
                return true;

            case R.id.itemHome:
                newTo("My Home", 42.726634, -84.467741);
                return true;

            case R.id.item1225:
                newTo("1225 Engineering", 42.724303, -84.480507);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle setting a new "to" location.
     * @param address Address to display
     * @param lat latitude
     * @param lon longitude
     */
    private void newTo(String address, double lat, double lon) {
        to = address;
        toLatitude = lat;
        toLongitude = lon;

        setUI();
    }

    public void onNew(View view) {
        EditText location = (EditText)findViewById(R.id.editText);
        final String address = location.getText().toString().trim();
        newAddress(address);
    }

    /**
     * Look up the provided address. This works in a thread!
     * @param address Address we are looking up
     */
    private void lookupAddress(final String address) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.US);
        boolean exception = false;
        List<Address> locations;
        try {
            locations = geocoder.getFromLocationName(address, 1);
        } catch(IOException ex) {
            // Failed due to I/O exception
            locations = null;
            exception = true;
        }
        final boolean tempException = exception;
        final List<Address> tempAddress = locations;

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                newLocation(address,tempException, tempAddress);
            }
        });
    }

    private void newLocation(String address, boolean exception, List<Address> locations) {

        if(exception) {
            Toast.makeText(MainActivity.this, R.string.exception, Toast.LENGTH_SHORT).show();
        } else {
            if(locations == null || locations.size() == 0) {
                Toast.makeText(this, R.string.couldnotfind, Toast.LENGTH_SHORT).show();
                return;
            }

            EditText location = (EditText)findViewById(R.id.editText);
            location.setText("");

            // We have a valid new location
            Address a = locations.get(0);
            newTo(address, a.getLatitude(), a.getLongitude());

        }
    }

    private void newAddress(final String address) {
        if(address.equals("")) {
            // Don't do anything if the address is blank
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                lookupAddress(address);

            }

        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        to = settings.getString(TO, "1225 Engineering");

        toLatitude = Double.parseDouble(settings.getString(TOLAT, "42.724303"));
        toLongitude = Double.parseDouble(settings.getString(TOLONG, "-84.480507"));

        latitude = 42.731138;
        longitude = -84.487508;
        valid = true;

        // Get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    }

    private void registerListeners() {
        unregisterListeners();
        // Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestAvailable = locationManager.getBestProvider(criteria, true);

        if(bestAvailable != null) {
            locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);
            TextView viewProvider = (TextView)findViewById(R.id.textView4);
            viewProvider.setText(bestAvailable);
            Location location = locationManager.getLastKnownLocation(bestAvailable);
            onLocation(location);
        }
    }

    private void onLocation(Location location) {
        if(location == null) {
            return;
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        valid = true;

        setUI();
    }

    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }

    /**
     * Set all user interface components to the current state
     */
    private void setUI() {
        TextView temp = (TextView)findViewById(R.id.textView10);
        temp.setText(to);
        if(!valid){
            TextView temp1 = (TextView)findViewById(R.id.textView5);
            temp1.setText(" ");
            TextView temp2 = (TextView)findViewById(R.id.textView11);
            temp2.setText(" ");
            TextView temp3 = (TextView)findViewById(R.id.textView6);
            temp3.setText(" ");
        }
        if(valid){
            TextView temp1 = (TextView)findViewById(R.id.textView5);
            temp1.setText(String.valueOf(latitude));
            TextView temp2 = (TextView)findViewById(R.id.textView6);
            temp2.setText(String.valueOf(longitude));


            Location locationA = new Location("A");

            locationA.setLatitude(latitude);
            locationA.setLongitude(longitude);

            Location locationB = new Location("B");

            locationB.setLatitude(toLatitude);
            locationB.setLongitude(toLongitude);

            float distance = locationA.distanceTo(locationB);

            TextView temp3 = (TextView)findViewById(R.id.textView11);
            temp3.setText(String.format("%1$6.1fm", distance));

            //Location.distanceBetween(locationA.getLatitude(),locationA.getLongitude(),locationB.getLatitude(),locationB.getLongitude());

        }
    }


    /**
     * Called when this application becomes foreground again.
     */
    @Override
    protected void onResume() {
        super.onResume();

        TextView viewProvider = (TextView)findViewById(R.id.textView4);
        viewProvider.setText("");

        setUI();
        registerListeners();
    }
    /**
     * Called when this application is no longer the foreground application.
     */
    @Override
    protected void onPause() {
        unregisterListeners();
        super.onPause();
    }


    private class ActiveListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            onLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            registerListeners();
        }
    };

}
