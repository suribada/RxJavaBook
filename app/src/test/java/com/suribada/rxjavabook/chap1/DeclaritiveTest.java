package com.suribada.rxjavabook.chap1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import io.reactivex.rxjava3.core.Observable;

public class DeclaritiveTest {

    String sql = "SELECT * from comments where user_id='suribada'";

    @Test
    public void imperative1() {
        List<Comment> userComments = new ArrayList<>();
        for (Comment comment : getAllComments()) {
            if (comment.getUserId().equals("suribada")) {
                userComments.add(comment);
            }
        }
        System.out.println(userComments);
    }

    @Test
    public void imperative2() {
        List<Comment> popularComments = new ArrayList<>();
        for (Comment comment : getAllComments()) {
            if (comment.getLike() >= 7) {
                popularComments.add(comment);
            }
        }
        System.out.println(popularComments);
    }

    @Test
    public void imperative3() {
        List<Comment> userComments = getUserComments("suribada");
        System.out.println(userComments);
    }

    @Test
    public void imperative4() {
        List<Comment> popularComments = getPopularComments(7);
        System.out.println(popularComments);
    }

    private List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("suribada", "ㅎㅎ", 10));
        comments.add(new Comment("heetak", "ibt", 2));
        comments.add(new Comment("ias", "cls", 3));
        comments.add(new Comment("horseriding.king", "ttt", 7));
        comments.add(new Comment("suribada", "쿠쿠", 6));
        return comments;
    }

    public List<Comment> getUserComments(String userId) {
        List<Comment> userComments = new ArrayList<>();
        for (Comment comment : getAllComments()) {
            if (comment.getUserId().equals("suribada")) {
                userComments.add(comment);
            }
        }
        return userComments;
    }

    public List<Comment> getPopularComments(int likeCount) {
        List<Comment> popularComments = new ArrayList<>();
        for (Comment comment : getAllComments()) {
            if (comment.getLike() >= likeCount) {
                popularComments.add(comment);
            }
        }
        return popularComments;
    }

    @Test
    public void declaritive1() {
        List<Comment> userComments = getAllComments().stream()
                .filter(comment -> comment.getUserId().equals("suribada"))
                .collect(Collectors.toList());
        System.out.println(userComments);
    }

    @Test
    public void declaritive2() {
        List<Comment> popularComments = getAllComments().stream()
                .filter(comment -> comment.getLike() >= 7)
                .collect(Collectors.toList());
        System.out.println(popularComments);
    }

    @Test
    public void reactive1() {
        Observable.fromIterable(getAllComments())
                .filter(comment -> comment.getUserId().equals("suribada"))
                .toList()
                .subscribe(System.out::println);
    }

    @Test
    public void reactive2() {
        Observable.fromIterable(getAllComments())
                .filter(comment -> comment.getLike() >= 7)
                .toList()
                .subscribe(System.out::println);
    }

}
