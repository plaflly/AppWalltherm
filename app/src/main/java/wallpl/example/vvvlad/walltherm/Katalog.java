package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class Katalog extends AppCompatActivity {
    private PDFView view;
    private TextView textView;
    private Button pdfDelite;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference myRef;
    private boolean connect = false;
    // private FirebaseStorage mFirebaseStorage;
    private ProgressDialog progressDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katalog);
        mFirebaseStorage = FirebaseStorage.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        view = findViewById(R.id.pdfViewkatalAdm);
        textView = findViewById(R.id.textKatalog);
        pdfDelite = findViewById(R.id.pdfDelite);
        progressDialog = new ProgressDialog(Katalog.this);
        progressDialog.setMessage("Proszę czekać!");
        progressDialog.show();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final String url = intent.getStringExtra("url");
        final String adres = intent.getStringExtra("adres");

        if (intent.getStringExtra("user").equals("user")) {
            pdfDelite.setVisibility(View.GONE);
        }
        textView.setText(name);
        pdfDelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFile(url, adres);
            }
        });
        new RetrievePDFStream().execute(url);


        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                sRec(networkStatus);
            }
        }, intentFilter);

        if (isOnline()) {
            connect = true;

        } else {
            Snackbar.make(findViewById(R.id.aktivityKatalog), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }


    private class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {

                URL urlx = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) urlx.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            view.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    progressDialog.dismiss();
                }
            }).load();


        }
    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.aktivityKatalog), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.aktivityKatalog), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
                connect = false;
            }

        }
    }


    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }

    private void DeleteFile(String url, final String adres) {
        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(url);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRef.child("Katalogi").child(adres).removeValue();
                Toast.makeText(Katalog.this, "Usunięcie zostało zakończone!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Katalog.this, "Wystąpił błąd podczas usuwania!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
