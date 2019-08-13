package wallpl.example.vvvlad.walltherm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import wallpl.example.vvvlad.walltherm.Nagroda;
import wallpl.example.vvvlad.walltherm.ObraliNagrode;
import wallpl.example.vvvlad.walltherm.UsersAkt2;

import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDlaObraliNagrode extends RecyclerView.Adapter<AdapterDlaObraliNagrode.MyViewHolder> {
    private Context context;
    private ArrayList<Nagroda> nagrody;
    private DatabaseReference myRef;
    String punktUser;
    String punktRezUser;


    public AdapterDlaObraliNagrode(ObraliNagrode obraliNagrode, ArrayList<Nagroda> list) {
        context = obraliNagrode;
        nagrody = list;
    }

    @NonNull
    @Override
    public AdapterDlaObraliNagrode.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.for_recycler_obrali_nagrod, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDlaObraliNagrode.MyViewHolder myViewHolder, int i) {
        final String image = nagrody.get(i).getImg();
        final String nazwa = nagrody.get(i).getNazwa();
        final String krOpis = nagrody.get(i).getKrutkiOpis();
        final String pkt = nagrody.get(i).getPunkt();
        final String mail = nagrody.get(i).getUserMailNagroda();
        final String kay = nagrody.get(i).getKay();
        final String idUser = nagrody.get(i).getIdUser();
        final String time = nagrody.get(i).getTimeNagroda();


        myViewHolder.douzytkownikaForRecyclerObraliNagrode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UsersAkt2.class);
                intent.putExtra("userid", idUser);
                intent.putExtra("users", "UsersB");
                context.startActivity(intent);
            }
        });
        myViewHolder.nazwaForRecyclerObraliNagrode.setText(nazwa);
        Picasso.with(context).load(image)
                .fit()
                .centerCrop()
                .into(myViewHolder.imageForRecyclerObraliNagrode);

        myViewHolder.opisNagrodyForRecyclerObraliNagrode.setText(krOpis);
        myViewHolder.punktyNagrodyForRecyclerObraliNagrode.setText(pkt + " pkt\n +1 zł");
        myViewHolder.timeNagrodyForRecyclerObraliNagrode.setText(time);

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Punkty").child(idUser).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (String.valueOf(dataSnapshot1.getKey()).equals("Punkt")) {
                        punktRezUser = String.valueOf(dataSnapshot1.child("PunktyRezerw").getValue());
                        punktUser = String.valueOf(dataSnapshot1.child("Punkty").getValue());
                        System.out.println(punktRezUser + " " + punktUser);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myViewHolder.naliczycForRecyclerObraliNagrode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int punktRez = Integer.parseInt(punktRezUser);
                int punktyUserint = Integer.parseInt(punktUser);
                punktRez = punktRez - Integer.parseInt(pkt);
                punktyUserint = punktyUserint - Integer.parseInt(pkt);
                myRef.child("Punkty").child(idUser).child("Punkt").child("PunktyRezerw").setValue(punktRez);
                myRef.child("Punkty").child(idUser).child("Punkt").child("Punkty").setValue(punktyUserint);
                myRef.child("ObraliNagrode").child(idUser).child(kay).removeValue();
                myRef.child("NalicNagrode").child(idUser).child(kay).child("Nazwa").setValue(nazwa);
                myRef.child("NalicNagrode").child(idUser).child(kay).child("email").setValue(mail);
                myRef.child("NalicNagrode").child(idUser).child(kay).child("PKT").setValue(pkt);
                Toast.makeText(context, "Naliczenia zostało zakończone!", Toast.LENGTH_SHORT).show();
                myRef.child("ObraliNagrodeZatwierdz").child(idUser).child(kay).child("Name").setValue(nazwa);
                myRef.child("ObraliNagrodeZatwierdz").child(idUser).child(kay).child("KrotkiOpis").setValue(krOpis);
                myRef.child("ObraliNagrodeZatwierdz").child(idUser).child(kay).child("email").setValue(mail);
                myRef.child("ObraliNagrodeZatwierdz").child(idUser).child(kay).child("PKT").setValue(pkt);

            }

        });
        myViewHolder.usunForRecyclerObraliNagrode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int punktRez = Integer.parseInt(punktRezUser);
                punktRez = punktRez - Integer.parseInt(pkt);
                myRef.child("Punkty").child(idUser).child("Punkt").child("PunktyRezerw").setValue(punktRez);
                myRef.child("ObraliNagrode").child(idUser).child(kay).removeValue();
                Toast.makeText(context, "Usunięcie zostało zakończone!", Toast.LENGTH_SHORT).show();
            }
        });
        myViewHolder.emailUserForRecyclerObraliNagrode.setText(mail);


    }

    @Override
    public int getItemCount() {
        return nagrody.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutForRecyclerObraliNagrode;
        Button douzytkownikaForRecyclerObraliNagrode;
        TextView nazwaForRecyclerObraliNagrode;
        TextView emailUserForRecyclerObraliNagrode;
        ImageView imageForRecyclerObraliNagrode;
        TextView opisNagrodyForRecyclerObraliNagrode;
        TextView punktyNagrodyForRecyclerObraliNagrode;
        TextView timeNagrodyForRecyclerObraliNagrode;
        Button naliczycForRecyclerObraliNagrode;
        Button usunForRecyclerObraliNagrode;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            emailUserForRecyclerObraliNagrode = itemView.findViewById(R.id.emailUserForRecyclerObraliNagrode);
            layoutForRecyclerObraliNagrode = itemView.findViewById(R.id.layoutForRecyclerObraliNagrode);
            douzytkownikaForRecyclerObraliNagrode = itemView.findViewById(R.id.douzytkownikaForRecyclerObraliNagrode);
            nazwaForRecyclerObraliNagrode = itemView.findViewById(R.id.nazwaForRecyclerObraliNagrode);
            imageForRecyclerObraliNagrode = itemView.findViewById(R.id.imageForRecyclerObraliNagrode);
            opisNagrodyForRecyclerObraliNagrode = itemView.findViewById(R.id.opisNagrodyForRecyclerObraliNagrode);
            punktyNagrodyForRecyclerObraliNagrode = itemView.findViewById(R.id.punktyNagrodyForRecyclerObraliNagrode);
            timeNagrodyForRecyclerObraliNagrode = itemView.findViewById(R.id.timeNagrodyForRecyclerObraliNagrode);
            naliczycForRecyclerObraliNagrode = itemView.findViewById(R.id.naliczycForRecyclerObraliNagrode);
            usunForRecyclerObraliNagrode = itemView.findViewById(R.id.usunForRecyclerObraliNagrode);


        }
    }


}
