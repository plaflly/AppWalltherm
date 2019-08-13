package wallpl.example.vvvlad.walltherm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class KorzysciAdmin extends AppCompatActivity {
    private Button instalatorAdmin;
    private Button deweloperAdmin;
    private Button architektAdmin;
    private Button klientAdmin;
    private String architekt;
    private String deweloper;
    private String instalator;
    private String klient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korzysci_admin);
        instalatorAdmin = findViewById(R.id.instalatorAdmin);
        deweloperAdmin = findViewById(R.id.deweloperAdmin);
        architektAdmin = findViewById(R.id.architektAdmin);
        klientAdmin = findViewById(R.id.klientAdmin);


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Zalety");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    if (String.valueOf(dataSnap.getKey()).equals("KLIENT")) {
                        klient = String.valueOf(dataSnap.getValue());
                    }
                    if (String.valueOf(dataSnap.getKey()).equals("ARCHITEKT")) {
                        architekt = String.valueOf(dataSnap.getValue());
                    }
                    if (String.valueOf(dataSnap.getKey()).equals("DEWELOPER")) {
                        deweloper = String.valueOf(dataSnap.getValue());
                    }
                    if (String.valueOf(dataSnap.getKey()).equals("INSTALATOR")) {
                        instalator = String.valueOf(dataSnap.getValue());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        instalatorAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAktivity(instalator, "INSTALATOR");
            }
        });
        deweloperAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAktivity(deweloper, "DEWELOPER");
            }
        });
        architektAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAktivity(architekt, "ARCHITEKT");
            }
        });
        klientAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAktivity(klient, "KLIENT");
            }
        });
    }

    private void nextAktivity(String s, String a) {
        Intent intent = new Intent(this, KorzyscAdmin.class);
        intent.putExtra("url", s);
        intent.putExtra("a", a);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, KabinAdmin.class);
            startActivity(intent);
        }
        return true;
    }

}
