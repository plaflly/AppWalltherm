package wallpl.example.vvvlad.walltherm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static wallpl.example.vvvlad.walltherm.reciever.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;


public class ReglaminAdmin extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference myRef;
    private Uri imgUri;
    private PDFView pdfViewReg;
    private LinearLayout.LayoutParams params;
    public static final String FB_STORAGE_PATH = "reglamin/";
    public static final String FB_DATABASE_PATH = "Reglamin/reglamin";

    public static final String FBK_STORAGE_PATH = "katalog/";
    public static final String FBK_DATABASE_PATH = "Katalogi";
    private Button btnListaKatalog;

    public static final int REQUEST_CODE = 1234;
    private int width;
    private int height;
    private boolean connect = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglamin_admin);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference();
        btnListaKatalog = findViewById(R.id.btn_ListaKatalogow);
        pdfViewReg = findViewById(R.id.pdfViewReg);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        params = new LinearLayout.LayoutParams(width, height * 8 / 10);
        pdfViewReg.setLayoutParams(params);

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
            Snackbar.make(findViewById(R.id.reglaminAdmin), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.reglaminAdmin), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.reglaminAdmin), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    public void btnBrowse_Click(View v) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                pdfViewReg.fromUri(imgUri)
                        .enableAnnotationRendering(true)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void btnUpload_Click(View v) {
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.show();

            final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH);


            ref.putFile(imgUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dialog.dismiss();
                            System.out.println(uri);
                            myRef.child(FB_DATABASE_PATH).setValue(uri.toString());
                            Toast.makeText(getApplicationContext(), "Przesłane PDF", Toast.LENGTH_SHORT).show();
                            finish();
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

                            dialog.setMessage("Przesłane " + (int) progress + "%");


                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Wybierz PDF", Toast.LENGTH_SHORT).show();
        }
    }


    public void btnUpload_Catal(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = dialogView.findViewById(R.id.edit1);
        dialogBuilder.setTitle("Nazwe katalogu");
        dialogBuilder.setMessage("Wpisz nazwe katalogu:");

        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(edt.getText())) {
                    Toast.makeText(ReglaminAdmin.this, "Katalog nie został zapisany!\nNazwa katalogu nie wprowadzona!", Toast.LENGTH_LONG).show();
                } else {
                    katal(edt.getText().toString());
                }
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    void katal(final String nazwa) {
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.show();
            final String id = String.valueOf(System.currentTimeMillis());
            final StorageReference ref = mStorageRef.child(id);
/////////////////////////////////////////////

            ref.putFile(imgUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dialog.dismiss();
                            System.out.println(uri);
                            myRef.child(FBK_DATABASE_PATH).child(id).child("url").setValue(uri.toString());
                            myRef.child(FBK_DATABASE_PATH).child(id).child("Name").setValue(nazwa);

                            Toast.makeText(getApplicationContext(), "uploaded PDF", Toast.LENGTH_SHORT).show();
                            finish();
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

                            dialog.setMessage("Przesłane " + (int) progress + "%");


                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Wybierz PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnList(View view) {
        Intent intent = new Intent(this, ListKatalogAdmin.class);
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

