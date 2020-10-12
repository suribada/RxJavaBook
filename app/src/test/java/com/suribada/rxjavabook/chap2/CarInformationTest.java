package com.suribada.rxjavabook.chap2;

import android.location.Location;
import android.location.LocationManager;
import android.util.Pair;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * Created by lia on 2018-01-22.
 */

public class CarInformationTest {

    boolean nearDestination(Location location) {
        return true;
    }

    @Test
    public void checkGasStationRxJava() {
        Observable<Boolean> filteredVelocity = getVelocityObservable() // (1) 시작
                .distinctUntilChanged()
                .map(velocity -> velocity >= 5 && velocity <= 10); // (1) 끝
        Observable<Boolean> filteredFuelPercent = getFuelPercentObservable() // (2) 시작
                .distinctUntilChanged()
                .map(fuelPercent -> fuelPercent <= 5.0f); // (2) 끝
        Observable.combineLatest(filteredVelocity, filteredFuelPercent,
                (justStarted, fuelNotEnough) -> justStarted && fuelNotEnough) // (3)
                .subscribe(showing -> {
                    if (showing) { // (4) 시작
                        // 주유 메시지
                        showRefuelMessage();
                    } else {
                        hideRefuelMessage();
                    } // (4) 끝
                });
        velocitySubject.onNext(5);
        fuelPercentSubject.onNext(3.0f);
        velocitySubject.onNext(12);
        fuelPercentSubject.onNext(8.0f);
        velocitySubject.onNext(7);
        fuelPercentSubject.onNext(2.0f);
    }

    private void showRefuelMessage() {
        System.out.println("주유하세요");
    }

    private void hideRefuelMessage() {
        System.out.println("지나갔어요");
    }

    @Test
    public void checkGarageRxJava() {
        Observable<Boolean> filteredVelocity = getVelocityObservable()
                .distinctUntilChanged()
                .map(velocity -> velocity <= 5);
        Observable<Boolean> filteredLocation = getLocationObservable()
                .distinctUntilChanged()
                .map(location -> nearDestination(location));
        Observable.combineLatest(filteredVelocity, filteredLocation,
                (slowVelocity, nearDestination) -> slowVelocity && nearDestination)
                .subscribe(showing -> {
                    if (showing) {
                        // 주유 메시지
                        showGarageMessage();
                    } else {
                        hideGarageMessage();
                    }
                });
        velocitySubject.onNext(7);
        locationSubject.onNext(new Location(LocationManager.GPS_PROVIDER));
        velocitySubject.onNext(2);
        velocitySubject.onNext(5);
    }

    private void showGarageMessage() {
        System.out.println("주차장이 가까워요");
    }

    private void hideGarageMessage() {
        System.out.println("주차장 지나갔어요");
    }

    private PublishSubject<Integer> velocitySubject = PublishSubject.create();

    public Observable<Integer> getVelocityObservable() {
        return velocitySubject;
        //...
        //return Observable.create(e -> e.onNext(10));
    }

    private PublishSubject<Float> fuelPercentSubject = PublishSubject.create();

    public Observable<Float> getFuelPercentObservable() {
        return fuelPercentSubject;
        //return Observable.create(e -> e.onNext(5.0f));
    }

    private PublishSubject<Location> locationSubject = PublishSubject.create();

    public Observable<Location> getLocationObservable() {
        return locationSubject;
        //return Observable.create(e -> e.onNext(new Location(LocationManager.GPS_PROVIDER)));
    }

}
