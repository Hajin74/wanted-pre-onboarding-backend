package org.example.preonboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateJobPostingRequest {

    @NotNull(message = "회사 아이디는 필수 입력 값입니다.")
    private Long companyId;

    @NotBlank(message = "채용 포지션은 필수 입력 값입니다.")
    private String jobPosition;

    @NotNull(message = "채용 보상금은 필수 입력 값입니다.")
    private int signingBonus;

    @NotBlank(message = "채용 내용을 필수 입력 값입니다.")
    private String jobDescription;

    @NotBlank(message = "기술 스택은 필수 입력 값입니다.")
    private String techStack;

}
