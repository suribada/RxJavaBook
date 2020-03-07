package com.suribada.rxjavabook.chap2;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Noh.Jaechun on 2018-04-10.
 */
public class SwitchConsumerTest {

    @Test
    public void testSwitchLambda() throws Throwable {
        List<SwitchConsumer.TypePredicate> typePredicateList = Arrays.asList(
                new SwitchConsumer.TypePredicate(ViewState.Error.class, value -> {
                    System.out.println(String.valueOf(value));
                }),
                new SwitchConsumer.TypePredicate(ViewState.Result.class, value -> {
                    System.out.println(String.valueOf(value));
                })
        );
        SwitchConsumer<ViewState> stateSwitchConsumer = new SwitchConsumer(typePredicateList);
        stateSwitchConsumer.accept(new ViewState.Error(new IllegalAccessError("errro")));
        stateSwitchConsumer.accept(new ViewState.Result("title"));
    }

    interface ViewState {
        class Error implements ViewState {
            Throwable e;

            Error(Throwable e) {
                this.e = e;
            }
        }

        class Result implements ViewState {
            String title;

            Result(String title) {
                this.title = title;
            }

            @Override
            public String toString() {
                return "title=" + title;
            }
        }
    }

}
