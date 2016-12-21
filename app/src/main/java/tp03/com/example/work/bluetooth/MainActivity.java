package tp03.com.example.work.bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main activity of the bluetooth app
 */

public class MainActivity extends Activity implements BluetoothCommunication{
    private Menu optionsMenu;
    public boolean refreshing;
    private BluetoothFragment fragmentBluetooth;

    // #####################################
    //      Overridden methods
    // ######################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshing = false;

        fragmentBluetooth = new BluetoothFragment();
        getFragmentManager().beginTransaction().add(R.id.activity_main, fragmentBluetooth).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_btdevices:
                Log.v("Bluetooth_MainActivity", "Appuie sur le bouton refresh");
                refreshing = !refreshing;
                setRefreshActionButtonState(refreshing);

                fragmentBluetooth.refreshBtDevices();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // #####################################
    //      New methods created
    // ######################################
    /**
     * Show a refresh circle in the menu bar
     * @param refreshing Si il faut activer ou non le rafraichissement
     */
    public void setRefreshActionButtonState(final boolean refreshing) {
        Log.d("Bluetooth_MainActivity","setRefreshActionButtonState//Rafraichissement");
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.refresh_btdevices);
            if (refreshItem != null) {
                if (refreshing) {
                    Log.d("Bluetooth_MainActivity","setRefreshActionButtonState//Debut Rafraichissement");
                    refreshItem.setActionView(R.layout.refresh_menu);
                } else {
                    refreshItem.setActionView(null);
                    Log.d("Bluetooth_MainActivity","setRefreshActionButtonState//Fin Rafraichissement");
                }
            }
        }
    }
}
