package com.nicecode.android.tender.library.interfaces;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 21.03.16.
 */

public interface OnAdapterHolderAdded<T extends IScrollViewData> {

    void onHolderAdded(T scrollViewData);

}
