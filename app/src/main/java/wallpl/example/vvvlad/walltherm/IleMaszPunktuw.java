package wallpl.example.vvvlad.walltherm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.Adapters.AdapterModul;
import wallpl.example.vvvlad.walltherm.Moduly.Modul;
import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class IleMaszPunktuw extends AppCompatActivity {

    private TextView userMail;
    private TextView user_punkt;
    private EditText iloscModul;
    private Button dodacModul;
    private RecyclerView recyclerFormodulUser;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayList<Modul> list;
    private ArrayList<Modul> list2;
    private AdapterModul adapter;
    private Spinner spiner;
    private ArrayList<String> listforSpiner;
    private ArrayList<String> listPunkt = new ArrayList<>();
    private ArrayList<String> listModul = new ArrayList<>();
    private String obranyModul;
    private String obranyPunkt;
    private boolean connect = false;
    private DatabaseReference notificationsRef;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ile_masz_punktuw);

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("Proszę czekać");

        notificationsRef = FirebaseDatabase.getInstance().getReference().child("Punkty");
        notificationsRef.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        spiner = findViewById(R.id.spiner);

        final ArrayAdapter<String> adapterSpiner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listModul);
        adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setSelection(0);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obranyModul = listModul.get(position);
                obranyPunkt = listPunkt.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        userMail = findViewById(R.id.userMail);
        userMail.setText(user.getEmail());
        user_punkt = findViewById(R.id.user_punkt);
        iloscModul = findViewById(R.id.iloscModul);
        dodacModul = findViewById(R.id.dodacModul);
        dodacModul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iloscModul.getText().length() < 1) {
                    iloscModul.setError("Wpisz ilosc sprzszedanych moduluw!");
                } else {
                    dialog();

                }
            }
        });
        recyclerFormodulUser = findViewById(R.id.recyclerFormodulUser);
        recyclerFormodulUser.setLayoutManager(new LinearLayoutManager(this));
        recyclerFormodulUser.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("Moduly");
        refer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapsho : dataSnapshot.getChildren()) {
                    String modul = dataSnapsho.getKey();
                    listModul.add(modul);
                    String punkt = String.valueOf(dataSnapsho.child("Punkt").getValue());
                    listPunkt.add(punkt);
                }
                spiner.setAdapter(adapterSpiner);
                try {
                    obranyModul = listModul.get(0);
                    obranyPunkt = listPunkt.get(0);
                } catch (Exception ex) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Punkty").child(user.getUid());
        reference.child("Punkt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotq : dataSnapshot.getChildren()){
                    String  qw = String.valueOf(dataSnapshot.child("Punkty").getValue());
                    if (qw.equals("null")){
                        reference.child("Punkt").child("Punkty").setValue("0");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Punkt").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String punkt1 = String.valueOf(dataSnapshot.getValue());
                String pkt = punkt1 + " pkt";
                String punktyRezerw = String.valueOf(dataSnapshot.child("PunktyRezerw").getValue());

                if (!punkt1.equals("null")) {
                    int i = Integer.parseInt(punkt1);
                    if (i > 0) {
                        user_punkt.setTextColor(getResources().getColor(R.color.colorOrange));
                        if (!punktyRezerw.equals("null")) {
                            if (!punktyRezerw.equals("0")) {
                                pkt = pkt + "\n -" + punktyRezerw + "(nagrody)";
                            }
                        }
                    }
                    user_punkt.setText(pkt);
                }
                dialog.dismiss();


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


        DatabaseReference ref = reference.child("Moduly");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                list2 = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String ilosc = String.valueOf(dataSnapshot1.child("Ilosc").getValue());
                    String status = String.valueOf(dataSnapshot1.child("Status").getValue());
                    String time = String.valueOf(dataSnapshot1.child("Time").getValue());
                    String nazwa = String.valueOf(dataSnapshot1.child("nazwa").getValue());
                    String textModl = String.valueOf(dataSnapshot1.child("Text").getValue());
                    String iloscPunkt = String.valueOf(dataSnapshot1.child("Naliczono").getValue());
                    String textForUser = String.valueOf(dataSnapshot1.child("TextForUser").getValue());

                    Modul m = dataSnapshot1.getValue(Modul.class);
                    m.setStatusModul(status);/////
                    m.setIloscModul(ilosc);///
                    m.setTimeModul(time);
                    m.setIloscPunkt(iloscPunkt);///
                    m.setTextModul(textModl);
                    m.setNazwaModul(nazwa);
                    m.setTextForUser(textForUser);

                    list2.add(m);
                }

                adapter = new AdapterModul(IleMaszPunktuw.this, list2);
                recyclerFormodulUser.setAdapter(adapter);

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
            Snackbar.make(findViewById(R.id.ileMaszPunktuw), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.ileMaszPunktuw), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.ileMaszPunktuw), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    private void zapisz(String text, String text2) {
        String uid = user.getUid();
        Date date = Calendar.getInstance().getTime();
        String time = time(date.toString());
        String id = String.valueOf(System.currentTimeMillis());
        if (!text2.equals("")) {
            notificationsRef.child(uid).child("Moduly").child(id).child("TextForUser").setValue(text2);
        }
        notificationsRef.child(uid).child("Moduly").child(id).child("Time").setValue(time);
        notificationsRef.child(uid).child("Moduly").child(id).child("Ilosc").setValue(text);
        notificationsRef.child(uid).child("Moduly").child(id).child("email").setValue(user.getEmail());
        notificationsRef.child(uid).child("Moduly").child(id).child("nazwa").setValue(obranyModul);
        notificationsRef.child(uid).child("Moduly").child(id).child("punkty").setValue(obranyPunkt);

        iloscModul.setText("");


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
        String dataEnd = ocloc + "   ' " + day + " " + month + " " + year + " '";
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


    private void dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_dla_text_urzytkownika, null);
        dialogBuilder.setView(dialogView);
        final EditText nazwa = dialogView.findViewById(R.id.nazwaText);
        dialogBuilder.setTitle("Dodawanie notatek");


        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                HashMap<String, String> notificationData = new HashMap<String, String>();
                notificationData.put("from", mAuth.getUid());


                zapisz(iloscModul.getText().toString(), nazwa.getText().toString());
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}
