package cc.com.example.work.chevaux;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Activité principale, par DAVID Florian
 * Un cheval peut avoir un boost, mais l'application de donne pas la possibilité d'en donner.
 * J'ai donc mis un boost au départ (2 tours)
 */

public class MainActivity extends ListActivity implements HorsesFragment.TaskCallback {
    private HorseAdapter adapter;
    private HorsesFragment horsesFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter= new HorseAdapter(this);
        /// Ajout de deux chevaux
//        nbHorses= 2;
//        adapter.add(new Horse());
//        adapter.add(new Horse());

        android.app.FragmentManager fm = getFragmentManager();

        horsesFragment = (HorsesFragment) fm.findFragmentByTag("tasks_saved");

        if (horsesFragment == null) {
            horsesFragment = new HorsesFragment();
            fm.beginTransaction().add(horsesFragment, "tasks_saved").commit();
            horsesFragment.horses = new ArrayList<>();
        } else {
            updateAdapter();
        }

        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Bouton de lancement grisé ou non
        MenuItem launchButton= menu.findItem(R.id.launch_race);
        if (horsesFragment.getNbHorses() > 0) {
            launchButton.setEnabled(true);
        } else launchButton.setEnabled(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {


        super.onListItemClick(l, v, position, id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.inscriptions:
                inscriptions();
                break;
            case R.id.launch_race:
                launchRace();
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemUpdate(int pos) {
        Log.v("Download Manager_Main", "onItemUpdate//maj " + pos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDone(int pos) {
        Log.v("Horse race", "onItemDone//cheval numéro "+ pos +" arrivé");
        Toast t = Toast.makeText(this,
                "Le cheval numéro "+ pos +" est arrivé !", Toast.LENGTH_LONG);
        t.show();
        adapter.notifyDataSetChanged();
    }

    private void inscriptions() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View alertDialogView = inflater.inflate(R.layout.alertdialog,
                null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);

//        TextView txtAlert = (TextView) alertDialogView
//                .findViewById(R.id.viewAlert);
//                txtAlert.setText();

        adb.setTitle(R.string.alert_new_race);


        // comment vérifier que le fichier a bien été crée pour renvoyer un
        // booléen
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText nbHorsesEdit = (EditText) alertDialogView
                        .findViewById(R.id.nb_horses_input);

                EditText raceLengthEdit = (EditText) alertDialogView
                        .findViewById(R.id.race_length_input);

                horsesFragment.setNbHorses(Integer.parseInt(nbHorsesEdit.getText().toString()));
                horsesFragment.setRaceLength(Integer.parseInt(raceLengthEdit.getText().toString()));

                updateRace();
//                Toast t = Toast.makeText(this,
//                        "", Toast.LENGTH_LONG);
//                t.show();
            }
        });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    private void launchRace() {
        horsesFragment.launchRace();
    }

    private void updateAdapter() {
        adapter.clear();
        for(int i= 0; i < horsesFragment.getNbHorses(); ++i) {
            adapter.add(horsesFragment.getHorse(i));
        }
        adapter.notifyDataSetChanged();
    }
    private void updateRace() {

        horsesFragment.updateRace();

        updateAdapter();

        invalidateOptionsMenu();
    }
}
