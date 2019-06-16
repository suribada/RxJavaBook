package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import io.reactivex.Observable;

import com.suribada.rxjavabook.model.Cafe;
import com.suribada.rxjavabook.model.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        // 결과 {010-5678-0000=권태환, 010-1234-5678=강사룡, 010-2000-1000=노재춘}
    }

    @Test
    public void printHashCode() {
        // 0, 15, 13
        System.out.println("010-2000-1000".hashCode() & 15);
        System.out.println("010-1234-5678".hashCode() & 15);
        System.out.println("010-5678-0000".hashCode() & 15);
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

    @Test
    public void orderHashMap() {
        Map<Integer, Integer> ctrArMap = new HashMap<>();
        ctrArMap.put( 1, 11);
        ctrArMap.put( 5, 15);
        ctrArMap.put( 3, 13);
        ctrArMap.put( 2, 12);
        ctrArMap.put( 4, 14);
        ctrArMap.put( 6, 14);
        ctrArMap.put( 7, 14);
        ctrArMap.put( 8, 14);
        ctrArMap.put( 9, 14);
        ctrArMap.put( 10, 14);
        ctrArMap.put( 11, 14);
        ctrArMap.put( 12, 14);
        ctrArMap.put( 19, 14);
        ctrArMap.put( 13, 14);
        ctrArMap.put( 14, 14);
        ctrArMap.put( 15, 14);
        ctrArMap.put( 16, 14);
        ctrArMap.put( 17, 14);
        ctrArMap.put( 18, 14);
        System.out.println(ctrArMap);
    }

    @Test
    public void orderHashMap_2() {
        Map<Value, Integer> ctrArMap = new HashMap<>();
        ctrArMap.put( new Value(1), 11);
        ctrArMap.put( new Value(5), 15);
        ctrArMap.put( new Value(3), 13);
        ctrArMap.put( new Value(2), 12);
        ctrArMap.put( new Value(4), 14);
        ctrArMap.put( new Value(6), 16);
        ctrArMap.put( new Value(7), 17);
        ctrArMap.put( new Value(8), 18);
        ctrArMap.put( new Value(9), 19);
        ctrArMap.put( new Value(15), 25);
        ctrArMap.put( new Value(11), 21);
        ctrArMap.put( new Value(12), 22);
        ctrArMap.put( new Value(19), 29);
        ctrArMap.put( new Value(13), 23);
        ctrArMap.put( new Value(14), 24);
        ctrArMap.put( new Value(10), 20);
        ctrArMap.put( new Value(16), 26);
        ctrArMap.put( new Value(17), 27);
        ctrArMap.put( new Value(18), 28);
        System.out.println(ctrArMap);
    }

    class Value {
        int a;
        Value(int a) {
            this.a = a;
        }

        @Override
        public int hashCode() {
            return a % 5;
        }
    }

    @Test
    public void testMultiMap() {
        getCafeObservable().toMultimap(cafe -> cafe.type, cafe -> cafe.name)
                .subscribe(System.out::println);
    }

    public Observable<Cafe> getCafeObservable() {
        List<Cafe> cafes = new ArrayList<>();
        cafes.add(new Cafe(1, "분당사랑", "suribada", "horseridingking"));
        cafes.add(new Cafe(2,"바둑사랑", "ias", "iasadbc"));
        cafes.add(new Cafe(2,"승마사랑", "eof", "endofhope"));
        return Observable.fromIterable(cafes);
    }
}
