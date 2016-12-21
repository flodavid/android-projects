package tp01.com.example.work.explorer;

import android.view.View;

/**
 * Interface décrivant la définition du bouton retour
 */

interface BackPressListener extends View.OnKeyListener{

    boolean backPressed();

}
