package wallpl.example.vvvlad.walltherm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class NagrodaDlaUser extends AppCompatActivity {
    private ImageView imageNagrodUser;
    private TextView nazwaNagrodUser;
    private TextView punktNagrodUser;
    private TextView opisNagrodUser;
    private Button zgodzicSieNaNagrode;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private Uri imgUri;
    private String nazwa;
    private String krOpis;
    private int pkt;
    private String kay;
    private String calOpis;

    private int userPunkt;
    private int userPunktRezerw;
    private boolean connect = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagroda_dla_user);
        imageNagrodUser = findViewById(R.id.imageNagrodUser);
        nazwaNagrodUser = findViewById(R.id.nazwaNagrodUser);
        punktNagrodUser = findViewById(R.id.punktNagrodUser);
        opisNagrodUser = findViewById(R.id.opisNagrodUser);
        zgodzicSieNaNagrode = findViewById(R.id.zgodzicSieNaNagrode);
        Intent intent = getIntent();
        imgUri = Uri.parse(intent.getStringExtra("image"));
        nazwa = intent.getStringExtra("nazwa");
        krOpis = intent.getStringExtra("krOpis");
        pkt = Integer.parseInt(intent.getStringExtra("pkt"));
        kay = intent.getStringExtra("kay");
        calOpis = intent.getStringExtra("calOpis");
        Picasso.with(NagrodaDlaUser.this).load(imgUri)
                .fit()
                .centerCrop()
                .into(imageNagrodUser);
        nazwaNagrodUser.setText(nazwa);
        punktNagrodUser.setText(pkt+" pkt\n +1 zł");
        opisNagrodUser.setText(calOpis);


        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Punkty").child(user.getUid());
        reference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String struserPunkt = String.valueOf(dataSnapshot.child("Punkty").getValue());
                if (!String.valueOf(struserPunkt).equals("null")){
                    userPunkt = Integer.parseInt(struserPunkt);
                }
                String struserPunktRezerw = String.valueOf(dataSnapshot.child("PunktyRezerw").getValue());
                if (!String.valueOf(struserPunktRezerw).equals("null")){
                    userPunktRezerw = Integer.parseInt(struserPunktRezerw);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        zgodzicSieNaNagrode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();
                String time = time(date.toString());
                if (String.valueOf(userPunktRezerw).equals("null")){
                    if(userPunkt - pkt >=0){
                        reference.child("Punkt").child("PunktyRezerw").setValue(pkt);
                        String id = String.valueOf(System.currentTimeMillis());
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("Name").setValue(nazwa);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("PKT").setValue(pkt);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("KrotkiOpis").setValue(krOpis);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("Opis").setValue(calOpis);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("url").setValue(String.valueOf(imgUri));
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("email").setValue(user.getEmail());
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("Time").setValue(time);
                        Toast.makeText(getApplicationContext(), "Wysłano!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    if(userPunktRezerw + pkt <= userPunkt){
                        reference.child("Punkt").child("PunktyRezerw").setValue(userPunktRezerw + pkt);
                        String id = String.valueOf(System.currentTimeMillis());
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("Name").setValue(nazwa);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("PKT").setValue(pkt);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("KrotkiOpis").setValue(krOpis);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("Opis").setValue(calOpis);
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("url").setValue(String.valueOf(imgUri));
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("email").setValue(user.getEmail());
                        myRef.child("ObraliNagrode").child(user.getUid()).child(id).child("Time").setValue(time);
                        Toast.makeText(getApplicationContext(), "Wysłano!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Nie ma wystarczającej liczby punktów", Toast.LENGTH_LONG).show();
                    }
                }
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
            Snackbar.make(findViewById(R.id.nagrodaDlaUser), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.nagrodaDlaUser), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.nagrodaDlaUser), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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


    private String time(String data) {
        String str = data;
        String[] subStr;
        String day = null;
        String month = null;
        String ocloc = null;
        String year = null;

        String delimeter = " "; // Разделитель
        subStr = str.split(delimeter); // Разделения строки str с помощью метода spl
        for (int i = 0; i <= 5; i++) {
            if (i == 1) {
                switch (subStr[1]) {
                    case "Jan":
                        month = "Styczeń";
                        break;
                    case "Feb":
                        month = "Luty";
                        break;
                    case "Mar":
                        month = "Marzec";
                        break;
                    case "Apr":
                        month = "Kwiecień";
                        break;
                    case "May":
                        month = "Maj";
                        break;
                    case "Jun":
                        month = "Czerwiec";
                        break;
                    case "Jul":
                        month = "Lipiec";
                        break;
                    case "Aug":
                        month = "Sierpień";
                        break;
                    case "Sep":
                        month = "Wrzesień";
                        break;
                    case "Oct":
                        month = "Październik";
                        break;
                    case "Nov":
                        month = "Listopad";
                        break;
                    case "Dec":
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
}
