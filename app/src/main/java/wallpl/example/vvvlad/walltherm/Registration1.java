package wallpl.example.vvvlad.walltherm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.Dialogs.DialogReglaminUser;
import wallpl.example.vvvlad.walltherm.Adapters.RAndom;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;
import static java.lang.String.*;

public class Registration1 extends AppCompatActivity implements DialogReglaminUser.DialogReglaminUserListner {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private EditText ETemail;
    private EditText et_1;
    private EditText et_2;
    private EditText et_3;
    private EditText et_4;
    private EditText et_5;
    private EditText et_6;
    private EditText et_7;
    private EditText log2;
    private TextView log1;
    private Button button;
    private int tr;
    private CheckBox box;
    private Button registration;
    private String url;
    private boolean connect = false;
    private String password;
    private int logi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        ETemail = findViewById(R.id.et_email_1);
        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        et_3 = findViewById(R.id.et_3);
        et_4 = findViewById(R.id.et_4);
        et_5 = findViewById(R.id.et_5);
        et_6 = findViewById(R.id.et_6);
        et_7 = findViewById(R.id.et_7);
        box = findViewById(R.id.checkBox);
        log1 = findViewById(R.id.log1);
        log2 = findViewById(R.id.log2);



        registration = findViewById(R.id.btn_registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ETemail.getText()) && Patterns.EMAIL_ADDRESS.matcher(ETemail.getText()).matches()) {
                    if (!TextUtils.isEmpty(et_1.getText())) {
                        if (!TextUtils.isEmpty(et_2.getText())) {
                            if (!TextUtils.isEmpty(et_3.getText())) {
                                if (!TextUtils.isEmpty(et_4.getText())) {
                                    if (!TextUtils.isEmpty(et_5.getText())) {
                                        if (et_6.length() == 10) {
                                            if (!TextUtils.isEmpty(et_7.getText())) {
                                                if (box.isChecked()){
                                                    if (String.valueOf(log2.getText()).equals(String.valueOf(logi))){
                                                        try {
                                                            registration(ETemail.getText().toString());
                                                        } catch (Exception e) {

                                                        }
                                                    }else {
                                                        log1.setError("Liczba musi się zgadzać z decyzją");
                                                        random();
                                                    }

                                                }
                                                else {
                                                    box.setError("Kliknij tutaj");
                                                }
                                            } else {
                                                et_7.setError("Pole musi być wypełnione");
                                            }
                                        } else {
                                            et_6.setError("NIP musi składać się z 10 liczb!");
                                        }
                                    } else {
                                        et_5.setError("Pole musi być wypełnione");
                                    }
                                } else {
                                    et_4.setError("Pole musi być wypełnione");
                                }
                            } else {
                                et_3.setError("Pole musi być wypełnione");
                            }
                        } else {
                            et_2.setError("Pole musi być wypełnione");
                        }
                    } else {
                        et_1.setError("Pole musi być wypełnione");
                    }
                } else {
                    ETemail.setError("Pole musi być prawidłowo wypełnione");
                }
            }

        });



        button = findViewById(R.id.prawa);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
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
            Snackbar.make(findViewById(R.id.registration), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }

        random();
    }

    private void random(){
        int a =0;
        int b = 99;
        int random_number1 = a + (int) (Math.random() * b);
        int random_number2 = a + (int) (Math.random() * b);
        String sr = valueOf(random_number1+"+"+random_number2+"=");
        log1.setText(sr);
        logi = random_number1+random_number2;
    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.registration), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.registration), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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
    public void onStart() {
        super.onStart();

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataref = mDatabaseRef.child("Reglamin");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String upload = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    upload = snapshot.getValue(String.class);

                }url = upload;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void openDialog() {
        FragmentManager manager = getSupportFragmentManager();
        DialogReglaminUser myDialogFragment = DialogReglaminUser.newInstance(url);
        myDialogFragment.show(manager, "dialog");
    }

    @Override
    public void applyTexts(boolean chek) {
        box.setChecked(chek);
    }

    public void registration(final String email) {
        password = RAndom.randomString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    signin(email, password);
                    Toast.makeText(Registration1.this, "Rejestracja się udała! Poczekaj na potwierdzenie administratora", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Registration1.this, "Użytkownik z takim adresem e-mail już istnieje", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    public void signin(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Registration1.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            if (user1 != null) {
                                String uid = user1.getUid();
                                zapisz(uid);
                                enx();
                            }
                        } else
                            Toast.makeText(Registration1.this, "Nie popravna autoryzacja", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void zapisz(String uid) {
        myRef.child("UsersA").child(uid).child("Email").setValue(ETemail.getText().toString());
        myRef.child("UsersA").child(uid).child("ID").setValue(uid);
        myRef.child("UsersA").child(uid).child("Imie").setValue(et_1.getText().toString());
        myRef.child("UsersA").child(uid).child("Nazwisko").setValue(et_2.getText().toString());
        myRef.child("UsersA").child(uid).child("Firma").setValue(et_3.getText().toString());
        myRef.child("UsersA").child(uid).child("Miasto").setValue(et_4.getText().toString());
        myRef.child("UsersA").child(uid).child("Ulica").setValue(et_5.getText().toString());
        myRef.child("UsersA").child(uid).child("NIP").setValue(et_6.getText().toString());
        myRef.child("UsersA").child(uid).child("Telefon").setValue(et_7.getText().toString());
        myRef.child("UsersA").child(uid).child("PASS").setValue(password);
        myRef.child("UsersA").child(uid).child("Status").setValue("UsersA");
        myRef.child("Punkty").child(uid).child("Punkt").child("Punkty").setValue("0");
    }

    private void enx() {
        Intent intent2 = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent2);
        mAuth.getInstance().signOut();
    }


}
