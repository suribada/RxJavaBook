package com.suribada.rxjavabook;

/**
 * 테스트 코드에서 안드로이드 API를 호출하기 위한 것일뿐
 *
 * Created by Noh.Jaechun on 2018. 9. 13..
 */
public class SystemClock {
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
