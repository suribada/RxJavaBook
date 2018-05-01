package com.suribada.rxjavabook.chap8;

/**
 * Created by lia on 2018-05-01.
 */

public interface State {

    final class LoadingState implements State {

        @Override public String toString() {
            return "LoadingState{}";
        }
    }

    final class ErrorState implements State {
        private final Throwable error;

        public ErrorState(Throwable error) {
            this.error = error;
        }

        public Throwable getError() {
            return error;
        }

        @Override public String toString() {
            return "ErrorState{error=" + error + '}';
        }
    }

    final class DataState implements State {
        private final String detail;

        public DataState(String detail) {
            this.detail = detail;
        }

        public String getDetail() {
            return detail;
        }

        @Override public String toString() {
            return "DataState{detail=" + detail + '}';
        }
    }

}
