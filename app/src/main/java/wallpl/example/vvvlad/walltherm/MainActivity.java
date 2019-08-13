package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.Servise.KontaktService;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static Typeface typeface;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private TextView walltherm;
    private LinearLayout layoutparam;
    private LinearLayout Linier1;
    private LinearLayout Linier2;
    private LinearLayout Linier3;
    private LinearLayout Linier4;
    private LinearLayout Linier5;
    private LinearLayout Linier6;
    private LinearLayout Linier7;
    private LinearLayout Linier7_1;
    private LinearLayout Linier7_2;
    private RelativeLayout mainLayout;
    private LinearLayout.LayoutParams params;
    private LinearLayout.LayoutParams paramsEnd;
    private int width;
    private int height;
    private float x;
    private float y;
    private int dfsf = 0;

    private ProgressDialog dialog1;

    private int[] locationbutton1 = new int[4];
    private int[] locationbutton2 = new int[4];
    private int[] locationbutton3 = new int[4];
    private int[] locationbutton4 = new int[4];
    private int[] locationbutton5 = new int[4];
    private int[] locationbutton6 = new int[4];
    private int[] locationbutton7 = new int[4];
    private int[] locationbutton8 = new int[4];

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View _mw = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(_mw);
        typeface = Typeface.createFromAsset(getAssets(), "Signika-SemiBold.ttf");
        walltherm = findViewById(R.id.textWalltherm);
        walltherm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAdmin();
                //           pushNotification("topic");
            }
        });
        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.walltherm.pl"));
                startActivity(browserIntent);
                finish();
            }
        });
        button1.setTypeface(typeface);

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kontaktService();
            }
        });
        button2.setTypeface(typeface);

        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                korzysci();
            }
        });
        button3.setTypeface(typeface);

        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policzWall();
            }
        });
        button4.setTypeface(typeface);
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sklepGdzieKup();
            }
        });
        button5.setTypeface(typeface);

        button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sklepKtoZam();
            }
        });
        button6.setTypeface(typeface);

        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityLogin();
            }
        });
        button7.setTypeface(typeface);

        button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1 = new ProgressDialog(MainActivity.this);
                dialog1.show();
                dialog1.setMessage("Proszę czekać");
                polec();
            }
        });
        button8.setTypeface(typeface);
        mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener(this);

        Linier1 = findViewById(R.id.Linier1);
        Linier2 = findViewById(R.id.Linier2);
        Linier3 = findViewById(R.id.Linier3);
        Linier4 = findViewById(R.id.Linier4);
        Linier5 = findViewById(R.id.Linier5);
        Linier6 = findViewById(R.id.Linier6);
        Linier7_1 = findViewById(R.id.Linier7_1);
        Linier7_2 = findViewById(R.id.Linier7_2);
        layoutparam = findViewById(R.id.layoutparam);
        layoutparam.setOnTouchListener(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        params = new LinearLayout.LayoutParams(width / 6 * 5, height / 9);
        paramsEnd = new LinearLayout.LayoutParams(width / 12 * 5, height / 9);

        Linier1.setLayoutParams(params);
        Linier2.setLayoutParams(params);
        Linier3.setLayoutParams(params);
        Linier4.setLayoutParams(params);
        Linier5.setLayoutParams(params);
        Linier6.setLayoutParams(params);
        Linier7_1.setLayoutParams(paramsEnd);
        Linier7_2.setLayoutParams(paramsEnd);


        Snackbar.make(findViewById(R.id.mainLayout), "Aplikacja działa tylko online.", Snackbar.LENGTH_LONG).show();
    }

    private void toAdmin() {
        dfsf = dfsf + 1;
        if (dfsf == 50) {
            Toast.makeText(MainActivity.this, "Application made by Stashko Vladyslav (plaflly@gmail.com) for the company Villa Fashion", Toast.LENGTH_LONG).show();
        }

    }

    private void korzysci() {
        Intent intent1 = new Intent(this, Korzysci.class);
        startActivity(intent1);
        finish();
    }

    private void kontaktService() {
        Intent intent2 = new Intent(this, KontaktService.class);
        startActivity(intent2);
        finish();
    }

    private void policzWall() {
        Intent intent3 = new Intent(this, PoliczWall.class);
        startActivity(intent3);
        finish();
    }

    private void sklepGdzieKup() {
        Intent intent4 = new Intent(this, Sklep.class);
        intent4.putExtra("format", "GdzieKup");
        startActivity(intent4);
        finish();
    }

    private void sklepKtoZam() {
        Intent intent5 = new Intent(this, Sklep.class);
        intent5.putExtra("format", "KtoZam");
        startActivity(intent5);
        finish();
    }

    private void activityLogin() {
        Intent intent6 = new Intent(this, ActivityLogin.class);
        startActivity(intent6);
        finish();
    }


    private void polec() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Aplikacja Walltherm, pobierz stąd:\n https://play.google.com/store/apps/details?id=wallpl.walltherm.vvvlad.walltherm_1&hl=pl");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Polec Znajomemu"));
        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                dialog1.dismiss();
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void normal() {
        Linier1.setLayoutParams(params);
        Linier2.setLayoutParams(params);
        Linier3.setLayoutParams(params);
        Linier4.setLayoutParams(params);
        Linier5.setLayoutParams(params);
        Linier6.setLayoutParams(params);
        Linier7_1.setLayoutParams(paramsEnd);
        Linier7_2.setLayoutParams(paramsEnd);
        button1.setBackgroundColor(getColor(R.color.colorOrange));
        button2.setBackgroundColor(getColor(R.color.colorOrange));
        button3.setBackgroundColor(getColor(R.color.colorGreen));
        button4.setBackgroundColor(getColor(R.color.colorGreen));
        button5.setBackgroundColor(getColor(R.color.colorGreen));
        button6.setBackgroundColor(getColor(R.color.colorGreen));
        button7.setBackgroundColor(getColor(R.color.colorGreen));
        button8.setBackgroundColor(getColor(R.color.colorGreen));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        x = event.getX();
        y = event.getY();
        System.out.println(x);
        button1.getLocationOnScreen(locationbutton1);
        locationbutton1[2] = locationbutton1[0] + button1.getRight();
        locationbutton1[3] = locationbutton1[1] + button1.getBottom();

        button2.getLocationOnScreen(locationbutton2);
        locationbutton2[2] = locationbutton2[0] + button2.getRight();
        locationbutton2[3] = locationbutton2[1] + button2.getBottom();

        button3.getLocationInWindow(locationbutton3);
        locationbutton3[2] = locationbutton3[0] + button3.getRight();
        locationbutton3[3] = locationbutton3[1] + button3.getBottom();

        button4.getLocationInWindow(locationbutton4);
        locationbutton4[2] = locationbutton4[0] + button4.getRight();
        locationbutton4[3] = locationbutton4[1] + button4.getBottom();

        button5.getLocationInWindow(locationbutton5);
        locationbutton5[2] = locationbutton5[0] + button5.getRight();
        locationbutton5[3] = locationbutton5[1] + button5.getBottom();

        button6.getLocationInWindow(locationbutton6);
        locationbutton6[2] = locationbutton6[0] + button6.getRight();
        locationbutton6[3] = locationbutton6[1] + button6.getBottom();

        button7.getLocationInWindow(locationbutton7);
        locationbutton7[2] = locationbutton7[0] + button7.getRight();
        locationbutton7[3] = locationbutton7[1] + button7.getBottom();

        button8.getLocationInWindow(locationbutton8);
        locationbutton8[2] = locationbutton8[0] + button8.getRight();
        locationbutton8[3] = locationbutton8[1] + button8.getBottom();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (locationbutton1[0] <= x && x <= locationbutton1[2] && locationbutton1[1] <= y && y <= locationbutton1[3]) {
                    Linier7_1.setLayoutParams(new LinearLayout.LayoutParams(width / 8 * 5, height / 9));
                    Linier1.setLayoutParams(params);
                    Linier2.setLayoutParams(params);
                    Linier3.setLayoutParams(params);
                    Linier4.setLayoutParams(params);
                    Linier5.setLayoutParams(params);
                    Linier6.setLayoutParams(params);
                    Linier7_2.setLayoutParams(paramsEnd);
                    button1.setBackgroundColor(getColor(R.color.colorOrange));
                    button2.setBackgroundColor(getColor(R.color.colorOrange2));
                    button3.setBackgroundColor(getColor(R.color.colorGreen2));
                    button4.setBackgroundColor(getColor(R.color.colorGreen2));
                    button5.setBackgroundColor(getColor(R.color.colorGreen2));
                    button6.setBackgroundColor(getColor(R.color.colorGreen2));
                    button7.setBackgroundColor(getColor(R.color.colorGreen2));
                    button8.setBackgroundColor(getColor(R.color.colorGreen2));
                } else {
                    if (locationbutton2[0] <= x && x <= locationbutton2[2] && locationbutton2[1] <= y && y <= locationbutton2[3]) {
                        Linier7_2.setLayoutParams(new LinearLayout.LayoutParams(width / 8 * 5, height / 9));
                        Linier1.setLayoutParams(params);
                        Linier2.setLayoutParams(params);
                        Linier3.setLayoutParams(params);
                        Linier4.setLayoutParams(params);
                        Linier5.setLayoutParams(params);
                        Linier6.setLayoutParams(params);
                        Linier7_1.setLayoutParams(paramsEnd);
                        button1.setBackgroundColor(getColor(R.color.colorOrange2));
                        button2.setBackgroundColor(getColor(R.color.colorOrange));
                        button3.setBackgroundColor(getColor(R.color.colorGreen2));
                        button4.setBackgroundColor(getColor(R.color.colorGreen2));
                        button5.setBackgroundColor(getColor(R.color.colorGreen2));
                        button6.setBackgroundColor(getColor(R.color.colorGreen2));
                        button7.setBackgroundColor(getColor(R.color.colorGreen2));
                        button8.setBackgroundColor(getColor(R.color.colorGreen2));
                    } else {
                        if (locationbutton3[0] <= x && x <= locationbutton3[2] && locationbutton3[1] <= y && y <= locationbutton3[3]) {
                            Linier1.setLayoutParams(new LinearLayout.LayoutParams(width, height / 9));
                            Linier2.setLayoutParams(params);
                            Linier3.setLayoutParams(params);
                            Linier4.setLayoutParams(params);
                            Linier5.setLayoutParams(params);
                            Linier6.setLayoutParams(params);
                            Linier7_1.setLayoutParams(paramsEnd);
                            Linier7_2.setLayoutParams(paramsEnd);
                            button1.setBackgroundColor(getColor(R.color.colorOrange2));
                            button2.setBackgroundColor(getColor(R.color.colorOrange2));
                            button3.setBackgroundColor(getColor(R.color.colorGreen));
                            button4.setBackgroundColor(getColor(R.color.colorGreen2));
                            button5.setBackgroundColor(getColor(R.color.colorGreen2));
                            button6.setBackgroundColor(getColor(R.color.colorGreen2));
                            button7.setBackgroundColor(getColor(R.color.colorGreen2));
                            button8.setBackgroundColor(getColor(R.color.colorGreen2));
                        } else {
                            if (locationbutton4[0] <= x && x <= locationbutton4[2] && locationbutton4[1] <= y && y <= locationbutton4[3]) {
                                Linier2.setLayoutParams(new LinearLayout.LayoutParams(width, height / 9));
                                Linier1.setLayoutParams(params);
                                Linier3.setLayoutParams(params);
                                Linier4.setLayoutParams(params);
                                Linier5.setLayoutParams(params);
                                Linier6.setLayoutParams(params);
                                Linier7_1.setLayoutParams(paramsEnd);
                                Linier7_2.setLayoutParams(paramsEnd);
                                button1.setBackgroundColor(getColor(R.color.colorOrange2));
                                button2.setBackgroundColor(getColor(R.color.colorOrange2));
                                button3.setBackgroundColor(getColor(R.color.colorGreen2));
                                button4.setBackgroundColor(getColor(R.color.colorGreen));
                                button5.setBackgroundColor(getColor(R.color.colorGreen2));
                                button6.setBackgroundColor(getColor(R.color.colorGreen2));
                                button7.setBackgroundColor(getColor(R.color.colorGreen2));
                                button8.setBackgroundColor(getColor(R.color.colorGreen2));
                            } else {
                                if (locationbutton5[0] <= x && x <= locationbutton5[2] && locationbutton5[1] <= y && y <= locationbutton5[3]) {
                                    Linier3.setLayoutParams(new LinearLayout.LayoutParams(width, height / 9));
                                    Linier1.setLayoutParams(params);
                                    Linier2.setLayoutParams(params);
                                    Linier4.setLayoutParams(params);
                                    Linier5.setLayoutParams(params);
                                    Linier6.setLayoutParams(params);
                                    Linier7_1.setLayoutParams(paramsEnd);
                                    Linier7_2.setLayoutParams(paramsEnd);
                                    button1.setBackgroundColor(getColor(R.color.colorOrange2));
                                    button2.setBackgroundColor(getColor(R.color.colorOrange2));
                                    button3.setBackgroundColor(getColor(R.color.colorGreen2));
                                    button4.setBackgroundColor(getColor(R.color.colorGreen2));
                                    button5.setBackgroundColor(getColor(R.color.colorGreen));
                                    button6.setBackgroundColor(getColor(R.color.colorGreen2));
                                    button7.setBackgroundColor(getColor(R.color.colorGreen2));
                                    button8.setBackgroundColor(getColor(R.color.colorGreen2));
                                } else {
                                    if (locationbutton6[0] <= x && x <= locationbutton6[2] && locationbutton6[1] <= y && y <= locationbutton6[3]) {
                                        Linier4.setLayoutParams(new LinearLayout.LayoutParams(width, height / 9));
                                        Linier1.setLayoutParams(params);
                                        Linier2.setLayoutParams(params);
                                        Linier3.setLayoutParams(params);
                                        Linier5.setLayoutParams(params);
                                        Linier6.setLayoutParams(params);
                                        Linier7_1.setLayoutParams(paramsEnd);
                                        Linier7_2.setLayoutParams(paramsEnd);
                                        button1.setBackgroundColor(getColor(R.color.colorOrange2));
                                        button2.setBackgroundColor(getColor(R.color.colorOrange2));
                                        button3.setBackgroundColor(getColor(R.color.colorGreen2));
                                        button4.setBackgroundColor(getColor(R.color.colorGreen2));
                                        button5.setBackgroundColor(getColor(R.color.colorGreen2));
                                        button6.setBackgroundColor(getColor(R.color.colorGreen));
                                        button7.setBackgroundColor(getColor(R.color.colorGreen2));
                                        button8.setBackgroundColor(getColor(R.color.colorGreen2));
                                    } else {
                                        if (locationbutton7[0] <= x && x <= locationbutton7[2] && locationbutton7[1] <= y && y <= locationbutton7[3]) {
                                            Linier5.setLayoutParams(new LinearLayout.LayoutParams(width, height / 9));
                                            Linier1.setLayoutParams(params);
                                            Linier2.setLayoutParams(params);
                                            Linier3.setLayoutParams(params);
                                            Linier4.setLayoutParams(params);
                                            Linier6.setLayoutParams(params);
                                            Linier7_1.setLayoutParams(paramsEnd);
                                            Linier7_2.setLayoutParams(paramsEnd);
                                            button1.setBackgroundColor(getColor(R.color.colorOrange2));
                                            button2.setBackgroundColor(getColor(R.color.colorOrange2));
                                            button3.setBackgroundColor(getColor(R.color.colorGreen2));
                                            button4.setBackgroundColor(getColor(R.color.colorGreen2));
                                            button5.setBackgroundColor(getColor(R.color.colorGreen2));
                                            button6.setBackgroundColor(getColor(R.color.colorGreen2));
                                            button7.setBackgroundColor(getColor(R.color.colorGreen));
                                            button8.setBackgroundColor(getColor(R.color.colorGreen2));
                                        } else {
                                            if (locationbutton8[0] <= x && x <= locationbutton8[2] && locationbutton8[1] <= y && y <= locationbutton8[3]) {
                                                Linier6.setLayoutParams(new LinearLayout.LayoutParams(width, height / 9));
                                                Linier1.setLayoutParams(params);
                                                Linier2.setLayoutParams(params);
                                                Linier3.setLayoutParams(params);
                                                Linier4.setLayoutParams(params);
                                                Linier5.setLayoutParams(params);
                                                Linier7_1.setLayoutParams(paramsEnd);
                                                Linier7_2.setLayoutParams(paramsEnd);
                                                button1.setBackgroundColor(getColor(R.color.colorOrange2));
                                                button2.setBackgroundColor(getColor(R.color.colorOrange2));
                                                button3.setBackgroundColor(getColor(R.color.colorGreen2));
                                                button4.setBackgroundColor(getColor(R.color.colorGreen2));
                                                button5.setBackgroundColor(getColor(R.color.colorGreen2));
                                                button6.setBackgroundColor(getColor(R.color.colorGreen2));
                                                button7.setBackgroundColor(getColor(R.color.colorGreen2));
                                                button8.setBackgroundColor(getColor(R.color.colorGreen));
                                            } else {
                                                normal();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                normal();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("Pewnie chcesz wyjść?");


            dialogBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            dialogBuilder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }
        return true;
    }

}
