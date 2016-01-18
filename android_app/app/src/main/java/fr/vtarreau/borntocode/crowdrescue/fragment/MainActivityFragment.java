package fr.vtarreau.borntocode.crowdrescue.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import fr.necmergitur.borntocode.crowdrescue.crowdrescue.R;
import fr.vtarreau.borntocode.crowdrescue.GPSTracker;
import fr.vtarreau.borntocode.crowdrescue.HttpConnection;
import fr.vtarreau.borntocode.crowdrescue.MainActivity;
import fr.vtarreau.borntocode.crowdrescue.msgs.ReturnMessage;
import fr.vtarreau.borntocode.crowdrescue.objects.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends android.app.Fragment implements View.OnClickListener {

    private String myLocation;

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ReturnMessage returnMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        sharedPreferences = getActivity().getSharedPreferences(MainActivity.sharedpreferencesString, Context.MODE_PRIVATE);

        Button sync_state = (Button) rootview.findViewById(R.id.syncState);
        Spinner dispo_spinner = (Spinner) rootview.findViewById(R.id.dispo_spinner);

        sync_state.setOnClickListener(this);

        handleSpinner(dispo_spinner);

        return rootview;
    }

    public void handleSpinner(final Spinner spinner) {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.dispo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    MainActivity.user_state = "1";
                else if (position == 1)
                    MainActivity.user_state = "2";
                else
                    MainActivity.user_state = "0";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (sharedPreferences.getString(MainActivity.sp_Dispo, null) == null)
            spinner.setSelection(0);
        else {
            spinner.setSelection(adapter.getPosition(sharedPreferences.getString(MainActivity.sp_Dispo, null)));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.syncState) {
            updateGps();
        }
    }

    public String getLocation()
    {
        GPSTracker gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.e(Double.toString(latitude), Double.toString(longitude));
            return Double.toString(latitude) + "," + Double.toString(longitude);
        }
        else
            gps.showSettingsAlert();
            return null;
    }

    public void updateDisponibilities()
    {
        myLocation = getLocation();
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Chargement...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... stuff) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", MainActivity.user_id));
                params.add(new BasicNameValuePair("state", MainActivity.user_state));
                params.add(new BasicNameValuePair("location", myLocation));

                HttpConnection httpConnection = new HttpConnection();
                Gson gson = new Gson();
                returnMessage = gson.
                        fromJson(httpConnection.makeRequest(MainActivity.dispo, HttpConnection.HttpMethod.POST, params )
                                , ReturnMessage.class);

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), returnMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();
                if (returnMessage.isStatus()) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), returnMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        task.execute();
    }

    public void updateGps(){
        myLocation = getLocation();
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Chargement...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... stuff) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", MainActivity.user_id));
                params.add(new BasicNameValuePair("state", MainActivity.user_state));
                params.add(new BasicNameValuePair("location", myLocation));

                HttpConnection httpConnection = new HttpConnection();
                Gson gson = new Gson();
                returnMessage = gson.fromJson(httpConnection.makeRequest(MainActivity.dispo, HttpConnection.HttpMethod.POST, params)
                        , ReturnMessage.class);

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), returnMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();
                if (returnMessage.isStatus()) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), returnMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        task.execute();
    }
}
