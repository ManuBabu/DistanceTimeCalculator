package com.apps.manu.distancetimecalculator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    GoogleMap map;
    private LatLng selectedloc;
    private LocationManager mLocationManager;
    private static final int MIN_TIME_BW_UPDATES = 5;
    private static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;
    private Location current_Location;
    Double lat,lng;
    Location location;
    Location myLocation;
    AsyncHttpClient client;
    Gson gson;
    Response response11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (map == null)
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().isCompassEnabled();
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        getMyCurrentLocation();

        gson= new Gson();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                String URL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="+selectedloc.latitude+","+selectedloc.longitude +"&destinations="+latLng.latitude+","+latLng.longitude +"&mode=driving&language=en-EN&sensor=false";
                Log.d("URL",""+URL);

                client = new AsyncHttpClient();
                client.get(getApplicationContext(),URL, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String Response2 = new String(responseBody);

                        response11 = gson.fromJson(Response2, Response.class);

                      //  Toast.makeText(getApplicationContext(),""+response11.getRows(),Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject object = new JSONObject(Response2);
                            JSONArray array = object.getJSONArray("rows");
                            JSONObject sub= array.getJSONObject(0);
                            Log.d("Resultsarray",""+array);
                            Log.d("Resultssub",""+sub);

                            JSONArray elements = sub.getJSONArray("elements");
                            JSONObject zero = elements.getJSONObject(0);

                            JSONObject dist = zero.getJSONObject("distance") ;
                            JSONObject dur = zero.getJSONObject("duration");

                            Log.d("Resultsarraydistary", "" + dist);
                            Log.d("Resultssubduraray", "" + dur);
                            String distance = dist.getString("text");
                            String duration = dur.getString("text");

                            Log.d("Results", "" + distance);
                            Log.d("Results", "" + duration);

                           // Toast.makeText(getApplicationContext(),"Distance"+distance+"\nDuration"+duration,Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder builder = new  AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Time And Distance");
                            builder.setMessage("The distance between your location and clicked destination is: \n Distance:" + distance + "\nTravel Time:" + duration);

                            builder.show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }
        });

    }

    public void getMyCurrentLocation() {

        // getting GPS status
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d("tryGPS ", "" + isGPSEnabled);
        Log.d("tryNEtwork", "" + isNetworkEnabled);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(getApplicationContext(), "Both GPS and network is not enabled", Toast.LENGTH_LONG).show();
        } else {
            // First get location from Network Provider
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
                Log.d("Network", "Network");
                if (mLocationManager != null) {
                    Location location1;
                    location1 = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    Log.d("Location_network", "" + location1);
                    if (location1 != null) {
                        selectedloc = new LatLng(location1.getLatitude(), location1.getLongitude());
                        setLocation(location1);
                        Log.d("selectedlochere", "" + selectedloc);
                    }

                    Log.d("selected_location", "" + selectedloc);

                    myLocation = location1;
                    if (location1 != null) {
                        lat = location1.getLatitude();
                        lng = location1.getLongitude();
                        myLocation = location1;
                        Log.d("try_mylocation", "NWLocation" + location1);
                    }
                }
            }

            //get the location by gps
            if (isGPSEnabled) {
                if (location == null) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (mLocationManager != null) {
                        Location location2;
                        location2 = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.d("Location_GPS", "" + location2);
                        //  myLocation = location2;
                        if (location2 != null) {
                            selectedloc = new LatLng(location2.getLatitude(), location2.getLongitude());
                            setLocation(location2);
                        }

                        Log.d("selected_location", "" + selectedloc);
                        if (location != null) {
                            lat = location2.getLatitude();
                            lng = location2.getLongitude();
                            location = location2;
                        }
                    }
                }
            }
        }
    }
    public void setLocation(Location latLng) {
        current_Location = latLng;
        selectedloc = new LatLng(latLng.getLatitude(), latLng.getLongitude());
    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
}
