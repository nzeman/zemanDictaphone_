package zeman.diktafonmev.Helpers;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Nikola on 16.3.2016..
 */
public class ZemanAnimation {

    public static void AlphaAnim(View view, float startAlpha, float endAlpha, int duration, int startOffSet) {

        AlphaAnimation animation1 = new AlphaAnimation(startAlpha, endAlpha);
        animation1.setDuration(duration);
        animation1.setStartOffset(startOffSet);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }

}
