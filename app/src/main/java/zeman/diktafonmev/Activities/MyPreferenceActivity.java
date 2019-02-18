package zeman.diktafonmev.Activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by praktikant on 2.3.2016..
 */
public class MyPreferenceActivity extends PreferenceActivity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }


}
