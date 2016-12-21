package cc.com.example.work.chevaux;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Fragment sans interface pour stocker les chevaux, par DAVID Florian
 */

public class HorsesFragment extends Fragment {
    private int raceLength; // Nombre de mètres (au moins 20 pour ne pas avoir une course trop rapide)
    private int nbHorses;

    ArrayList<Horse> horses;

    interface TaskCallback {
        void onItemUpdate(int pos);
        void onItemDone(int pos);
    }
    public TaskCallback _MainActivityListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        raceLength= 0;
        nbHorses= 0;

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        _MainActivityListener = (TaskCallback)context;
    }


    @Override
    public void onDetach() {
        Log.v("Horses Race", "onDetach//");
        _MainActivityListener = null;
        super.onDetach();
    }

    public void setRaceLength(int raceLength) {
        this.raceLength = raceLength;
    }

    public void setNbHorses(int nbHorses) {
        this.nbHorses = nbHorses;
    }

    public int getNbHorses() {
        return nbHorses;
    }

    public Horse getHorse(int pos) {
        return horses.get(pos);
    }

    private void addHorse(int pos) {
        horses.add(new Horse());
        HorseTask horseTask= new HorseTask(this, pos);
    }

    public void updateRace() {
        horses.clear();

        for(int i= 0; i < nbHorses; ++i) {
            addHorse(i);
        }
    }

    public void launchRace() {
        Log.v("Horse Race", "launchRace//Course démarrée");
        for(int i= 0; i < nbHorses; ++i) {
            HorseTask task = new HorseTask(this, i);
            //        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item);
//            task.execute(adapter.getItem(i));
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, horses.get(i));
        }
    }

    protected void updateProgress(int pos, int meters) {
        Horse item = horses.get(pos);
        item.setProgress((meters *100) / raceLength);
        if(_MainActivityListener != null){
            _MainActivityListener.onItemUpdate(pos);
        }
    }

    protected void postExecute(int pos){
        Horse item = horses.get(pos);
        if(_MainActivityListener != null){
            _MainActivityListener.onItemDone(pos);
        }
    }
}
