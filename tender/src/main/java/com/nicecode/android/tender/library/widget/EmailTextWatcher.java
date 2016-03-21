package com.nicecode.android.tender.library.widget;

import android.widget.EditText;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public class EmailTextWatcher extends TextWatcher {

    @SuppressWarnings("unused")
    private static final String TAG = "EmailTextWatcher";

    private boolean showErrorHint;
    private boolean capFirstLetter;

    public EmailTextWatcher(EditText editText, EditTextWatcherChanged editTextWatcherListener) {
        super(editText, editTextWatcherListener);
        this.showErrorHint = false;
        this.capFirstLetter = false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            this.mEditText.removeTextChangedListener(this);
            int iniLen;
            iniLen = this.mEditText.getText().length();
            if (iniLen < 1) {
                this.mEditText.setText(null);
                if (this.showErrorHint) {
                    this.mEditText.setError("Invalid value."); // TODO customize icon ?
                }
                this.valid = false;
            } else {
                int cp = this.mEditText.getSelectionStart();
                if (this.capFirstLetter) {
                    this.mEditText.setText(capFirstLetter(s.toString()));
                } else {
                    this.mEditText.setText(s);
                }
                int endLen = this.mEditText.getText().length();
                int sel = (cp + (endLen - iniLen));
                if (sel > 0 && sel <= this.mEditText.getText().length()) {
                    this.mEditText.setSelection(sel);
                } else {
                    // place cursor at the end?
                    this.mEditText.setSelection(this.mEditText.getText().length() - 1);
                }

                this.valid =  android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();;
            }
        } finally {
            this.mEditText.addTextChangedListener(this);
        }
        super.onTextChanged(s, start, before, count);
    }

    public void setShowErrorHint(boolean showErrorHint) {
        this.showErrorHint = showErrorHint;
    }

    public void setCapFirstLetter(boolean capFirstLetter) {
        this.capFirstLetter = capFirstLetter;
    }

    private static String capFirstLetter(String input) {
        return input.substring(0,1).toUpperCase() + input.substring(1,input.length());
    }
}
