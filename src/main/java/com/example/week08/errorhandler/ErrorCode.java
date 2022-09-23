package com.example.week08.errorhandler;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),


    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    EMAIL_INPUT_INVALID(400, "M002", "Sign input is invalid"),

    MEMBER_NOT_EXIST(400, "M001", "MEMBER_NOT_EXIST"),

    //JWT
    JWT_NOT_PERMIT(400, "J001", "JWT is NOT PERMIT"),

    //POST
    POST_NOT_EXIST(400, "P001", "POST_NOT_EXIST"),

    //PLACE
    PLACE_NOT_EXIST(400, "P001", "PLACE_NOT_EXIST"),


    //COMMENT
    COMMENT_NOT_EXIST(400, "C001", "COMMENT_NOT_EXIST"),
    MEMBER_NOT_EQUALS(400, "C002", "MEMBER_NOT_EQUALS"),
    //SUB COMMENT
    SUBCOMMENT_NOT_EXIST(400, "C001", "SUBCOMMENT_NOT_EXIST"),
    //MAIL
    NUMBER_NOT_PERMIT(400, "M010", "NUMBER_NOT_PERMIT"),
    EMAIL_NOT_EXIST(400, "M011", "EMAIL_NOT_EXIST"),
            ;
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

}
