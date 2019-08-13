package wallpl.example.vvvlad.walltherm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class PoliczWall extends AppCompatActivity {
    private Button hurt;
    private Button insta;
    private EditText et_WT1;
    private TextView text;
    private TextView zlot;
    private EditText telefon;
    private Button telefonOK;
    private DatabaseReference myRef;
    private boolean connect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///  String id = String.valueOf(System.currentTimeMillis());
        super.onCreate(savedInstanceState);
        myRef = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_policz_wall);
        text = findViewById(R.id.textView8);
        zlot = findViewById(R.id.zlot);
        et_WT1 = (EditText) findViewById(R.id.et_WT1);
        et_WT1.addTextChangedListener(textWatcher);
        hurt = findViewById(R.id.btn_hurt);
        insta = findViewById(R.id.btn_insta);
        telefon = findViewById(R.id.nomTel);

        Date date = Calendar.getInstance().getTime();
        final String time = time(date.toString());

        telefonOK = findViewById(R.id.nomTel_btn);
        telefonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (telefon.getText().length() == 9) {
                    myRef.child("RuchAppTelefon").child(telefon.getText().toString()).setValue(telefon.getText().toString());
                    myRef.child("RuchAppTime").child(telefon.getText().toString()).setValue(time);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PoliczWall.this);
                    dialogBuilder.setMessage("Czekaj na kontakt z administratorem");

                    dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                    telefon.setText("");

                } else {
                    Toast.makeText(PoliczWall.this, "Numer telefonu musi składać się z 9 cyfr, na przykład: 123456789", Toast.LENGTH_LONG).show();
                }
            }
        });
        hurt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next("GdzieKup");
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next("KtoZam");
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
            Snackbar.make(findViewById(R.id.policzWall), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.policzWall), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.policzWall), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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


    private void Next(String value) {
        Intent intent = new Intent(this, Sklep.class);
        intent.putExtra("format", value);
        startActivity(intent);
        finish();
    }



    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                int metr = Integer.parseInt(et_WT1.getText().toString());
                int wt25 = metr / 6;
                if (metr == 0) {
                    zlot.setText("0 PLN");
                    text.setText("0 WT25");
                } else {
                    if (metr <= 5) {
                        text.setText("0 WT25");
                        zlot.setText("0 PLN");
                    } else {
                        text.setText(wt25 + " WT25");
                        zlot.setText((wt25*350)+" PLN netto");
                    }
                }
            } catch (Exception e) {
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
