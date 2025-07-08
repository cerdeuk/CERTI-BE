package org.sopt.certi_server.global.error.code;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {

	/* 400 Bad Request */

	BAD_REQUEST_DATA(HttpStatus.BAD_REQUEST, "E400001", "잘못된 요청입니다"),
	INVALID_FIELD_ERROR(HttpStatus.BAD_REQUEST, "E400002", "요청 필드 값이 유효하지 않습니다."),
	MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "E400003", "필수 요청 파라미터가 누락되었습니다"),
	MISSING_HEADER(HttpStatus.BAD_REQUEST, "E400004", "필수 요청 헤더가 누락되었습니다."),
	TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "E400005", "요청 값 타입이 올바르지 않습니다"),
	DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "E400006", "데이터 무결성 제약 조건을 위반했습니다"),
	TEST_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "E400006", "요청 테스트 타입이 올바르지 않습니다"),

	/* 401 */
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E401001", "리소스 접근 권한이 없습니다."),

	/* 403  FORBIDDEN */

	ACCESS_DENIED(HttpStatus.FORBIDDEN, "E403000", "접근 권한이 없습니다."),

	/* 404 NOT FOUND */

	DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "E404001", "데이터가 존재하지 않습니다"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404002", "유저가 존재하지 않습니다"),
	MAJOR_NOT_FOUND(HttpStatus.NOT_FOUND, "E404003", "존재하지 않는 전공입니다."),

	/* 409 CONFLICT */

	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "E409001", "리소스 중복입니다"),

	/* 500 INTERNAL SERVER ERROR */

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500001", "서버 내부에서 오류가 발생했습니다");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}



