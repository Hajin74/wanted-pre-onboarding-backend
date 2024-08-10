package org.example.preonboarding.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.preonboarding.dto.UpdateJobPostingRequest;

@Entity
@Getter
@NoArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private String jobPosition;

    private String techStack;

    private String jobDescription;

    private int signingBonus;

    @Builder
    public JobPosting(Long companyId, String jobPosition, String techStack, String jobDescription, int signingBonus) {
        this.companyId = companyId;
        this.jobPosition = jobPosition;
        this.techStack = techStack;
        this.jobDescription = jobDescription;
        this.signingBonus = signingBonus;
    }

    public void updateJobPosting(UpdateJobPostingRequest updateJobPostingRequest) {
        this.jobPosition = updateJobPostingRequest.getJobPosition();
        this.signingBonus = updateJobPostingRequest.getSigningBonus();
        this.jobDescription = updateJobPostingRequest.getJobDescription();
        this.techStack = updateJobPostingRequest.getTechStack();
    }

}
