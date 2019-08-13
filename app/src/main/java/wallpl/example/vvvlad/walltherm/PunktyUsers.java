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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import wallpl.example.vvvlad.walltherm.Adapters.AdapterDlaPktUsera;
import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class PunktyUsers extends AppCompatActivity {
    private RecyclerView punktyUsersRecycler;
    private DatabaseReference myRef;
    private DatabaseReference myRef1;
    private ArrayList<User> lista;
    private AdapterDlaPktUsera adapter;
    private boolean connect = false;
    private EditText editText;
    private Button btnszuk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkty_users);





        punktyUsersRecycler = findViewById(R.id.punktyUsersRecycler);
        punktyUsersRecycler.setLayoutManager(new LinearLayoutManager(this));
        punktyUsersRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef1 = FirebaseDatabase.getInstance().getReference();
        myRef.child("Punkty").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String id = String.valueOf(dataSnapshot1.getKey());
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        if (dataSnapshot2.getKey().equals("Punkt")) {
                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                if (dataSnapshot3.getKey().equals("Punkty")) {
                                    final String punkty = String.valueOf(dataSnapshot3.getValue());

                                    myRef1.child("UsersB").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String eMail = String.valueOf(dataSnapshot.child("Email").getValue());
                                            if (!eMail.equals("null")) {
                                                User user = dataSnapshot.getValue(User.class);
                                                user.setEmailUser(eMail);
                                                user.setPktUser(punkty);
                                                lista.add(user);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }

                                    });
                                }
                            }
                        }
                    }
                }
                adapter = new AdapterDlaPktUsera(PunktyUsers.this, lista);
                punktyUsersRecycler.setAdapter(adapter);
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
            Snackbar.make(findViewById(R.id.PunktyUsers), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }



        editText = findViewById(R.id.edittextuser);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(String.valueOf(editText.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(String.valueOf(editText.getText()));
            }
        });


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.PunktyUsers), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.PunktyUsers), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    private void filter(String text) {
        ArrayList<User> filteredList1 = new ArrayList<>();
        for (User item : lista) {
            if (
                    item.getEmailUser().toLowerCase().contains(text.toLowerCase())||
                            item.getPktUser().toLowerCase().contains(text.toLowerCase())){
                filteredList1.add(item);
            }

        }
        adapter.filterlist1(filteredList1);
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