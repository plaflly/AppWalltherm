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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class DaneOsobowe extends AppCompatActivity {
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAuth;
    private String Email;
    private String Imie;
    private String Miasto;
    private String NIP;
    private String Nazwisko;
    private String Telefon;
    private String Ulica;
    private String Firma;
    private TextView email1;
    private TextView name1;
    private TextView surname1;
    private TextView firma1;
    private TextView nip1;
    private TextView miasto1;
    private TextView ulica1;
    private TextView telefon1;
    private Button usunkonto;
    private boolean connect = false;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dane_osobowe);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("UsersB");
        mAuth = FirebaseAuth.getInstance();
        email1 = findViewById(R.id.email1);
        name1 = findViewById(R.id.name1);
        surname1 = findViewById(R.id.surname1);
        firma1 = findViewById(R.id.firma1);
        nip1 = findViewById(R.id.nip1);
        miasto1 = findViewById(R.id.miasto1);
        ulica1 = findViewById(R.id.ulica1);
        telefon1 = findViewById(R.id.telefon1);
        usunkonto = findViewById(R.id.usunkonto);

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        if (String.valueOf(dataSnapshot1.getKey()).equals(mAuth.getCurrentUser().getUid())) {
                            Email = String.valueOf(dataSnapshot1.child("Email").getValue());
                            Imie = String.valueOf(dataSnapshot1.child("Imie").getValue());
                            Miasto = String.valueOf(dataSnapshot1.child("Miasto").getValue());
                            NIP = String.valueOf(dataSnapshot1.child("NIP").getValue());
                            Nazwisko = String.valueOf(dataSnapshot1.child("Nazwisko").getValue());
                            Telefon = String.valueOf(dataSnapshot1.child("Telefon").getValue());
                            Ulica = String.valueOf(dataSnapshot1.child("Ulica").getValue());
                            Firma = String.valueOf(dataSnapshot1.child("Firma").getValue());
                        }
                    }catch (Exception ez){
                    }

                }
                email1.setText(Email);
                name1.setText(Imie);
                surname1.setText(Nazwisko);
                firma1.setText(Firma);
                nip1.setText(NIP);
                miasto1.setText(Miasto);
                ulica1.setText(Ulica);
                telefon1.setText(Telefon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usunkonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DaneOsobowe.this);
                dialogBuilder.setTitle("Chcesz usunąć konto?")
                        .setMessage("Wyrażam zgodę na usunięcie moich danych osobowych zgodnie z RODO od dnia 25 maja 2018, z brakiem możliwości ich późniejszego przywrócenia.");

                dialogBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final FirebaseUser user = mAuth.getCurrentUser();
                        final String user0 = user.getUid();
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Punkty").child(user0);

                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Architekt");
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Hurtownia");
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Instalator");
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic(user0);
                                            try {

                                                firebaseDatabase.child(user0).removeValue();
                                                reference.removeValue();
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                                                reference1.child("Tokens").child(user0).removeValue();
                                                reference1.child("WidokUzytkownika").child(user0).removeValue();

                                            }catch (Exception ex){
                                            }
                                            mAuth.signOut();
                                            finish();
                                            Intent intent = new Intent(DaneOsobowe.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                        dialog.cancel();
                    }
                });
                dialogBuilder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
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
            Snackbar.make(findViewById(R.id.DaneOsobowe), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }
    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.DaneOsobowe), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.DaneOsobowe), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
            Intent intent = new Intent(this, KabinUser.class);
            startActivity(intent);
        }
        return true;
    }

}
