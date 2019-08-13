package wallpl.example.vvvlad.walltherm.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.Nagroda;
import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.WybraliNagrParth2;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterDlaObraliNagrodeParth2 extends RecyclerView.Adapter<AdapterDlaObraliNagrodeParth2.MyViewHolder> {
    private Context context;
    private ArrayList<Nagroda> nagrody;
    private DatabaseReference myRef;
    String punktUser;
    String punktRezUser;

    public AdapterDlaObraliNagrodeParth2(WybraliNagrParth2 wybraliNagrParth2, ArrayList<Nagroda> list) {
        context = wybraliNagrParth2;
        nagrody = list;

    }

    @NonNull
    @Override
    public AdapterDlaObraliNagrodeParth2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AdapterDlaObraliNagrodeParth2.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.for_adapter_wybrali_parth2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDlaObraliNagrodeParth2.MyViewHolder myViewHolder, int i) {

        final String nazwa = nagrody.get(i).getNazwa();
        final String krOpis = nagrody.get(i).getKrutkiOpis();
        final String pkt = nagrody.get(i).getPunkt();
        final String mail = nagrody.get(i).getUserMailNagroda();
        final String id = nagrody.get(i).getIdUser();
        final String kay = nagrody.get(i).getKay();
        myViewHolder.nazwaForRecyclerObraliNagrode.setText(nazwa);
        myViewHolder.emailUserForRecyclerObraliNagrode.setText(mail);
        myViewHolder.opisNagrodyForRecyclerObraliNagrode.setText(krOpis);
        myViewHolder.punktyNagrodyForRecyclerObraliNagrode.setText(pkt+" pkt\n +1 zł");
       myViewHolder.layoutForRecyclerObraliNagrode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);

               aBuilder.setMessage("Co robimy?")
                       .setCancelable(true)
                       .setNegativeButton("Usunąć", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                               myRef = FirebaseDatabase.getInstance().getReference().child("ObraliNagrodeZatwierdz");
                               myRef.child(id).child(kay).removeValue();
                           }
                       });
               AlertDialog alert = aBuilder.create();
               alert.show();

           }
       });
    }

    @Override
    public int getItemCount() {
        return nagrody.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutForRecyclerObraliNagrode;
        TextView nazwaForRecyclerObraliNagrode;
        TextView emailUserForRecyclerObraliNagrode;
        TextView opisNagrodyForRecyclerObraliNagrode;
        TextView punktyNagrodyForRecyclerObraliNagrode;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            emailUserForRecyclerObraliNagrode = itemView.findViewById(R.id.emailUserForRecyclerObraliNagrodeParth2);
            layoutForRecyclerObraliNagrode = itemView.findViewById(R.id.layoutForRecyclerObraliNagrodeParth2);
            nazwaForRecyclerObraliNagrode = itemView.findViewById(R.id.nazwaForRecyclerObraliNagrodeParth2);
            opisNagrodyForRecyclerObraliNagrode = itemView.findViewById(R.id.opisNagrodyForRecyclerObraliNagrodeParth2);
            punktyNagrodyForRecyclerObraliNagrode = itemView.findViewById(R.id.punktyNagrodyForRecyclerObraliNagrodeParth2);

        }
    }
}
