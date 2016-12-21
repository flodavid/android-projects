package tp01.com.example.work.explorer;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Fragment to list files of a folder created by @flodavid
 */

public class ListFiles extends ListFragment implements BackPressListener {
    public String pathname;
    private FileAdapter files;
    private MainActivity activity;

    public final static String EXTRA_PATH = "ctp01.com.example.work.explorer.EXTRA_PATH";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();

        pathname=  Environment.getExternalStorageDirectory().getPath();
        files= new FileAdapter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        files.listDirFiles(pathname);

        // Bind to the adapter.
        setListAdapter(files);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView pathName= (TextView)view.findViewById(R.id.path);
        pathName.setText(pathname);
    }

    @Override
    public void onListItemClick(ListView x, View v, int pos, long id) {
        try {
            File item = files.getItem(pos);
            if ( item != null) {
                String newPath = pathname + "/" + item.getName();
                doActionFilename(newPath);
            } else {
                Log.e("Explorer_Frag-Nav", "onListItemClick//, l'item n'a pas été trouvé");
                throw new NullPointerException();
            }
        } catch (java.lang.NullPointerException npe) {
            Toast t = Toast.makeText(activity.getApplicationContext(),
                    "Une erreur s'est produite", Toast.LENGTH_LONG);
            t.show();
        }

    }

    @Override
    public boolean backPressed() {
        if (! "/".equals(pathname)) {
            File parentPath = new File(pathname).getParentFile();
            doActionFile(parentPath);
            return true;
        } else return false;
    }

    @Override
    public boolean onKey( View v, int keyCode, KeyEvent event )
    {
//        if (event == KeyEvent.ACTION_DOWN) {
            if( keyCode == KeyEvent.KEYCODE_BACK )
            {
                backPressed();
                return true;
            }
            return false;
//        }
//        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return askName(id);
        // return super.onOptionsItemSelected(item);
    }

    public void doActionFilename(String filename) {
        File path= new File(filename);
        doActionFile(path);
    }

    public void doActionFile(File path) {
        if (files.tryGoFolder(path)) {
            // Si on arrive à changer de répertoire, on sauvegarde le nouveau répertoire
            pathname= path.getPath();
            // On met à jour le label du dossier courant
            TextView pathName= (TextView)activity.findViewById(R.id.path);
            pathName.setText(pathname);
        }
        else if (!tryOpenFile(path)) {
            Log.d("Explorer_Frag-Nav", "Impossible d'aller dans le répertoire indiqué : " + pathname);
        }
    }

    private String fileExt(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    private boolean tryOpenFile(File file)
    {
        // Get the type of the file
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String ext= fileExt(file.getName());

        if ("txt".equals(ext) || "php".equals(ext)) {
            Log.v("Explorer_Frag-Nav", "Chemin à envoyer : "+  file.getPath());
            if (getResources().getBoolean(R.bool.landscape)) {
//                ViewFileContent fileView= (ViewFileContent)getFragmentManager().findFragmentById(R.id.read_file);
//                fileView.setFilename(file.getPath());
                activity.displayContent(file.getPath());
            } else {
                Intent intent= new Intent(activity, TextContentActivity.class);
//                Bundle intent_bundle= intent.getExtras();
                intent.putExtra(EXTRA_PATH, file.getPath());

                startActivity(intent);
            }
            return true;
        } else {
            String mimeType = myMime.getMimeTypeFromExtension(ext);
            System.out.println("Extension du fichier : " + ext + ". Mime : " + mimeType);

            newIntent.setDataAndType(Uri.fromFile(file),mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                activity.startActivity(newIntent);
                return true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    public boolean askName(final int id) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View alertDialogView = inflater.inflate(R.layout.alertdialog,
                null);
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setView(alertDialogView);

        TextView txtAlert = (TextView) alertDialogView
                .findViewById(R.id.viewAlert);

        switch (id) {
            case R.id.create_rep: {
                txtAlert.setText(R.string.foldername_input);
                adb.setTitle("Creation du dossier");
            }
            break;
            case R.id.create_file: {
                txtAlert.setText(R.string.filename_input);
                adb.setTitle("Creation du fichier");
            }
            break;
        }

        // comment vérifier que le fichier a bien été crée pour renvoyer un
        // booléen
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText nom = (EditText) alertDialogView
                        .findViewById(R.id.champSaisie);
                File file = new File(pathname, nom.getText().toString());
                Log.d("Explorer_Frag-Nav", "askName()//id : " + id);
                if (file.getParentFile().canWrite()) {
                    switch (id) {
                        case R.id.create_rep: {
                            if (file.mkdirs()) doActionFile(file);
                            else System.out.println("Erreur de création du dossier");
                        }
                        break;
                        case R.id.create_file: {
                            try {
                                if (file.createNewFile()) doActionFile(file);
                                else System.out.println("Erreur de création du fichier");

                            } catch (IOException e) {
                                e.printStackTrace();
                                doActionFile(file);
                            }
                        }
                        break;
                    }
                } else {
                    Toast t = Toast.makeText(activity.getApplicationContext(),
                            "Impossible de créer ici", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
        return true;
    }


}

