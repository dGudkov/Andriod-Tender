package com.nicecode.android.tender.library.utils;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public class WeakReference<T> {

    private java.lang.ref.WeakReference<T> mWeakReference;

    public WeakReference(T reference) {
        this.mWeakReference = new java.lang.ref.WeakReference<>(reference);
    }

    public T get() {
        if (this.mWeakReference != null) {
            return this.mWeakReference.get();
        }
        return null;
    }

}
