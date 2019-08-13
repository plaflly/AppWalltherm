package wallpl.example.vvvlad.walltherm;

import android.app.ProgressDialog;
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

import wallpl.example.vvvlad.walltherm.Adapters.AdapterDlaKatalogNagrodAdmin;
import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class KatalogNagrodAdmin extends AppCompatActivity {
    private Button addNagrode;
    private RecyclerView recyclerNagrod;
    private ArrayList<Nagroda> list;
    private AdapterDlaKatalogNagrodAdmin adapter;
    private DatabaseReference myRef;
    private boolean connect = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katalog_nagrod_admin);
        myRef = FirebaseDatabase.getInstance().getReference().child("Nagrody");
        addNagrode = findViewById(R.id.addNagrode);
        addNagrode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNagroda();
            }
        });
        recyclerNagrod = findViewById(R.id.recyclerNagrod);
        recyclerNagrod.setLayoutManager(new LinearLayoutManager(this));
        recyclerNagrod.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("Proszę czekać");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String kay = String.valueOf(dataSnapshot1.getKey());
                    String krotkiOpis = String.valueOf(dataSnapshot1.child("KrotkiOpis").getValue());
                    String nazwa = String.valueOf(dataSnapshot1.child("Name").getValue());
                    String opis = String.valueOf(dataSnapshot1.child("Opis").getValue());
                    String pkt = String.valueOf(dataSnapshot1.child("PKT").getValue());
                    String url = String.valueOf(dataSnapshot1.child("url").getValue());

                    Nagroda m = dataSnapshot1.getValue(Nagroda.class);
                    m.setKay(kay);
                    m.setKrutkiOpis(krotkiOpis);
                    m.setNazwa(nazwa);
                    m.setOpis(opis);
                    m.setPunkt(pkt);
                    m.setImg(url);
                    list.add(m);
                }
                adapter = new AdapterDlaKatalogNagrodAdmin(KatalogNagrodAdmin.this, list);
                recyclerNagrod.setAdapter(adapter);
                dialog.dismiss();

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
            Snackbar.make(findViewById(R.id.katalogNagrodAdmin), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.katalogNagrodAdmin), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.katalogNagrodAdmin), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    void AddNagroda() {
        Intent intent = new Intent(this, EditNagroda.class);
        finish();
        //  intent2.putExtra();
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, Punkty.class);
            startActivity(intent);
        }
        return true;
    }
}

