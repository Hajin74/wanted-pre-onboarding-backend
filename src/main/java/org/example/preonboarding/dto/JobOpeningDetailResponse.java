package org.example.preonboarding.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;

import java.util.List;

@Getter
@Builder
public class JobOpeningDetailResponse {

    private Long jobOpeningId;
    private String companyName;
    private String country;
    private String region;
    private String jobPosition;
    private int signingBonus;
    private String techStack;
    private String jobDescription;
    private List<Long> otherJobOpeningIdsByCompany;

    public static JobOpeningDetailResponse from(JobOpening jobOpening, Company company, List<Long> otherJobOpeningIdsByCompany) {
        return JobOpeningDetailResponse.builder()
                .jobOpeningId(jobOpening.getId())
                .companyName(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .jobPosition(jobOpening.getJobPosition())
                .signingBonus(jobOpening.getSigningBonus())
                .techStack(jobOpening.getTechStack())
                .jobDescription(jobOpening.getJobDescription())
                .otherJobOpeningIdsByCompany(otherJobOpeningIdsByCompany)
                .build();
    }

}
