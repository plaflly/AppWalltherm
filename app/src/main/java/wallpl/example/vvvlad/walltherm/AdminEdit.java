package wallpl.example.vvvlad.walltherm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.Adapters.RAndom;
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

import java.util.ArrayList;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class AdminEdit extends AppCompatActivity {
    private DatabaseReference myRef;
    private ArrayList<String> list = new ArrayList<>();
    private ListView listAdminEdit;
    private ArrayList<String> list2 = new ArrayList<>();
    private ArrayList<String> list3 = new ArrayList<>();
    private Button oK;
    private EditText aDDAdmin;
    private boolean connect = false;
    private FirebaseAuth mAuth;
    private String etContent = "Administrator potwierdził twoją aplikację. Twoje hasło do konta: ";
    private String passAdmin;
    private String aDmin = getString(R.string.Admin);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Administrator");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                passAdmin = String.valueOf(dataSnapshot.child("pass").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listAdminEdit = findViewById(R.id.listAdminEdit);
        oK = findViewById(R.id.oK);
        mAuth = FirebaseAuth.getInstance();
        aDDAdmin = findViewById(R.id.aDDAdmin);
        myRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        oK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(aDDAdmin.getText()) && Patterns.EMAIL_ADDRESS.matcher(aDDAdmin.getText()).matches()) {

                    registration(String.valueOf(aDDAdmin.getText()));
                    aDDAdmin.setText("");
                } else {
                    aDDAdmin.setError("Należy wypełnić poprawnie!");
                }
            }
        });


        final ArrayAdapter<String> aDapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        listAdminEdit.setAdapter(aDapter);
        listAdminEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dialog(list.get(position), list2.get(position), list3.get(position));

            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String email = String.valueOf(dataSnapshot.child("email").getValue());
                String pass = String.valueOf(dataSnapshot.child("pass").getValue());
                String id = dataSnapshot.getKey();
                list.add(email);
                list2.add(id);
                list3.add(pass);
                aDapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String email = String.valueOf(dataSnapshot.child("email").getValue());
                String pass = String.valueOf(dataSnapshot.child("pass").getValue());
                String id = dataSnapshot.getKey();
                list.remove(email);
                list2.remove(id);
                list3.remove(pass);
                aDapter.notifyDataSetChanged();
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
            Snackbar.make(findViewById(R.id.AdminEdit), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.AdminEdit), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.AdminEdit), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    private void dialog(final String s, final String s1, final String s2) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(AdminEdit.this);
        aBuilder.setMessage("Co robimy? \n")
                .setCancelable(true)
                .setNegativeButton("Usunąć", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        delitUser(s, s1, s2);
                    }
                });
        AlertDialog alert = aBuilder.create();
        alert.setTitle("");
        alert.show();
    }

    private void delitUser(String s, final String s1, String s2) {
        mAuth.getInstance().signOut();
        mAuth.signInWithEmailAndPassword(s, s2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final FirebaseUser user = mAuth.getCurrentUser();

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    myRef.child(s1).removeValue();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Punkty").child(user.getUid());
                                    reference.removeValue();
                                    singnIn();
                                }
                            }
                        });
            }
        });
    }

    private void singnIn() {
        mAuth.getInstance().signOut();
        mAuth.signInWithEmailAndPassword(aDmin, passAdmin);
    }

    public void registration(final String email) {
        final String password = RAndom.randomString();
        final String id = String.valueOf(System.currentTimeMillis());
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminEdit.this, "Rejestracja nowego administratora się udała! ", Toast.LENGTH_LONG).show();
                    sendMessage(email, password);
                    myRef.child(id).child("email").setValue(email);
                    myRef.child(id).child("pass").setValue(password);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Punkty").child(mAuth.getUid()).child("Punkt").child("Punkty");
                    reference.setValue("0");
                    mAuth.getInstance().signOut();
                    singnIn();
                } else {
                    Toast.makeText(AdminEdit.this, "Użytkownik z takim adresem e-mail już istnieje", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void sendMessage(final String text, final String pass) {
        final ProgressDialog dialog = new ProgressDialog(AdminEdit.this);
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    GMailSender sender = new GMailSender(getString(R.string.emailforgmail), getString(R.string.passforgmail));
                    sender.sendMail("Walltherm Administrator", etContent + pass +getString(R.string.podpis), "Walltherm", text);
                    dialog.dismiss();


                } catch (Exception e) {

                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });

        sender.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, KabinAdmin.class);
            startActivity(intent);
        }
        return true;
    }

}
