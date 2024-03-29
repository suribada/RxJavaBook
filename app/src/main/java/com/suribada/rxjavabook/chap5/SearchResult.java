package com.suribada.rxjavabook.chap5;

import java.util.Random;

/**
 * Created by Noh.Jaechun on 2018. 8. 15..
 */
public class SearchResult {
    String result;

    public SearchResult(String keyword) {
        result = keyword + "_result";
    }

    public int count() {
        return result.length();
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "result='" + result + '\'' +
                '}';
    }
}
