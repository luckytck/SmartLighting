package my.edu.tarc.smartlighting;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final static String MAC_ADDRESS = "mac address";
    public final static String DEVICE_NAME = "device name";
    private TextView textViewWelcome;
    private ListView listViewDevice;
    private ArrayList<String> mDeviceName;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        textViewWelcome.setText(getString(R.string.welcome) + ", " + user.getEmail());

        mRef = FirebaseDatabase.getInstance().getReference();
        mDeviceName = new ArrayList<>();
        listViewDevice = (ListView) findViewById(R.id.listViewDevice);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDeviceName);
        listViewDevice.setAdapter(arrayAdapter);

        mRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDeviceName.clear();
                for (DataSnapshot obj : dataSnapshot.getChildren()){
                    String macAddress = obj.getKey();
                    String name = obj.child("name").getValue(String.class);
                    mDeviceName.add(macAddress + " - " + name);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listViewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), DeviceActivity.class);
                intent.putExtra(MainActivity.DEVICE_NAME, item.split("-")[1].trim());
                intent.putExtra(MainActivity.MAC_ADDRESS, item.split("-")[0].trim());
                startActivity(intent);
            }
        });

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });



    }
}
