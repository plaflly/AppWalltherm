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
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class AddSklep extends AppCompatActivity {
    private DatabaseReference myRef;
    private TextView title;
    private EditText miasto;
    private EditText ulica;
    private EditText firma;
    private EditText telefon;
    private EditText telefon1;
    private EditText telefon2;
    private Button addSklep;
    private String hurtownia;
    private String id;
    Spinner spinner;
    private String wojew;
    private boolean connect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sklep);
        myRef = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        hurtownia = intent.getStringExtra("hurtownia");

        title = findViewById(R.id.titleAddSklep);
        if (hurtownia.equals("GdzieKup")) {
            title.setText("Dodajemy sklep");
        } else {
            if (hurtownia.equals("KtoZam")) {
                title.setText("Dodajemy hurtownie");
            }
        }
        spinner = findViewById(R.id.wojewodstwoAddSklep);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Województwo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


        //      wojew = findViewById(R.id.wojewodstwoAddSklep);
        miasto = findViewById(R.id.miastoAddSklep);
        ulica = findViewById(R.id.ulicaAddSklep);
        firma = findViewById(R.id.firmaAddSklep);
        telefon = findViewById(R.id.telefonAddSklep);
        telefon1 = findViewById(R.id.telefonAddSklep1);
        telefon2 = findViewById(R.id.telefonAddSklep2);

        addSklep = findViewById(R.id.btn_add_sklep);
        addSklep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(miasto.getText())) {
                    if (!TextUtils.isEmpty(ulica.getText())) {
                        if (!TextUtils.isEmpty(firma.getText())) {
                            if (!TextUtils.isEmpty(telefon.getText())) {
                                try {
                                    zapisz();
                                } catch (Exception ex) {
                                    Toast.makeText(AddSklep.this, "Nie zapisano! Error: " + ex, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                miasto.setError("Wprowadź telefon");
                            }
                        } else {
                            miasto.setError("Wprowadź firmę");
                        }
                    } else {
                        miasto.setError("Wprowadź ulicę");
                    }
                } else {
                    miasto.setError("Wprowadź miasto");
                }

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.Województwo);

                wojew = choose[selectedItemPosition];

            }

            public void onNothingSelected(AdapterView<?> parent) {
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
            Snackbar.make(findViewById(R.id.addSklep), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.addSklep), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.addSklep), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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


    private void zapisz() {
        String id = String.valueOf(System.currentTimeMillis());
        myRef.child(hurtownia).child(wojew).child(id).child("Firma").setValue(firma.getText().toString());
        myRef.child(hurtownia).child(wojew).child(id).child("Miasto").setValue(miasto.getText().toString());
        //  myRef.child(status).child(wojew).child(id).child("Wojewodstwo").setValue(wojew.getText().toString());
        myRef.child(hurtownia).child(wojew).child(id).child("Ulica").setValue(ulica.getText().toString());
        myRef.child(hurtownia).child(wojew).child(id).child("Telefon").setValue(telefon.getText().toString());
        myRef.child(hurtownia).child(wojew).child(id).child("Telefon1").setValue(telefon1.getText().toString());
        myRef.child(hurtownia).child(wojew).child(id).child("Telefon2").setValue(telefon2.getText().toString());
        Toast.makeText(AddSklep.this, "Zapisano!", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, SklepForAdmin.class);
        intent.putExtra("format",hurtownia);
        startActivity(intent);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, SklepForAdmin.class);
            intent.putExtra("format",hurtownia);
            startActivity(intent);
        }
        return true;
    }

}



