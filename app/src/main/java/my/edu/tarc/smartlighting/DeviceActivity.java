package my.edu.tarc.smartlighting;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DeviceActivity extends AppCompatActivity {
    public final static String PI = "pi";
    private TextView textViewDeviceName, textViewMacAddress, textViewStatusA, textViewStatusB;
    private TextView textViewA, textViewB, textViewUpTimeA, textViewUpTimeB;
    private Button buttonConnHistoryA, getButtonConnHistoryB;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        final String deviceName, macAddress;
        textViewDeviceName = (TextView) findViewById(R.id.textViewDeviceName);
        textViewMacAddress = (TextView) findViewById(R.id.textViewMacAddress);
        textViewStatusA = (TextView) findViewById(R.id.textViewStatusA);
        textViewStatusB = (TextView) findViewById(R.id.textViewStatusB);
        textViewA = (TextView) findViewById(R.id.textViewA);
        textViewB = (TextView) findViewById(R.id.textViewB);
        textViewUpTimeA = (TextView) findViewById(R.id.textViewUpTimeA);
        textViewUpTimeB = (TextView) findViewById(R.id.textViewUpTimeB);

        Intent intent = getIntent();
        deviceName = intent.getStringExtra(MainActivity.DEVICE_NAME);
        macAddress = intent.getStringExtra(MainActivity.MAC_ADDRESS);
        textViewDeviceName.setText(getString(R.string.device_name) + deviceName);
        textViewMacAddress.setText(getString(R.string.mac_address)+ macAddress);

        mRef = FirebaseDatabase.getInstance().getReference();
        Query lastQueryA = mRef.child("Pi A").child(macAddress).orderByKey().limitToLast(1);

        lastQueryA.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String strConnectedTime, strDisconnectedTime;
                Calendar connectedTime;
                int year, month, day, hour, minute, second;
                for (DataSnapshot obj : dataSnapshot.getChildren()){
                    strConnectedTime = obj.child("connectedTime").getValue(String.class);
                    strDisconnectedTime = obj.child("disconnectedTime").getValue(String.class);
                    if (strDisconnectedTime.isEmpty()){
                        textViewStatusA.setText("Connected");
                        textViewStatusA.setTextColor(Color.parseColor("#006400"));
                    } else {
                        textViewStatusA.setText("Disconnected");
                        textViewStatusA.setTextColor(Color.parseColor("#ffcc0000"));
                    }
                    if (!strConnectedTime.isEmpty() && strDisconnectedTime.isEmpty()){
                        year = 2000 + Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[2]);
                        month = Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[0]) - 1;
                        day = Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[1]);
                        hour = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[0]);
                        minute = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[1]);
                        second = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[2]);
                        connectedTime = new GregorianCalendar(year, month, day, hour, minute, second);
                        long upTimeInSeconds = (new GregorianCalendar().getTimeInMillis() - connectedTime.getTimeInMillis()) / 1000;
                        textViewA.setVisibility(View.VISIBLE);
                        textViewUpTimeA.setVisibility(View.VISIBLE);
                        int seconds, minutes, hours ;
                        minutes = (int) upTimeInSeconds / 60;
                        seconds = (int) upTimeInSeconds % 60;
                        hours = (int) minutes / 60;
                        minutes = (int) minutes % 60;
                        if (hours == 0){
                            if (minutes == 0){
                                if (seconds == 0){
                                    textViewUpTimeA.setText("-");
                                } else {
                                    textViewUpTimeA.setText(seconds + " seconds");
                                }
                            } else {
                                textViewUpTimeA.setText(minutes + " minutes " + seconds + " seconds");
                            }
                        } else {
                            textViewUpTimeA.setText(hours + " hour " + minutes + " minutes " + seconds + " seconds");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query lastQueryB = mRef.child("Pi B").child(macAddress).orderByKey().limitToLast(1);
        lastQueryB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String strConnectedTime, strDisconnectedTime;
                Calendar connectedTime;
                int year, month, day, hour, minute, second;
                for (DataSnapshot obj : dataSnapshot.getChildren()){
                    strConnectedTime = obj.child("connectedTime").getValue(String.class);
                    strDisconnectedTime = obj.child("disconnectedTime").getValue(String.class);
                    if (strDisconnectedTime.isEmpty()){
                        textViewStatusB.setText("Connected");
                        textViewStatusB.setTextColor(Color.parseColor("#006400"));
                    } else {
                        textViewStatusB.setText("Disconnected");
                        textViewStatusB.setTextColor(Color.parseColor("#ffcc0000"));
                    }
                    if (!strConnectedTime.isEmpty() && strDisconnectedTime.isEmpty()){
                        year = 2000 + Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[2]);
                        month = Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[0]) - 1;
                        day = Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[1]);
                        hour = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[0]);
                        minute = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[1]);
                        second = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[2]);
                        connectedTime = new GregorianCalendar(year, month, day, hour, minute, second);
                        long upTimeInSeconds = (new GregorianCalendar().getTimeInMillis() - connectedTime.getTimeInMillis()) / 1000;
                        textViewB.setVisibility(View.VISIBLE);
                        textViewUpTimeB.setVisibility(View.VISIBLE);
                        int seconds, minutes, hours ;
                        minutes = (int) upTimeInSeconds / 60;
                        seconds = (int) upTimeInSeconds % 60;
                        hours = (int) minutes / 60;
                        minutes = (int) minutes % 60;
                        if (hours == 0){
                            if (minutes == 0){
                                if (seconds == 0){
                                    textViewUpTimeB.setText("-");
                                } else {
                                    textViewUpTimeB.setText(seconds + " seconds");
                                }
                            } else {
                                textViewUpTimeB.setText(minutes + " minutes " + seconds + " seconds");
                            }
                        } else {
                            textViewUpTimeB.setText(hours + " hour " + minutes + " minutes " + seconds + " seconds");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button buttonConnHistoryA = (Button) findViewById(R.id.buttonConnHistoryA);
        buttonConnHistoryA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeviceActivity.this, "Pi A - " + macAddress, Toast.LENGTH_LONG).show();
                Intent intentA = new Intent(getApplicationContext(), ConnectionHistoryActivity.class);
                intentA.putExtra(DeviceActivity.PI, "Pi A");
                intentA.putExtra(MainActivity.MAC_ADDRESS, macAddress);
                startActivity(intentA);
            }
        });

        Button buttonConnHistoryB = (Button) findViewById(R.id.buttonConnHistoryB);
        buttonConnHistoryB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeviceActivity.this, "Pi B - " + macAddress, Toast.LENGTH_LONG).show();
                Intent intentB = new Intent(getApplicationContext(), ConnectionHistoryActivity.class);
                intentB.putExtra(DeviceActivity.PI, "Pi B");
                intentB.putExtra(MainActivity.MAC_ADDRESS, macAddress);
                startActivity(intentB);
            }
        });
    }
}
