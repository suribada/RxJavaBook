package com.suribada.rxjavabook.seudo;

import android.location.Location;

/**
 * Created by lia on 2018-01-22.
 */

public class CarInformation {

    private int velocity;
    private float fuelPercent;
    private Location location;

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void setFuelPercent(float fuelPercent) {
        this.fuelPercent = fuelPercent;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void check() {
        while (true) {
            if (velocity <= 10 && fuelPercent <= 5.0f) {
                // 주유 메시지
            }
            if (velocity <= 5 && nearDestination(location)) {
                // 주차장 메시지
            }
            //...
        }
    }

    boolean nearDestination(Location location) {
        return true;
    }

}
