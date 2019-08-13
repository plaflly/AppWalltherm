package wallpl.example.vvvlad.walltherm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import wallpl.example.vvvlad.walltherm.R;

public class Punkty extends AppCompatActivity implements View.OnClickListener {

    private Button listaPunktuw;
    private Button sprzedaz;
    private Button katalogNagrod;
    private Button obraliNagrode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkty);

        listaPunktuw = findViewById(R.id.listaPunktBtn);
        sprzedaz = findViewById(R.id.sprzedazBtn);
        katalogNagrod = findViewById(R.id.katalodNadrodBtn);
        obraliNagrode = findViewById(R.id.obraliNagrodeBtn);
        listaPunktuw.setOnClickListener(this);
        sprzedaz.setOnClickListener(this);
        katalogNagrod.setOnClickListener(this);
        obraliNagrode.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listaPunktBtn:
                Intent intent = new Intent(this, PunktyList.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sprzedazBtn:
                Intent intent2 = new Intent(this, Sprzedaz.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.katalodNadrodBtn:
                Intent intent3 = new Intent(this, KatalogNagrodAdmin.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.obraliNagrodeBtn:
                Intent intent4 = new Intent(this, ObraliNagrode.class);
                startActivity(intent4);
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
            Intent intent = new Intent(this, Users.class);
            startActivity(intent);
        }
        return true;
    }
}