package com.suribada.rxjavabook.error;
import android.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

/**
 * Created by lia on 2018-01-13.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class Base64DecoderRoboletricTest {

    @Test
    public void encodeAndDecode() {
        String input = "Hello RxJava";
        String encoded = Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
        System.out.print("encoded=" + encoded);
        assertNotNull(encoded);
    }

}
