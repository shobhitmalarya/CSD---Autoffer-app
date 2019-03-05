package com.csd.shobhit.csd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ProgressBar;
import android.net.wifi.ScanResult;
import android.view.View;
import android.content.BroadcastReceiver;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Profile extends AppCompatActivity {

    private TextView mTextMessage;
    FirebaseAuth mAuth;
    String ssid;
    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView wifilist;
    ListView offerlistView;
    ListView NearbylistView;
    String wifis[];
    EditText pass;
    LinearLayout WifiView;
    LinearLayout offersView;
    LinearLayout nearbyView;
    LinearLayout QrView;
    DatabaseReference ref;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_wifi:
                    WifiView.setVisibility(View.VISIBLE);
//                    scan_btn.setVisibility(View.VISIBLE);
                    offersView.setVisibility(View.GONE);
                    nearbyView.setVisibility(View.GONE);
                    QrView.setVisibility(View.GONE);

                    //mTextMessage.setText(R.string.title_wifi);
                    return true;
                case R.id.navigation_offers:
                    WifiView.setVisibility(View.GONE);
//                    scan_btn.setVisibility(View.GONE);
                    offersView.setVisibility(View.VISIBLE);
                    nearbyView.setVisibility(View.GONE);
                    QrView.setVisibility(View.GONE);
                    //mTextMessage.setText(R.string.title_offers);
                    return true;
                case R.id.navigation_nearby:
                    WifiView.setVisibility(View.GONE);
//                    scan_btn.setVisibility(View.GONE);
                    offersView.setVisibility(View.GONE);
                    nearbyView.setVisibility(View.VISIBLE);
                    QrView.setVisibility(View.GONE);
                    //mTextMessage.setText(R.string.title_nearby);
                    return true;
                case R.id.navigation_qr:
                    WifiView.setVisibility(View.GONE);
//                    scan_btn.setVisibility(View.GONE);
                    offersView.setVisibility(View.GONE);
                    nearbyView.setVisibility(View.GONE);
                    QrView.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    private HashMap<String, DataSnapshot> offers;
    private HashMap<String, DataSnapshot> history;

    private ChildEventListener offerListener;
    private ChildEventListener historyListener;

    private DatabaseReference offerRef;
    private DatabaseReference historyRef;

    private ArrayList<String> finalOffers;
    private ArrayAdapter<String> offerAdapter;

    private ArrayList<String> nearbyOffers;
    private ArrayAdapter<String> nearbyOffersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        WifiView = findViewById(R.id.WifiView);
        offersView = findViewById(R.id.offersView);
        offerlistView = findViewById(R.id.offerList);
        nearbyView = findViewById(R.id.nearbyView);
        NearbylistView = findViewById(R.id.NearofferList);
//        scan_btn = findViewById(R.id.scan_b);
        QrView = findViewById(R.id.qrLayout);
        TextView ssids = findViewById(R.id.ssids);
        ssids.setText(ssid);
        wifilist = (ListView) findViewById(R.id.wlist);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        offersView.setVisibility(View.GONE);
        nearbyView.setVisibility(View.GONE);
        QrView.setVisibility(View.GONE);

        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();

        ref = FirebaseDatabase.getInstance().getReference("offers");

        final ArrayList<String> nearbyofferlist = new ArrayList<>();

        finalOffers = new ArrayList<>();
        offerAdapter = new ArrayAdapter<>(Profile.this, R.layout.offerlist_item, R.id.label, finalOffers);
        offerlistView.setAdapter(offerAdapter);

        offers = new HashMap<>();
        offerRef = FirebaseDatabase.getInstance().getReference("offers");
        offerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                offers.put(dataSnapshot.getKey(), dataSnapshot);
                notifyDataChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                offers.put(dataSnapshot.getKey(), dataSnapshot);
                notifyDataChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                offers.remove(dataSnapshot.getKey());
                notifyDataChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                offers.put(dataSnapshot.getKey(), dataSnapshot);
                notifyDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(Profile.class.toString(), databaseError.getMessage());
            }
        };

        history = new HashMap<>();
        historyRef = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getUid() + "/history");
        historyListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                history.put(dataSnapshot.getKey(), dataSnapshot);
                notifyDataChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                history.put(dataSnapshot.getKey(), dataSnapshot);
                notifyDataChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                history.remove(dataSnapshot.getKey());
                notifyDataChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                history.put(dataSnapshot.getKey(), dataSnapshot);
                notifyDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(Profile.class.toString(), databaseError.getMessage());
            }
        };

        nearbyOffers = new ArrayList<>();
        nearbyOffersAdapter = new ArrayAdapter<>(Profile.this, R.layout.list_item, R.id.label, nearbyOffers);
        NearbylistView.setAdapter(nearbyOffersAdapter);

        // listening to single list item on click
        wifilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String ssid = ((TextView) view).getText().toString();
                connectToWifi(ssid);
                filterOnSsid();
                Toast.makeText(Profile.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();

            }
        });

        FirebaseDatabase.getInstance().getReference("users/" + mAuth.getUid() + "/emailId").setValue(mAuth.getCurrentUser().getEmail());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                filterOnSsid();
            }
        }, 0, 10);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(Profile.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        offerRef.addChildEventListener(offerListener);
        historyRef.addChildEventListener(historyListener);
    }

    public void signOut(View view) {

        mAuth.signOut();
        Intent intent = new Intent(Profile.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void filterOnSsid() {

        WifiInfo wifiInfo;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)
            ssid = wifiInfo.getSSID();
        else
            ssid = null;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView ssids = findViewById(R.id.ssids);
                if (ssid == null)
                    ssids.setText("Disconnected.");
                else if (ssid.equals("<unknown ssid>"))
                    ssids.setText("Please enable location");
                else
                    ssids.setText("Connected to: " + ssid);
            }
        });
    }

    protected void onPause() {
        super.onPause();

        unregisterReceiver(wifiReciever);

        offerRef.removeEventListener(offerListener);
        historyRef.removeEventListener(historyListener);
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {

            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];
            for (int i = 0; i < wifiScanList.size(); i++) {
                wifis[i] = ((wifiScanList.get(i)).toString());
            }
            String filtered[] = new String[wifiScanList.size()];
            int counter = 0;
            filterOnSsid();
            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");

                filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength

                counter++;

            }
            wifilist.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, R.id.label, filtered));

            nearbyOffers.clear();
            for (DataSnapshot offerSnap : offers.values()) {
                Offer offer = offerSnap.getValue(Offer.class);

                for(String nearbySsid: filtered) {
                    if (nearbySsid != null && offer.getBrand().equals(nearbySsid))
                        nearbyOffers.add(offer.getValue() + " by " + offer.getBrand());
                }
            }
            nearbyOffersAdapter.notifyDataSetChanged();
        }

    }

    private void finallyConnect(String networkPass, String networkSSID) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        // remember id
        int netId = mainWifiObj.addNetwork(wifiConfig);
        mainWifiObj.disconnect();
        mainWifiObj.enableNetwork(netId, true);
        mainWifiObj.reconnect();

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"\"" + networkSSID + "\"\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        mainWifiObj.addNetwork(conf);
    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connect);
        dialog.setTitle("Connect to Network");
        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);

        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        pass = (EditText) dialog.findViewById(R.id.textPassword);
        textSSID.setText(wifiSSID);

        // if button is clicked, connect to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkPassword = pass.getText().toString();
                finallyConnect(checkPassword, wifiSSID);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void notifyDataChanged() {

        finalOffers.clear();
        for (DataSnapshot offerSnap : offers.values()) {

            Offer offer = offerSnap.getValue(Offer.class);
            for (DataSnapshot historySnap : history.values()) {
                History history = historySnap.getValue(History.class);

                if (offer.getCategory().equals(history.getCategory()))
                    finalOffers.add(offer.getValue() + " by " + offer.getBrand());
            }
        }
        offerAdapter.notifyDataSetChanged();
    }
}

