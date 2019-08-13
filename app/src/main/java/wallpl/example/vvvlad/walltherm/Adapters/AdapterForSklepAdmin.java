package wallpl.example.vvvlad.walltherm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.EditSklep;

import wallpl.example.vvvlad.walltherm.R;
import wallpl.example.vvvlad.walltherm.Sklepy;

import java.util.ArrayList;

public class AdapterForSklepAdmin extends RecyclerView.Adapter<AdapterForSklepAdmin.MyViewHolder> {
    private Context context;
    private ArrayList<Sklepy> sklepy;

    public AdapterForSklepAdmin(Context c, ArrayList<Sklepy> p) {
        context = c;
        sklepy = p;
    }

    @NonNull
    @Override
    public AdapterForSklepAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new AdapterForSklepAdmin.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.lista, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final AdapterForSklepAdmin.MyViewHolder holder, final int position) {
        final String id = sklepy.get(position).getiD();
        final String status = sklepy.get(position).getStatus();
        holder.firma.setText("firma: " + sklepy.get(position).getNazwa());
        holder.telefon.setText("telefon: " + sklepy.get(position).getTelefon());
        holder.miasto.setText("miasto: " + sklepy.get(position).getMiasto());
        holder.ulica.setText("ulica: " + sklepy.get(position).getUlica());
        holder.wojewodstwo.setText("wojewodstwo: " + sklepy.get(position).getWojewodstwo());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, EditSklep.class);
                intent.putExtra("id", id);
                intent.putExtra("status", status);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return sklepy.size();
    }

    public void filterlist(ArrayList<Sklepy> filteredList) {
        sklepy = filteredList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;


        TextView firma, telefon, miasto, ulica, wojewodstwo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            wojewodstwo = itemView.findViewById(R.id.wojew);
            firma = itemView.findViewById(R.id.firma);
            telefon = itemView.findViewById(R.id.telefon);
            miasto = itemView.findViewById(R.id.miasto);
            ulica = itemView.findViewById(R.id.ulica);
            layout = itemView.findViewById(R.id.layoutLista);

        }
    }
}