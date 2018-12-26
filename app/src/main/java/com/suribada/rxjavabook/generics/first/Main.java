package com.suribada.rxjavabook.generics.first;

/**
 * Created by Noh.Jaechun on 2018. 12. 21..
 */
public class Main {

    public void assignList() {
        //List<Number> list = new ArrayList<? extends Integer>();
        //List<Number> list2 = new ArrayList<? super Integer>();
    }

    public void makeAndShare(ArticleImage image) {
        image.makeThumbnail();
        image.share();
    }

    public void makeAndShare(CommentImage image) {
        image.makeThumbnail();
        image.share();
    }

    private void makeThumbnail() {
    }
}
