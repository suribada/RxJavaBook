package com.suribada.rxjavabook.chap1;

import android.os.SystemClock;

/**
 * Created by lia on 2018-01-31.
 */

public class BookmarkClient {

    public void saveBookmark() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitle("heetak's advocate");
        System.out.println(bookmarkRepository.getTitle());
        bookmarkRepository.setUrl("http://endofhope.com");
        System.out.println(bookmarkRepository.getUrl());
    }

    public void saveBookmark2() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitle("heetak's advocate");
        SystemClock.sleep(1000);
        bookmarkRepository.getTitle();
        SystemClock.sleep(1000);
        System.out.println(bookmarkRepository.getFetchedTitle());
        bookmarkRepository.setUrl("http://endofhope.com");
        SystemClock.sleep(1000);
        bookmarkRepository.getUrl();
        SystemClock.sleep(1000);
        System.out.println(bookmarkRepository.getFetchedUrl());
    }

    public void saveBookmark3() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitle("heetak's advocate", new BookmarkRepository.Callback<Void>() { // (1)
            @Override
            public void onSucess(Void aVoid) {
                bookmarkRepository.getTitle(new BookmarkRepository.Callback<String>() { // (2)
                    @Override
                    public void onSucess(String result) {
                        System.out.println(result);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        //TODO show error
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                //TODO show error
            }
        });
        bookmarkRepository.setUrl("http://endofhope.com", new BookmarkRepository.Callback<Void>() { // (3)
            @Override
            public void onSucess(Void aVoid) {
                bookmarkRepository.getUrl(new BookmarkRepository.Callback<String>() { // (4)
                    @Override
                    public void onSucess(String result) {
                        System.out.println(result);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        //TODO show error
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                //TODO show error
            }
        });
    }

}
