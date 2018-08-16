package my.edu.tarc.smartlighting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import my.edu.tarc.smartlighting.Model.Connection;
import my.edu.tarc.smartlighting.Model.ConnectionAdapter;

public class ConnectionHistoryActivity extends AppCompatActivity {
    private ListView listViewConn;
    private List<Connection> connectionList;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_history);

        listViewConn = (ListView) findViewById(R.id.listViewConn);
        connectionList = new ArrayList<>();

        String pi, macAddress;
        Intent intent = getIntent();
        pi = intent.getStringExtra(DeviceActivity.PI);
        macAddress = intent.getStringExtra(MainActivity.MAC_ADDRESS);

        mRef = FirebaseDatabase.getInstance().getReference();
        Query descQuery = mRef.child(pi).child(macAddress).orderByKey();

        descQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GregorianCalendar connectedTime, disconnectedTime;
                String strConnectedTime, strDisconnectedTime;
                int year, month, day, hour, minute, second;
                for (DataSnapshot conn : dataSnapshot.getChildren()){
                    strConnectedTime = conn.child("connectedTime").getValue(String.class);
                    if (!strConnectedTime.isEmpty()){
                        year = 2000 + Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[2]);
                        month = Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[0]) - 1;
                        day = Integer.parseInt(strConnectedTime.split(" ")[0].split("/")[1]);
                        hour = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[0]);
                        minute = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[1]);
                        second = Integer.parseInt(strConnectedTime.split(" ")[1].split(":")[2]);
                        connectedTime = new GregorianCalendar(year, month, day, hour, minute, second);

                        strDisconnectedTime = conn.child("disconnectedTime").getValue(String.class);
                        if (!strDisconnectedTime.isEmpty()){
                            year = 2000 + Integer.parseInt(strDisconnectedTime.split(" ")[0].split("/")[2]);
                            month = Integer.parseInt(strDisconnectedTime.split(" ")[0].split("/")[0]) - 1;
                            day = Integer.parseInt(strDisconnectedTime.split(" ")[0].split("/")[1]);
                            hour = Integer.parseInt(strDisconnectedTime.split(" ")[1].split(":")[0]);
                            minute = Integer.parseInt(strDisconnectedTime.split(" ")[1].split(":")[1]);
                            second = Integer.parseInt(strDisconnectedTime.split(" ")[1].split(":")[2]);
                            disconnectedTime = new GregorianCalendar(year, month, day, hour, minute, second);

                            Connection tempConn = new Connection(connectedTime, disconnectedTime);
                            connectionList.add(tempConn);
                        } else {
                            Connection tempConn = new Connection(connectedTime);
                            connectionList.add(tempConn);
                        }
                    }
                }
                Collections.reverse(connectionList);
                if (connectionList.size() > 0){
                    ConnectionAdapter connectionAdapter = new ConnectionAdapter(getApplicationContext(), connectionList);
                    listViewConn.setAdapter(connectionAdapter);
                }
                listViewConn.setEmptyView(findViewById(R.id.textViewEmptyElement));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
