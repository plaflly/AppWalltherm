package wallpl.example.vvvlad.walltherm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class EditNagroda extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference myRef;
    private ImageView imageEditNagr;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri imgUri;
    public static final String DATABASE_PATH = "Nagrody";
    private FirebaseStorage mFirebaseStorage;
    private Button btnAddEditNagrode;
    private Button btnUsunEditNagrode;
    private EditText punktyEditNagr;
    private EditText nazwaEditNagr;
    private EditText krotkiOpisEditNagrode;
    private EditText opisEditNagrode;
    private Uri image;
    private  String kay;
    private String nazwa;
    private String calOpis;
    private String pkt;
    private String krOpis;
    private boolean connect = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nagroda);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference();

        punktyEditNagr = findViewById(R.id.punktyEditNagr);
        nazwaEditNagr = findViewById(R.id.nazwaEditNagr);
        krotkiOpisEditNagrode = findViewById(R.id.krotkiOpisEditNagrode);
        opisEditNagrode = findViewById(R.id.opisEditNagrode);

        imageEditNagr = findViewById(R.id.editNagr);
        imageEditNagr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnAddEditNagrode = findViewById(R.id.btnAddEditNagrode);
        btnUsunEditNagrode = findViewById(R.id.btnUsunEditNagrode);
        btnUsunEditNagrode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseStorage = FirebaseStorage.getInstance();
                StorageReference imageRef = mFirebaseStorage.getReferenceFromUrl(String.valueOf(image));
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myRef.child(DATABASE_PATH).child(kay).removeValue();
                        Toast.makeText(getApplicationContext(), "Usunięto!", Toast.LENGTH_SHORT).show();
                        back();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditNagroda.this, "Wystąpił błąd podczas usuwania!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        btnAddEditNagrode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(punktyEditNagr.getText())) {
                    punktyEditNagr.setError("Wpisz Pkt");
                } else {
                    if (TextUtils.isEmpty(nazwaEditNagr.getText())) {
                        nazwaEditNagr.setError("Wpisz Nazwe");
                    } else {
                        if (TextUtils.isEmpty(krotkiOpisEditNagrode.getText())) {
                            krotkiOpisEditNagrode.setError("Wpisz Opis");
                        } else {
                            if (TextUtils.isEmpty(opisEditNagrode.getText())) {
                                opisEditNagrode.setError("Wpisz Opis");
                            } else {

                                if (imgUri.equals(image)) {
                                    myRef.child(DATABASE_PATH).child(kay).child("Name").setValue(String.valueOf(nazwaEditNagr.getText()));
                                    myRef.child(DATABASE_PATH).child(kay).child("PKT").setValue(String.valueOf(punktyEditNagr.getText().toString()));
                                    myRef.child(DATABASE_PATH).child(kay).child("KrotkiOpis").setValue(String.valueOf(krotkiOpisEditNagrode.getText().toString()));
                                    myRef.child(DATABASE_PATH).child(kay).child("Opis").setValue(String.valueOf(opisEditNagrode.getText().toString()));
                                    Toast.makeText(getApplicationContext(), "Wysłano!", Toast.LENGTH_SHORT).show();
                                    back();
                                } else {
                                    if (!String.valueOf(image).equals("null")) {
                                        mFirebaseStorage = FirebaseStorage.getInstance();
                                        StorageReference imageRef = mFirebaseStorage.getReferenceFromUrl(String.valueOf(image));
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                myRef.child(DATABASE_PATH).child(kay).removeValue();
                                                upload(punktyEditNagr.getText().toString(), nazwaEditNagr.getText().toString(), krotkiOpisEditNagrode.getText().toString(), opisEditNagrode.getText().toString());

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Toast.makeText(EditNagroda.this, "Wystąpił błąd podczas wysyłania!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }else{
                                        upload(punktyEditNagr.getText().toString(), nazwaEditNagr.getText().toString(), krotkiOpisEditNagrode.getText().toString(), opisEditNagrode.getText().toString());

                                    }

                                }


                            }
                        }
                    }
                }


            }
        });


        Intent intent = getIntent();
        if (String.valueOf(intent.getStringExtra("image")).equals("null")) {
            btnUsunEditNagrode.setVisibility(View.GONE);
        } else {
            imgUri = Uri.parse(intent.getStringExtra("image"));
            image = Uri.parse(intent.getStringExtra("image"));
            nazwa = intent.getStringExtra("nazwa");
            krOpis = intent.getStringExtra("krOpis");
            pkt = intent.getStringExtra("pkt");
            kay = intent.getStringExtra("kay");
            calOpis = intent.getStringExtra("calOpis");
            punktyEditNagr.setText(pkt);
            nazwaEditNagr.setText(nazwa);
            krotkiOpisEditNagrode.setText(krOpis);
            opisEditNagrode.setText(calOpis);
            Picasso.with(EditNagroda.this).load(imgUri)
                    .fit()
                    .centerCrop()
                    .into(imageEditNagr);
        }

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
            Snackbar.make(findViewById(R.id.editNagroda), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.editNagroda), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.editNagroda), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgUri = data.getData();

            Picasso.with(EditNagroda.this).load(imgUri)
                    .fit()
                    .centerCrop()
                    .into(imageEditNagr);
        }
    }


    void upload(final String pkt, final String nazwa, final String krotkiOpis, final String opis) {
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.show();
            final String id = String.valueOf(System.currentTimeMillis());
            final StorageReference ref = mStorageRef.child("image").child(id);

            ref.putFile(imgUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dialog.dismiss();

                            myRef.child(DATABASE_PATH).child(id).child("url").setValue(uri.toString());
                            myRef.child(DATABASE_PATH).child(id).child("Name").setValue(nazwa);
                            myRef.child(DATABASE_PATH).child(id).child("PKT").setValue(pkt);
                            myRef.child(DATABASE_PATH).child(id).child("KrotkiOpis").setValue(krotkiOpis);
                            myRef.child(DATABASE_PATH).child(id).child("Opis").setValue(opis);
                            Toast.makeText(getApplicationContext(), "Wysłano!", Toast.LENGTH_SHORT).show();
                            back();
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            ////error
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            ///show upload proces
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            dialog.setMessage("Wysyłanie " + (int) progress + "%");


                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Wybierz obraz", Toast.LENGTH_SHORT).show();
        }
    }

    private void back() {
        finish();
        Intent intent = new Intent(this, KatalogNagrodAdmin.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(this, KatalogNagrodAdmin.class);
            startActivity(intent);
        }
        return true;
    }

}
