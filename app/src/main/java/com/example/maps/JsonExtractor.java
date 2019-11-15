package com.example.maps;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonExtractor extends AppCompatActivity {

      TextView tv;
     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.volley_layout);
         tv=findViewById(R.id.tvlayout);
         String url="https://api.tomtom.com/routing/1/calculateRoute/28.723715,77.125397:28.724715,77.125397/json?key=R8WuUMrIHzdGJXAtr63dSS8SCCw7fwyZ";
         RequestQueue req= Volley.newRequestQueue(JsonExtractor.this);

         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                 (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         tv.setText(response.toString());
//                       Toast.makeText(JsonExtractor.this,"Response: " + response.toString(),Toast.LENGTH_SHORT).show();
                         try {
                             Integer length= ((Integer)((JSONObject)((JSONObject) ((JSONArray)response.get("routes")).get(0)).get("summary")).get("lengthInMeters"));
                             Integer Time= ((Integer)((JSONObject)((JSONObject) ((JSONArray)response.get("routes")).get(0)).get("summary")).get("travelTimeInSeconds"));
                             String departureTime=((String)((JSONObject)((JSONObject) ((JSONArray)response.get("routes")).get(0)).get("summary")).get("departureTime"));
                             String ArrivalTime=((String)((JSONObject)((JSONObject) ((JSONArray)response.get("routes")).get(0)).get("summary")).get("arrivalTime"));
                             JSONArray points=((JSONArray)((JSONObject)((JSONArray)((JSONObject)((JSONArray)response.get("routes")).get(0))
                                     .get("legs")).get(0)).get("points"));
//                             LatLng l1=new LatLng()

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
