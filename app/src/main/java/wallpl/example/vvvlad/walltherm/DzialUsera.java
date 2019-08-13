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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import wallpl.example.vvvlad.walltherm.Adapters.AdapterDlaDzialUsera;
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

public class DzialUsera extends AppCompatActivity {
    private RecyclerView dzialuserrecycler;
    private DatabaseReference myRef;
    private ArrayList<Modul> list;
    private AdapterDlaDzialUsera adapter;
    private LinearLayout DzialUsera;
    private boolean connect = false;
    private Button delite;
    private Button zapisz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dzial_usera);
        myRef = FirebaseDatabase.getInstance().getReference().child("Obliczenia");
        dzialuserrecycler = findViewById(R.id.dzialuserrecycler);
        dzialuserrecycler.setLayoutManager(new LinearLayoutManager(this));
        dzialuserrecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
   //     DzialUsera = findViewById(R.id.dzialUsera);
        delite = findViewById(R.id.deliteDzial);
        delite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.removeValue();
            }
        });
        zapisz = findViewById(R.id.doDzial);
        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donext();
            }
        });



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String id = String.valueOf(dataSnapshot1.getKey());
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        String id2 = String.valueOf(dataSnapshot2.getKey());
                        String time = String.valueOf(dataSnapshot2.child("time").getValue());
                        String email = String.valueOf(dataSnapshot2.child("email").getValue());
                        String nazwa = String.valueOf(dataSnapshot2.child("val").getValue());

                        Modul m = dataSnapshot2.getValue(Modul.class);
                        m.setIdUrzytkownika(id);
                        m.setIdModul(id2);
                        m.setTimeModul(time);
                        m.setMailUser(email);
                        m.setNazwaModul(nazwa);
                        list.add(m);
                    }
                }
                adapter = new AdapterDlaDzialUsera(DzialUsera.this, list);
                dzialuserrecycler.setAdapter(adapter);
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
            Snackbar.make(findViewById(R.id.dzialUsera), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void donext() {
        Intent intent = new Intent(this, DzialUseraZapisane.class);
        startActivity(intent);
    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.dzialUsera), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.dzialUsera), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
            Intent intent = new Intent(this, DzialUser1.class);
            startActivity(intent);
        }
        return true;
    }
}
