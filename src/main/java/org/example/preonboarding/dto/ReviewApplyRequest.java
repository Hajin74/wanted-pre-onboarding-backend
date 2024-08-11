package org.example.preonboarding.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewApplyRequest {
    @NotNull(message = "지원서 아이디는 필수 입력 값입니다.")
    private Long applyId;

    @NotNull(message = "회사 아이디는 필수 입력 값입니다.")
    private Long companyId;
}
