package fr.vtarreau.borntocode.crowdrescue;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.necmergitur.borntocode.crowdrescue.crowdrescue.R;
import fr.vtarreau.borntocode.crowdrescue.fragment.MainActivityFragment;
import fr.vtarreau.borntocode.crowdrescue.msgs.ReturnMessage;

public class MainActivity extends FragmentActivity {

    public final static String sharedpreferencesString = "CrowdRescuePreferences";
    public final static String sp_isLogged = "isLogged";
    public final static String sp_Dispo= "isAvailable";
    public final static String sp_Id = "myId";
    public final static String sp_Username = "myUsername";


    public static String user_id = "";
    public static String user_name = "";
    public static String user_state = "";
    public static String user_ = "";


    public final static String login = "users/login";
    public final static String dispo = "available/update";
    public final static String position = "available/update";
    public final static String response = "available/response";

    private String registerId;

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    boolean tmp;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);

        Fragment fragment = new MainActivityFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();

        sharedPreferences = this.getSharedPreferences(sharedpreferencesString, MODE_PRIVATE);

        checkConnection();
        Intent intenta = getIntent();
        String msg = intenta.getStringExtra("PUSH");
        if (msg != null)
        {
            askIfAvailable(msg);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickStartPreferences.SENT_TOKEN_TO_SERVER, false);
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    private void askIfAvailable(String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerte")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp = true;
                        avaibilityAsync();
                    }
                })
                .setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp = false;
                        avaibilityAsync();
                    }
                });
        builder.show();
    }

    private void askIfEquipement() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Equipement")
                .setMessage("Possedez-vous votre equipement ?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp = true;
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp = false;
                    }
                });
        builder.show();
    }

    private void avaibilityAsync()
    {
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {

            ReturnMessage returnMessage;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Chargement...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(true);
                progressDialog.show();
                registerInBackground();
            }

            @Override
            protected String doInBackground(String... stuff) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", user_id));
                params.add(new BasicNameValuePair("status", Boolean.toString(tmp)));
                if (tmp)
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            askIfEquipement();
                        }
                    });
                }
                params.add(new BasicNameValuePair("has_stuff", Boolean.toString(tmp)));

                HttpConnection httpConnection = new HttpConnection();
                Gson gson = new Gson();
                returnMessage = gson.fromJson(httpConnection.makeRequest(response, HttpConnection.HttpMethod.POST, params), ReturnMessage.class);
                System.out.println(returnMessage);

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();
                if (returnMessage.isStatus())
                    Toast.makeText(MainActivity.this, returnMessage.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickStartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void checkConnection() {
        if (sharedPreferences == null || sharedPreferences.getBoolean(sp_isLogged, false) == false) {
            Toast.makeText(this, "Erreur lors de la recuperation des donnees de l'application", Toast.LENGTH_LONG).show();
                login();
        }
    }

    public void login() {
        View loginView = View.inflate(this, R.layout.alert_login, null);
        final EditText id = (EditText) loginView.findViewById(R.id.alert_login_user_id);
        final EditText password = (EditText) loginView.findViewById(R.id.alert_login_user_password);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Identifiez-vous");
        builder.setCancelable(false);
        builder.setView(loginView);

        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {

                    ReturnMessage returnMessage;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        registerInBackground();
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Chargement...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... stuff) {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username", stuff[0]));
                        params.add(new BasicNameValuePair("password", stuff[1]));
                        params.add(new BasicNameValuePair("registerId", registerId));

                        HttpConnection httpConnection = new HttpConnection();
                        Gson gson = new Gson();
                        returnMessage = gson.fromJson(httpConnection.makeRequest(login, HttpConnection.HttpMethod.POST, params), ReturnMessage.class);

                        user_id = returnMessage.getMessage();
                        user_name = stuff[0];
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        progressDialog.dismiss();
                        if (returnMessage.isStatus()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(sp_isLogged, true);
                            editor.putString(sp_Id, returnMessage.getMessage());
                            editor.commit();
                        } else
                            login();
                    }
                };
                task.execute(id.getText().toString(), password.getText().toString());
            }
        });
        builder.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        builder.show();
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
        switch (item.getItemId())
        {

        //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                break;
            case R.id.quitter:
                this.finish();
                break;
        }

        return true;
    }

    private void registerInBackground()
    {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    registerId = gcm.register("935391980782");
                    Log.e("id gcm", registerId);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        }.execute(null, null, null);
    }
}
