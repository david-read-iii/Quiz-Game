package com.davidread.quizgame.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.davidread.quizgame.R;

/**
 * This activity class represents a splash screen.
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Called when activity is initially created. Hides the action bar, inflates a splash screen
     * layout, and waits two seconds before starting the login activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar and inflate the splash screen layout.
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        // Wait two seconds before starting the login activity.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}