package wallpl.example.vvvlad.walltherm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.ExpandableListView;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static wallpl.example.vvvlad.walltherm.MainActivity.typeface;
import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class Sklep extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapterUser listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, ArrayList<Sklepy>> listHash;
    private DatabaseReference ref;
    private ArrayList<Sklepy> list;
    private int in = -1;
    private TextView texter;
    private String format;
    private boolean connect = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sklep);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("Proszę czekać");

        listView = findViewById(R.id.lvExpl);
        Intent intent = getIntent();
        format = intent.getStringExtra("format");

        texter = findViewById(R.id.texter);
        texter.setTypeface(typeface);
        ref = FirebaseDatabase.getInstance().getReference().child(format);
        if (format.equals("GdzieKup")) {
            texter.setText("Sklepy:");
        }
        if (format.equals("KtoZam")) {
            texter.setText("Instalatorzy:");
        }

        initData();
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
            Snackbar.make(findViewById(R.id.sklep), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.sklep), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.sklep), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    private void initData2() {
        listAdapter = new ExpandableListAdapterUser(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void initData() {

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<String, ArrayList<Sklepy>>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    listDataHeader.add(dataSnapshot1.getKey());
                    final String woj = dataSnapshot1.getKey();//////////wojewudstwa
                    //   in = 0;
                    ref.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<>();
                            in = in + 1;
                            for (DataSnapshot dataSnapshot0 : dataSnapshot.getChildren()) {
                                String ID = dataSnapshot0.getKey();
                                String miasto = dataSnapshot0.child("Miasto").getValue(String.class);
                                String ulica = dataSnapshot0.child("Ulica").getValue(String.class);
                                String firma = dataSnapshot0.child("Firma").getValue(String.class);
                                String mobile_phone = (String) dataSnapshot0.child("Telefon").getValue();
                                String mobile_phone1 = (String) dataSnapshot0.child("Telefon1").getValue();
                                String mobile_phone2 = (String) dataSnapshot0.child("Telefon2").getValue();
                                String status = format + "/" + woj + "/" + ID;
                                Sklepy s = dataSnapshot0.getValue(Sklepy.class);
                                s.setNazwa(firma);
                                s.setiD(ID);
                                s.setUlica(ulica);
                                s.setMiasto(miasto);
                                s.setAdres(format);
                                s.setTelefon(String.valueOf(mobile_phone));
                                s.setTelefon1(String.valueOf(mobile_phone1));
                                s.setTelefon2(String.valueOf(mobile_phone2));
                                s.setStatus(status);
                                s.setWojewodstwo(woj);
                                list.add(s);
                            }
                            try {
                                listHash.put(listDataHeader.get(in), list);
                            } catch (Exception ex) {

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                initData2();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


