package org.example.preonboarding.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobPosting;

@Getter
@Builder
public class JobPostingOverViewResponse {

    private Long jobPostingId;
    private String companyName;
    private String country;
    private String region;
    private String jobPosition;
    private int signingBonus;
    private String techStack;

    public static JobPostingOverViewResponse from(JobPosting jobPosting, Company company) {
        return JobPostingOverViewResponse.builder()
                .jobPostingId(jobPosting.getId())
                .companyName(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .jobPosition(jobPosting.getJobPosition())
                .signingBonus(jobPosting.getSigningBonus())
                .techStack(jobPosting.getTechStack())
                .build();
    }

}
