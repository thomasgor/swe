package com.swe.gruppe4.mockup2;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;


// A project by Ferdousur Rahman Shajib
// www.androstock.com

public class LoginActivity extends AppCompatActivity implements OnClickListener,
        GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener {

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
    * us from starting further intents.
    */
    private boolean mIntentInProgress;

    private boolean mShouldResolve;

    private ConnectionResult connectionResult;

    private SignInButton signInButton;
    private Button signOutButton;
    private TextView tvName, tvMail, tvNotSignedIn;
    private ImageView imgProfilePic;
    private LinearLayout viewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        tvName = (TextView) findViewById(R.id.tvName);
        tvMail = (TextView) findViewById(R.id.tvMail);
        tvNotSignedIn = (TextView) findViewById(R.id.notSignedIn_tv);
        viewContainer = (LinearLayout) findViewById(R.id.text_view_container);


        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        notification();

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private void resolveSignInError() {
        if (connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /*
    When the GoogleApiClient object is unable to establish a connection onConnectionFailed() is called
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {

            connectionResult = result;

            if (mShouldResolve) {

                resolveSignInError();
            }
        }

    }

    /*
    onConnectionFailed() was started with startIntentSenderForResult and the code RC_SIGN_IN,
    we can capture the result inside Activity.onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mShouldResolve = false;
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person person = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = person.getDisplayName();
                String personPhotoUrl = person.getImage().getUrl();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;


                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("profileName",personName);
                intent.putExtra("profileEmail",email);
                intent.putExtra("profilePicture",personPhotoUrl);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Couldnt Get the Person Info", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void signOutUI() {
        signInButton.setVisibility(View.GONE);
        tvNotSignedIn.setVisibility(View.GONE);
        signOutButton.setVisibility(View.VISIBLE);
        viewContainer.setVisibility(View.VISIBLE);
    }

    private void signInUI() {
        signInButton.setVisibility(View.VISIBLE);
        tvNotSignedIn.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.GONE);
        viewContainer.setVisibility(View.GONE);
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
       signInUI();
    }

    public void notification(){
        Resources res = getResources();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_notify_appicon)
                        .setContentTitle(res.getString(R.string.app_name))
                        .setContentText("Ihre Sitzung läuft in 5 Minuten aus");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("profileName","Max Mustermann");
        resultIntent.putExtra("profileEmail","max@mustermann.de");
        resultIntent.putExtra("profilePicture","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg");

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(3, mBuilder.build());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.sign_out_button:
                onSignOutClicked();
                break;
        }
    }


    private void onSignInClicked() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            login();
        }
    }

    private void login(){

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("profileName","Max Mustermann");
        intent.putExtra("profileEmail","max@mustermann.de");
        intent.putExtra("profilePicture","https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg");
        startActivity(intent);
        finish();

        /*ProgressDialog pd1 = new ProgressDialog(this);
        pd1.setTitle("Laden...");
        pd1.setMessage("Bitte warten...");
        pd1.show();*/

        /*Intent intent = new Intent(getApplicationContext(), RoomDetailsActivity.class);
        startActivity(intent);*/

        /*if (!mGoogleApiClient.isConnecting()) {
            mShouldResolve = true;
            resolveSignInError();
        }*/
    }


    private void onSignOutClicked() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            signInUI();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    login();
                } else {

                    // permission denied, boo!

                    AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                    build.setCancelable(false);

                    build.setTitle("Fehlende Berechtigung");
                    build.setMessage("Bitte erlauben Sie den Zugriff auf die Kontakte, um sich einloggen zu können.");

                    build.setPositiveButton("Verstanden", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    //build.setNegativeButton("Nein", null);
                    AlertDialog alert1 = build.create();
                    alert1.show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Background Async task to load user profile picture from url
     */
}