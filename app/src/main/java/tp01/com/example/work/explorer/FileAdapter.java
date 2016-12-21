package tp01.com.example.work.explorer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Adapter to display a vertical list of files
 */

public class FileAdapter extends ArrayAdapter<File>
{
    FileAdapter(Context context) {
        super(context, R.layout.ligne);
    }

    public void listDirFiles(String pathname)
    {
        File path= new File(pathname);

        if (path != null) {
            if (path.isDirectory()) {
                if (null != path.listFiles()) {
                    for(File file: path.listFiles()) {
                        add(file);
                    }
                } else System.err.println("Le contenu du chemin est null");
            } else System.err.println("Ce n'est pas un dossier");
        }
        else {
            System.err.println("Le chemin est null");
        }
    }

    // utilise le fichier xml pour afficher chaque élément de l'ArrayAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File item= getItem(position);

//		applique le texte à la vue
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.ligne, parent, false);

//        TextView text= (TextView)convertView.findViewById(R.id.TextFic_Dos);
//        text.setText(nom);

        ImageView image= (ImageView)convertView.findViewById(R.id.fileIcon);
        if (item.isDirectory()){
            image.setImageResource(R.drawable.directory_icon);
        }
        else{
            image.setImageResource(R.drawable.blank_file_icon);
        }

        TextView acronym= (TextView)convertView.findViewById(R.id.fileName);
        acronym.setText(item.getName());

        return convertView;
    }

////    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        File path= getItem(position);
////        tryRefreshFiles(path);
//        MainActivity main= (MainActivity) getContext();
////        refresh(path);
//    }


    public boolean tryGoFolder(File newPath)
    {
        return tryRefreshFiles(newPath);
    }

    private void updateListContent(File path) {
        Log.d("Explorer_Adapter", "Refresh files//Path: " + path.toString());
        // On vide le contenu de l'adapteur
        clear();

        // Puis on remplit avec les nouveaux éléments
        for (File file : path.listFiles()) {
            add(file);
        }
//                registerForContextMenu(getListView());
    }

    private boolean tryRefreshFiles(File path) {

        if (path.exists() && path.canRead()) {
            if (path.isDirectory()) {
                updateListContent(path);
                return true;
            } else {
                return false;
            }
        } else {
            Toast t = Toast.makeText(getContext(),
                    "Impossible de lire ici", Toast.LENGTH_LONG);
            t.show();
            if (path.getParentFile().exists()
                    && path.getParentFile().canRead()) {
                path= path.getParentFile();
                return tryRefreshFiles(path);
            } else {
                path = Environment.getExternalStorageDirectory();
                return tryRefreshFiles(path);
            }
        }
    }
}
