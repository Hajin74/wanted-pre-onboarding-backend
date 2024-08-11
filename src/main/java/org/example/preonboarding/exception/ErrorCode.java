package org.example.preonboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    COMPANY_NOT_FOUND("회사를 찾을 수 없습니다."),
    INVALID_COMPANY_ACCESS("접근 권한이 없는 회사입니다."),
    JOB_OPENING_NOT_FOUND("채용 공고를 찾을 수 없습니다."),
    APPLY_NOT_FOUND("지원 내역을 찾을 수 없습니다."),
    APPLY_ALREADY_EXISTS("이미 해당 채용 공고에 지원한 내역이 있습니다."),
    INVALID_APPLY_STATUS("유효한 지원 상태가 아닙니다."),
    VALIDATION_ERROR("유효하지 않은 값은 입력할 수 없습니다.");

    private final String errorMessage;

}
