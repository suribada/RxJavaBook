package com.suribada.rxjavabook;

import android.support.test.runner.AndroidJUnit4;
import android.util.SparseArray;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SparseArrayTest {

    @Test
    public void sparseArray() {
        SparseArray<String> array = new SparseArray<>();
        String s1 = "hello";
        array.append(20, s1);
        array.append(30, "1");
        array.remove(20);

        int key = array.keyAt(0);
        assertEquals("1", array.get(key));

        assertEquals(1, array.size());
    }

}
