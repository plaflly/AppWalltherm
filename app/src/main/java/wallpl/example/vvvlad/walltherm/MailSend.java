package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import wallpl.example.vvvlad.walltherm.MainHalpers.GMailSender;
import wallpl.example.vvvlad.walltherm.R;

import static android.graphics.Color.RED;


public class MailSend extends AppCompatActivity {
    private EditText etContent;
    private TextView etRecipient;
    private Button btnSend;
    private String email;
   // private String podpis = "\n" + "\n" + "\n" + "\n" + " " + "\n" + " " + "\n" + "-----------------------------------" + "\n" + "\n" + "W razie potrzeby możesz skontaktowac sie z administratorem:" + "\n" + "Telefon 999-999-9999" + "\n" + "Telefon: 555-555-5555" + "\n" + "Email: Walltherm@gmail.com";
//  private String podpis = podpil;
    private boolean status ;
    private boolean connect = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_send);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        etContent = (EditText) findViewById(R.id.etContent);
        etRecipient = (TextView) findViewById(R.id.etRecipient);
        etRecipient.setText(email);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                btnSendRec(networkStatus);
            }
        }, intentFilter);

        if (isOnline()) {
            connect = true;
            btnSend.setBackgroundColor(Color.BLUE);
            btnSend.setClickable(true);
        } else {
            Snackbar.make(findViewById(R.id.activity_mail_send), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
            btnSend.setBackgroundColor(RED);
            btnSend.setClickable(false);
        }

    }

    private void btnSendRec(String status) {
        if (status.equals("connected")){
            if (!connect){
                Snackbar.make(findViewById(R.id.activity_mail_send), "Internet dostępny!" , Snackbar.LENGTH_LONG).show();
                btnSend.setBackgroundColor(Color.BLUE);
                btnSend.setClickable(true);
                connect= true;
            }

        }else if (status.equals("disconnected")){
            if (connect){
                Snackbar.make(findViewById(R.id.activity_mail_send), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
                btnSend.setBackgroundColor(RED);
                btnSend.setClickable(false);
                connect= false;
            }

        }

    }


    private void sendMessage() {
        final ProgressDialog dialog = new ProgressDialog(MailSend.this);
        dialog.setTitle("Wysyłanie wiadomości e-mail");
        dialog.setMessage("Proszę czekać");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(getString(R.string.emailforgmail), getString(R.string.emailforgmail));
                    sender.sendMail("Walltherm Administrator", etContent.getText().toString() +getString(R.string.podpis), "Walltherm", email);
                    dialog.dismiss();
                    status = true;
                } catch (Exception e) {
                    status = false;
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
                    Toast.makeText(MailSend.this, "Email został wysłany!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    finish();
                }
            }.start();
        } else {
            if (status = false) {
                Toast.makeText(MailSend.this, "Email nie został wysłany!", Toast.LENGTH_LONG).show();
            }
        }
    }
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}
