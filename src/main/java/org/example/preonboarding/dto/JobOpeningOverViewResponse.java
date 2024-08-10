package org.example.preonboarding.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;

@Getter
@Builder
public class JobOpeningOverViewResponse {

    private Long jobOpeningId;
    private String companyName;
    private String country;
    private String region;
    private String jobPosition;
    private int signingBonus;
    private String techStack;

    public static JobOpeningOverViewResponse from(JobOpening jobPosting, Company company) {
        return JobOpeningOverViewResponse.builder()
                .jobOpeningId(jobPosting.getId())
                .companyName(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .jobPosition(jobPosting.getJobPosition())
                .signingBonus(jobPosting.getSigningBonus())
                .techStack(jobPosting.getTechStack())
                .build();
    }

}
