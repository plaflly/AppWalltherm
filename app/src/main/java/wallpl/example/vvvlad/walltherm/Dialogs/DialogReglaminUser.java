package wallpl.example.vvvlad.walltherm.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import wallpl.example.vvvlad.walltherm.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DialogReglaminUser extends AppCompatDialogFragment {
    //  private DatabaseReference mDatabaseRef;
    PDFView pdfView;
    private DialogReglaminUserListner listner;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String urlFile = getArguments().getString("url");


        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.layout_dialog, null);
        pdfView = view.findViewById(R.id.pdfView);
        new RetrievePDFStream().execute(urlFile);

        builder.setView(view)
                .setTitle("Regulamin Korzystania z Serwisu")
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean chek = false;
                        listner.applyTexts(chek);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean chek = true;
                        listner.applyTexts(chek);
                    }
                });
        return builder.create();
    }

    private class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {
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
            pdfView.fromStream(inputStream).load();
        }
    }

    public static DialogReglaminUser newInstance(String url) {
        DialogReglaminUser fragment = new DialogReglaminUser();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listner = (DialogReglaminUserListner) context;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public interface DialogReglaminUserListner {
        void applyTexts(boolean chek);
    }
}
