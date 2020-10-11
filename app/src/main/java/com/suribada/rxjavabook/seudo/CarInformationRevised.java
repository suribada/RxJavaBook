package com.suribada.rxjavabook.seudo;

import android.location.Location;
import android.location.LocationManager;
import android.util.Pair;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by lia on 2018-01-22.
 */

public class CarInformationRevised {

    private int velocity;
    private float fuelPercent;
    private Location location;

    public void setVelocity(int velocity) {
        this.velocity = velocity;
        checkGasStation();
        checkGarage();
    }

    public void setFuelPercent(float fuelPercent) {
        this.fuelPercent = fuelPercent;
        checkGasStation();
    }

    public void setLocation(Location location) {
        this.location = location;
        checkGarage();
    }

    public void checkGasStation() {
        if (velocity >= 5 && velocity <= 10 && fuelPercent <= 5.0f) {
            // 주유 메시지
        }
        //...
    }

    public void checkGarage() {
        if (velocity <= 5 && nearDestination(location)) {
            // 주차장 메시지
        }
        //...
    }

    boolean nearDestination(Location location) {
        return true;
    }

    public void checkGasStationRxJava() {
        Observable filteredVelocity = getVelocityObservable() // (1) 시작
                .filter(velocity -> velocity >= 5 && velocity <= 10)
                .distinctUntilChanged(); // (1) 끝
        Observable filteredFuelPercent = getFuelPercentObservable() // (2) 시작
                .filter(fuelPercent -> fuelPercent <= 5.0f)
                .distinctUntilChanged(); // (2) 끝
        Observable.combineLatest(filteredVelocity, filteredFuelPercent, Pair::create) // (3)
                .subscribe(pair -> {
                    // 주유 메시지
                });
    }

    public void checkGarageRxJava() {
        Observable filteredVelocity = getVelocityObservable()
                .filter(velocity -> velocity <= 5);
        Observable filteredLocation = getLocationObservable()
                .filter(location -> nearDestination(location));
        Observable.combineLatest(filteredVelocity, filteredLocation, Pair::new)
                .subscribe(pair -> {
                    // 주차장 메시지
                });
    }

    public Observable<Integer> getVelocityObservable() {
        //...
        return Observable.create(e -> e.onNext(10));
    }

    public Observable<Float> getFuelPercentObservable() {
        return Observable.create(e -> e.onNext(5.0f));
    }

    public Observable<Location> getLocationObservable() {
        return Observable.create(e -> e.onNext(new Location(LocationManager.GPS_PROVIDER)));
    }

}
