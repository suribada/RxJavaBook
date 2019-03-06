package com.suribada.rxjavabook.etc;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

public class ChangeLineActivity extends Activity {

    private EditText postEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_edit_text);
        postEditText = findViewById(R.id.edit_text);
    }

    public void onClickButton1(View view) {
        postEditText.setMaxHeight(80);
        postEditText.requestFocus();
        postEditText.setCursorVisible(true);
        postEditText.setSingleLine(true);
        postEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        showMode();
    }

    private void showMode() {
        Toast.makeText(this, "multline=" + isMultiLine(), Toast.LENGTH_LONG).show();
    }

    public void onClickButton2(View view) {
        postEditText.setSingleLine(false);
        postEditText.setMaxHeight(160);
        postEditText.requestFocus();
        postEditText.setGravity(Gravity.LEFT);
        showMode();
    }

    public boolean isMultiLine() {
        return (postEditText.getInputType() & InputType.TYPE_TEXT_FLAG_MULTI_LINE) == InputType.TYPE_TEXT_FLAG_MULTI_LINE;
    }
}
