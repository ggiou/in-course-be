package com.example.week08.errorhandler;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public BusinessException(String message, ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
//요구사항에 맞지 않을 경우 발생기키는 Exception(예외처리)