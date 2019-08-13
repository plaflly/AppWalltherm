package wallpl.example.vvvlad.walltherm.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.R;

import wallpl.example.vvvlad.walltherm.User;

import java.util.ArrayList;

public class AdapterDlaPktUsera extends RecyclerView.Adapter<AdapterDlaPktUsera.MyViewHolder> {
    private Context context;
    private ArrayList<User> profiles;

    public AdapterDlaPktUsera(Context c, ArrayList<User> p) {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.punkty_user, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final String userEm = profiles.get(position).getEmailUser();
        final String pKT = profiles.get(position).getPktUser()+" pkt";
        holder.email.setText(userEm);
        holder.pkt.setText(pKT);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void filterlist1(ArrayList<User> filteredList1) {
        profiles = filteredList1;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email;
        public TextView pkt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.user_punktText1);
            pkt = itemView.findViewById(R.id.user_punktText2);
        }


    }

}

