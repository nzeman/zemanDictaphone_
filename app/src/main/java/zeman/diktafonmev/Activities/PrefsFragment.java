package zeman.diktafonmev.Activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import zeman.diktafonmev.R;

/**
 * Created by praktikant on 2.3.2016..
 */
public class PrefsFragment extends PreferenceFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


    }

}
