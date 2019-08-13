package wallpl.example.vvvlad.walltherm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.auth.FirebaseAuth;

public class ObliczIlosczWall extends AppCompatActivity implements View.OnClickListener {
    //    private EditText etInput;
//    private Button btnAdd;
//    private ListView lvItem;
//    private ArrayList<String> itemArrey;
//    private ArrayAdapter<String> itemAdapter;
    private FirebaseAuth mAuth;
    private Spinner spinner;
    private LinearLayout llMain1;
    private LinearLayout llMain2;
    private LinearLayout llMain3;
    private LinearLayout llMain4;
    private RadioGroup rgGravity;
    private RadioGroup rgWysok;
    private RadioGroup rgDom;
    private EditText etName;
    private Button btnCreate;
    private Button btnClear;
    private Button btnOblic;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_3;
    private LinearLayout ll_4;
    private LinearLayout lay1;
    private LinearLayout lay2;
    private LinearLayout lay3;
    private LinearLayout lay4;
    private TextView usermailWall;
    private int i1 = 0;
    private int i2 = 0;
    private int i3 = 0;
    private int i4 = 0;
    private int i1_1 = 1;
    private int i2_1 = 1;
    private int i3_1 = 1;
    private int i4_1 = 1;
    private int pokoje=0;
    private int lazienki=0;
    private int hol=0;
    private int kuchnie=0;


    private int strefa = 0;
    private int wysokosc = 0;
    private int dom = 0;


    ViewGroup.LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oblicz_iloscz_wall);

        mAuth = FirebaseAuth.getInstance();
        usermailWall = findViewById(R.id.usermailWall);
        usermailWall.setText(mAuth.getCurrentUser().getEmail());
        spinner = findViewById(R.id.spinnerObl);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Strefy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        strefa = position;
                        break;
                    case 1:
                        strefa = position;
                        break;
                    case 2:
                        strefa = position;
                        break;
                    case 3:
                        strefa = position;
                        break;
                    case 4:
                        strefa = position;
                        break;
                    case 5:
                        dialog();
                        spinner.setSelection(0);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        llMain1 = findViewById(R.id.llMain1);
        llMain2 = findViewById(R.id.llMain2);
        llMain3 = findViewById(R.id.llMain3);
        llMain4 = findViewById(R.id.llMain4);
        rgGravity = findViewById(R.id.rgGravity);
        rgWysok = findViewById(R.id.rg);
        rgDom = findViewById(R.id.rg1);
        etName = findViewById(R.id.etName);
        btnCreate = findViewById(R.id.btnCreate);
        btnClear = findViewById(R.id.btnClear);
        btnOblic = findViewById(R.id.btnOblic);
        btnClear.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnOblic.setOnClickListener(this);
        ll_1 = findViewById(R.id.ll_1);
        ll_2 = findViewById(R.id.ll_2);
        ll_3 = findViewById(R.id.ll_3);
        ll_4 = findViewById(R.id.ll_4);
        lay1 = ll_1;
        lay2 = ll_2;
        lay3 = ll_3;
        lay4 = ll_4;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                if (etName.getText().toString().trim().length() < 1) {
                    etName.setError("Wpisz dane!");
                } else {
//                    LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //  int btnGravity = Gravity.LEFT;
                    switch (rgGravity.getCheckedRadioButtonId()) {
                        case R.id.rbLeft:
                            i1 = i1 + 1;
                            if (i1_1 >= 4) {
                                LinearLayout layout;
                                i1_1 = 1;
                                layout = newLayout();
                                llMain1.addView(layout);
                                lay1 = layout;
                            }
                            Button textNew1 = new Button(this);
                            textNew1.setText("Pokój " + i1 + ": " + etName.getText().toString() + " m²");

                            pokoje = pokoje + Integer.parseInt(etName.getText().toString());
                            textNew1.setTransformationMethod(null);
                            lay1.addView(textNew1);
                            textNew1.setTextSize(14);
                            textNew1.setTextColor(Color.BLACK);
                            etName.setText("");
                            i1_1 = i1_1 + 1;
                            break;

                        case R.id.rbCenter:
                            i2 = i2 + 1;
                            if (i2_1 >= 4) {
                                LinearLayout layout;
                                i2_1 = 1;
                                layout = newLayout();
                                llMain2.addView(layout);
                                lay2 = layout;
                            }
                            Button textNew2 = new Button(this);
                            textNew2.setText("Łazienka " + i2 + ": " + etName.getText().toString() + " m²");
                            lazienki = lazienki + Integer.parseInt(etName.getText().toString());
                            textNew2.setTransformationMethod(null);
                            lay2.addView(textNew2);
                            textNew2.setTextSize(14);
                            textNew2.setTextColor(Color.BLACK);
                            etName.setText("");
                            i2_1 = i2_1 + 1;
                            break;

                        case R.id.rbkuch:
                            i4 = i4 + 1;
                            if (i4_1 >= 4) {
                                LinearLayout layout;
                                i4_1 = 1;
                                layout = newLayout();
                                llMain4.addView(layout);
                                lay4 = layout;
                            }
                            Button textNewe = new Button(this);
                            textNewe.setText("Kuchnia " + i4 + ": " + etName.getText().toString() + " m²");
                            kuchnie = kuchnie + Integer.parseInt(etName.getText().toString());
                            textNewe.setTransformationMethod(null);
                            lay4.addView(textNewe);
                            textNewe.setTextSize(14);
                            textNewe.setTextColor(Color.BLACK);
                            etName.setText("");
                            i4_1 = i4_1 + 1;
                            break;

                        case R.id.rbRight:
                            i3 = i3 + 1;
                            if (i3_1 >= 4) {
                                LinearLayout layout;
                                i3_1 = 1;
                                layout = newLayout();
                                llMain3.addView(layout);
                                lay3 = layout;
                            }
                            Button textNew3 = new Button(this);
                            textNew3.setText("Hol " + i3 + ": " + etName.getText().toString() + " m²");
                            hol = hol + Integer.parseInt(etName.getText().toString());
                            textNew3.setTransformationMethod(null);
                            lay3.addView(textNew3);
                            textNew3.setTextSize(14);
                            textNew3.setTextColor(Color.BLACK);
                            etName.setText("");
                            i3_1 = i3_1 + 1;
                            break;
                    }
                }
                break;
            case R.id.btnClear:
                clear();
                break;

            case R.id.btnOblic:

                switch (rgWysok.getCheckedRadioButtonId()) {
                    case R.id.rbL:
                        wysokosc = 0;
                        break;

                    case R.id.rbC:
                        wysokosc = 1;
                        break;

                    case R.id.rbR:
                        wysokosc = 2;

                        break;
                }
                switch (rgDom.getCheckedRadioButtonId()) {
                    case R.id.rbL1:
                        dom = 0;
                        break;

                    case R.id.rbC1:
                        dom = 1;
                        break;

                    case R.id.rbR1:
                        dom = 2;

                        break;
                }


                Intent intent = new Intent(this, ObliceniaWall.class);
                intent.putExtra("strefa", strefa);
                intent.putExtra("wysokosc", wysokosc);
                intent.putExtra("dom", dom);
                intent.putExtra("pokoj", pokoje);
                intent.putExtra("laz", lazienki);
                intent.putExtra("hol", hol);
                intent.putExtra("kuchnie", kuchnie);
                startActivity(intent);
                clear();

        }
    }

    public LinearLayout newLayout() {
        LinearLayout lin = new LinearLayout(this);
        lin.setOrientation(LinearLayout.HORIZONTAL);
        lin.setId(View.generateViewId());
        return lin;
    }

    private void clear() {
        LinearLayout layout;
        llMain1.removeAllViews();
        llMain2.removeAllViews();
        llMain3.removeAllViews();
        llMain4.removeAllViews();
        ll_2.removeAllViews();
        ll_3.removeAllViews();
        ll_4.removeAllViews();
        i1 = 0;
        i2 = 0;
        i3 = 0;
        i4 = 0;
        i1_1 = 1;
        i2_1 = 1;
        i3_1 = 1;
        i4_1 = 1;
        pokoje = 0;
        lazienki = 0;
        hol = 0;
        kuchnie = 0;
        layout = newLayout();
        llMain1.addView(layout);
        lay1 = layout;
        layout = newLayout();
        llMain2.addView(layout);
        lay2 = layout;
        layout = newLayout();
        llMain3.addView(layout);
        lay3 = layout;
        layout = newLayout();
        llMain4.addView(layout);
        lay4 = layout;
    }

    private void dialog() {

        ImageView image = new ImageView(ObliczIlosczWall.this);
        image.setImageResource(R.drawable.strefy);

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(ObliczIlosczWall.this);
        aBuilder.setView(image)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = aBuilder.create();
        alert.setTitle("Strefy: ");
        alert.show();


    }
}