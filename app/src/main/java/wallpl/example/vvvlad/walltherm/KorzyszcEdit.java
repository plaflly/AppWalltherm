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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
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

public class KorzyszcEdit extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference myRef;
    private Uri imgUri;
    private PDFView pdfView;
    public static final int REQUEST_CODE = 1234;
    private int width;
    private int height;
    private LinearLayout.LayoutParams params;
    private boolean connect = false;
    private static final String FB_STORAGE_PATH = "zalety/";
    private static final String FB_DATABASE_PATH = "Zalety/zalety";
    private Button wyszuk;
    private Button wysli;
    private String id;
    private String qdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korzyszc_edit);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        qdi = intent.getStringExtra("qdi");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference();
        pdfView = findViewById(R.id.pdfViewZalety);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        params = new LinearLayout.LayoutParams(width, height * 7 / 10);
        pdfView.setLayoutParams(params);
        wyszuk = findViewById(R.id.wyszukajZalety);
        wyszuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                szukaj();
            }
        });
        wysli = findViewById(R.id.btnZapiszZalety);
        wysli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wysli();
            }
        });

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                sRec(networkStatus);
            }
        }, intentFilter);

        if (isOnline()) {
            connect = true;

        } else {
            Snackbar.make(findViewById(R.id.korzyszcEdit), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();

        }


    }

    private void sRec(String status) {
        if (status.equals("connected")) {
            if (!connect) {
                Snackbar.make(findViewById(R.id.korzyszcEdit), "Internet dostępny!", Snackbar.LENGTH_LONG).show();
                connect = true;
            }

        } else if (status.equals("disconnected")) {
            if (connect) {
                Snackbar.make(findViewById(R.id.korzyszcEdit), "Internet niedostępny!", Snackbar.LENGTH_LONG).show();
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

    public void szukaj() {
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
                pdfView.fromUri(imgUri)
                        .enableAnnotationRendering(true)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void wysli() {
        final StorageReference as;
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.show();


            final StorageReference ref = mStorageRef.child(String.valueOf(System.currentTimeMillis()));
            if (qdi==null){
                as = FirebaseStorage.getInstance().getReference();
            }else {
                 as = FirebaseStorage.getInstance().getReferenceFromUrl(qdi);
            }


            ref.putFile(imgUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dialog.dismiss();
                            System.out.println(uri);
                            myRef.child("Zalety").child(id).setValue(uri.toString());
                            if(!(qdi==null)){
                                as.delete();
                            }
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

}
