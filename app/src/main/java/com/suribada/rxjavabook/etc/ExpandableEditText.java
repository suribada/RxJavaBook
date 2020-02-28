package com.suribada.rxjavabook.etc;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.TextView;

public class ExpandableEditText extends EditText {

    public ExpandableEditText(Context context) {
        super(context);
        //initialize();
    }

    public ExpandableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initialize();
    }

    public ExpandableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initialize();
    }

    @Override
    public void beginBatchEdit() {
        Log.d("suribada", "beginBatchEdit");
        super.beginBatchEdit();
    }

    @Override
    public void onBeginBatchEdit() {
        Log.d("suribada", "onBeginBatchEdit");
        super.onBeginBatchEdit();
    }

    private void initialize() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!isMultiLine()) {
//                    changeMultiline();
//                }
            }
        });
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    if (!isMultiLine()) {
//                        changeMultiline();
//                    }
                }
            }
        });
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("suribada", "onEditorAction inner");
//                if (!isMultiLine()) {
//                    changeMultiline();
//                }
                return false;
            }
        });
        setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return ExpandableEditText.this.getInputType();
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                Log.d("suribada", "onKeyDown inner=" + keyCode + "/" + event.toString());
                return false;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                return false;
            }

            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("suribada", "onKey inner=" + keyCode + "/" + event.toString());
                return false;
            }
        });
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        Log.d("suribada", "dispatchKeyEventPreIme=" + event.toString());
        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("suribada", "dispatchKeyEvent=" + event.toString());
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        Log.d("suribada", "onKeyMultiple=" + event.toString());
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("suribada", "onKeyDown=" + keyCode + "/" + event.toString());
//        if (!isMultiLine()) {
//            changeMultiline();
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEditorAction(int actionCode) {
        Log.d("suribada", "onEditorAction");
//        if (!isMultiLine()) {
//            changeMultiline();
//        }
        super.onEditorAction(actionCode);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        Log.d("suribada", "onKeyPreIme");
//        if (!isMultiLine()) {
//            changeMultiline();
//        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return super.onCreateInputConnection(outAttrs);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        Log.d("suribada", "onTextChanged");
        /*
        if (!isMultiLine()) {
            changeMultiline();
        }
        */
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("suribada", "onKeyUp=" + keyCode + "/" + event.toString());
        return super.onKeyUp(keyCode, event);
    }

//    private boolean isMultiLine() {
//        return (getInputType() & InputType.TYPE_TEXT_FLAG_MULTI_LINE) == InputType.TYPE_TEXT_FLAG_MULTI_LINE;
//    }

    public void changeSingleLine() {
        //setMaxHeight(80);
        setLines(1);
        //requestFocus();
        //setCursorVisible(true);
        //setSingleLine(true);
        //setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    public void changeMultiline() {
        //setSingleLine(false);
        setLines(4);
        //setMaxHeight(160);
        //requestFocus();
        //setGravity(Gravity.LEFT);
    }


}
