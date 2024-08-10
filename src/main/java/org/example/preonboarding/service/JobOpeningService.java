package org.example.preonboarding.service;

import lombok.RequiredArgsConstructor;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;
import org.example.preonboarding.dto.*;
import org.example.preonboarding.repository.CompanyRepository;
import org.example.preonboarding.repository.JobOpeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOpeningService {

    private final JobOpeningRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void createJobPosting(CreateJobOpeningRequest createJobPostingRequest) {
        boolean isExistedCompany = companyRepository.existsById(createJobPostingRequest.getCompanyId());
        if (!isExistedCompany) {
            return;
            // todo : 예외 던지기
        }

        JobOpening jobPosting = JobOpening.builder()
                .companyId(createJobPostingRequest.getCompanyId())
                .jobPosition(createJobPostingRequest.getJobPosition())
                .signingBonus(createJobPostingRequest.getSigningBonus())
                .jobDescription(createJobPostingRequest.getJobDescription())
                .techStack(createJobPostingRequest.getTechStack())
                .build();
        jobPostingRepository.save(jobPosting);
    }

    @Transactional
    public void updateJobPosting(Long jobPostingId, Long companyId, UpdateJobOpeningRequest updateJobPostingRequest) {
        JobOpening targetJobPosting = jobPostingRepository.findById(jobPostingId).orElse(null);

        if (targetJobPosting == null) {
            // todo: null이면 예외 던지기
            return;
        }

        if (!targetJobPosting.getCompanyId().equals(companyId)) {
            // todo: 수정하려는 공고의 회사가, 접근 회사와 동일한지 확인
            return;
        }

        targetJobPosting.updateJobPosting(updateJobPostingRequest);
    }

    @Transactional
    public void deleteJobPosting(Long jobPostingId, Long companyId) {
        JobOpening targetJobPosting = jobPostingRepository.findById(jobPostingId).orElse(null);

        if (targetJobPosting == null) {
            // todo: null이면 예외 던지기
            return;
        }

        if (!targetJobPosting.getCompanyId().equals(companyId)) {
            // todo: 삭제하려는 공고의 회사가, 접근 회사와 동일한지 확인
            return;
        }

        jobPostingRepository.delete(targetJobPosting);
    }

    @Transactional(readOnly = true)
    public List<JobOpeningOverViewResponse> getAllJobPostings() {
        List<JobOpeningOverViewResponse> allJobPosting =  new ArrayList<>();
        List<JobOpening> jobPostings = jobPostingRepository.findAll();
        for (JobOpening jobPosting : jobPostings) {
            Company company = companyRepository.findById(jobPosting.getCompanyId()).orElse(null);
            if (company == null) {
                // todo: 예외 처리하기
                break;
            }

            JobOpeningOverViewResponse jobPostingItem = JobOpeningOverViewResponse.from(jobPosting, company);
            allJobPosting.add(jobPostingItem);
        }
        return allJobPosting;
    }

    @Transactional(readOnly = true)
    public JobOpeningDetailResponse getDetailJobPosting(Long jobPostingId) {
        JobOpening targetJobPosting = jobPostingRepository.findById(jobPostingId).orElse(null);
        if (targetJobPosting == null) {
            // todo: 예외 처리하기
            return null;
        }

        Company company = companyRepository.findById(targetJobPosting.getCompanyId()).orElse(null);
        if (company == null) {
            // todo: 예외 처리하기
            return null;
        }

        List<Long> otherJobPostingIdsByCompany = jobPostingRepository.findByCompanyId(targetJobPosting.getCompanyId())
                .stream()
                .map(JobOpening::getId)
                .filter(id -> !id.equals(targetJobPosting.getId()))
                .toList();

        return JobOpeningDetailResponse.from(targetJobPosting, company, otherJobPostingIdsByCompany);
    }

}
