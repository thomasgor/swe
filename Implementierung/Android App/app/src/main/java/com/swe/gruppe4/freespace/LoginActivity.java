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
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.iid.FirebaseInstanceId;
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

    /**
     * Sign-In Client wird konfiguriert und aufgebaut
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //showDemoDialog();

        //Kommentar entfernen wenn nicht richtiger debug-keystore vorhanden
        //startHomeActivityTest();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Konfiguriert den Sign-In und bestimmt welche Informationen abgerufen werden sollen
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Baut den Google Sign-In Client anhand der im gso konfigurierten Informationen
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    /**
     * Wird für den Silent Sign-In benötigt. Wenn man vor kurzen angemeldet wurde startet die App sofort,
     * ohne eine Interaktionen mit dem Anwender. Zusätzlich wird ein Ladedialog eingefügt wenn der Google
     * Account geprüft werden muss.
     */
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Wenn auf den SignIn Button gedrückt wird über die interne signIn Methode
     * der Authentifzierungsvorgang gestart
     * @param v Element auf das gedrückt wurde
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
        }
    }

    /**
     * Starte Sign-In Vorgang über Google API
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Call Back, wenn das Resultat vorliegt. Delegiert weiteres Vorgehen an interne
     * Methode handleSignInResult
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Bei erfolgreicher Authentifizierung werden der Profilname, die Email Adresse und
     * das Benutzerprofilbild geladen und an die MainActivity über ein Intent übergeben.
     * @param result Ergebnis des SignIn Prozesses
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);


            String pictureURL;
            if(acct.getPhotoUrl() != null) {
                pictureURL = acct.getPhotoUrl().toString();
                //Log.d(TAG, "PictureURL: " + pictureURL);

            } else {
                pictureURL = "https://support.plymouth.edu/kb_images/Yammer/default.jpeg";
            }

            String familyName, givenName;
            if(acct.getFamilyName() == null || acct.getFamilyName().equals("null")) {
                familyName = "";
            }
            else {
                familyName = acct.getFamilyName();
            }

            if(acct.getGivenName()== null || acct.getGivenName().equals("null")) {
                givenName = "NA";
            }
            else {
                givenName = acct.getGivenName();
            }

            String displayName;
            if(acct.getDisplayName() == null || acct.getDisplayName() == "null") {
                displayName = "NA";
            }
            else {
                displayName = acct.getDisplayName();
            }


            RestConnection connection = new RestConnection(this);
            connection.benutzerPost(acct.getId(),acct.getEmail(), familyName, givenName, pictureURL);
            connection.benutzerPut(FirebaseInstanceId.getInstance().getToken());

            intent.putExtra("profilePicture",pictureURL);
            intent.putExtra("profileName",displayName);
            intent.putExtra("profileEmail",acct.getEmail());

            startActivity(intent);
            finish();

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    /**
     * Zeigt einen ProgressDialog wenn nicht schon vorhanden
     */
    private void showProgressDialog() {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    /**
     * Entfernt Progressdialog, wenn vorhanden
     */
    private void hideProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            //mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

    /**
     * Umgehen des Login für Testzwecke
     */
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

    /**
     * Warnung für Präsentation der Alphaversion
     */
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
