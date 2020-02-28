package com.suribada.rxjavabook.binding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.core.Observable;

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
