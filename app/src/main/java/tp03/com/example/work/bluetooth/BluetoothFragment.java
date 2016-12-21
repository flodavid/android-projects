package tp03.com.example.work.bluetooth;

import android.app.Activity;
import android.app.ListFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Dynamic fragment that handle bluetooth connection
 */

public class BluetoothFragment extends ListFragment {
    private Activity parentActivity;
    private BroadcastReceiver btReceiver;
    private BtDevicesAdapter adapter;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    public void onStart() {
        btReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d("Bluetooth Manager", action);

                // When bluetooth is (de)activated
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    Toast t = Toast.makeText(parentActivity.getApplicationContext(),
                        "Changement de l'état du bluetooth", Toast.LENGTH_LONG);
                    t.show();
                    refreshBtDevices();
                }
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    adapter.add(device);

                    Notification notification =
                        new Notification.Builder(getActivity())
                                .setContentTitle("Bluetooth Manager")
                                .setContentText( "New bluetooth device found : " + device.getName())
                                    .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                                    .setAutoCancel(true)
                                    .build();
                    NotificationManager notificationManager = (NotificationManager) parentActivity.getSystemService(Activity.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, notification);
                }

//                Bundle extras = intent.getExtras();
//                if (extras != null) {
//                    String state = extras.getString(TelephonyManager.EXTRA_STATE);
//                    Log.w("MY_DEBUG_TAG", state);
//                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                        String phoneNumber = extras .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                        Log.w("MY_DEBUG_TAG", phoneNumber);
//                    }
//                }
            }
        };

        super.onStart();
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(btReceiver, filter);
        // ...
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister the BroadcastReceiver
        getActivity().unregisterReceiver(btReceiver);
        /// ...
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = getActivity();
        adapter = new BtDevicesAdapter(parentActivity); // getContext() would be better, but API23 required

        // Adds all previously paired devices to the list
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            Log.d("BluetoothM_onCreate", "bluetoothAdapter null, pas de bluetooth sur l'appareil");
        }

        getPairedDevices();

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setListAdapter(adapter);

        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        // Inflate the layout for this fragment

        TextView bluetoothState= (TextView)view.findViewById(R.id.bluetooth_state);
        if (bluetoothAdapter == null) {
            bluetoothState.setText("L'appareil ne dispose pas du bluetooth");
        } else if (bluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
            bluetoothState.setText("Le bluetooth n'est pas activé sur l'appareil");
        }

        return view;
    }

    public void getPairedDevices() {
        if (bluetoothAdapter != null) {
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            if (bondedDevices.size() > 0) {
                for (BluetoothDevice device : bondedDevices) {
                    adapter.add(device);
                    Log.v("Bluetooth_Fragment","onStart//Paired device : "+device.getName()+" "+
                            device.getBluetoothClass().getDeviceClass());
                }
            } else {
                TextView bluetoothState= (TextView)getView().findViewById(R.id.bluetooth_state);
                bluetoothState.setText("Aucun ancien périphérique trouvé");
            }
        }
    }

    public void refreshBtDevices() {
        adapter.clear();
        TextView bluetoothState= (TextView)getView().findViewById(R.id.bluetooth_state);
        bluetoothState.setText("Recherche des périphériques bluetooth");
        getPairedDevices();
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }
    }
}
