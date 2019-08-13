package wallpl.example.vvvlad.walltherm.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.MainActivity;
import wallpl.example.vvvlad.walltherm.Sklepy;

import wallpl.example.vvvlad.walltherm.R;

import java.util.ArrayList;

public class AdapterSklep extends RecyclerView.Adapter<AdapterSklep.MyViewHolder>{
    private Context context;
    private ArrayList<Sklepy> sklepy;

    AdapterSklep(Context c, ArrayList<Sklepy> p) {
        context = c;
        sklepy = p;
    }

    @NonNull
    @Override
    public AdapterSklep.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new AdapterSklep.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.lista, parent, false));
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AdapterSklep.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.firma.setText("Firma: " + sklepy.get(position).getNazwa());
        holder.telefon.setText("Telefon: " + sklepy.get(position).getTelefon());
        holder.miasto.setText("Miasto: " + sklepy.get(position).getMiasto());
        holder.ulica.setText("Ulica: " + sklepy.get(position).getUlica());
        holder.wojewodstwo.setText("Wojewodstwo: " + sklepy.get(position).getWojewodstwo());

        holder.telefon.setTypeface(MainActivity.typeface);
        holder.miasto.setTypeface(MainActivity.typeface);
        holder.ulica.setTypeface(MainActivity.typeface);
        holder.wojewodstwo.setTypeface(MainActivity.typeface);
        holder.firma.setTypeface(MainActivity.typeface);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                dialog(position);
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

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            wojewodstwo = (TextView) itemView.findViewById(R.id.wojew);
            firma = (TextView) itemView.findViewById(R.id.firma);
            telefon = (TextView) itemView.findViewById(R.id.telefon);
            miasto = (TextView) itemView.findViewById(R.id.miasto);
            ulica = (TextView) itemView.findViewById(R.id.ulica);
            layout = itemView.findViewById(R.id.layoutLista);
        }
    }
    private void dialog(int i){

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        final String telefon = sklepy.get(i).getTelefon();
        String firma = sklepy.get(i).getNazwa();
        String miasto = sklepy.get(i).getMiasto();
        String ulica = sklepy.get(i).getUlica();

        aBuilder.setMessage("Miasto: "+miasto+"\n"+"Ulica: "+ ulica +"\n"+"Telefon: "+telefon)
                .setCancelable(true)
                .setPositiveButton("Zadzwonić", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+telefon));
                        context.startActivity(intent);

                    }
                });
//                .setNegativeButton("Zniszczyć", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//
//                    }
//                });
        AlertDialog alert = aBuilder.create();
        alert.setTitle("Firma: "+firma);
        alert.show();


    }
}
