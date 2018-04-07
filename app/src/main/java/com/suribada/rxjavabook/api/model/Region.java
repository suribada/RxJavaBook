package com.suribada.rxjavabook.api.model;

/**
 {
 "areaCode":10101,
 "areaName" : "삼성동"
 }
 * Created by Noh.Jaechun on 2018-04-08.
 */
public class Region {
    public int areaCode;
    public String areaName;

    @Override
    public String toString() {
        return "Region{" +
                "areaCode=" + areaCode +
                ", areaName='" + areaName + '\'' +
                '}';
    }
}
