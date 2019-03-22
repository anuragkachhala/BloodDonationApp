package com.tekit.software.bloodDonationApp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class SplashScreenActivity extends AppCompatActivity {

    private int SLEEP_TIMER = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        new LogoLuncher().start();

    }


    private class LogoLuncher extends Thread {

        public void run() {

            try {

                sleep(1000 * SLEEP_TIMER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


                startNewActivity(LoginActivity.class);

        }
    }

    public void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }
}
