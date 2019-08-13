package wallpl.example.vvvlad.walltherm.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.Moduly.Modul;
import wallpl.example.vvvlad.walltherm.R;

import java.util.ArrayList;
import java.util.Collections;


public class AdapterModul extends RecyclerView.Adapter<AdapterModul.MyViewHolder> {
    private Context context;
    private ArrayList<Modul> moduls;


    public AdapterModul(Context c, ArrayList<Modul> m) {
        context = c;
        moduls = m;
        Collections.reverse(moduls);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_modul, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final String ilosc = moduls.get(position).getIloscModul();
        final String time = moduls.get(position).getTimeModul();
        String nazwamodulaUser = moduls.get(position).getNazwaModul();
        String iloscPunktowUser = moduls.get(position).getIloscPunkt();
        String statusText = moduls.get(position).getStatusModul();
        String textModulUser = moduls.get(position).getTextModul();
        String textForUs = moduls.get(position).getTextForUser();

        if (textForUs.equals("null")){
            holder.textForUser.setVisibility(View.GONE);
            holder.befortextForUser.setVisibility(View.GONE);
        }else{
            holder.textForUser.setText(textForUs);
        }
        //  System.out.println(statusText);
        holder.ilosc.setText("Ilosc: " + ilosc);
        holder.time.setText("Data:  " + time);
        holder.nazwamodulaUser.setText(nazwamodulaUser);
        holder.iloscPunktowUser.setText("Ilosc: " +iloscPunktowUser);
        if (iloscPunktowUser.equals("null")){
            holder.iloscPunktowUser.setText("Nie naliczono");
        }
        holder.textModulUser.setText(textModulUser);
        if (textModulUser.equals("null")){
            holder.textModulUser.setVisibility(View.GONE);
        }

        if (statusText == null) {
            holder.statusText.setText("Czeka na potwierdzenie");
            holder.layout.setBackgroundColor(Color.parseColor("#303F9F"));
        }

        if (statusText.equals("fal")) {
            holder.statusText.setText("Odrzucono");
            holder.layout.setBackgroundColor(Color.parseColor("#ff0400"));
        } else {
            if (statusText.equals("tr")) {
                holder.statusText.setText("Potwierdzone");
                holder.layout.setBackgroundColor(Color.parseColor("#229100"));
            }
        }


    }

    @Override
    public int getItemCount() {
        return moduls.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView ilosc;
        public TextView nazwamodulaUser;
        public TextView iloscPunktowUser;
        public TextView time;
        public TextView statusText;
        public TextView textModulUser;
        public TextView textForUser;
        public TextView befortextForUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.adaptertModulLayout);
            ilosc = itemView.findViewById(R.id.iloscmodUser);
            nazwamodulaUser = itemView.findViewById(R.id.nazwamodulaUser);
            iloscPunktowUser = itemView.findViewById(R.id.iloscPunktowUser);
            time = itemView.findViewById(R.id.dataUserModul);
            statusText = itemView.findViewById(R.id.statusModulUser);
            textModulUser = itemView.findViewById(R.id.textModulUser);
            textForUser= itemView.findViewById(R.id.textForUser);
            befortextForUser= itemView.findViewById(R.id.befortextForUser);
        }
    }


}
