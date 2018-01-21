package com.suribada.rxjavabook.seudo;

import android.location.Location;

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
        if (velocity <= 10 && fuelPercent <= 5.0f) {
            // 주유 메시지
        }
        ...
    }

    public void checkGarage() {
        if (velocity <= 5 && nearDestination(location)) {
            // 주차장 메시지
        }
        ...
    }

    boolean nearDestination(Location location) {
        return true;
    }

}
