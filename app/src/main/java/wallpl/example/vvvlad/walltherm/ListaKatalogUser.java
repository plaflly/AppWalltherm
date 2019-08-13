package wallpl.example.vvvlad.walltherm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;


public class ListaKatalogUser extends AppCompatActivity {
    private DatabaseReference myRef;
    private ListView listview;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> adres = new ArrayList<>();
    private boolean connect = false;
private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(ListaKatalogUser.this);
        progressDialog.setMessage("Proszę czekać");
        progressDialog.show();
        setContentView(R.layout.activity_list_katalog_user);
        listview = findViewById(R.id.listCatalogsUser);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getItem(position);
                String url = urls.get(position);
                String ad = adres.get(position);
                System.out.println(position);
                System.out.println(name);
                System.out.println(url);
                ToKatalog(name, url, ad);

                //  openDialog(url);

            }
        });

        myRef = FirebaseDatabase.getInstance().getReference().child("Katalogi");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                names.add(dataSnapshot.child("Name").getValue(String.class));
                urls.add(dataSnapshot.child("url").getValue(String.class));
                adres.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                names.remove(dataSnapshot.child("Name").getValue(String.class));
                urls.remove(dataSnapshot.child("url").getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
            Snackbar.make(findViewById(R.id.listKatalogUser), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.listKatalogUser), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.listKatalogUser), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    private void ToKatalog(String name, String url, String ad) {
        Intent intent = new Intent(this, Katalog.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);
        intent.putExtra("adres", ad);
        intent.putExtra("user", "user");
        startActivity(intent);

    }
}