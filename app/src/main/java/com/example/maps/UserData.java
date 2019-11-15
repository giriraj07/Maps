package com.example.maps;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;


public class UserData extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {
    public EditText etName;
    public EditText etWallet;
    public EditText etAge;
    public EditText etphone;
    public Button btnSave;
    BitmapDescriptor bitmapDescriptor1;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap gmap;
    ArrayList<Marker> markers;
    ArrayList<Polyline> polylines;
    ArrayList<String> keys;
    User user;
    Location UserLocation;
    int COUNTER = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinput);
        Boolean check = getIntent().getBooleanExtra("first_check", true);
        if (check) {
            etName =  findViewById(R.id.etName);
            etWallet= findViewById(R.id.etWallet);
            etAge =   findViewById(R.id.etAge);
            etphone = findViewById(R.id.etPhone);
            btnSave = findViewById(R.id.btnSave);
            final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("User");
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etName.getText().toString().trim();  // taking name as input
                    Float Wallet = Float.parseFloat(etWallet.getText().toString().trim());
                    int age = Integer.parseInt(etAge.getText().toString());
                    String phoneNo = etphone.getText().toString();
                    Log.d("TAG", name + " " + Wallet + " " + age + " " + phoneNo);
                    user = new User(name, Wallet, age, phoneNo);
                    mref.child(FirebaseAuth.getInstance().getUid()).setValue(user);         // adding user in firebase

                    Toast.makeText(UserData.this, "User data Inserted Successfully!!", LENGTH_SHORT).show();
                    UserData.this.finish(); // on clicking button, return to previous activity
                }
            });
        } else {
            LinearLayout ll = findViewById(R.id.l1);
            ll.setVisibility(View.GONE);
            ll = findViewById(R.id.l2);
            ll.setVisibility(View.VISIBLE);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);                // setting map Fragment

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserData.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }                 // Asking user to turn on GPS
            bitmapDescriptor1 = bitmapDescriptorFromVector(this, R.drawable.ic_directions_bus_black_24dp);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);   // for user's current Location

            DatabaseReference mTest = FirebaseDatabase.getInstance().getReference();
            markers =   new ArrayList<>();     // Array of Driver markers
            polylines = new ArrayList<>();  // Array of lines being drawn
                keys =  new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                markers.add(null);
                polylines.add(null);
                keys.add(null);
            }

            mTest.child("Drivers").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // called when data is  changed
                    COUNTER++;
                    int i = -1;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                            i++;
                        if (keys.get(i) != null && !dataSnapshot.child(keys.get(i)).exists()) { // Removing marker and line
                            markers.get(i).remove();
                            keys.set(i, null);
                            markers.set(i, null);
                            polylines.get(i).remove();
                            polylines.set(i, null);
                            break;
                        }
                        if (markers.get(i) == null) {              // for first time, marker will be null
                            if (!child.child("long").exists())
                                continue;                    // longitude is null when new driver is added in runTime
                            keys.set(i, child.getKey());
                            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom
                                    (new LatLng(
                                            Double.parseDouble(child.child("lat").getValue().toString()), Double.parseDouble(child.child("long").
                                            getValue().toString())), 15));  // Focussing camera on Giving marker's position
                            markers.set(i, gmap.addMarker(new MarkerOptions().position(new LatLng(
                                    Double.parseDouble(child.child("lat").getValue().toString()), Double.parseDouble(child.child("long").
                                    getValue().toString())))
                                    .icon(bitmapDescriptor1)));
                        } else {
                            if (COUNTER % 3 == 0) {  // On every third  firebase callback
                                VolleyCall(i);       // making request to Tom API
                                Toast.makeText(getApplicationContext(), "loc. update", LENGTH_SHORT).show();
                            }
                            if (polylines.get(i) == null) {
                                polylines.set(i, (gmap.addPolyline(new PolylineOptions().width(16).
                                        color(Color.BLUE).add(markers.get(i).
                                                getPosition(),
                                        new LatLng(
                                                Double.parseDouble(child.child("lat").getValue().toString()),
                                                Double.parseDouble(child.child("long").
                                                        getValue().toString()))))));
                            }
                            markers.get(i).remove();                                                // remove old marker
                            markers.set(i, gmap.addMarker(new MarkerOptions().position(new LatLng( // Add latest marker
                                    Double.parseDouble(child.child("lat").getValue().toString()),
                                    Double.parseDouble(child.child("long").
                                            getValue().toString()))).icon(bitmapDescriptor1)));
                            if (polylines.get(i) != null) {      // Adding new Points in Polyline
                                List<LatLng> points = polylines.get(i).getPoints(); // Adding multiple points
                                points.add(markers.get(i).getPosition());
                                polylines.get(i).setPoints(points);
                            }
                        }
                        if (gmap.getCameraPosition().zoom < 15)
                            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom
                                    (new LatLng((Double) child.child("lat").getValue(), (Double) child.child("long").getValue()), 15));
                    }
                    if (keys.get(i + 1) != null && !dataSnapshot.child(keys.get(i + 1)).exists()) {
                        markers.get(i + 1).remove();
                        keys.set(i + 1, null);
                        markers.set(i + 1, null);
                        if (polylines.get(i + 1) != null)
                            polylines.get(i + 1).remove();
                        polylines.set(i + 1, null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
             if (locationList.size() > 0) {
                UserLocation = locationList.get(locationList.size() - 1);
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom
                        (new LatLng(UserLocation.getLatitude(), UserLocation.getLongitude()), 15));
                gmap.addMarker(new MarkerOptions().position(new LatLng(UserLocation.getLatitude(), UserLocation.getLongitude())).
                        icon(bitmapDescriptorFromVector(
                        getApplicationContext(), R.drawable.ic_person_pin_circle_black_24dp)));
                fusedLocationProviderClient.removeLocationUpdates(this);
            }
        }
    };


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setOnMapClickListener(this);
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.725929, 77.121061), 13)
        );
      /*  Polyline polyline = gmap.addPolyline(new PolylineOptions()
                .add(new LatLng(28.725929, 77.121061))
                .add(new LatLng(28.727924, 77.118486))
                .add(new LatLng(28.730888, 77.117585)));
        final ArrayList<LatLng> dynamicMarkers = (ArrayList<LatLng>) polyline.getPoints();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        i++;
                        i = i % 3;
                        if (marker1 != null) {
                            marker1.remove();
                        }
                        marker1 = gmap.addMarker(new MarkerOptions().position(dynamicMarkers.get(i)));
                        marker1.setIcon(bitmapDescriptor1);
                    }
                });
            }


        }, 0, 1000);*/

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
    /*@RequiresApi(api = Build.VERSION_CODES.O)
    long GiveMeTimeInSecond() {
        LocalDateTime now = LocalDateTime.now();
        long totalSeconds;
        System.out.println("Before Formatting: " + now);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(format);
        String[] res = formatDateTime.split(" ");
        long minutes = Long.parseLong(res[1].substring(3,5));
        long seconds = Long.parseLong(res[1].substring(6));
        totalSeconds = minutes * 60 + seconds;
        Log.d("TAG", String.valueOf(totalSeconds));
           return totalSeconds;
       }*/
         void VolleyCall(final int pos/*, LatLng source, LatLng dest, final ArrayList<LatLng> wayPoints, final Marker marker*/)
                                                               // Calling the Volley Library
         {
             String url="https://api.tomtom.com/routing/1/calculateRoute/"
                     +markers.get(pos).getPosition().latitude+","+markers.get(pos).getPosition().longitude+":"+
                     UserLocation.getLatitude()+","+UserLocation.getLongitude()+"/json?key=R8WuUMrIHzdGJXAtr63dSS8SCCw7fwyZ";

             RequestQueue req= Volley.newRequestQueue(UserData.this);
             JsonObjectRequest jsonObjectRequest = new JsonObjectRequest

                     (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                         @Override
                         public void onResponse(JSONObject response) {
                             try {
                                 Integer length= ((Integer)((JSONObject)((JSONObject) ((JSONArray)response.get("routes")).get(0)).get("summary")).
                                         get("lengthInMeters"));
                                 String ArrivalTime=((String)((JSONObject)((JSONObject) ((JSONArray)response.get("routes")).
                                         get(0)).get("summary")).
                                         get("arrivalTime"));
                                ArrivalTime=ArrivalTime.split("T")[1].substring(0, 8);
                                  JSONArray Points=((JSONArray)((JSONObject)((JSONArray)((JSONObject)((JSONArray)response.get("routes")).get(0))
                                         .get("legs")).get(0)).get("points"));
                                 List<LatLng> points = polylines.get(pos).getPoints();
                                 ArrayList<LatLng> point= new ArrayList<>();
                                 for (int i=0;i<Points.length();i++){
                                     points.add(new LatLng((Double)((JSONObject)Points.get(i)).get("latitude"),
                                             (Double)((JSONObject)Points.get(i)).get("longitude")));
                                 }
                                 polylines.get(pos).setPoints(point);
                                 markers.get(pos).setTitle(ArrivalTime+" "+length);
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             // TODO: Handle error
                         }
                     });
             req.add(jsonObjectRequest);
             //req.stop();
         }
}
