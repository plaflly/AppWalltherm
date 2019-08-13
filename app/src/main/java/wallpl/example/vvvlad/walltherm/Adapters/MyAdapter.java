package wallpl.example.vvvlad.walltherm.Adapters;

import android.annotation.SuppressLint;
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


import wallpl.example.vvvlad.walltherm.R;

import java.util.ArrayList;

import wallpl.example.vvvlad.walltherm.Profile;
import wallpl.example.vvvlad.walltherm.UsersAkt;
import wallpl.example.vvvlad.walltherm.UsersAkt2;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Profile> profiles;

    public MyAdapter(UsersAkt c, ArrayList<Profile> list) {
        context = c;
        profiles = list;
        for (int i = 0; i < profiles.size(); i++) {
            if (!String.valueOf(profiles.get(i).getDataEnt()).equals("null")) {
                long max = Long.parseLong(profiles.get(i).getDataEnt());
                long max_i = i;
                for (int j = i + 1; j < profiles.size(); j++) {
                    if (Long.parseLong(profiles.get(j).getDataEnt()) > max) {
                        max = Long.parseLong(profiles.get(j).getDataEnt());
                        max_i = j;
                    }
                }
                if (i != max_i) {
                    Profile per = profiles.get(i);
                    profiles.set(i, profiles.get((int) max_i));
                    profiles.set((int) max_i, per);
                }
            }
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.usertest, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final String userID = profiles.get(position).getId();
        final String status = profiles.get(position).getStatus();
        holder.name.setText("Imie: " + profiles.get(position).getName());
        holder.surname.setText("Nazwisko: " + profiles.get(position).getSurname());
        holder.email.setText("Email: " + profiles.get(position).getEmail());
        holder.firma.setText("Firma: " + profiles.get(position).getFirma());
        holder.nip.setText("NIP: " + profiles.get(position).getNip());
        holder.telefon.setText("Telefon: " + profiles.get(position).getTelefon());
        holder.miasto.setText("Miasto: " + profiles.get(position).getCity());
        holder.ulica.setText("Ulica: " + profiles.get(position).getStreet());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, UsersAkt2.class);
                intent.putExtra("userid", userID);
                intent.putExtra("users", status);
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void filterlist(ArrayList<Profile> filteredList) {
        profiles = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView name;
        public TextView email;
        TextView firma;
        public TextView surname;
        TextView nip;
        TextView telefon;
        TextView miasto;
        TextView ulica;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            firma = itemView.findViewById(R.id.firma);
            surname = itemView.findViewById(R.id.surname);
            nip = itemView.findViewById(R.id.nip);
            telefon = itemView.findViewById(R.id.telefon);
            miasto = itemView.findViewById(R.id.miasto);
            ulica = itemView.findViewById(R.id.ulica);
            layout = itemView.findViewById(R.id.linierlayout);
        }


    }
}
