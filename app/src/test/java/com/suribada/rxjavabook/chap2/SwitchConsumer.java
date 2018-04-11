package com.suribada.rxjavabook.chap2;

import com.suribada.rxjavabook.chap3.ContractErrorRevisedActivity;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by Noh.Jaechun on 2018-04-10.
 */

public class SwitchConsumer<T> implements Consumer<T> {

    private List<TypePredicate> typeConsumers;

    public SwitchConsumer(List<TypePredicate> typeConsumers) {
        this.typeConsumers = typeConsumers;
    }

    @Override
    public void accept(T t) throws Exception {
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
        public boolean test(T t) throws Exception {
            if (clazz.isInstance(t)) {
                consumer.accept(clazz.cast(t));
                return true;
            }
            return false;
        }

    }

}
