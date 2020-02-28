package com.suribada.rxjavabook.binding;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.android.MainThreadDisposable;

/**
 * Created by Noh.Jaechun on 2018. 7. 4..
 */
public class RecyclerViewItemLongClickObservable extends Observable<Integer> {

    private final RecyclerView view;

    RecyclerViewItemLongClickObservable(RecyclerView view) {
        this.view = view;
    }

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        Listener listener = new Listener(view, observer);
        observer.onSubscribe(listener);
    }

    final class Listener extends MainThreadDisposable {

        private final RecyclerView recyclerView;
        private final RecyclerView.OnChildAttachStateChangeListener childAttachStateChangeListener;

        Listener(RecyclerView recyclerView, Observer<? super Integer> observer) {
            this.recyclerView = recyclerView;
            childAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {

                @Override
                public void onChildViewAttachedToWindow(View view) {
                    recyclerView.getChildViewHolder(view).itemView.setOnLongClickListener(v -> {
                        if (!isDisposed()) {
                            int position = recyclerView.getChildAdapterPosition(view);
                            observer.onNext(position);
                        }
                        return true;
                    });
                }

                @Override
                public void onChildViewDetachedFromWindow(View view) {
                }

            };
            recyclerView.addOnChildAttachStateChangeListener(childAttachStateChangeListener);
            recyclerView.swapAdapter(recyclerView.getAdapter(), true);
        }

        @Override
        protected void onDispose() {
            recyclerView.removeOnChildAttachStateChangeListener(childAttachStateChangeListener);
        }

    }

}
