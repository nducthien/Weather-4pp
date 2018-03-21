package com.tnd.weather4pp.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tnd.weather4pp.MainActivity;
import com.tnd.weather4pp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class FragmentMain extends Fragment {

    TextView tvConditon;
    TextView tvLocation;
    TextView tvTemperature;
    TextView tvDate;
    TextView tvPressure;
    TextView tvWind;
    TextView tvWet;
    ImageView imgWeatherIcon;
    String location = "Nam Dinh, Viet Nam";
    ProgressDialog dialog;
    View view;
    int count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        init();
        dialog = new ProgressDialog(getActivity());
        refreshWeather(location);
        setHasOptionsMenu(true);
        return view;
    }

    private void init() {
        tvConditon = (TextView) view.findViewById(R.id.tv_codition);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        tvPressure = (TextView) view.findViewById(R.id.tv_pressure);
        tvWet = (TextView) view.findViewById(R.id.tv_humidity);
        tvWind = (TextView) view.findViewById(R.id.tv_wind);
        tvTemperature = (TextView) view.findViewById(R.id.tv_temperature);
        imgWeatherIcon = (ImageView) view.findViewById(R.id.img_weather_icon);
    }

    public void refreshWeather(String l) {
        this.location = l;
        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();
                dialog.setMessage("Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")and u='c'", params[0]);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                return ReadContentFromURL(endpoint);

            }


            @Override
            protected void onPostExecute(String s) {
                dialog.hide();
                super.onPostExecute(s);
                try {


                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonQuery = jsonObject.getJSONObject("query");
                    JSONObject jsonResults = jsonQuery.getJSONObject("results");
                    JSONObject jsonChannel = jsonResults.getJSONObject("channel");
                    JSONObject jsonItem = jsonChannel.getJSONObject("item");
                    JSONObject jsonCoditon = jsonItem.getJSONObject("condition");
                    JSONObject jsonUnit = jsonChannel.getJSONObject("units");
                    JSONObject jsonLocation = jsonChannel.getJSONObject("location");
                    JSONObject jsonWind = jsonChannel.getJSONObject("wind");
                    JSONObject jsonAtmostphere = jsonChannel.getJSONObject("atmosphere");


                    tvLocation.setText(jsonLocation.getString("city"));
                    tvTemperature.setText(jsonCoditon.getInt("temp") + "\u00B0" + jsonUnit.getString("temperature"));
                    tvConditon.setText(jsonCoditon.getString("text"));
                    tvDate.setText(jsonChannel.getString("lastBuildDate"));
                    double pressure = (jsonAtmostphere.getInt("pressure")) / 33.8637526;
                    tvPressure.setText(Math.round(pressure) + " mb");
                    tvWet.setText(jsonAtmostphere.getInt("humidity") + "%");
                    tvWind.setText(jsonWind.getInt("speed") + jsonUnit.getString("speed"));
                    int Icon = getResources().getIdentifier("drawable/icon_" + jsonCoditon.getString("code"), null, getActivity().getPackageName());
                    imgWeatherIcon.setImageResource(Icon);
                    count = jsonQuery.getInt("count");


                } catch (JSONException e) {
                    if (count == 0) {
                        Toast.makeText(getActivity(), "No weather information  found for " + getLocation(), Toast.LENGTH_SHORT).show();
                    }
                    e.printStackTrace();
                }
            }
        }.execute(l);
    }

    public String getLocation() {
        return location;
    }

    private static String ReadContentFromURL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void ShowDialogChangeLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Location");
        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHint("Nam Dinh, Viet Nam");
        builder.setView(editText);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshWeather(editText.getText().toString().trim());

                String textPassToFC = editText.getText().toString();

                String TabOfFragmentFC = ((MainActivity) getActivity()).getTabFragmentB();

                FragmentForecast fragmentB = (FragmentForecast) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(TabOfFragmentFC);

                fragmentB.b_updateText(textPassToFC);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_location:
                ShowDialogChangeLocation();
                break;
            case R.id.menu_refresh:
                Toast.makeText(getActivity(), "Updating.....", Toast.LENGTH_SHORT).show();
                refreshWeather(tvLocation.getText().toString().trim());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
