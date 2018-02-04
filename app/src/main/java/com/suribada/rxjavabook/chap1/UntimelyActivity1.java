package com.suribada.rxjavabook.chap1;

import android.app.Activity;
import android.view.View;

/**
 * Created by lia on 2018-01-31.
 */
public class UntimelyActivity1 extends Activity implements View.OnClickListener {

    private boolean initializedSomething;

    @Override
    public void onClick(View v) {
        if (!initializedSomething) {
            return;
        }
        //...
    }

}
