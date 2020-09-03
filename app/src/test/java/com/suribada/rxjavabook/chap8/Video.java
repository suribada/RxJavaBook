package com.suribada.rxjavabook.chap8;

import android.widget.ProgressBar;

public class Video {
    private Program program;

    public Video(Program program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return "Video{" +
                "program=" + program +
                '}';
    }
}
