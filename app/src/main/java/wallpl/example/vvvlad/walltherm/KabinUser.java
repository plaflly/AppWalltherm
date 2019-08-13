package wallpl.example.vvvlad.walltherm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Date;

public class KabinUser extends AppCompatActivity {
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private Boolean usersAkt = false;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button02;
    private Button button6;
    private Button button7;
    private String widok;
    private TextView resetPass;
    private LinearLayout layoutUserKabin;
    private String DeviceToken = FirebaseInstanceId.getInstance().getToken();
    private String fOrmat;
    private String format;
    private int width;
    private int height;
    private String dataEnd01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabin_user);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Oblicz();
            }
        });
        button02 = findViewById(R.id.button02);
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daneOsobowe();
            }
        });

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPunktuw();
            }
        });
        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doKatalogNagrod();
            }
        });
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doKatalog();
            }
        });


        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });


        resetPass = findViewById(R.id.resetPass);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodialog();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;


        layoutUserKabin = findViewById(R.id.layoutUserKabin);


        final FirebaseUser user = mAuth.getCurrentUser();
        Date date = Calendar.getInstance().getTime();
        String time = time(date.toString());


        Intent intent = getIntent();
        format = intent.getStringExtra("format");
        fOrmat = intent.getStringExtra("fOrmat");

        if (format == null && !(mAuth == null)) {
            myRef.child("RuchUsers").child(dataEnd01).child(user.getUid()).child("Time").setValue(time);

            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("UsersC").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String id = dataSnapshot1.getKey();
                        if (id != null) {
                            usersAkt = true;
                        } else {

                        }

                    }
                    notActiv(usersAkt);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }


    }

    private void daneOsobowe() {
        Intent intent = new Intent(this, DaneOsobowe.class);
        startActivity(intent);
    }

    private void dodialog() {

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(KabinUser.this);
        aBuilder.setCancelable(true)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(KabinUser.this, "Wiadomość e-mail została wysłana do Twojej skrzynki odbiorczej z linkiem do zmiany hasła!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = aBuilder.create();
        alert.setTitle("Czy na pewno chcesz zmienić swoje hasło?");
        alert.show();


    }

    private void doKatalogNagrod() {
        Intent intent = new Intent(this, KatalogNagrodDlaUser.class);
        startActivity(intent);
    }

    private void doPunktuw() {
        Intent intent = new Intent(this, IleMaszPunktuw.class);
        startActivity(intent);
    }

    private void doKatalog() {
        Intent intent = new Intent(this, ListaKatalogUser.class);
        startActivity(intent);
    }

    private void Oblicz() {
        Intent intent = new Intent(this, ObliczIlosczWall.class);
        startActivity(intent);
    }

    private void exit() {

        FirebaseMessaging.getInstance().unsubscribeFromTopic("Architekt");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Hurtownia");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Instalator");

        FirebaseMessaging.getInstance().unsubscribeFromTopic(mAuth.getCurrentUser().getUid());
        reference.child("Tokens").child(mAuth.getCurrentUser().getUid()).removeValue();
        Toast.makeText(KabinUser.this, "Wylogowano", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        mAuth.getInstance().signOut();
        finish();
    }

    private void notActiv(Boolean usersAkt) {
        if (usersAkt == true) {
            finish();
            Toast.makeText(KabinUser.this, "Użytkownik zablokowany przez administratora! \n Prosimy o kontakt z naszym przedstawicielem.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            mAuth.getInstance().signOut();
        } else {
            try {
                FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid());
            } catch (Exception q) {

            }

            if (!TextUtils.isEmpty(fOrmat)) {
                DatabaseReference dat = FirebaseDatabase.getInstance().getReference().child("UsersB").child(mAuth.getCurrentUser().getUid());
                dat.child("PASS").setValue(fOrmat);
            }


            reference.child("Tokens").child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());

            DatabaseReference widokUzytkownika = myRef.child("WidokUzytkownika").child(mAuth.getCurrentUser().getUid());
            widokUzytkownika.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    widok = String.valueOf(dataSnapshot.child("widok").getValue());
                    FirebaseMessaging.getInstance().subscribeToTopic(widok);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    private String time(String data) {
        String str = data;
        String[] subStr;
        String day = null;
        String month = null;
        String month01 = null;
        String ocloc = null;
        String year = null;

        String delimeter = " "; // Разделитель
        subStr = str.split(delimeter); // Разделения строки str с помощью метода spl
        for (int i = 0; i <= 5; i++) {
            if (i == 1) {
                switch (subStr[1]) {
                    case "Jan":
                        month = "Styczeń";
                        month01 = "01";
                        break;
                    case "Feb":
                        month = "Luty";
                        month01 = "02";
                        break;
                    case "Mar":
                        month01 = "03";
                        month = "Marzec";
                        break;
                    case "Apr":
                        month01 = "04";
                        month = "Kwiecień";
                        break;
                    case "May":
                        month01 = "05";
                        month = "Maj";
                        break;
                    case "Jun":
                        month01 = "06";
                        month = "Czerwiec";
                        break;
                    case "Jul":
                        month01 = "07";
                        month = "Lipiec";
                        break;
                    case "Aug":
                        month01 = "08";
                        month = "Sierpień";
                        break;
                    case "Sep":
                        month01 = "09";
                        month = "Wrzesień";
                        break;
                    case "Oct":
                        month01 = "10";
                        month = "Październik";
                        break;
                    case "Nov":
                        month01 = "11";
                        month = "Listopad";
                        break;
                    case "Dec":
                        month01 = "12";
                        month = "Grudzień";
                        break;
                }
            }
            if (i == 2) {
                day = subStr[i];
            }
            if (i == 3) {
                ocloc = tiME(subStr[i]);
            }
            if (i == 5) {
                year = subStr[i];
            }
        }
        String dataEnd = ocloc + "    ' " + day + " " + month + " " + year + " '";
        dataEnd01 = year + month01 + day;
        return dataEnd;
    }

    private String tiME(String ti) {
        String str = ti;
        String[] subStr;
        String godz = null;
        String min = null;

        String delimeter = ":"; // Разделитель
        subStr = str.split(delimeter); // Разделения строки str с помощью метода spl
        for (int i = 0; i <= 1; i++) {
            if (i == 0) {
                godz = subStr[i];
            }
            if (i == 1) {
                min = subStr[i];
            }

        }
        String dataEnd = godz + ":" + min;
        return dataEnd;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (format == null) {
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                finish();
                Intent intent = new Intent(this, KabinAdmin.class);
                startActivity(intent);
            }

        }
        return true;
    }
}
