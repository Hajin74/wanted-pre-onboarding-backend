package org.example.preonboarding.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.preonboarding.domain.JobPosting;

@Getter
@Builder
public class JobPostingDetailResponse {

    private Long jobPostingId;
    private String jobPosition;
    private int signingBonus;
    private String techStack;
    private String jobDescription;
    private CompanyInfo company;

    public static JobPostingDetailResponse from(JobPosting jobPosting, CompanyInfo companyInfo) {
        return JobPostingDetailResponse.builder()
                .jobPostingId(jobPosting.getId())
                .jobPosition(jobPosting.getJobPosition())
                .signingBonus(jobPosting.getSigningBonus())
                .techStack(jobPosting.getTechStack())
                .jobDescription(jobPosting.getJobDescription())
                .company(companyInfo)
                .build();
    }

}
