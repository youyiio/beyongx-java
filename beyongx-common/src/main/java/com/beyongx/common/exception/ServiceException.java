package com.beyongx.common.exception;

import com.beyongx.common.vo.Result;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    private int code = Result.Code.ACTION_FAILED;

	public ServiceException(int code) {
		super();
        this.code = code;
	}

	public ServiceException(int code, String message) {
		super(message);
        this.code = code;
	}

	public ServiceException(int code, Throwable cause) {
		super(cause);
        this.code = code;
	}

    public int getCode() {
        return code;
    }
}
