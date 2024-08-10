package org.example.preonboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    COMPANY_NOT_FOUND("회사를 찾을 수 없습니다."),
    INVALID_COMPANY_ACCESS("접근 권한이 없는 회사입니다."),
    JOB_OPENING_NOT_FOUND("채용 공고를 찾을 수 없습니다.");

    private final String errorMessage;

}
