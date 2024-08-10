package org.example.preonboarding.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.preonboarding.domain.Company;

@Getter
@Builder
public class CompanyInfo {

    private Long id;

    private String name;

    private String country;

    private String region;

    public static CompanyInfo from(Company company) {
        return CompanyInfo.builder()
                .id(company.getId())
                .name(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .build();
    }

}
