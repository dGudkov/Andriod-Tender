package com.nicecode.android.tender.library.interfaces;

import android.content.Context;
import android.view.View;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 21.03.16.
 */

public interface IScrollViewData {
    View initViews(Context context);
    View cloneView();
    void clean();
}
