package cc.com.example.work.horses;

/**
 * Classe représentant un cheval, par DAVID Florian
 */

public class Horse {
    private int progress;
    private int meters;
    private int nb_boost_step;

    Horse() {
        progress= 0;
        meters= 0;
        nb_boost_step= 2;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void run(int step) {
        meters+= step;
    }

    public int getMeters() {
        return meters;
    }

    public void decreaseBoostStep() {
        this.nb_boost_step-= 1;
    }

    public int getNb_boost_step() {
        return nb_boost_step;
    }

    public void setNb_boost_step(int nb_boost_step) {
        this.nb_boost_step = nb_boost_step;
    }
}
