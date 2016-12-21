package cc.com.example.work.chevaux;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Adapteur de chevaux, par DAVID Florian
 */

public class HorseAdapter extends ArrayAdapter<Horse> {


    HorseAdapter(Context context) {
        super(context, R.layout.horse_line);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Horse item = getItem(position);

        if (convertView == null) {
            //	Inflate la vue
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.horse_line, parent, false);
        }

        ImageView horseImage = (ImageView) convertView.findViewById(R.id.horse_image);
        ProgressBar horseProgress = (ProgressBar) convertView.findViewById(R.id.horse_progress);

        horseImage.setImageResource(R.drawable.image_cheval);
        horseProgress.setProgress(item.getProgress());
        if (item.getNb_boost_step() > 0) {
            horseProgress.setBackgroundColor(Color.RED);
        } else {
            horseProgress.setBackgroundColor(Color.DKGRAY);
        }

        return convertView;
    }
}
