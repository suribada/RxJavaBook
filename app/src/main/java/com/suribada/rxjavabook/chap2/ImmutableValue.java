package com.suribada.rxjavabook.chap2;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 변수가 공유돼서 결국 맞지 않음
 *
 * Created by Noh.Jaechun on 2018. 3. 26..
 */
public class ImmutableValue {

    private final long val;

    public ImmutableValue(long val) {
        this.val = val;
    }

    public ImmutableValue add(long j) {
        return new ImmutableValue(val + j);
    }

    public long getSum() {
        return val;
    }

}
