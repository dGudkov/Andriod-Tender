package com.nicecode.android.tender.library.widget;

import android.text.Editable;
import android.widget.EditText;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public class TextWatcher implements android.text.TextWatcher {

    protected EditText mEditText;
    protected EditTextWatcherChanged mEditTextWatcherListener;
    protected boolean valid;

    public TextWatcher(EditText editText, EditTextWatcherChanged editTextWatcherListener) {
        this.mEditText = editText;
        this.mEditTextWatcherListener = editTextWatcherListener;
        this.valid = false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (this.mEditTextWatcherListener != null) {
            this.mEditTextWatcherListener.onEditTextWatcherChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public interface EditTextWatcherChanged {
        void onEditTextWatcherChanged();
    }

    @SuppressWarnings("unused")
    public boolean isValid() {
        return valid;
    }

}
