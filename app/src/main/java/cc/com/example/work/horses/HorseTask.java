package cc.com.example.work.horses;

/**
 * AsyncTask d'un cheval, par DAVID Florian
 */

import android.os.AsyncTask;
import android.util.Log;

import java.util.Random;


public class HorseTask extends AsyncTask<Horse, Integer, Void> {
    public HorsesFragment parent;
    private int pos;

    public HorseTask (HorsesFragment _context, int _pos){
        parent= _context;
        pos= _pos;
    }

    @Override
    protected Void doInBackground(Horse... horses) {

//        for (Horse horse : horses) {

            Horse horse = horses[0];
            int minStep = 2;
            int maxStep = 4 + 1;
            Random r = new Random();

            while (horse.getProgress() < 100) {
                int step = r.nextInt(maxStep - minStep) + minStep;

                if (horse.getNb_boost_step() > 0) {
                    horse.decreaseBoostStep();
                    Log.v("HorseTask", "doInBackground//Le cheval " + pos + " avance de 3 * " + step +
                            ". Il était rendu à " + horse.getMeters() + "m ("+ horse.getProgress() +"%)");
                    horse.run(step * 3);
                } else {
                    Log.v("HorseTask", "doInBackground//Le cheval " + pos + " avance de " + step +
                            ". Il était rendu à " + horse.getMeters() + "m ("+ horse.getProgress() +"%)");
                    horse.run(step);
                }

                publishProgress(horse.getMeters());

                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//        }

        return (null);
    }

    @Override
    protected void onProgressUpdate(Integer... meters) {
        super.onProgressUpdate();
        parent.updateProgress(pos, meters[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        parent.postExecute(pos);
        Log.d("HorseTask", "onPostExecute//Le cheval " + pos + " est arrivé");

        super.onPostExecute(aVoid);
    }
}
