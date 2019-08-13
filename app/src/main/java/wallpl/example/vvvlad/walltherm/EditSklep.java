package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class EditSklep extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseReference myRef;
    private Spinner spiner ;
    //  private Spinner spinner;
    private EditText miasto;
    private EditText ulica;
    private EditText firma;
    private EditText telefon;
    private EditText telefon1;
    private EditText telefon2;
    private String wojew;
    private Button zapisz;
    private Button delite;
    private String adres;
    private String ID;
    private String status;
    private String wojeW;
    private int i = 0;
    private boolean connect = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myRef = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_edit_sklep);
        miasto = findViewById(R.id.miasto_edit);
        ulica = findViewById(R.id.ulica_edit);
        firma = findViewById(R.id.firma_edit);
        telefon = findViewById(R.id.telefon_edit);
        telefon1 = findViewById(R.id.telefon_edit1);
        telefon2 = findViewById(R.id.telefon_edit2);
        zapisz = findViewById(R.id.zapisz_edit);
        delite = findViewById(R.id.delite_edit);
        spiner = findViewById(R.id.wojewodstwoAddSklep);
        Intent intent = getIntent();

        miasto.setText(intent.getStringExtra("miasto"));
        ulica.setText(intent.getStringExtra("ulica"));
        firma.setText(intent.getStringExtra("firma"));
        telefon.setText(intent.getStringExtra("telefon"));
        telefon1.setText(intent.getStringExtra("telefon1"));
        telefon2.setText(intent.getStringExtra("telefon2"));
        status = intent.getStringExtra("status");
        wojew = intent.getStringExtra("wojew");
        adres = intent.getStringExtra("adres");
        ID = intent.getStringExtra("ID");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Województwo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);
        switch (wojew){
            case "Dolnośląskie":
                spiner.setSelection(1);
                break;
            case "Kujawsko-pomorskie":
                spiner.setSelection(2);
                break;
            case "Lubelskie":
                spiner.setSelection(3);
                break;
            case "Lubuskie":
                spiner.setSelection(4);
                break;
            case "Łódzkie":
                spiner.setSelection(5);
                break;
            case "Małopolskie":
                spiner.setSelection(6);
                break;
            case "Mazowieckie":
                spiner.setSelection(7);
                break;
            case "Opolskie":
                spiner.setSelection(8);
                break;
            case "Podkarpackie":
                spiner.setSelection(9);
                break;
            case "Podlaskie":
                spiner.setSelection(10);
                break;
            case "Pomorskie":
                spiner.setSelection(11);
                break;
            case "Śląskie":
                spiner.setSelection(12);
                break;
            case "Świętokrzyskie":
                spiner.setSelection(13);
                break;
            case "Warmińsko-mazurskie":
                spiner.setSelection(14);
                break;
            case "Wielkopolskie":
                spiner.setSelection(15);
                break;
            case "Zachodniopomorskie":
                spiner.setSelection(16);
                break;
        }
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.Województwo);

                wojeW = choose[selectedItemPosition];

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        delite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delite();
            }
        });
        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zapisz();
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
            Snackbar.make(findViewById(R.id.editSklep), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.editSklep), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.editSklep), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    private void delite() {
        myRef.child(adres).child(wojew).child(ID).removeValue();
        Toast.makeText(EditSklep.this, "Usunięcie zostało zakończone!", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, SklepForAdmin.class);
        intent.putExtra("format",adres);
        startActivity(intent);
    }

    private void zapisz() {


        myRef.child(adres).child(wojew).child(ID).removeValue();
        myRef.child(adres).child(wojeW).child(ID).child("Firma").setValue(firma.getText().toString());
        myRef.child(adres).child(wojeW).child(ID).child("Miasto").setValue(miasto.getText().toString());
        //  myRef.child(status).child(wojew).child(id).child("Wojewodstwo").setValue(wojew.getText().toString());
        myRef.child(adres).child(wojeW).child(ID).child("Ulica").setValue(ulica.getText().toString());
        myRef.child(adres).child(wojeW).child(ID).child("Telefon").setValue(telefon.getText().toString());
        myRef.child(adres).child(wojeW).child(ID).child("Telefon1").setValue(telefon1.getText().toString());
        myRef.child(adres).child(wojeW).child(ID).child("Telefon2").setValue(telefon2.getText().toString());
        Toast.makeText(EditSklep.this, "Zapisano!", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, SklepForAdmin.class);
        intent.putExtra("format",adres);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        wojew = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, SklepForAdmin.class);
            intent.putExtra("format",adres);
            startActivity(intent);
        }
        return true;
    }
}
