package com.suribada.rxjavabook.etc;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

public class SpannableActivity extends Activity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
        showTitle();
    }

    private void showTitle() {
        String titlePrefix = "RxJava";
        String titlePostfix = "뭐시여";
        String marginSpannable = " ";
//        SpannableString marginSpannable = new SpannableString(" ");
//        marginSpannable.setSpan(new AbsoluteSizeSpan(15, true),
//                0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString postfixSpannable = new SpannableString(titlePostfix);
        postfixSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, titlePostfix.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        CharSequence concatTitle = TextUtils.concat(titlePrefix, marginSpannable, postfixSpannable);
        title.setText(concatTitle);
    }
}
