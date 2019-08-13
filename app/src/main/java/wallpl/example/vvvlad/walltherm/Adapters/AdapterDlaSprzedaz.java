package wallpl.example.vvvlad.walltherm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.Moduly.Modul;
import wallpl.example.vvvlad.walltherm.UsersAkt2;

import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterDlaSprzedaz extends RecyclerView.Adapter<AdapterDlaSprzedaz.MyViewHolder> {
    private Context context;
    private ArrayList<Modul> moduls;
    private DatabaseReference myRef;
    int exit;
    int q;



    public AdapterDlaSprzedaz(Context c, ArrayList<Modul> m) {
        context = c;
        moduls = m;
    }

    @NonNull
    @Override
    public AdapterDlaSprzedaz.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.modul, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterDlaSprzedaz.MyViewHolder myViewHolder, int i) {
        final String idUrzytkownika = moduls.get(i).getIdUrzytkownika();
        final String email = moduls.get(i).getMailUser();
        String ilosc = moduls.get(i).getIloscModul();
        if (ilosc.equals("null")){
            ilosc ="0";
        }
        final String time = moduls.get(i).getTimeModul();
        final String nazwaMod = moduls.get(i).getNazwaModul();
        String pubkty = moduls.get(i).getIloscPunkt();
        final String idmodul = moduls.get(i).getIdModul();
        if (pubkty.equals("null")){
            pubkty ="0";
        }

        final String iloscPunkt = String.valueOf(Integer.parseInt(pubkty) * Integer.parseInt(ilosc));

        final String finalPubkty = pubkty;
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    myViewHolder.iloscPunktow.setText(String.valueOf(Integer.parseInt(myViewHolder.ilosc.getText().toString()) * Integer.parseInt(finalPubkty)));

                } catch (Exception e) {
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        };

        myViewHolder.douzytkownika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UsersAkt2.class);
                intent.putExtra("userid", idUrzytkownika);
                intent.putExtra("users", "UsersB");
                context.startActivity(intent);
            }
        });
        myViewHolder.emailUserModul.setText(email);
        myViewHolder.ilosc.setText(ilosc);
        myViewHolder.ilosc.addTextChangedListener(textWatcher);
        myViewHolder.iloscPunktow.setText(iloscPunkt);
        myViewHolder.nazwaModul.setText(nazwaMod);
        myViewHolder.punktzamod.setText(pubkty + " punkty");
        myViewHolder.time.setText(time);
        myViewHolder.textDoUrzytkownika.setVisibility(View.GONE);



        myViewHolder.naliczyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance().getReference().child("Punkty").child(idUrzytkownika);
                myRef.child("Moduly").child(idmodul).child("Status").setValue("tr");
                myRef.child("Moduly").child(idmodul).child("Ilosc").setValue(myViewHolder.ilosc.getText().toString());
                myRef.child("Moduly").child(idmodul).child("Naliczono").setValue(myViewHolder.iloscPunktow.getText().toString());
                q = 0;
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (q < 1) {
                            exit = Integer.parseInt(String.valueOf(dataSnapshot.child("Punkt").child("Punkty").getValue())) + Integer.parseInt(String.valueOf(myViewHolder.iloscPunktow.getText()));
                            q = q + 1;
                            myRef.child("Punkt").child("Punkty").setValue(exit);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        myViewHolder.usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( TextUtils.isEmpty(myViewHolder.textDoUrzytkownika.getText()) ){
                    myViewHolder.textDoUrzytkownika.setError("Wpisz text!");
                    myViewHolder.textDoUrzytkownika.setVisibility(View.VISIBLE);
                }else{
                    myRef = FirebaseDatabase.getInstance().getReference().child("Punkty").child(idUrzytkownika);
                    myRef.child("Moduly").child(idmodul).child("Text").setValue(String.valueOf(myViewHolder.textDoUrzytkownika.getText()));
                    myRef.child("Moduly").child(idmodul).child("Status").setValue("fal");

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return moduls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView emailUserModul;
        public Button douzytkownika;
        public EditText ilosc;
        public TextView nazwaModul;
        public EditText iloscPunktow;
        public TextView time;
        public TextView statusText;
        public TextView textModul;
        public TextView punktzamod;
        public Button naliczyc;
        public Button usun;
        public EditText textDoUrzytkownika;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            emailUserModul = itemView.findViewById(R.id.emailUserModul);
            douzytkownika = itemView.findViewById(R.id.douzytkownika);
            ilosc = itemView.findViewById(R.id.iloscmod);
            nazwaModul = itemView.findViewById(R.id.nazwaModul);
            iloscPunktow = itemView.findViewById(R.id.iloscPunktow);
            textModul = itemView.findViewById(R.id.textModul);
            time = itemView.findViewById(R.id.timemod);
            statusText = itemView.findViewById(R.id.statusmod);
            naliczyc = itemView.findViewById(R.id.naliczyc);
            usun = itemView.findViewById(R.id.usun);
            statusText.setVisibility(View.GONE);
            textModul.setVisibility(View.GONE);
            punktzamod = itemView.findViewById(R.id.iloscPunktowZaModul);
            textDoUrzytkownika = itemView.findViewById(R.id.textDoUrzytkownika);
        }
    }

}
