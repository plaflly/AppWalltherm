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

import wallpl.example.vvvlad.walltherm.Profile;
import wallpl.example.vvvlad.walltherm.UsersAkt2;

import wallpl.example.vvvlad.walltherm.R;

import java.util.ArrayList;

public class AdapterForRuchUsers extends RecyclerView.Adapter<AdapterForRuchUsers.MyViewHolder> {
    private Context context;
    private ArrayList<Profile> profiles;

    public AdapterForRuchUsers(Context c, ArrayList<Profile> p) {
        context = c;
        profiles = p;
    }


    @NonNull
    @Override
    public AdapterForRuchUsers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.ruch_user,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder,final int position) {
        final String userID = profiles.get(position).getId();
        final String status = profiles.get(position).getStatus();
        myViewHolder.emailruch.setText("Email: " + profiles.get(position).getEmail());
        myViewHolder.firmaruch.setText("Firma: " + profiles.get(position).getFirma());
        myViewHolder.telefonruch.setText("Telefon: " + profiles.get(position).getTelefon());
        myViewHolder.timeruch.setText("Data: " + profiles.get(position).getLastEnter());

        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView nameruch;
        public TextView timeruch;
        public TextView emailruch;
        TextView firmaruch;
        TextView telefonruch;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            emailruch = itemView.findViewById(R.id.emailruch);
            firmaruch = itemView.findViewById(R.id.firmaruch);
            telefonruch = itemView.findViewById(R.id.telefonruch);
            timeruch = itemView.findViewById(R.id.timeruch);
            layout = itemView.findViewById(R.id.ruchUser);

        }
    }
}


