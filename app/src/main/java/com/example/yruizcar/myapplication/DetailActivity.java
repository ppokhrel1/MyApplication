package com.example.yruizcar.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ArrayList<Spot> spots = new ArrayList<Spot>();

        //don't change this part till the app runs
        spots.add(new Spot("green"));
        spots.add(new Spot("red"));
        spots.add(new Spot("red"));
        spots.add(new Spot("green"));

        try{
            ArrayList<Spot> spots1 = new Feed().execute().get();
            if (spots1.size() > 0){
                spots = spots1;
            }
        }catch(Exception e){
            Log.d("Exception", "Can't get anything from server");
        }
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button)findViewById(R.id.button));
        buttons.add((Button)findViewById(R.id.button2));
        buttons.add((Button)findViewById(R.id.button3));
        buttons.add((Button)findViewById(R.id.button4));

        Log.d("something", spots.toString());



        for (int i = 0; i < buttons.size(); i++) {
            if (spots.get(i).color.equals("green"))
                buttons.get(i).setBackgroundColor(Color.GREEN);
            else if (spots.get(i).color.equals("red"))
                buttons.get(i).setBackgroundColor(Color.RED);
            else if (spots.get(i).color.equals("yellow"))
                buttons.get(i).setBackgroundColor(Color.YELLOW);
            else {
                //because you didn't assign any color
                buttons.get(i).setBackgroundColor(Color.BLACK);
                Log.d("Exception", "No color found in the api");
            }
        }

    }

    private class Feed extends AsyncTask<String, Void, ArrayList<Spot>>{
        protected ArrayList<Spot> doInBackground(String... urls){
            return this.populate();
        }

        protected void onPostExecute(ArrayList<Spot> spots){
            super.onPostExecute(spots);
        }

        private ArrayList<Spot> populate(){
            ArrayList<Spot> spots = new ArrayList<Spot>();
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            String myurl = "http://spotfinder.herokuapp.com/get_data/";

            try {
                URL url = new URL
                        (myurl);
                //Log.d("kjhdfkjas", myurl);
                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // gets the server json data
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                String next;


                while ((next = bufferedReader.readLine()) != null) {
                    //JSONArray ja = new JSONArray(next);
                    Log.d("app", next);
                    JSONObject jas = new JSONObject(next);
                    //JSONArray ja = new JSONArray("results");
                    Log.d("uweyuri", jas.toString());

                    JSONArray ja = jas.getJSONArray("results");
                    //Log.d("adfas", ja.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        //Log.d("adf", jo.toString());
                        //Log.d("adfas", "adfa");
                        //items.add(jo.getString("text"));
                        Spot myspot = new Spot();
                        String name = jo.getString("name");
                        //String description = jo.getString("description");
                        String color = jo.getString("color");
                        myspot.name = name;
                        myspot.color = color;
                        spots.add(myspot);
                        //get all the jsonobjects, create different recipes from them and add to items array.

                    }
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (JSONException eg) {
                // TODO Auto-generated catch block
                eg.printStackTrace();
            }
            return spots;
        }

    }
}
