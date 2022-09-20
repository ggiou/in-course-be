package com.example.week08.errorhandler;

public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
//요구사항에 맞지 않을 경우 발생기키는 Exception(예외처리)