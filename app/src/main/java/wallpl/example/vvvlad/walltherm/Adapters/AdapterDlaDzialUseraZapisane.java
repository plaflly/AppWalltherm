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

import wallpl.example.vvvlad.walltherm.DzialUseraZapisane;
import wallpl.example.vvvlad.walltherm.Moduly.Modul;
import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterDlaDzialUseraZapisane  extends RecyclerView.Adapter<AdapterDlaDzialUseraZapisane.MyViewHolder>{
    private Context context;
    private ArrayList<Modul> moduls;
    private DatabaseReference myRef;




    public AdapterDlaDzialUseraZapisane(DzialUseraZapisane dzialUseraZapisane, ArrayList<Modul> list) {
        context = dzialUseraZapisane;
        moduls = list;
    }

    @NonNull
    @Override
    public AdapterDlaDzialUseraZapisane.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AdapterDlaDzialUseraZapisane.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.dla_dzial_user, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDlaDzialUseraZapisane.MyViewHolder myViewHolder, final int i) {
        final String email = moduls.get(i).getMailUser();
        final String time = moduls.get(i).getTimeModul();
        final String nazwa = moduls.get(i).getNazwaModul();
        final String id2= moduls.get(i).getIdModul();
        myViewHolder.emailUser.setText(email);
        myViewHolder.time.setText(time);
        myViewHolder.nazwa.setText(nazwa);
        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);

                aBuilder.setMessage("Co robimy?")
                        .setCancelable(true)
                        .setNegativeButton("Usunąć", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                myRef = FirebaseDatabase.getInstance().getReference().child("ObliczeniaZapisany");
                                myRef.child(id2).removeValue();
                            }
                        });
                AlertDialog alert = aBuilder.create();
//                alert.setTitle("Usunąć?");
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView emailUser;
        public TextView nazwa;
        public TextView time;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            emailUser = itemView.findViewById(R.id.dla_dzial_user1);
            nazwa = itemView.findViewById(R.id.dla_dzial_user2);
            time = itemView.findViewById(R.id.dla_dzial_user3);
            layout= itemView.findViewById(R.id.dla_dzial_user0);

        }
    }
}

