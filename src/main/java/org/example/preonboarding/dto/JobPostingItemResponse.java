package org.example.preonboarding.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.preonboarding.domain.JobPosting;

@Getter
@Builder
public class JobPostingItemResponse {

    private Long jobPostingId;
    private String jobPosition;
    private int signingBonus;
    private String techStack;
    private CompanyInfo company;

    public static JobPostingItemResponse from(JobPosting jobPosting, CompanyInfo companyInfo) {
        return JobPostingItemResponse.builder()
                .jobPostingId(jobPosting.getId())
                .jobPosition(jobPosting.getJobPosition())
                .signingBonus(jobPosting.getSigningBonus())
                .techStack(jobPosting.getTechStack())
                .company(companyInfo)
                .build();
    }

}
