package tp01.com.example.work.explorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Listener du fragement
//        ListFiles list= (ListFiles) getFragmentManager().findFragmentById(R.id.list);
//        BackPressListener back_listener = new BackPressListener;
//        list.getView().setOnKeyListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        ListFiles list= (ListFiles) getFragmentManager().findFragmentById(R.id.list);

        return list.askName(id);
        // return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { // Mauvaise implémentation, utiliser des listener
        ListFiles list= (ListFiles) getFragmentManager().findFragmentById(R.id.list);

        if (! list.backPressed()) {
            super.onBackPressed();
        }
    }

    public void displayContent(String filename) {
        if (getResources().getBoolean(R.bool.landscape)) {
            ViewFileContent fileView= (ViewFileContent)getFragmentManager().findFragmentById(R.id.read_file);
            fileView.setFilename(filename);
            fileView.printContent();
        } else {
            Intent intent= new Intent(this, TextContentActivity.class);
//                Bundle intent_bundle= intent.getExtras();
            intent.putExtra(ListFiles.EXTRA_PATH, filename);

            startActivity(intent);
        }
    }

}
