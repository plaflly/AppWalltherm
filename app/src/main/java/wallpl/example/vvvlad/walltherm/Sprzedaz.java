package wallpl.example.vvvlad.walltherm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import wallpl.example.vvvlad.walltherm.Adapters.AdapterDlaSprzedaz;
import wallpl.example.vvvlad.walltherm.Moduly.Modul;
import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class Sprzedaz extends AppCompatActivity {
    private RecyclerView listaRecyclirSprzedaz;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private ArrayList<Modul> list;
    private AdapterDlaSprzedaz adapter;
    private boolean connect = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprzedaz);

        listaRecyclirSprzedaz = findViewById(R.id.listaRecyclerSprzedaz);
        listaRecyclirSprzedaz.setLayoutManager(new LinearLayoutManager(this));
        listaRecyclirSprzedaz.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));



        myRef = FirebaseDatabase.getInstance().getReference().child("Punkty");
        myRef2 = FirebaseDatabase.getInstance().getReference().child("Moduly");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String idUser = dataSnapshot1.getKey();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        String modulORpunkt = dataSnapshot2.getKey();
                        if (modulORpunkt.equals("Moduly")) {
                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                if (dataSnapshot3.child("Status").getValue(String.class) == null) {
                                    String ilosc = String.valueOf(dataSnapshot3.child("Ilosc").getValue());
                                    String time = String.valueOf(dataSnapshot3.child("Time").getValue());
                                    String email = String.valueOf(dataSnapshot3.child("email").getValue());
                                    String nazwa = String.valueOf(dataSnapshot3.child("nazwa").getValue());
                                    String punkty = String.valueOf(dataSnapshot3.child("punkty").getValue());

                                    Modul m = dataSnapshot3.getValue(Modul.class);
                                    m.setIloscPunkt(punkty);
                                    m.setIdModul(dataSnapshot3.getKey());
                                    m.setIloscModul(ilosc);
                                    m.setTimeModul(time);
                                    m.setIdUrzytkownika(idUser);
                                    m.setMailUser(email);
                                    m.setNazwaModul(nazwa);
                                    list.add(m);
                                }
                            }
                        }

                    }
                }
                adapter = new AdapterDlaSprzedaz(Sprzedaz.this, list);
                listaRecyclirSprzedaz.setAdapter(adapter);
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
            Snackbar.make(findViewById(R.id.sprzedaz), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.sprzedaz), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.sprzedaz), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
            Intent intent = new Intent(this, Punkty.class);
            startActivity(intent);
        }
        return true;
    }
}
