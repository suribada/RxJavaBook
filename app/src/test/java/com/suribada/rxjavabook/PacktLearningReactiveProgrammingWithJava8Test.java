package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lia on 2018-01-14.
 */
public class PacktLearningReactiveProgrammingWithJava8Test {

    @Test
    public void testGroupBy() {
        List<String> albums = Arrays.asList(
                "The Piper at the Gates of Dawn", "A Saucerful of Secrets", "More",
                "Ummagumma", "Atom Heart Mother", "Meddle", "Obscured by Clouds",
                "The Dark Side of the Moon", "Wish You Were Here", "Animals",
                "The Wall");

        Observable.fromIterable(albums).groupBy(album -> album.split(" ").length)
                .subscribe(obs -> {
                    obs.subscribe(v -> System.out.println(obs.getKey() + " word(s)"));
                });

        Observable
                .fromIterable(albums)
                .groupBy(album -> album.replaceAll("[^mM]", "").length(),
                        album -> album.replaceAll("[mM]", "*"))
                .subscribe(obs -> {
                    obs.subscribe(v -> System.out.println(obs.getKey() + " occurences of 'm' : " + obs));
                });
    }
}
