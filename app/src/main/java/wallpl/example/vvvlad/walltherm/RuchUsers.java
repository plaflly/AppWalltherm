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
import android.widget.ExpandableListView;

import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class RuchUsers extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapterRuchUser listAdapter;
    private List<String> listDataHeader;
    private List<String> listDataHeader0;
    private ArrayList<Profile> profiles;
    private HashMap<String, ArrayList<Profile>> listHash;
    private DatabaseReference ref;
    private DatabaseReference ref1;
    private ArrayList<Profile> list;
    private int in = -1;
    private boolean connect = false;
    private ProgressDialog dialog;
    String emailQ;
    String firmaQ;
    String telefonQ;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruch_users);
        ref = FirebaseDatabase.getInstance().getReference();
        ref1 = FirebaseDatabase.getInstance().getReference();
        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("Proszę czekać");

        listView = findViewById(R.id.recyclerForRuchUsers);

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
            Snackbar.make(findViewById(R.id.ruchUsers), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }

        initData();
    }

    private void initData2() {
        listAdapter = new ExpandableListAdapterRuchUser(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void initData() {

        listDataHeader = new ArrayList<>();
        listDataHeader0 = new ArrayList<>();
        listHash = new HashMap<String, ArrayList<Profile>>();
        profiles = new ArrayList<>();
        ref.child("UsersB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    String email = String.valueOf(dataSnapshot2.child("Email").getValue());
                    String firma = String.valueOf(dataSnapshot2.child("Firma").getValue());
                    String telefon = String.valueOf(dataSnapshot2.child("Telefon").getValue());
                    String status = String.valueOf(dataSnapshot2.child("Status").getValue());
                    Profile q = dataSnapshot2.getValue(Profile.class);
                    q.setEmail(email);
                    q.setTelefon(telefon);
                    q.setFirma(firma);
                    q.setStatus(status);
                    q.setId(dataSnapshot2.getKey());
                    profiles.add(q);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("RuchUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDataHeader.clear();
                listDataHeader0.clear();
                listHash.clear();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    listDataHeader0.add(dataSnapshot1.getKey());
                }
                for (int r = 0; r < listDataHeader0.size(); r++) {
                    long max = Integer.parseInt(listDataHeader0.get(r));
                            long max_i = r;
                            for (int j = r + 1; j < listDataHeader0.size(); j++) {
                                if (Long.parseLong(listDataHeader0.get(j)) > max) {
                                    max = Long.parseLong(listDataHeader0.get(j));
                                    max_i = j;
                                }
                            if (r != max_i) {
                                String per = listDataHeader0.get(r);
                                listDataHeader0.set(r, listDataHeader0.get((int) max_i));
                                listDataHeader0.set((int) max_i, per);
                            }
                        }
                    }
                    for (int i=0; i<listDataHeader0.size();i++){
                        long max = Integer.parseInt(listDataHeader0.get(0));
                        long min = Integer.parseInt(listDataHeader0.get((listDataHeader0.size()-1)));
                        if(max-min>=100){
                            if (max-min>=8870){
                                if (max-min>=8890){
                                    //delite
                                    ref.child("RuchUsers").child(listDataHeader0.get(listDataHeader0.size()-1)).removeValue();
                                    listDataHeader0.remove(listDataHeader0.size()-1);
                                }
                            }
                            if (max-min<=150){
                                // delite
                                ref.child("RuchUsers").child(listDataHeader0.get(listDataHeader0.size()-1)).removeValue();
                                listDataHeader0.remove(listDataHeader0.size()-1);
                            }else{

                            }
                        }
                    }

                for (int i = 0; i < listDataHeader0.size(); i++) {
                    String data = listDataHeader0.get(i);
                    data = new StringBuilder(data).insert(data.length() - 2, " ").toString();
                    data = new StringBuilder(data).insert(data.length() - 5, " ").toString();
                    listDataHeader.add(data);
                }


                for (int im = 0; im < listDataHeader0.size(); im++) {
                    ref.child("RuchUsers").child(listDataHeader0.get(im)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<>();
                            in = in + 1;
                            for (DataSnapshot dataSnapshot0 : dataSnapshot.getChildren()) {
                                String time = String.valueOf(dataSnapshot0.child("Time").getValue());
                                final String idUser = String.valueOf(dataSnapshot0.getKey());
                                Profile s = dataSnapshot0.getValue(Profile.class);
                                for (int i = 0; i < profiles.size(); i++) {
                                    if (idUser.equals(profiles.get(i).getId())) {
                                        s.setDataEnt(time);
                                        s.setFirma(profiles.get(i).getFirma());
                                        s.setEmail(profiles.get(i).getEmail());
                                        s.setTelefon(profiles.get(i).getTelefon());
                                        s.setId(idUser);
                                        list.add(s);
                                    }
                                }
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

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.ruchUsers), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }
        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.ruchUsers), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
}
