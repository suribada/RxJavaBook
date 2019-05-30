package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import io.reactivex.Observable;
import com.suribada.rxjavabook.model.Profile;

import java.util.LinkedHashMap;
import java.util.TreeMap;

public class ToMapTest {

    @Test
    public void toMap_default() {
        Observable.just(Profile.create("노재춘_직장", "010-2000-1000"),
                Profile.create("강사룡", "010-1234-5678"),
                Profile.create("노재춘", "010-2000-1000"),
                Profile.create("권태환", "010-5678-0000"))
                .toMap(profile -> profile.getPhoneNumber(), profile -> profile.getName()) // (1)
                .subscribe(System.out::println); // (2)
    }

    @Test
    public void toMap_preserverInsertionOrder() {
        Observable.just(Profile.create("노재춘_직장", "010-2000-1000"),
                Profile.create("강사룡", "010-1234-5678"),
                Profile.create("노재춘", "010-2000-1000"),
                Profile.create("권태환", "010-5678-0000"))
                .toMap(profile -> profile.getPhoneNumber(), profile -> profile.getName(), LinkedHashMap::new) // (1)
                .subscribe(System.out::println); // (2)
    }

    @Test
    public void toMap_preserverCompareOrder() {
        Observable.just(Profile.create("노재춘_직장", "010-2000-1000"),
                Profile.create("강사룡", "010-1234-5678"),
                Profile.create("노재춘", "010-2000-1000"),
                Profile.create("권태환", "010-5678-0000"))
                .toMap(profile -> profile.getPhoneNumber(), profile -> profile.getName(), TreeMap::new) // (1)
                .subscribe(System.out::println); // (2)
    }
}
