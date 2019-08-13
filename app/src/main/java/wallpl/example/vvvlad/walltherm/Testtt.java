package wallpl.example.vvvlad.walltherm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import wallpl.example.vvvlad.walltherm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class Testtt extends AppCompatActivity {
    Button qweqwe;
    private DatabaseReference userreference;
    FirebaseAuth mAuth;
    private DatabaseReference notificationReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testtt);
        userreference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        String online_user__id = mAuth.getCurrentUser().getUid();
        final String senger_user__id = mAuth.getCurrentUser().getUid();
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        final String recived_user_id = "BXM0t16zlkhQ2NvvgEI4GxWZ2t03";

        userreference.child(online_user__id).child("device_token").setValue(deviceToken);
        notificationReference = FirebaseDatabase.getInstance().getReference().child("UsersA");
        notificationReference.keepSynced(true);

        qweqwe = findViewById(R.id.qweqwe);
        qweqwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> notificationData = new HashMap<String, String>();
                notificationData.put("from", senger_user__id );
                notificationData.put("type", "rever");
                notificationReference.child(recived_user_id).push().setValue(notificationData);

            }
        });
    }
}
