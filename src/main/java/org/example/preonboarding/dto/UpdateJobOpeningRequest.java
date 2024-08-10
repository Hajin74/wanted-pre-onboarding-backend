package org.example.preonboarding.dto;

import lombok.Data;

@Data
public class UpdateJobOpeningRequest {

    private String jobPosition;
    private int signingBonus;
    private String jobDescription;
    private String techStack;

}
