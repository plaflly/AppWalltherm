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

import wallpl.example.vvvlad.walltherm.Adapters.AdapterDlaObraliNagrodeParth2;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class WybraliNagrParth2 extends AppCompatActivity {
    private RecyclerView recyclerWydaniaNagrod;

    private DatabaseReference myRef;
    private ArrayList<Nagroda> list;

    private AdapterDlaObraliNagrodeParth2 adapter;
    private boolean connect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybrali_nagr_parth2);
        recyclerWydaniaNagrod = findViewById(R.id.recyclerWydaniaNagrod);
        recyclerWydaniaNagrod.setLayoutManager(new LinearLayoutManager(this));
        recyclerWydaniaNagrod.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        myRef = FirebaseDatabase.getInstance().getReference().child("ObraliNagrodeZatwierdz");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String idUser = dataSnapshot1.getKey();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        String nagrodaKay = dataSnapshot2.getKey();


                        String kropis = String.valueOf(dataSnapshot2.child("KrotkiOpis").getValue());
                        String nazwa = String.valueOf(dataSnapshot2.child("Name").getValue());
                        String pkt = String.valueOf(dataSnapshot2.child("PKT").getValue());
                        String usermail = String.valueOf(dataSnapshot2.child("email").getValue());

                        Nagroda n = dataSnapshot2.getValue(Nagroda.class);

                        n.setKrutkiOpis(kropis);
                        n.setPunkt(pkt);
                        n.setNazwa(nazwa);
                        n.setKay(nagrodaKay);
                        n.setIdUser(idUser);
                        n.setUserMailNagroda(usermail);
                        list.add(n);




                    }
                }
                adapter = new AdapterDlaObraliNagrodeParth2(WybraliNagrParth2.this, list);
                recyclerWydaniaNagrod.setAdapter(adapter);
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
            Snackbar.make(findViewById(R.id.wybraliNagrParth2), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.wybraliNagrParth2), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.wybraliNagrParth2), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
