package zeman.diktafonmev.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import zeman.diktafonmev.R;

/**
 * Created by praktikant on 1.3.2016..
 */
public class AboutActivity extends AppCompatActivity {

    private TextView textViewGodina;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        textViewGodina = (TextView) findViewById(R.id.textViewGodina);

        int year = Calendar.getInstance().get(Calendar.YEAR);

        textViewGodina.setText(Integer.toString(year));

    }

}
