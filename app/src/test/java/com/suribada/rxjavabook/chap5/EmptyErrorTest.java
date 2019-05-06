package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 8. 5..
 */
public class EmptyErrorTest {

    @Test
    public void testEmptyError() {
        Observable.just(123, -6, 101, 305, 421, 0, -1, 324)
            .flatMap(userNo -> {
                if (userNo < 0) {
                    return Observable.error(
                        new IllegalArgumentException("only positive number allowed")); // (2)
                }
                if (userNo % 3 == 0) {
                    return Observable.empty(); // (1)
                }
                return getProfile(userNo); // (3)
            })
            .subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("onComplete"));
    }

    Observable<Profile> getProfile(int userNo) {
        return Observable.just(new Profile(userNo));
    }

    class Profile {
        int userNo;
        Profile(int userNo) {
            this.userNo = userNo;
        }

        @Override
        public String toString() {
            return "userNo=" + userNo;
        }
    }



}
