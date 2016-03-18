package com.nicecode.android.tender.library.async;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public abstract class AsyncTask<Params, Progress, Result>  extends android.os.AsyncTask<Params, Progress, Result> {

    @Override
    protected void onPostExecute(Result result) {
        this.finalizeTask();
    }

    public void interrupt(boolean mayInterruptIfRunning) {
        if (!this.isCancelled()) {
            this.cancel(mayInterruptIfRunning);
        }
    }

    @Override
    protected void onCancelled() {
        finalizeTask();
    }

    protected  void stopIfCancelled() throws InterruptedException {
        if (isCancelled()) {
            throw new InterruptedException();
        }
    }

    protected abstract void finalizeTask();

}
