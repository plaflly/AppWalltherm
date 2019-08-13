package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.MainHalpers.GMailSender;
import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class ObliceniaWall extends AppCompatActivity {
    private int strefa;
    private double wysokosc;
    private double domkuchnia;
    private double domhol;
    private double domlazienka;
    private double dompokoj;

    private int str;
    private int wys;
    private int domek;


    private int pok;
    private int laz;
    private int hol;
    private int kuchnie;


    private double oblKuh;
    private double oblPok;
    private double oblLaz;
    private double oblHol;

    private int cenazamod = 330;

    private TextView sumaPokm2;
    private TextView sumaLazm2;
    private TextView sumaKuchm2;
    private TextView sumaHolm2;
    private TextView sumaPokmodul;
    private TextView sumaLazmodul;
    private TextView sumaKuchmodul;
    private TextView sumaHolmodul;
    private TextView symaM2;
    private TextView sumaMod;
    private TextView ceNa;
    private Button zapiszObliczenia;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oblicenia_wall);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        str = intent.getIntExtra("strefa", 0);
        wys = intent.getIntExtra("wysokosc", 0);
        domek = intent.getIntExtra("dom", 0);

        // m2 wszystkich pokojów
        pok = intent.getIntExtra("pokoj", 0);
        // m2 wszystkich lazienek
        laz = intent.getIntExtra("laz", 0);
        // m2 wszystkich holuw
        hol = intent.getIntExtra("hol", 0);
        // m2 wszystkich kuchen
        kuchnie = intent.getIntExtra("kuchnie", 0);

        // mamy 5 stref
        //zapisujemy parametr obranej strefy
        switch (str) {
            case 0:
                strefa = 0;
                break;
            case 1:
                strefa = 1;
                break;
            case 2:
                strefa = 2;
                break;
            case 3:
                strefa = 3;
                break;
            case 4:
                strefa = 4;
                break;
        }
        //wysokosc pomieszczenia
        switch (wys) {
            case 0:
                wysokosc = 2.5;
                break;
            case 1:
                wysokosc = 2.7;
                break;
            case 2:
                wysokosc = 3.0;
                break;
        }
        //wybieramy parametry domu
        switch (domek) {
            case 0:
                // Dom energooszedny
                domkuchnia = 1;
                domhol = 2;
                domlazienka = 3;
                dompokoj = 4;
                break;
            case 1:
                //Dom dobrze ocieplony
                domkuchnia = 1;
                domhol = 2;
                domlazienka = 3;
                dompokoj = 4;
                break;
            case 2:
                //Dom standard
                domkuchnia = 1;
                domhol = 2;
                domlazienka = 3;
                dompokoj = 4;
                break;
        }

        //obliczamy ilosc modułów
        oblKuh = kuchnie * wysokosc * strefa / 100 * domkuchnia;
        oblHol = hol * wysokosc * strefa / 100 * domhol;
        oblLaz = laz * wysokosc * strefa / 100 * domlazienka;
        oblPok = pok * wysokosc * strefa / 100 * dompokoj;

        //zaokraglimy ilosc modułów do gury
        // oblKuh = 1.001 => resultobloblKuh = 2
        long resultobloblKuh = (int) Math.ceil(oblKuh);
        long resultobloblHol = (int) Math.ceil(oblHol);
        long resultobloblLaz = (int) Math.ceil(oblLaz);
        long resultobloblPok = (int) Math.ceil(oblPok);
        long rezult = (int) Math.round(resultobloblPok + resultobloblLaz + resultobloblKuh + resultobloblHol);


        sumaPokm2 = findViewById(R.id.sumaPokm2);
        sumaLazm2 = findViewById(R.id.sumaLazm2);
        sumaKuchm2 = findViewById(R.id.sumaKuchm2);
        sumaHolm2 = findViewById(R.id.sumaHolm2);
        sumaPokmodul = findViewById(R.id.sumaPokmodul);
        sumaLazmodul = findViewById(R.id.sumaLazmodul);
        sumaKuchmodul = findViewById(R.id.sumaKuchmodul);
        sumaHolmodul = findViewById(R.id.sumaHolmodul);
        symaM2 = findViewById(R.id.symaM2);
        sumaMod = findViewById(R.id.sumaMod);
        ceNa = findViewById(R.id.ceNa);
        zapiszObliczenia = findViewById(R.id.zapiszObliczenia);
        zapiszObliczenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        sumaPokm2.setText(pok + " m²");
        sumaLazm2.setText(laz + " m²");
        sumaKuchm2.setText(kuchnie + " m²");
        sumaHolm2.setText(hol + " m²");
        sumaPokmodul.setText(resultobloblPok + " modułów");
        sumaLazmodul.setText(resultobloblLaz + " modułów");
        sumaKuchmodul.setText(resultobloblKuh + " modułów");
        sumaHolmodul.setText(resultobloblHol + " modułów");
        int suma = pok + laz + kuchnie + hol;
        symaM2.setText(suma + " m²");
        sumaMod.setText(rezult + " modułów");
        ceNa.setText(rezult * cenazamod + " zł");

        String id = String.valueOf(System.currentTimeMillis());
        Date date = Calendar.getInstance().getTime();
        final String time = time(date.toString());
        myRef.child("Obliczenia").child(mAuth.getCurrentUser().getUid()).child(id).child("email").setValue(mAuth.getCurrentUser().getEmail());
        myRef.child("Obliczenia").child(mAuth.getCurrentUser().getUid()).child(id).child("val").setValue("Pokoje:" + sumaPokm2.getText() + "    " + sumaPokmodul.getText() + "\n"
                + "Lazienki:" + sumaLazm2.getText() + "    " + sumaLazmodul.getText() + "\n"
                + "Kuchnie:" + sumaKuchm2.getText() + "    " + sumaKuchmodul.getText() + "\n"
                + "Hol i inne:" + sumaHolm2.getText() + "    " + sumaHolmodul.getText() + "\n");
        myRef.child("Obliczenia").child(mAuth.getCurrentUser().getUid()).child(id).child("time").setValue(time);
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


    private void dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mail_obl, null);
        dialogBuilder.setView(dialogView);
        final EditText nazwa = dialogView.findViewById(R.id.emailObl);
        dialogBuilder.setTitle("Wysylanie dannych");
        if (TextUtils.isEmpty(nazwa.getText())) {
            nazwa.setError("Wpisz Email!");
        }

        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(nazwa.getText())) {
                    Toast.makeText(ObliceniaWall.this, "Wpisz nazwe modula!", Toast.LENGTH_LONG).show();
                } else {
                    sendMessage(nazwa.getText().toString());

                }

            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void sendMessage(final String email) {
        final ProgressDialog dialog = new ProgressDialog(ObliceniaWall.this);
        dialog.setTitle("Wysyłanie wiadomości e-mail");
        dialog.setMessage("Proszę czekać");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(getString(R.string.emailforgmail), getString(R.string.passforgmail));
                    sender.sendMail(
                            "Walltherm Administrator",
                            "Pokoje:" + sumaPokm2.getText() + "    " + sumaPokmodul.getText() + "\n"
                                    + "Lazienki:" + sumaLazm2.getText() + "    " + sumaLazmodul.getText() + "\n"
                                    + "Kuchnie:" + sumaKuchm2.getText() + "    " + sumaKuchmodul.getText() + "\n"
                                    + "Hol i inne:" + sumaHolm2.getText() + "    " + sumaHolmodul.getText() + "\n"
                                    + getString(R.string.podpis),
                            "Walltherm", email);
                    dialog.dismiss();

                } catch (Exception e) {

                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });

        sender.start();
    }
}
