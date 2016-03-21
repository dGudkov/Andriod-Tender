package com.nicecode.android.tender.library.async;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 21.03.16.
 */

public interface AsyncTaskManager {

    void onTaskCreate(AsyncTask asyncTask);
    void onTaskStop(AsyncTask asyncTask);

}
