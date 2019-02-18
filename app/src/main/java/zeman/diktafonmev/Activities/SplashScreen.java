package zeman.diktafonmev.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import zeman.diktafonmev.R;

/**
 * Created by Nikola on 10.3.2016..
 */
public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 0;
    public void ProvjeraSplashScreen() {
        Context context = getApplicationContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        boolean checkBoxSplashScreen = pref.getBoolean("checkBoxSplashScreen", false);

        if (checkBoxSplashScreen == true) {
            SPLASH_TIME_OUT = 1500;
        } else {
            SPLASH_TIME_OUT = 0;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscren_activity);

        ProvjeraSplashScreen();
        TextView text = (TextView) findViewById(R.id.textView5);
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        TextView autor = (TextView) findViewById(R.id.textView16);

        Animation animation = new TranslateAnimation(0, 0, 110, 0);
        animation.setDuration(700);
        animation.setRepeatMode(Animation.RESTART);


        iv.startAnimation(animation);
        autor.startAnimation(animation);
        text.startAnimation(animation);

        TextView txGodina = (TextView) findViewById(R.id.textView17);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        txGodina.setText(Integer.toString(year));

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
