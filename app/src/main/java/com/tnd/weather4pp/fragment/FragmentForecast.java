package com.tnd.weather4pp.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tnd.weather4pp.MainActivity;
import com.tnd.weather4pp.R;
import com.tnd.weather4pp.adapter.ForeCastAdapter;
import com.tnd.weather4pp.model.ForeCastDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FragmentForecast extends Fragment{
    View view;
    TextView tvFcLocation, tvFcLastUpdate;
    ArrayList<ForeCastDay> mData = new ArrayList<ForeCastDay>();
    ForeCastAdapter adapter = null;
    ListView lvForecast;
    private String locationFC = "Nam Dinh, Viet Nam";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forecast, container, false);
        lvForecast = (ListView) view.findViewById(R.id.lv_forecast);
        tvFcLocation = (TextView) view.findViewById(R.id.tv_fc_location);
        tvFcLastUpdate = (TextView) view.findViewById(R.id.tv_fc_lastupdate);
        String myTag = getTag();
        ((MainActivity) getActivity()).setTabFragmentB(myTag);
        refreshWeather(locationFC);
        setHasOptionsMenu(true);
        adapter = new ForeCastAdapter(getContext(), mData);
        lvForecast.setAdapter(adapter);
        return view;
    }

    public void b_updateText(String t) {
        if (t != null) {
            refreshWeather(t);
        } else {
            Toast.makeText(getActivity(), "Proccess Failure !!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshWeather(String l) {
        this.locationFC = l;
        new AsyncTask<String, Void, String>() {


            @Override
            protected String doInBackground(String... params) {
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")and u='c'", params[0]);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                return ReadContentFromURL(endpoint);

            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mData.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonQuery = jsonObject.getJSONObject("query");
                    JSONObject jsonResults = jsonQuery.getJSONObject("results");
                    JSONObject jsonChannel = jsonResults.getJSONObject("channel");
                    JSONObject jsonItem = jsonChannel.getJSONObject("item");
                    JSONObject jsonLocation = jsonChannel.getJSONObject("location");

                    tvFcLocation.setText(jsonLocation.getString("city"));
                    tvFcLastUpdate.setText(jsonChannel.getString("lastBuildDate"));


                    JSONArray arrayForecast = jsonItem.getJSONArray("forecast");
                    for (int i = 1; i < arrayForecast.length() - 2; i++) {
                        JSONObject fc = arrayForecast.getJSONObject(i);
                        mData.add(new ForeCastDay(fc.getInt("code"),
                                fc.getString("day"),
                                fc.getInt("high"),
                                fc.getInt("low"),
                                fc.getString("text")));
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(l);
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
        inflater.inflate(R.menu.menu_forecast, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_fc_refresh) {
            Toast.makeText(getActivity(), "Updating.....", Toast.LENGTH_SHORT).show();
            refreshWeather(tvFcLocation.getText().toString().trim());
        }

        return super.onOptionsItemSelected(item);
    }


}
