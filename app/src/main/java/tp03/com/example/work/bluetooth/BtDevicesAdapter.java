package tp03.com.example.work.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewHolder for a bluetooth device
 */
class BluetoothViewHolder{

    private ImageView device_image;
    private TextView device_name;
    private TextView hardware_address;

    BluetoothViewHolder(View view){
        device_image = (ImageView) view.findViewById(R.id.device_image);
        device_name = (TextView) view.findViewById(R.id.device_name);
        hardware_address = (TextView) view.findViewById(R.id.hardware_address);
    }

    void setDevice_image(int device_imageId) {
        this.device_image.setImageResource(device_imageId);
    }

    void setDevice_name(String device_name) {
        this.device_name.setText(device_name);
    }

    void setHardware_address(String hardware_address) {
        this.hardware_address.setText(hardware_address);
    }

    void modifyAlpha(int bond_state) {
        if (bond_state == BluetoothDevice.BOND_BONDED) {
            this.device_name.setAlpha((float)0.4);
        } else if (bond_state == BluetoothDevice.BOND_BONDING) {
            this.device_name.setAlpha((float)0.6);
            this.device_image.setBackgroundColor(Color.BLUE);
        } else {
            if (bond_state == BluetoothDevice.BOND_NONE) {
                this.device_name.setBackgroundColor(Color.YELLOW);
            }
            this.device_name.setAlpha(1);
        }
    }
}

/**
 * Adapter for bluetooth devices
 */

class BtDevicesAdapter extends ArrayAdapter<BluetoothDevice> {

    BtDevicesAdapter(Context context) {
        super(context, R.layout.bluetooth_line);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice item= getItem(position);

        BluetoothViewHolder holder;
        if (convertView != null) {
            holder = (BluetoothViewHolder) convertView.getTag();
        } else {
            //	Inflate la vue
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.bluetooth_line, parent, false);

            holder = new BluetoothViewHolder(convertView);
            convertView.setTag(holder);
        }

        switch (item.getBluetoothClass().getDeviceClass()) {
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                holder.setDevice_image(android.R.drawable.stat_sys_speakerphone);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                holder.setDevice_image(android.R.drawable.stat_sys_headset);
                break;

            default :
                switch (item.getBluetoothClass().getMajorDeviceClass()) {
                    case BluetoothClass.Device.Major.AUDIO_VIDEO:
                        holder.setDevice_image(android.R.drawable.ic_media_play);
                        break;
                    case BluetoothClass.Device.Major.COMPUTER:
                        holder.setDevice_image(R.drawable.computer);
                        break;
                    case BluetoothClass.Device.Major.HEALTH:
                        break;
                    case BluetoothClass.Device.Major.IMAGING:
                        holder.setDevice_image(android.R.drawable.stat_sys_data_bluetooth);
                        break;
                    case BluetoothClass.Device.Major.NETWORKING:
                        break;
                    case BluetoothClass.Device.Major.PERIPHERAL:
                        holder.setDevice_image(android.R.drawable.stat_sys_data_bluetooth);
                        break;
                    case BluetoothClass.Device.Major.PHONE:
                        holder.setDevice_image(R.drawable.phone);
                        break;
                    case BluetoothClass.Device.Major.TOY:
                        break;
                    case BluetoothClass.Device.Major.WEARABLE:
                        break;
                    case BluetoothClass.Device.Major.UNCATEGORIZED:
                        System.out.println("Not any category");
                    default:
                        holder.setDevice_image(R.drawable.blank_file_icon);
                }
        }
        holder.setDevice_name(item.getName());
        holder.setHardware_address(item.getAddress());
        holder.modifyAlpha(item.getBondState());

        return convertView;
    }
}
