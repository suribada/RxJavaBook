package com.suribada.rxjavabook.etc;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

public class ChangeLineActivity extends Activity {

    private EditText postEditText;
    //private ExpandableEditText postEditText2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_edit_text);
        postEditText = findViewById(R.id.edit_text);
        postEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before != count) {
                    postEditText.setMaxLines(4);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        postEditText2 = findViewById(R.id.edit_text2);
//        postEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isMultiLine()) {
//                    changeMultiline();
//                }
//            }
//        });
//        postEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Log.d("suribada1", "action=" + actionId + "/" + event.toString());
//                if (!isMultiLine()) {
//                    changeMultiline();
//                }
//                return false;
//            }
//        });
//        postEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.d("suribada1", "keyCode=" + keyCode + "/" + event.toString());
//                if (!isMultiLine()) {
//                    changeMultiline();
//                }
//                return false;
//            }
//        });
//        postEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.d("suribada1", "beforeTextChanged=" + s + ", "  + start + ", " + count + ", " + after);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("suribada1", "onTextChanged=" + s + ", "  + start + ", " + count + ", " + before);
////                if (before != count) { // 한글자라도 바뀌면
////                    if (!isMultiLine()) {
////                        changeMultiline();
////                    }
////                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    public void onClickButton1(View view) {
        postEditText.setMaxLines(1);
//        changeSingleLine();
//        showMode();
//        postEditText2.changeSingleLine();
    }

//    private void changeSingleLine() {
//        postEditText.setMaxHeight(80);
//        postEditText.requestFocus();
//        postEditText.setCursorVisible(true);
//        postEditText.setSingleLine(true);
//        postEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//    }

//    private void showMode() {
//        Toast.makeText(this, "multline=" + isMultiLine(), Toast.LENGTH_LONG).show();
//    }

    public void onClickButton2(View view) {
        postEditText.setMaxLines(4);
//        changeMultiline();
//        showMode();
//        postEditText2.changeMultiline();
    }

//    private void changeMultiline() {
//        postEditText.setSingleLine(false);
//        postEditText.setMaxHeight(160);
//        postEditText.requestFocus();
//        postEditText.setGravity(Gravity.LEFT);
//    }
//
//    public boolean isMultiLine() {
//        return (postEditText.getInputType() & InputType.TYPE_TEXT_FLAG_MULTI_LINE) == InputType.TYPE_TEXT_FLAG_MULTI_LINE;
//    }
}
