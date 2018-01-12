package com.suribada.rxjavabook.error;

import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Created by lia on 2018-01-13.
 */
@RunWith(AndroidJUnit4.class)
public class Base64DecoderTest {

    @Test
    public void encodeAndDecode() {
        String input = "Hello RxJava";
        String encoded = Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
        System.out.print("encoded=" + encoded);
        assertNotNull(encoded);
    }

}
