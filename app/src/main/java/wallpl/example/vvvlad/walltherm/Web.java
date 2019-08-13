package wallpl.example.vvvlad.walltherm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wallpl.example.vvvlad.walltherm.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Web extends AppCompatActivity {

    private PDFView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();


        webView = findViewById(R.id.webview);
        Bundle bundle = getIntent().getExtras();
        String url = "https://firebasestorage.googleapis.com/v0/b/walltherm01.appspot.com/o/1543484133245?alt=media&token=f931f62e-d83a-494d-9239-948c2bc6a6f7";
        url = intent.getStringExtra("url");
        if (bundle != null) {
            url = getIntent().getStringExtra("url");
        }
        new RetrievePDFStream().execute(url);
    }


    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {

                URL urlx = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) urlx.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;

        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            webView.fromStream(inputStream).load();
        }
    }
}
