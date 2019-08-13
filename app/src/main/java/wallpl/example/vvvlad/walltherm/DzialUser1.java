package wallpl.example.vvvlad.walltherm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import wallpl.example.vvvlad.walltherm.R;

public class DzialUser1 extends AppCompatActivity {
    private Button obliczan;
    private Button punktyuser;
    private Button nagroda;

    //   Button obliczan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dzial_user1);
        obliczan = findViewById(R.id.obliczan);
        obliczan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAkt();
            }
        });
        punktyuser = findViewById(R.id.punktyuser);
        punktyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAkt1();
            }
        });
        nagroda = findViewById(R.id.nagroda);
        nagroda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          nextAkt2();
            }
        });
    }
    public void nextAkt2() {
        Intent intent = new Intent(this, WybraliNagrParth2.class);
        startActivity(intent);
        finish();

    }

    private void nextAkt1() {
        Intent intent = new Intent(this, PunktyUsers.class);
        startActivity(intent);
        finish();
    }

    public void nextAkt() {
        Intent intent = new Intent(this, DzialUsera.class);
        startActivity(intent);
        finish();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, Users.class);
            startActivity(intent);
        }
        return true;
    }
}
