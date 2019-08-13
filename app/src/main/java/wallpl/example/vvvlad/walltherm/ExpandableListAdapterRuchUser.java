package wallpl.example.vvvlad.walltherm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static wallpl.example.vvvlad.walltherm.MainActivity.typeface;

class ExpandableListAdapterRuchUser  extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, ArrayList<Profile>> listHashMap;
    private int rew = 1;

    public ExpandableListAdapterRuchUser(Context context, List<String> listDataHeader, HashMap<String, ArrayList<Profile>> listHashMap) {
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
            convertView = inflater.inflate(R.layout.list_group2, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lbListHeader);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTypeface(typeface);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final Profile profile = (Profile) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_ruch_user, null);
        }
        TextView list_ruch_user_email = convertView.findViewById(R.id.list_ruch_user_email);
        TextView list_ruch_user_data = convertView.findViewById(R.id.list_ruch_user_data);
        TextView list_ruch_user_firma = convertView.findViewById(R.id.list_ruch_user_firma);
        TextView list_ruch_user_telefon = convertView.findViewById(R.id.list_ruch_user_telefon);

        list_ruch_user_email.setText("Email:  " + profile.getEmail());
        list_ruch_user_email.setTypeface(typeface);
        list_ruch_user_firma.setText("Firma:  " + profile.getFirma());
        list_ruch_user_firma.setTypeface(typeface);
        list_ruch_user_telefon.setText("Telefon:  " + profile.getTelefon());
        list_ruch_user_telefon.setTypeface(typeface);
        list_ruch_user_data.setText(profile.getDataEnt());
        list_ruch_user_data.setTypeface(typeface);

        final String userID = profile.getId();
        final String status = profile.getStatus();
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UsersAkt2.class);
//                intent.putExtra("userid", userID);
//                intent.putExtra("users", status);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
