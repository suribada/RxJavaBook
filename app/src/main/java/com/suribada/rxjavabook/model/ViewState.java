package com.suribada.rxjavabook.model;

public interface ViewState {
    class Error implements ViewState {
        Throwable e;

        public Error(Throwable e) {
            this.e = e;
        }
    }

    class Result implements ViewState {
        public String title;

        public Result(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    class Loading implements ViewState {
    }
}