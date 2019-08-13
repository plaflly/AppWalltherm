
package wallpl.example.vvvlad.walltherm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import wallpl.example.vvvlad.walltherm.R;

public class RuchApp extends AppCompatActivity {
    private Button aKuR;
    private Button pOmuR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruch_app);
        aKuR = findViewById(R.id.button1);
        pOmuR = findViewById(R.id.button2);
        aKuR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSt1();
            }
        });
        pOmuR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSt();
            }
        });
    }

    private void nextSt(){
        Intent intent = new Intent(this, PomocUser.class);
        startActivity(intent);
    }

    private void nextSt1(){
        Intent intent = new Intent(this, RuchUsers.class);
        startActivity(intent);
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
