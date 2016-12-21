package tp01.com.example.work.explorer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Fragment to display a file's text content
 */

public class ViewFileContent extends Fragment {

    private String filename;

    public void setFilename(String newFilename){
        filename= newFilename;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();

        if (null != intent) { //Null Checking
//            Bundle extras = intent.getExtras();
//            if (extras != null) {
            filename= intent.getStringExtra(ListFiles.EXTRA_PATH);
            //        String filename= savedInstanceState.getString("filename");
            Log.v("Explorer_Frag-View", "Chemin reçu du fichier à lire : " + filename);



//            } else Log.d("OpenFile", "Impossible de récupérer le bundle dans l'intent, donc le chemin du fichier à ouvrir");
        } else Log.d("OpenFile", "Impossible de récupérer l'intent, donc le chemin du fichier à ouvrir");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_file, container, false);
    }

    public void printContent() {
        TextView contentView= (TextView)getActivity().findViewById(R.id.text_content);
        contentView.setText(getFileContent());
    }

    private String getFileContent() {
        Log.v("Explorer_Frag-View", "getFileContent//Chargement du fichier texte");
        String content ="<DEBUT>\n";
        if (null != filename) {
            try {
                FileInputStream instream = new FileInputStream(filename);

                InputStreamReader inputReader = new InputStreamReader(instream);
                BufferedReader bufferReader = new BufferedReader(inputReader);
                String line;
                try
                {
                    while ((line = bufferReader.readLine()) != null) {
                        content+= line;
                    }
                    content += "\n<END>";

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            } catch (java.io.IOException e)
            {
                System.err.println("Impossibe de lire le fichier csv");
                e.printStackTrace();
            }
        }
        return content;
    }
}
