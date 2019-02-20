package whitechapel.webmd.mmu.ac.uk.myapplication;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.AsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    LocationManager locMan;
    LocationListener locL;

    double locLat=0;
    double locLong=0;
    ArrayList<String> listLat;
    ArrayList<String> listLong;
    ArrayList<String> listRating;
    ArrayList<String> listName;
    ArrayList<String> listAddress1;
    ArrayList<String> listAddress2;
    ArrayList<String> listAddress3;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // get the map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
        map.getUiSettings().setZoomControlsEnabled(true);



        // check if any parameters has been passed
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            listLat = intent.getStringArrayListExtra("latArray");
            listLong = intent.getStringArrayListExtra("longArray");
            listRating = intent.getStringArrayListExtra("ratingArray");
            listName = intent.getStringArrayListExtra("nameArray");
            listAddress1 = intent.getStringArrayListExtra("add1Array");
            listAddress2 = intent.getStringArrayListExtra("add2Array");
            listAddress3 = intent.getStringArrayListExtra("add3Array");

            for (int i = 0; i < listRating.size(); i++) {
                Double lat = Double.valueOf(listLat.get(i));
                Double longi = Double.valueOf(listLong.get(i));
                LatLng ll = new LatLng(lat,longi);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lat, longi), 10));
                BitmapDescriptor bitmapMarker;
                switch (listRating.get(i)) {
                    case "0":
                        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.images00);
                        break;
                    case "-1":
                        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.imagese);
                       break;
                    case "1":
                        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.images1);
                        break;
                    case "2":
                        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.images2);
                        break;
                    case "3":
                        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.images3);
                        break;
                    case "4":
                        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.images4);
                        break;
                    case "5":
                        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.images5);
                        break;
                    default:
                        bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        break;
                }
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(ll).icon(bitmapMarker).title(listName.get(i) +" "+ listAddress1.get(i) +" "+ listAddress2.get(i) + " "+listAddress3.get(i)));

            }

        }

            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locL = new mylocationlistener();
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locL);




    }

    private void addMarkersToMap() {

    }

    class mylocationlistener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {
                if (null != location) {
                    locLat = location.getLatitude();
                    locLong = location.getLongitude();
                    LatLng ll = new LatLng(locLat, locLong);
                    //map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
                    //map.getUiSettings().setZoomControlsEnabled(true);
                    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 20));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(locLat, locLong), 12));
                    // create marker on map
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(ll).title("Current Location"));

                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
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

    public void findNearestOnClick(View view) {
                map.clear();
                Intent intent = new Intent(MainActivity.this, NearestRestActivity.class);
                intent.putExtra("lat",locLat);
                intent.putExtra("long",locLong);
                startActivity(intent);
                locLat=0;
                locLong=0;
    }

    public void findByNameBtnOnClick(View view ) {
        map.clear();
        EditText  enteredNameET = (EditText) findViewById(R.id.editText);
        String enteredNameStr = enteredNameET.getText().toString();
        if (enteredNameStr.matches("")) {
            Toast.makeText(getApplicationContext(), "Name Required!!!", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(MainActivity.this, PostCodeSearchActivity.class);
            intent.putExtra("restName", enteredNameStr);
            startActivity(intent);
            enteredNameStr.equals("");
            enteredNameET.setText("");
        }
    }

    public void postCodeBtnOnClick(View view) {
        map.clear();
        EditText  enteredPostCodeET = (EditText) findViewById(R.id.editText2);
        String enteredPostCodeStr = enteredPostCodeET.getText().toString();
        if (enteredPostCodeStr.matches("")) {
            Toast.makeText(getApplicationContext(), "PostCode Required!!!", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(MainActivity.this, PostCodeSearchActivity.class);
            intent.putExtra("postCode", enteredPostCodeStr);
            startActivity(intent);
            enteredPostCodeStr.equals("");
            enteredPostCodeET.setText("");
        }


    }

}