package com.example.week08.errorhandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    //member
    MEMBER_NOT_EXIST(400, "M001", "MEMBER_NOT_EXIST(회원이 존재하지 않습니다.)"),
    MEMBER_NOT_FOUND(400, "M001", "MEMBER_NOT_FOUND(회원을 찾을 수 없습니다.)"),
    MEMBER_NOT_EMAIL_AUTH(403, "M002", "Member didn't email auth(이메일 인증이 필요합니다.)"),
    LOGIN_INPUT_INVALID(400, "M003", "Login input is invalid(로그인 시 잘못된 입력입니다.)"),
    DUPLICATED_USER_EMAIL(400, "M004", "Email input is duprication(이미 존재하는 이메일 입니다.)"),
    DUPLICATED_USER_NICKNAME(400, "M004", "Nickname input is duprication(이미 존재하는 닉네임 입니다.)"),
    PASSWORDS_NOT_MATCHED(400, "M005", "Password and PasswordConfirm do not match. (비밀번호와 비밀번호 확인이 일치하지 않습니다.)"),

    //Heart
    FAIL_HEART (400, "H001", "Heart is duprication (이미 관심항목에 추가된 게시글 입니다.)"),
    FAIL_DISHEART (400, "H002", "Heart is Empty (해당 관심 항목이 존재하지 않습니다.)"),


    //JWT
    JWT_NOT_PERMIT(400, "J001", "JWT is NOT PERMIT(존재하지 않는 Token 입니다.)"),
    JWT_INVALID_TOKEN(401, "J002", "TOKEN IS INVALID(토큰이 유효하지 않습니다.)"),

    //email
    MAIL_SEND_FAIL(500, "M001", "Transmission failed(메일 전송에 실패했습니다.)"),
    MAIL_AUTH_INCORRECT(400, "M002", "Auth key is incorrect(인증 키가 올바르지 않습니다.)"),

    //file
    FILE_NO_EXIST(500, "F001", "File is no exixt(파일이 존재하지 않습니다.)"),
//    // Member
//    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
//    EMAIL_INPUT_INVALID(400, "M002", "Sign input is invalid"),

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
