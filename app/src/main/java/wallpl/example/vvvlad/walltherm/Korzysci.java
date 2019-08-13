package wallpl.example.vvvlad.walltherm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Korzysci extends AppCompatActivity {
    private Button instalatorKorz;
    private Button deweloperKorz;
    private Button architektKorz;
    private Button klientKorz;
    private String architekt;
    private String deweloper;
    private String instalator;
    private String klient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korzysci);
        instalatorKorz = findViewById(R.id.instalatorKorz);
        deweloperKorz = findViewById(R.id.deweloperKorz);
        architektKorz = findViewById(R.id.architektKorz);
        klientKorz = findViewById(R.id.klientKorz);


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Zalety");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                architekt = String.valueOf(dataSnapshot.child("ARCHITEKT").getValue());
                deweloper = String.valueOf(dataSnapshot.child("DEWELOPER").getValue());
                instalator = String.valueOf(dataSnapshot.child("INSTALATOR").getValue());
                klient = String.valueOf(dataSnapshot.child("KLIENT").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        instalatorKorz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                if (mAuth==null){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Korzysci.this);
                    dialogBuilder.setMessage("Zaloguj się do aplikacji");

                    dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }else {
                    nextAktivity(instalator);
                }

            }
        });
        deweloperKorz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                if (mAuth==null){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Korzysci.this);
                    dialogBuilder.setMessage("Zaloguj się do aplikacji");

                    dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }else {
                    nextAktivity(deweloper);
                }

            }
        });
        architektKorz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                if (mAuth==null){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Korzysci.this);
                    dialogBuilder.setMessage("Zaloguj się do aplikacji");

                    dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }else {
                    nextAktivity(architekt);
                }

            }
        });
        klientKorz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAktivity(klient);
            }
        });
    }


    private void nextAktivity(String s){
        Intent intent = new Intent(this, Korzyszc.class);
        intent.putExtra("url", s);
        startActivity(intent);
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
