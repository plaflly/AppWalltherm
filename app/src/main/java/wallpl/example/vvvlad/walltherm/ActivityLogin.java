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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase reF;
    private Button btn_loga;
    private EditText ETemail;
    private EditText ETpassword;
    private String admin = getString(R.string.Admin);
    private String userMail;
    private int fart = 0;
    private TextView pass;
    private String DummyEmail;
    private boolean connect = false;
    private boolean aDmIn = false;
    private FirebaseUser currentUser;
    private ArrayList<String> list = new ArrayList<>();
    private boolean wfd = true;
    private boolean dsf = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View _mw1 = getLayoutInflater().inflate(R.layout.activity_login, null);
        setContentView(_mw1);
        ETemail = findViewById(R.id.et_email);
        ETpassword = findViewById(R.id.et_password);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("Proszę czekać");
        btn_loga = findViewById(R.id.btn_loga);
        btn_loga.setOnClickListener(this);

        pass = findViewById(R.id.pass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ETemail.getText())) {
                    ETemail.setError("Wpisz Email");
                } else {
                    DummyEmail = String.valueOf(ETemail.getText());

                    mAuth.sendPasswordResetEmail(DummyEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ActivityLogin.this, "Wiadomość e-mail została wysłana do Twojej skrzynki odbiorczej z linkiem do resetowania hasła!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ActivityLogin.this, "Wiadomość e-mail nie została wysłana do Twojej skrzynki odbiorczej!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        reF = FirebaseDatabase.getInstance();
        findViewById(R.id.btn_sign_in).setOnClickListener(this);


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
            Snackbar.make(findViewById(R.id.login), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }
        if (wfd) {
            wfd = false;
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            currentUser = mAuth.getCurrentUser();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.getKey().equals("Admin")) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                list.add(String.valueOf(dataSnapshot2.child("email").getValue()));
                            }

                        }
                    }
                    if (currentUser != null) {
                        userMail = currentUser.getEmail();
                        enter(userMail);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }
    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.login), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.login), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
                connect = false;
            }

        }
    }


    @Override
    public void onClick(View view) {
        try {
            if (view.getId() == R.id.btn_sign_in) {

                signin(ETemail.getText().toString(), ETpassword.getText().toString());
            }

        } catch (Exception e) {
            Toast.makeText(ActivityLogin.this, "Wpisz prawidłowe dane", Toast.LENGTH_SHORT).show();
        }


        switch (view.getId()) {
            case R.id.btn_loga:
                Intent intent1 = new Intent(this, Registration1.class);
                startActivity(intent1);
                break;
            default:
                break;
        }

    }


    public void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ActivityLogin.this, "Autoryzacja zakończona", Toast.LENGTH_SHORT).show();
                    enter(ETemail.getText().toString());
                } else if (!isOnline()) {
                    Toast.makeText(ActivityLogin.this, "Nie mamy połączenia z serwerem", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityLogin.this, "Niepoprawna autoryzacja", Toast.LENGTH_SHORT).show();
                    fart = fart + 1;
                    if (fart >= 3) {
                        pass.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    private void enter(String a) {
        currentUser = mAuth.getCurrentUser();
        if (list.size()<=0){
            if (dsf) {
                dsf = false;
                if ((a.equals(admin)) || mAuth.getCurrentUser().getEmail().equals(admin)) {
                    Intent intent = new Intent(this, KabinAdmin.class);
                    String pasS = String.valueOf(ETpassword.getText());
                    intent.putExtra("fOrmat", pasS);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent1 = new Intent(this, KabinUser.class);
                    String pasS = String.valueOf(ETpassword.getText());
                    intent1.putExtra("fOrmat", pasS);
                    startActivity(intent1);
                    finish();
                }
            }
        }else {
        if (!(currentUser == null)) {
            for (int i = 0; i < list.size(); i++) {
                if (currentUser != null) {
                    try {
                        if (list.get(i).equals(mAuth.getCurrentUser().getEmail())) {
                            aDmIn = true;
                        }
                    } catch (Exception ex) {
                    }
                }
                if (currentUser == null) {
                    if (list.get(i).equals(a)) {
                        aDmIn = true;
                    }
                }
                if (i >= list.size() - 1) {
                    if (dsf) {
                        dsf = false;
                        if ((a.equals(admin)) || (aDmIn) || mAuth.getCurrentUser().getEmail().equals(admin)) {
                            Intent intent = new Intent(this, KabinAdmin.class);
                            String pasS = String.valueOf(ETpassword.getText());
                            intent.putExtra("fOrmat", pasS);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent1 = new Intent(this, KabinUser.class);
                            String pasS = String.valueOf(ETpassword.getText());
                            intent1.putExtra("fOrmat", pasS);
                            startActivity(intent1);
                            finish();
                        }
                    }
                }
            }

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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}