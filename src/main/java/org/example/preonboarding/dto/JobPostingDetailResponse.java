package org.example.preonboarding.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobPosting;

import java.util.List;

@Getter
@Builder
public class JobPostingDetailResponse {

    private Long jobPostingId;
    private String companyName;
    private String country;
    private String region;
    private String jobPosition;
    private int signingBonus;
    private String techStack;
    private String jobDescription;
    private List<Long> otherJobPostingIdsByCompany;

    public static JobPostingDetailResponse from(JobPosting jobPosting, Company company, List<Long> otherJobPostingIdsByCompany) {
        return JobPostingDetailResponse.builder()
                .jobPostingId(jobPosting.getId())
                .companyName(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .jobPosition(jobPosting.getJobPosition())
                .signingBonus(jobPosting.getSigningBonus())
                .techStack(jobPosting.getTechStack())
                .jobDescription(jobPosting.getJobDescription())
                .otherJobPostingIdsByCompany(otherJobPostingIdsByCompany)
                .build();
    }

}
