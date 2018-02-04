package com.suribada.rxjavabook;

import com.suribada.rxjavabook.chap1.Bookmark;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lia on 2018-01-22.
 */

public class StreamTest {

    @Test
    public void listBookmark() {
        List<Bookmark> bookmarks = new ArrayList<>();
        List<Bookmark> userBookmarks = bookmarks.stream()
                .filter(bookmark -> bookmark.equals("suribada"))
                .collect(Collectors.toList());
    }
    @Test
    public void listStream() {
        Arrays.asList(1, 2, 4, 7, 8, 11, 14).stream()
                .filter(x -> x % 2 == 0)
                .map(x -> x * 100)
                .filter(x -> x <= 1000)
                .map(x -> findNearPrime(x))
                .forEach(System.out::println);

    }

    private int findNearPrime(Integer x) {
        return 997;
    }

}
