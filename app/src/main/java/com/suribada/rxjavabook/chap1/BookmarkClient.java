package com.suribada.rxjavabook.chap1;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

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
        SystemClock.sleep(3000);
        bookmarkRepository.getTitle();
        SystemClock.sleep(3000);
        System.out.println(bookmarkRepository.getFetchedTitle());
        bookmarkRepository.setUrl("http://endofhope.com");
        SystemClock.sleep(3000);
        bookmarkRepository.getUrl();
        SystemClock.sleep(3000);
        System.out.println(bookmarkRepository.getFetchedUrl());
    }

    public void saveBookmark3() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitle("heetak's advocate");
        for (int i = 0; i < 3; i++) {
            SystemClock.sleep(1000);
            if (bookmarkRepository.savedSuccessful()) {
                bookmarkRepository.getTitle();
                for (int j = 0; j < 3; j++) {
                    SystemClock.sleep(1000);
                    if (bookmarkRepository.fetchedTitleSuccessful()) {
                        System.out.println(bookmarkRepository.getFetchedTitle());
                    }
                }
            }
        }
        //...
    }

    public void saveBookmark4() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitle("heetak's advocate", new BookmarkRepository.Callback<Void>() { // (1)
            @Override
            public void onSucess(Void aVoid) {
                getBookmarkTitle();
            }

            @Override
            public void onFailed(Exception e) {
                //TODO show error
            }
        });
        bookmarkRepository.setUrl("http://endofhope.com", new BookmarkRepository.Callback<Void>() { // (3)
            @Override
            public void onSucess(Void aVoid) {
                getBookmarkUrl();
            }


            @Override
            public void onFailed(Exception e) {
                //TODO show error
            }
        });
    }

    public void saveBookmark6() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitleRxJava("heetak's advocate")
                .andThen(bookmarkRepository.getTitleRxJava())
                .subscribe(System.out::println,
                        e -> {
                            //TODO show error
                        });
        //...
    }

    private void getBookmarkUrl() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
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

    private void getBookmarkTitle() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
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

    public void saveBookmark5() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitle("heetak's advocate", new BookmarkRepository.Callback<Void>() { // (1)
            @Override
            public void onSucess(Void aVoid) {
                saveBookmark(bookmarkRepository);
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

    private void saveBookmark(BookmarkRepository bookmarkRepository) {
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

    public void declarativeOrImperative() {
        String sql = "SELECT * from bookmark where user_id='suribada'";
        List<Bookmark> bookmarks = new ArrayList<>();
        List<Bookmark> userBookmarks = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            if (bookmark.getUserId().equals("suribada")) {
                userBookmarks.add(bookmark);
            }
        }
    }

}
