package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.MainHalpers.GMailSender;
import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;


public class UsersAkt2 extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference myRef;
    private DatabaseReference myRefer;
   private FirebaseAuth mAuth = FirebaseAuth.getInstance();
   private FirebaseAuth mAuth2 = FirebaseAuth.getInstance();

    private RadioGroup rGBtn;
    private EditText et_1;
    private EditText et_2;
    private EditText et_3;
    private EditText et_4;
    private EditText et_5;
    private EditText et_6;
    private EditText et_7;
    private Button doAkt;
    private Button doNieakt;
    private Button zapisz;
    private Button wyslEmail;
    private Button zadzwon;
    private String email;
    private String firma;
    private String nip;
    private String miasto;
    private String ulica;
    private String imie;
    private String nazwisko;
    private String id;
    private String pass;
    private String telefon;
    private String statusUser;
    int i = 0;
    private boolean connect = false;
    private TextView emaIl;
    private String a1 = "UsersA";
    private String a2 = "UsersB";
    private String a3 = "UsersC";
    private DatabaseReference widokUzytkownika;
    private String widok;
    private String users;
    private boolean status = false;
    private String etContent = "Administrator potwierdził twoją aplikację. Twoje hasło do konta: ";
    private String etContent_2 = "Administrator zablokował Twoje konto! Skontaktuj się z administratorem, aby zidentyfikować problem.";
    private String eMaIlAdm;
    private String pAssWorDAdmin = "";
    private DatabaseReference dataBase;
    private ProgressDialog dialogproc;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_akt2);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");
        users = intent.getStringExtra("users");

        rGBtn = findViewById(R.id.rGBtn);

        emaIl = findViewById(R.id.email);
        zadzwon = findViewById(R.id.zadzwon);
        zadzwon.setOnClickListener(this);

        wyslEmail = findViewById(R.id.button9);
        wyslEmail.setOnClickListener(this);

        doAkt = findViewById(R.id.button10);
        doAkt.setOnClickListener(this);

        zapisz = findViewById(R.id.button12);
        zapisz.setOnClickListener(this);

        doNieakt = findViewById(R.id.button11);
        doNieakt.setOnClickListener(this);

        et_1 = findViewById(R.id.name);
        et_2 = findViewById(R.id.surname);
        et_3 = findViewById(R.id.firma);
        et_4 = findViewById(R.id.nip);
        et_5 = findViewById(R.id.miasto);
        et_6 = findViewById(R.id.ulica);
        et_7 = findViewById(R.id.telefon);
        if (users.equals(a2)) {
            View b = findViewById(R.id.button10);
            b.setVisibility(View.INVISIBLE);
        }
        if (users.equals(a3)) {

            doNieakt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UsersAkt2.this);
                    dialogBuilder.setMessage("Pewnie chcesz usunąć?");

                    dialogBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialogproc = new ProgressDialog(UsersAkt2.this);
                            dialogproc.setMessage("Proszę czekać!");
                            dialogproc.show();
                            mAuth.signOut();
                            mAuth2.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    final FirebaseUser user = mAuth2.getCurrentUser();
                                    final String useruid = String.valueOf(user.getUid());
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        myRefer = FirebaseDatabase.getInstance().getReference();
                                                        myRefer.child("UsersC").child(useruid).removeValue();
                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Punkty").child(useruid);
                                                        reference.removeValue();
                                                        singnIn();
                                                    }
                                                }
                                            });
                                }
                            });
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
            doNieakt.setBackgroundColor(Color.RED);
            doNieakt.setText("Usunąć");
        }

        myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = myRef.child(users).child(userid);
        widokUzytkownika = myRef.child("WidokUzytkownika").child(userid);
        widokUzytkownika.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                widok = String.valueOf(dataSnapshot.child("widok").getValue());
                if (widok.equals("Instalator")) {
                    ((RadioButton) rGBtn.getChildAt(0)).setChecked(true);
                }
                if (widok.equals("Hurtownia")) {
                    ((RadioButton) rGBtn.getChildAt(1)).setChecked(true);
                }
                if (widok.equals("Architekt")) {
                    ((RadioButton) rGBtn.getChildAt(2)).setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Object lip = dataSnapshot.getValue();
                String pa = lip.toString();

                if (i == 0) {
                    //mail
                    email = (pa);
                }
                if (i == 1) {
                    //firma
                    firma = (pa);
                }
                if (i == 2) {
                    //id
                    id = pa;
                }
                if (i == 3) {
                    //Imie
                    imie = (pa);
                }
                if (i == 4) {
                    //Miasto
                    miasto = (pa);
                }
                if (i == 5) {
                    //nip
                    nip = (pa);
                }
                if (i == 6) {
                    //nazwisko
                    nazwisko = (pa);
                }
                if (i == 7) {
                    //pass
                    pass = pa;
                }
                if (i == 8) {

                    statusUser = (pa);
                }
                if (i == 9) {
                    //"Telefon: "
                    telefon = (pa);
                }
                if (i == 10) {
                    //"Ulica: "
                    ulica = (pa);
                }
                i++;
                if (i == 11) {
                    et_1.setText(imie);
                    et_2.setText(nazwisko);
                    et_3.setText(firma);
                    et_4.setText(nip);
                    et_5.setText(miasto);
                    et_6.setText(ulica);
                    et_7.setText(telefon);
                    emaIl.setText(email);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
            Snackbar.make(findViewById(R.id.usersAkt2), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }

        dataBase = FirebaseDatabase.getInstance().getReference();
        dataBase.child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (mAuth.getCurrentUser().getEmail().equals(String.valueOf(dataSnapshot1.child("email")))) {
                        eMaIlAdm = mAuth.getCurrentUser().getEmail();
                        pAssWorDAdmin = String.valueOf(dataSnapshot1.child("email"));
                    }
                }
                if (pAssWorDAdmin.equals("")) {
                    dataBase.child("Administrator").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            pAssWorDAdmin = String.valueOf(dataSnapshot.child("pass").getValue());
                            eMaIlAdm = "wallthermpl@gmail.com";
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void singnIn() {
        mAuth.signInWithEmailAndPassword(eMaIlAdm,pAssWorDAdmin).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                dialogproc.dismiss();
                finish();
            }
        });

    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.usersAkt2), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.usersAkt2), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
                connect = false;
            }

        }
    }


    @Override
    public void onClick(View view) {
        switch (rGBtn.getCheckedRadioButtonId()) {
            case R.id.rbInst:
                widok = "Instalator";
                break;

            case R.id.rbHurt:
                widok = "Hurtownia";
                break;

            case R.id.rbArh:
                widok = "Architekt";
                break;
        }


        switch (view.getId()) {
            case R.id.button9:
                Intent intent = new Intent(UsersAkt2.this, MailSend.class);
                intent.putExtra("email", email);
                startActivity(intent);
                //Email();
                break;
            case R.id.button10:
                if (isOnline() == true) {
                    doAktywn();
                } else {
                    Toast.makeText(UsersAkt2.this, "Brak połączenia z Internetem", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.button12:
                zapisz();
                break;

            case R.id.button11:
                doNieaktywn();
                break;

            case R.id.zadzwon:
                Intent intentza = new Intent(Intent.ACTION_DIAL);
                intentza.setData(Uri.parse("tel:" + telefon));
                startActivity(intentza);
                break;
            default:
                break;
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

    private void zapisz() {
        widokUzytkownika.child("widok").setValue(widok);
        myRef.child(users).child(id).child("Imie").setValue(et_1.getText().toString());
        myRef.child(users).child(id).child("Nazwisko").setValue(et_2.getText().toString());
        myRef.child(users).child(id).child("Firma").setValue(et_3.getText().toString());
        myRef.child(users).child(id).child("NIP").setValue(et_4.getText().toString());
        myRef.child(users).child(id).child("Miasto").setValue(et_5.getText().toString());
        myRef.child(users).child(id).child("Ulica").setValue(et_6.getText().toString());
        myRef.child(users).child(id).child("Telefon").setValue(et_7.getText().toString());
        Toast.makeText(UsersAkt2.this, "Zapisano!", Toast.LENGTH_SHORT).show();

    }

    private void doNieaktywn() {
        widokUzytkownika.child("widok").setValue(widok);
        myRef.child("UsersC").child(id).child("Email").setValue(email);
        myRef.child("UsersC").child(id).child("Imie").setValue(et_1.getText().toString());
        myRef.child("UsersC").child(id).child("Nazwisko").setValue(et_2.getText().toString());
        myRef.child("UsersC").child(id).child("Firma").setValue(et_3.getText().toString());
        myRef.child("UsersC").child(id).child("NIP").setValue(et_4.getText().toString());
        myRef.child("UsersC").child(id).child("Miasto").setValue(et_5.getText().toString());
        myRef.child("UsersC").child(id).child("Ulica").setValue(et_6.getText().toString());
        myRef.child("UsersC").child(id).child("Telefon").setValue(et_7.getText().toString());
        myRef.child("UsersC").child(id).child("ID").setValue(id);
        myRef.child("UsersC").child(id).child("PASS").setValue(pass);
        myRef.child("UsersC").child(id).child("Status").setValue("UsersB");
        myRef.child("UsersB").child(id).removeValue();
        myRef.child(users).child(id).removeValue();
        sendMessage(etContent_2 + getString(R.string.podpis));

    }

    private void doAktywn() {
        widokUzytkownika.child("widok").setValue(widok);
        myRef.child("UsersB").child(id).child("data").setValue(System.currentTimeMillis());
        myRef.child("UsersB").child(id).child("Email").setValue(email);
        myRef.child("UsersB").child(id).child("Imie").setValue(et_1.getText().toString());
        myRef.child("UsersB").child(id).child("Nazwisko").setValue(et_2.getText().toString());
        myRef.child("UsersB").child(id).child("Firma").setValue(et_3.getText().toString());
        myRef.child("UsersB").child(id).child("NIP").setValue(et_4.getText().toString());
        myRef.child("UsersB").child(id).child("Miasto").setValue(et_5.getText().toString());
        myRef.child("UsersB").child(id).child("Ulica").setValue(et_6.getText().toString());
        myRef.child("UsersB").child(id).child("Telefon").setValue(et_7.getText().toString());
        myRef.child("UsersB").child(id).child("ID").setValue(id);
        myRef.child("UsersB").child(id).child("PASS").setValue(pass);
        myRef.child("UsersB").child(id).child("Status").setValue("UsersB");
        myRef.child(users).child(id).removeValue();
        sendMessage(etContent + pass + getString(R.string.podpis));

    }

    private void sendMessage(final String text) {
        final ProgressDialog dialog = new ProgressDialog(UsersAkt2.this);
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(getString(R.string.emailforgmail), getString(R.string.passforgmail));
                    sender.sendMail("Walltherm Administrator", text, "Walltherm", email);
                    dialog.dismiss();
                    status = true;


                } catch (Exception e) {

                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });

        sender.start();


        if (status = true) {
            new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Toast.makeText(UsersAkt2.this, "Zapisano i e-mail wysłany do użytkownika!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    finish();
                }
            }.start();
        } else {
            if (status = false) {
                Toast.makeText(UsersAkt2.this, "Error! Spróbuj ponownie!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
