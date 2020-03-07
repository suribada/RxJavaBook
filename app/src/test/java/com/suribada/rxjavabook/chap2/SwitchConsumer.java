package com.suribada.rxjavabook.chap2;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.internal.functions.ObjectHelper;
import io.reactivex.rxjava3.internal.operators.observable.ObservableInternalHelper;

/**
 * Created by Noh.Jaechun on 2018-04-10.
 */

public class SwitchConsumer<T> implements Consumer<T> {

    private List<TypePredicate> typeConsumers;

    public SwitchConsumer(List<TypePredicate> typeConsumers) {
        this.typeConsumers = typeConsumers;
    }

    @Override
    public void accept(T t) throws Throwable {
        for (TypePredicate each : typeConsumers) {
            if (each.test(t)) {
                return;
            }
        }

    }

    public static class TypePredicate<T> implements Predicate<T> {

        final Class<T> clazz;
        final Consumer<? super T> consumer;

        public TypePredicate(Class<T> clazz, Consumer<? super T> consumer) {
            this.clazz = clazz;
            this.consumer = consumer;
        }

        @Override
        public boolean test(T t) throws Throwable {
            if (clazz.isInstance(t)) {
                consumer.accept(clazz.cast(t));
                return true;
            }
            return false;
        }

    }

    public static <T, S, M> Observable<T> generate(
            final Callable<S> initialState,
            final BiConsumer<S, Emitter<S>> generator,
            Consumer<? super M> disposeState) {
        Objects.requireNonNull(generator, "generator  is null");
        return null;
    }

    public static <T, S> Observable<T> fromIterable(Iterable<? extends S> source) {
        return null;
    }

    public class Observable2<T> {
        public <T> void accept(T t) {

        }
    }

}
