package com.suribada.rxjavabook.generics.second;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noh.Jaechun on 2018. 12. 21..
 */
public class Main {

    public void assignList() {
        //List<Number> list = new ArrayList<? extends Integer>();
        //List<Number> list2 = new ArrayList<? super Integer>();
        List<ArticleImage> articleImages = new ArrayList<>();
        makeAndShareList(articleImages);
    }

    public void makeAndShare(ArticleImage image) {
        image.makeThumbnail();
        image.share();
    }

    public void makeAndShare(CommentImage image) {
        image.makeThumbnail();
        image.share();
    }

    public void makeAndShare(Image image) {
        image.makeThumbnail();
        image.share();
    }

    /*
    public void makeAndShareList(List<Image> images) {
        for (Image image : images) {
            image.makeThumbnail();
            image.share();
        }
    }
    */

    public void makeAndShareList(List<? extends Image> images) {
        for (Image image : images) {
            image.makeThumbnail();
            image.share();
        }
    }

    private void makeThumbnail() {
    }
}
