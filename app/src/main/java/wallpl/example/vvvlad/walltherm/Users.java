package wallpl.example.vvvlad.walltherm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import wallpl.example.vvvlad.walltherm.R;

public class Users extends AppCompatActivity implements View.OnClickListener {
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button dzialusra;
    private Button komunikaty;
    private String a1 ="UsersA";
    private String a2="UsersB";
    private String a3="UsersC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        dzialusra = (Button) findViewById(R.id.dzialusra);
        dzialusra.setOnClickListener(this);
        komunikaty= (Button) findViewById(R.id.komunikaty);
        komunikaty.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Intent intent = new Intent(this, UsersAkt.class);
                finish();
                intent.putExtra("users",a2);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intent1 = new Intent(this, UsersAkt.class);
                finish();
                intent1.putExtra("users",a1);
                startActivity(intent1);
                break;
            case R.id.button3:
                Intent intent2 = new Intent(this, UsersAkt.class);
                finish();
                intent2.putExtra("users",a3);
                startActivity(intent2);
                break;
            case R.id.button4:
                Intent intent3 = new Intent(this, Punkty.class);
                finish();
                startActivity(intent3);
                break;
            case R.id.dzialusra:
                Intent intent4 = new Intent(this, DzialUser1.class);
                finish();
                startActivity(intent4);
                break;
            case R.id.komunikaty:
                Intent intent5 = new Intent(this, Komunikaty.class);
                finish();
                startActivity(intent5);
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
            Intent intent = new Intent(this, KabinAdmin.class);
            startActivity(intent);
        }
        return true;
    }
}
