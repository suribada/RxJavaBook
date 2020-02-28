package com.suribada.rxjavabook.seudo;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

public class FormatEllipsizeTextView extends AppCompatTextView {

    private String format;
    private String arg;
    private String argColor;

    public FormatEllipsizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String format, String arg, String argColor) {
        this.format = format;
        this.arg = arg;
        this.argColor = argColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        float inputWidth = paint.measureText(arg, 0, arg.length());
        float constWidth = paint.measureText(format.replace("%s", ""));
        String coloredArg = String.format("<font color=\"%s\">%s</font>", argColor, arg);
        if(inputWidth + constWidth > getWidth()){
            float maxInputWidth = getWidth() - constWidth;
            String ellipsized = TextUtils.ellipsize(arg, paint, maxInputWidth, TextUtils.TruncateAt.END).toString();
            coloredArg = String.format("<font color=\"%s\">%s</font>", argColor, ellipsized);
        }
        CharSequence text = Html.fromHtml(String.format(format, coloredArg));
        canvas.drawText(text, 0, text.length(), 0, 0, getPaint());
    }
}
