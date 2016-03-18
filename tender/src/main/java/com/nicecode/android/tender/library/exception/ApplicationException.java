package com.nicecode.android.tender.library.exception;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 27.01.16.
 */

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -872035483640535907L;

    public ApplicationException(String detailMessage) {
        super(detailMessage);
    }

    public ApplicationException(Throwable throwable) {
        super(throwable);
    }

    public ApplicationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
