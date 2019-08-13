package wallpl.example.vvvlad.walltherm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.EditNagroda;
import wallpl.example.vvvlad.walltherm.Nagroda;

import wallpl.example.vvvlad.walltherm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDlaKatalogNagrodAdmin extends RecyclerView.Adapter<AdapterDlaKatalogNagrodAdmin.MyViewHolder> {
    private Context context;
    private ArrayList<Nagroda> nagrody;

    public AdapterDlaKatalogNagrodAdmin(Context c, ArrayList<Nagroda> m) {
        context = c;
        nagrody = m;
    }

    @NonNull
    @Override
    public AdapterDlaKatalogNagrodAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.for_recycler_katalog_nagrod, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDlaKatalogNagrodAdmin.MyViewHolder myViewHolder, int i) {
        final String image = nagrody.get(i).getImg();
        final String nazwa = nagrody.get(i).getNazwa();
        final String krOpis = nagrody.get(i).getKrutkiOpis();
        final String pkt = nagrody.get(i).getPunkt();
        final String calOpis = nagrody.get(i).getOpis();
        final String kay = nagrody.get(i).getKay();


        Picasso.with(context).load(image)
                .fit()
                .centerCrop()
                .into(myViewHolder.imageRecyclernagrod);

        myViewHolder.nazwaNagrodyRecycler.setText(nazwa);
        myViewHolder.opisNagrodyRecycler.setText(krOpis);
        myViewHolder.punktyNagrodyRecycler.setText(pkt+" pkt\n +1 zł");

        myViewHolder.layoutRecyclernagrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditNagroda.class);
                intent.putExtra("image", image);
                intent.putExtra("nazwa", nazwa);
                intent.putExtra("krOpis", krOpis);
                intent.putExtra("pkt", pkt);
                intent.putExtra("kay", kay);
                intent.putExtra("calOpis", calOpis);
                context.startActivity(intent);
            }
        });

        myViewHolder.scrolRecyclernagrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditNagroda.class);
                intent.putExtra("image", image);
                intent.putExtra("nazwa", nazwa);
                intent.putExtra("krOpis", krOpis);
                intent.putExtra("pkt", pkt);
                intent.putExtra("kay", kay);
                intent.putExtra("calOpis", calOpis);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nagrody.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRecyclernagrod;
        TextView nazwaNagrodyRecycler;
        TextView opisNagrodyRecycler;
        TextView punktyNagrodyRecycler;
        LinearLayout layoutRecyclernagrod;
        HorizontalScrollView scrolRecyclernagrod;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRecyclernagrod = itemView.findViewById(R.id.imageRecyclernagrod);
            nazwaNagrodyRecycler = itemView.findViewById(R.id.nazwaNagrodyRecycler);
            opisNagrodyRecycler = itemView.findViewById(R.id.opisNagrodyRecycler);
            punktyNagrodyRecycler = itemView.findViewById(R.id.punktyNagrodyRecycler);
            layoutRecyclernagrod = itemView.findViewById(R.id.layoutRecyclernagrod);
            scrolRecyclernagrod = itemView.findViewById(R.id.scrolRecyclernagrod);
        }
    }
}
