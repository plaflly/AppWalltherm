package wallpl.example.vvvlad.walltherm.Servise;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.MainActivity;
import wallpl.example.vvvlad.walltherm.R;

import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KontaktService extends AppCompatActivity {
    TextView masage;
    private DatabaseReference myRef;
    private boolean connect = false;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontakt_service);
        masage = findViewById(R.id.kontakt);
        myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rool = myRef.child("Kontakt");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Proszę czekać");
        dialog.show();
        rool.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Object lip = dataSnapshot.getValue();
                masage.setText(lip.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                sRec(networkStatus);
            }
        }, intentFilter);

        if (isOnline()) {
            connect = true;

        } else {
            Snackbar.make(findViewById(R.id.kontaktService), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.kontaktService), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.kontaktService), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
