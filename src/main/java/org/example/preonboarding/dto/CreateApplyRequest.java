package org.example.preonboarding.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateApplyRequest {

    @NotNull(message = "사용자 아이디는 필수 입력 값입니다.")
    private Long userId;

    @NotNull(message = "채용 공고 아이디는 필수 입력 값입니다.")
    private Long jobOpeningId;

}
