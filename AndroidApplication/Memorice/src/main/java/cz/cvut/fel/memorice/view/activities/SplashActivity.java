package cz.cvut.fel.memorice.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Splash activity displayed on the very start of the application. It waits for an amount of time
 * and then initiates {@link MainActivity}
 */
public class SplashActivity extends AppCompatActivity {

    private static final Logger LOG = Logger.getLogger(SplashActivity.class.getName());

    private static final int TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, "Interrupted wait!", e);
        }
        startActivity(intent);
        finish();
    }
}