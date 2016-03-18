package com.nicecode.android.tender.library.exception;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @since 19.11.15
 */
public class HttpException extends ApplicationException {

    private static final long serialVersionUID = -1952903382099030118L;

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}

