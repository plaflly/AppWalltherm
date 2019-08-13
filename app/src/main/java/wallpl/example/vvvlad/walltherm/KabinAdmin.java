package wallpl.example.vvvlad.walltherm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.Servise.KontaktSerEddit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class KabinAdmin extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button toEditAdmin;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams params;
    private int width;
    private int height;
    private DatabaseReference userreference;

    private String topic = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("Admin");

        setContentView(R.layout.activity_kabin_admin);
        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this);
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button6 = findViewById(R.id.button6);
        button6.setOnClickListener(this);
        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(this);
        button8 = findViewById(R.id.button8);
        button8.setOnClickListener(this);
        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(this);
        toEditAdmin = findViewById(R.id.toEditAdmin);
        toEditAdmin.setOnClickListener(this);
        linearLayout = findViewById(R.id.liniearAdmin);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

       if(!mAuth.getCurrentUser().getEmail().equals("Admin@******.com")){
           toEditAdmin.setVisibility(View.GONE);
           Intent intent = getIntent();
           String format = intent.getStringExtra("fOrmat");
           if (!TextUtils.isEmpty(format)){
               addPass(format);
           }
        }else{
           Intent intent = getIntent();
           String format = intent.getStringExtra("fOrmat");
           if (!TextUtils.isEmpty(format)){
               DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Administrator");
               reference.child("pass").setValue(format);
           }

       }

    }

    private void addPass(final String format) {
        final DatabaseReference base = FirebaseDatabase.getInstance().getReference().child("Admin");
        base.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String itur = String.valueOf(dataSnapshot1.child("email").getValue());
                    if (itur.equals(mAuth.getCurrentUser().getEmail())){
                        DatabaseReference base3 = FirebaseDatabase.getInstance().getReference().child("Admin");
                        base3.child(dataSnapshot1.getKey()).child("pass").setValue(format);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Intent intent = new Intent(this, Users.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button2:
                Intent intent2 = new Intent(this, KontaktSerEddit.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.button3:
                Intent intent3 = new Intent(this, SklepForAdmin.class);
                intent3.putExtra("format", "GdzieKup");
                startActivity(intent3);
                finish();
                break;
            case R.id.button4:
                Intent intent4 = new Intent(this, SklepForAdmin.class);
                intent4.putExtra("format", "KtoZam");
                startActivity(intent4);
                finish();
                break;
            case R.id.button5:
                Intent intent5 = new Intent(this, RuchApp.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.button6:
                Intent intent6 = new Intent(this, KabinUser.class);
                intent6.putExtra("format", "Admin");
                startActivity(intent6);
                finish();
                break;
            case R.id.button7:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Admin");
                mAuth.getInstance().signOut();
                Toast.makeText(KabinAdmin.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(this, MainActivity.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.button8:
                Intent intent8 = new Intent(this, KorzysciAdmin.class);
                startActivity(intent8);
                finish();
                break;
            case R.id.button9:
                Intent intent9 = new Intent(this, ReglaminAdmin.class);
                startActivity(intent9);
                finish();
                break;
            case R.id.toEditAdmin:
                Intent intent10 = new Intent(this, AdminEdit.class);
                startActivity(intent10);
                finish();
                break;

            default:
                break;
        }
    }


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

}
