package com.suribada.rxjavabook.chap1;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.suribada.rxjavabook.SystemClock.*;

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
        sleep(3000);
        bookmarkRepository.getTitle();
        sleep(3000);
        System.out.println(bookmarkRepository.getFetchedTitle());
        bookmarkRepository.setUrl("http://endofhope.com");
        sleep(3000);
        bookmarkRepository.getUrl();
        sleep(3000);
        System.out.println(bookmarkRepository.getFetchedUrl());
    }

    @Test
    public void saveBookmark3() {
        BookmarkRepository bookmarkRepository = new BookmarkRepository();
        bookmarkRepository.setTitle("heetak's advocate");
        for (int i = 0; i < 3; i++) {
            sleep(1000);
            if (bookmarkRepository.savedSuccessful()) {
                bookmarkRepository.getTitle();
                for (int j = 0; j < 3; j++) {
                    sleep(1000);
                    if (bookmarkRepository.fetchedTitleSuccessful()) {
                        System.out.println(bookmarkRepository.getFetchedTitle());
                        break;
                    }
                }
                break;
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

    @Test
    public void declarativeOrImperative() {
        String sql = "SELECT * from bookmark where user_id='suribada'";
        List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.add(new Bookmark("suribada", "http://suribada.com", 10));
        bookmarks.add(new Bookmark("heetak", "http://suribada2.com", 2));
        bookmarks.add(new Bookmark("ias", "http://suribada3.com", 3));
        bookmarks.add(new Bookmark("horseriding.king", "http://suribada4.com", 7));
        bookmarks.add(new Bookmark("suribada", "http://suribada5.com", 6));
        List<Bookmark> userBookmarks = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            if (bookmark.getUserId().equals("suribada")) {
                userBookmarks.add(bookmark);
            }
        }
        System.out.println(userBookmarks);
    }

}
