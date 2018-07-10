package com.suribada.rxjavabook.binding;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 7. 5..
 */
public class RxRecyclerViewEx {

    public static Observable<Integer> itemClicks(@NonNull RecyclerView recyclerView) {
        return new RecyclerViewItemClickObservable(recyclerView);
    }

    public static Observable<Integer> itemLongClicks(@NonNull RecyclerView recyclerView) {
        return new RecyclerViewItemLongClickObservable(recyclerView);
    }

}
