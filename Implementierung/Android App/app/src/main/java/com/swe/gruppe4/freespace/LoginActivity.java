package com.swe.gruppe4.freespace;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.swe.gruppe4.freespace.Objektklassen.Sitzung;

/**
 * Dient zum Login der Anwender. Nutzt die Google API um das lokal vorhanden Googlekonto
 * zur Authentifizierung zu verwenden. Zur Identifizierung wird die Google ID des Nutzern abgegriffen
 * Zusätzlich wird der Nutzername, die Emailadresse und die URL des Profilbildes abgerufen
 *
 * @author Marco Linnartz
 * @version 1.1
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showDemoDialog();

        //Kommentar entfernen wenn nicht richtiger debug-keystore vorhanden
        //startHomeActivityTest();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("profileName",acct.getDisplayName());
            intent.putExtra("profileEmail",acct.getEmail());
            Log.d(TAG, "UserID: " + acct.getId());
            //Log.d(TAG, "UserIDToken: " + acct.getIdToken());
            String pictureURL;
            if(acct.getPhotoUrl() != null) {
                pictureURL = acct.getPhotoUrl().toString();
                Log.d(TAG, "PictureURL: " + pictureURL);

            } else {
                pictureURL = "https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg";
            }

            intent.putExtra("profilePicture",pictureURL);
            startActivity(intent);

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void startHomeActivityTest() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("profileName","Max Mustermann");
        intent.putExtra("profileEmail","max.muster@gmail.com");

        String pictureURL;

        pictureURL = "https://lernperspektiventest.files.wordpress.com/2014/06/2502728-bewerbungsfotos-in-berlin1.jpg";

        intent.putExtra("profilePicture",pictureURL);
        startActivity(intent);
        finish();
    }

    private void showDemoDialog(){
        new AlertDialog.Builder(LoginActivity.this)
                .setCancelable(false)
                .setTitle("Alpha")
                .setMessage("Dies ist nur eine frühe Alphaversion.\nNoch nicht alles funktioniert einwandfrei und noch nicht alle Daten sind korrekt.")
                .setPositiveButton("Verstanden",null)
                .create()
                .show();
    }


}
