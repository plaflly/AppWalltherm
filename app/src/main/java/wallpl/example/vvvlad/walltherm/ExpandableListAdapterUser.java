package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import wallpl.example.vvvlad.walltherm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static wallpl.example.vvvlad.walltherm.MainActivity.typeface;

public class ExpandableListAdapterUser extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, ArrayList<Sklepy>> listHashMap;
    private int rew = 1;

    public ExpandableListAdapterUser(Context context, List<String> listDataHeader, HashMap<String, ArrayList<Sklepy>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lisy_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lbListHeader);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTypeface(typeface);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final Sklepy skleps = (Sklepy) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView txtListChild = convertView.findViewById(R.id.lblListItem);
        TextView miasto = convertView.findViewById(R.id.miasto);
        TextView ulica = convertView.findViewById(R.id.ulica);
        TextView telefon = convertView.findViewById(R.id.telefon);
        final TextView telefon1 = convertView.findViewById(R.id.telefon1);
        final TextView telefon2 = convertView.findViewById(R.id.telefon2);
        txtListChild.setText("Firma:  " + skleps.getNazwa());
        txtListChild.setTypeface(typeface);
        miasto.setText("Miasto:  " + skleps.getMiasto());
        miasto.setTypeface(typeface);
        ulica.setText("Ulica:  " + skleps.getUlica());
        ulica.setTypeface(typeface);
        telefon.setText("Telefon:  " + skleps.getTelefon());
        telefon.setTypeface(typeface);
        if (!TextUtils.isEmpty(skleps.getTelefon1()) && !skleps.getTelefon1().equals("null")) {
            telefon1.setText("Telefon:  " + skleps.getTelefon1());
            telefon1.setTypeface(typeface);
        } else {
            telefon1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(skleps.getTelefon2()) && !skleps.getTelefon2().equals("null")) {
            telefon2.setText("Telefon:  " + skleps.getTelefon2());
            telefon2.setTypeface(typeface);
        } else {
            telefon2.setVisibility(View.GONE);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, EditSklep.class);
//                intent.putExtra("status", skleps.getStatus());
//                intent.putExtra("adres", skleps.getAdres());
//                intent.putExtra("ID", skleps.getiD());
//                intent.putExtra("miasto", skleps.getMiasto());
//                intent.putExtra("ulica", skleps.getUlica());
//                intent.putExtra("firma", skleps.getNazwa());
//                intent.putExtra("telefon", skleps.getTelefon());
//                intent.putExtra("wojew", skleps.getWojewodstwo());
//                context.startActivity(intent);
//                ((SklepForAdmin)context).finish();

                rew = 1;
                //              AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
                final String telefon = skleps.getTelefon();
                final String telefon1 = skleps.getTelefon1();
                final String telefon2 = skleps.getTelefon2();
                String firma = skleps.getNazwa();
                String miasto = skleps.getMiasto();
                String ulica = skleps.getUlica();
                String masage = "Miasto: " + miasto + "\n" + "Ulica: " + ulica + "\n" + "Telefon 1: " + telefon;
                if (!TextUtils.isEmpty(skleps.getTelefon1()) && !skleps.getTelefon1().equals("null")) {
                    masage = masage + "\n" + "Telefon 2: " + telefon1;
                    rew = 2;
                }
                if (!TextUtils.isEmpty(skleps.getTelefon2()) && !skleps.getTelefon2().equals("null")) {
                    masage = masage + "\n" + "Telefon 3: " + telefon2;
                    rew = 3;
                }
                dialogEdit(firma, telefon, telefon1, telefon2, masage);
            }
        });

        return convertView;
    }

    private void dialogEdit(String firma, final String telefon, final String telefon1, final String telefon2, String masage) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    //    final AlertDialog dialog = dialogBuilder.create();
        //LayoutInflater inflater = this.context.getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_sklep, null);
        dialogBuilder.setView(dialogView);
        final Button buttonSklepTel1 = dialogView.findViewById(R.id.buttonSklepTel1);
        final Button buttonSklepTel2 = dialogView.findViewById(R.id.buttonSklepTel2);
        buttonSklepTel2.setVisibility(View.GONE);
        final Button buttonSklepTel3 = dialogView.findViewById(R.id.buttonSklepTel3);
        buttonSklepTel3.setVisibility(View.GONE);
        dialogBuilder.setTitle(firma);
        dialogBuilder.setMessage(masage);
        buttonSklepTel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + telefon));
                context.startActivity(intent);
            }
        });
        if(rew>=2){
            buttonSklepTel2.setVisibility(View.VISIBLE);
            buttonSklepTel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + telefon1));
                    context.startActivity(intent);
                }
            });
        }
        if (rew==3){
            buttonSklepTel3.setVisibility(View.VISIBLE);
            buttonSklepTel3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + telefon2));
                    context.startActivity(intent);
                }
            });
        }


        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
