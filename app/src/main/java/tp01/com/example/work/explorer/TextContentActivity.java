package tp01.com.example.work.explorer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Activity to read a file
 */

public class TextContentActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.textcontent, menu);
        return true;
    }

}
