package wallpl.example.vvvlad.walltherm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class Komunikaty extends AppCompatActivity {
    private  ArrayList<String> qwer =new ArrayList<String>();
    private boolean connect = false;
    private  Spinner spinerKomunikat;
    private  EditText tytulKomunikat;
    private  EditText textKomunikat;
    private   Button btnKomunikat;
    private  String spinerSelect;
    private  DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komunikaty);
        qwer.add("Instalator");
        qwer.add("Hurtownia");
        qwer.add("Architekt");

        spinerKomunikat =findViewById(R.id.spinerKomunikat);
        myRef = FirebaseDatabase.getInstance().getReference().child("Massage");
        final ArrayAdapter<String> adapterSpiner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qwer);
        adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerKomunikat.setAdapter(adapterSpiner);
        spinerKomunikat.setSelection(0);
        spinerKomunikat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinerSelect = qwer.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tytulKomunikat = findViewById(R.id.tytulKomunikat);
        textKomunikat = findViewById(R.id.textKomunikat);
        btnKomunikat = findViewById(R.id.btnKomunikat);
        btnKomunikat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tytulKomunikat.getText())){
                    tytulKomunikat.setError("Wpisz tytuł");
                } else {
                    if (TextUtils.isEmpty(textKomunikat.getText())){
                        textKomunikat.setError("Wpisz text");
                    }else {
                        String gfd = String.valueOf(System.currentTimeMillis());
                        myRef.child(gfd).child("status").setValue(spinerSelect);
                        myRef.child(gfd).child("tytul").setValue(String.valueOf(tytulKomunikat.getText()));
                        myRef.child(gfd).child("text").setValue(String.valueOf(textKomunikat.getText()));
                        Toast.makeText(Komunikaty.this, "Wysyłane do użytkowników!", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(Komunikaty.this, KabinAdmin.class);
                        startActivity(intent);

                    }
                }
            }
        });
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
            Snackbar.make(findViewById(R.id.Komunikaty), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.Komunikaty), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.Komunikaty), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
            Intent intent = new Intent(this, Users.class);
            startActivity(intent);
        }
        return true;
    }
}
