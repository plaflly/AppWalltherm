package wallpl.example.vvvlad.walltherm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class PunktyList extends AppCompatActivity {
    private Button addPunkt;
    private ListView pPunktRecycler;
    private DatabaseReference myRef;

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> listNazwa;
    private ArrayList<String> listpunkt;
    private boolean connect = false;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkty_list);

        myRef = FirebaseDatabase.getInstance().getReference();
        addPunkt = findViewById(R.id.dodacPunkty);
        pPunktRecycler = findViewById(R.id.listviewPunktList);
        addPunkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);

        pPunktRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogEdit(listNazwa.get(position), listpunkt.get(position));
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNazwa = new ArrayList<>();
                listpunkt = new ArrayList<>();
                adapter.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (String.valueOf(dataSnapshot1.getKey()).equals("Moduly")) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String nazwa = String.valueOf(dataSnapshot2.getKey());
                            listNazwa.add(nazwa);
                            String punkt = String.valueOf(dataSnapshot2.child("Punkt").getValue());
                            listpunkt.add(punkt);
                            list.add(nazwa + "  =>  " + punkt + " pkt");
                            adapter.notifyDataSetChanged();

                        }
                    }
                }
                pPunktRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            Snackbar.make(findViewById(R.id.punktyList), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.punktyList), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.punktyList), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    private void dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_punktuw, null);
        dialogBuilder.setView(dialogView);
        final EditText nazwa = dialogView.findViewById(R.id.nazwamodula);
        final EditText punktzamodul = dialogView.findViewById(R.id.punktzamodul);
        dialogBuilder.setTitle("Dodawanie modula");
        if (TextUtils.isEmpty(nazwa.getText())) {
            nazwa.setError("Wpisz nazwe modula!");
        }
        if (TextUtils.isEmpty(punktzamodul.getText())) {
            punktzamodul.setError("Wpisz ile pkt za jeden modul!");
        }

        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(nazwa.getText())) {
                    Toast.makeText(PunktyList.this, "Wpisz nazwe modula!", Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(punktzamodul.getText())) {
                        Toast.makeText(PunktyList.this, "Wpisz ile pkt za jeden modul!", Toast.LENGTH_LONG).show();
                    } else {
                        addPunkt(nazwa.getText().toString(), punktzamodul.getText().toString());
                    }
                }

            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void dialogEdit(final String s, String s1) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_punktuw, null);
        dialogBuilder.setView(dialogView);
        final EditText nazwa = dialogView.findViewById(R.id.nazwamodula);
        nazwa.setText(s);
        final EditText punktzamodul = dialogView.findViewById(R.id.punktzamodul);
        punktzamodul.setText(s1);
        dialogBuilder.setTitle("Redaguwanie modula");
        if (TextUtils.isEmpty(nazwa.getText())) {
            nazwa.setError("Wpisz nazwe modula!");
        }
        if (TextUtils.isEmpty(punktzamodul.getText())) {
            punktzamodul.setError("Wpisz ile pkt za jeden modul!");
        }

        dialogBuilder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(nazwa.getText())) {
                    Toast.makeText(PunktyList.this, "Wpisz nazwe modula!", Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(punktzamodul.getText())) {
                        Toast.makeText(PunktyList.this, "Wpisz ile pkt za jeden modul!", Toast.LENGTH_LONG).show();
                    } else {
                        myRef.child("Moduly").child(s).removeValue();
                        addPunkt(nazwa.getText().toString(), punktzamodul.getText().toString());
                    }
                }

            }
        });
        dialogBuilder.setNegativeButton("Usun", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRef.child("Moduly").child(s).removeValue();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    void addPunkt(String nazwa, String punkt) {
        myRef.child("Moduly").child(nazwa).child("Punkt").setValue(punkt);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);

        Toast.makeText(PunktyList.this, "Zapisano!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, Punkty.class);
            startActivity(intent);
        }
        return true;
    }


}
