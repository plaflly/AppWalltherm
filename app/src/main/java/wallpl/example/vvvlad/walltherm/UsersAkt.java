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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;

import wallpl.example.vvvlad.walltherm.Adapters.MyAdapter;
import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class UsersAkt extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Profile> list;
    private MyAdapter adapter;
    private boolean connect = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final String users = intent.getStringExtra("users");

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("Proszę czekać");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_akt);
        EditText editText = findViewById(R.id.edittext);
////// filter
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
/////////////////////////

        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(users);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String name = String.valueOf(dataSnapshot1.child("Imie").getValue());
                    String nip = String.valueOf(dataSnapshot1.child("NIP").getValue());
                    String nazwisko = String.valueOf(dataSnapshot1.child("Nazwisko").getValue());
                    String firma = String.valueOf(dataSnapshot1.child("Firma").getValue());
                    String email = String.valueOf(dataSnapshot1.child("Email").getValue());
                    String te = String.valueOf(dataSnapshot1.child("Telefon").getValue());
                    String ul = String.valueOf(dataSnapshot1.child("Ulica").getValue());
                    String miasto = String.valueOf(dataSnapshot1.child("Miasto").getValue());
                    String id = String.valueOf(dataSnapshot1.child("ID").getValue());
                    String pass = String.valueOf(dataSnapshot1.child("PASS").getValue());
                    String dataEnt = String.valueOf(dataSnapshot1.child("data").getValue());
                    Profile p = dataSnapshot1.getValue(Profile.class);
                    p.setStatus(users);
                    p.setName(name);
                    p.setSurname(nazwisko);
                    p.setFirma(firma);
                    p.setNip(nip);
                    p.setEmail(email);
                    p.setTelefon(te);
                    p.setStreet(ul);
                    p.setId(id);
                    p.setCity(miasto);
                    p.setPass(pass);
                    p.setDataEnt(dataEnt);
                    list.add(p);
                }

                adapter = new MyAdapter(UsersAkt.this, list);
                recyclerView.setAdapter(adapter);
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
            Snackbar.make(findViewById(R.id.usersAkt), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.usersAkt), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.usersAkt), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
        ArrayList<Profile> filteredList = new ArrayList<>();
        for (Profile item : list) {
            if (
                    item.getName().toLowerCase().contains(text.toLowerCase())||
                            item.getSurname().toLowerCase().contains(text.toLowerCase())||
                            item.getEmail().toLowerCase().contains(text.toLowerCase())||
                            item.getFirma().toLowerCase().contains(text.toLowerCase())||
                            item.getNip().toLowerCase().contains(text.toLowerCase())||
                            item.getTelefon().toLowerCase().contains(text.toLowerCase())||
                            item.getCity().toLowerCase().contains(text.toLowerCase())||
                            item.getStreet().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }

        }
        adapter.filterlist(filteredList);
    }


    public void onClick() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, Users.class);
            startActivity(intent);
        }
        return true;
    }
}