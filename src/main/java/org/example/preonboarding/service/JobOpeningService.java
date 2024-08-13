package org.example.preonboarding.service;

import lombok.RequiredArgsConstructor;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;
import org.example.preonboarding.dto.*;
import org.example.preonboarding.exception.CustomException;
import org.example.preonboarding.exception.ErrorCode;
import org.example.preonboarding.repository.CompanyRepository;
import org.example.preonboarding.repository.JobOpeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void createJobOpening(CreateJobOpeningRequest createJobOpeningRequest) {
        boolean isExistedCompany = companyRepository.existsById(createJobOpeningRequest.getCompanyId());
        if (!isExistedCompany) {
            throw new CustomException(ErrorCode.COMPANY_NOT_FOUND);
        }

        JobOpening jobOpening = JobOpening.builder()
                .companyId(createJobOpeningRequest.getCompanyId())
                .jobPosition(createJobOpeningRequest.getJobPosition())
                .signingBonus(createJobOpeningRequest.getSigningBonus())
                .jobDescription(createJobOpeningRequest.getJobDescription())
                .techStack(createJobOpeningRequest.getTechStack())
                .build();
        jobOpeningRepository.save(jobOpening);
    }

    @Transactional
    public void updateJobOpening(Long jobOpeningId, Long companyId, UpdateJobOpeningRequest updateJobOpeningRequest) {
        JobOpening targetJobOpening = jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));

        if (!targetJobOpening.getCompanyId().equals(companyId)) {
            throw new CustomException(ErrorCode.INVALID_COMPANY_ACCESS);
        }

        targetJobOpening.updateJobOpening(updateJobOpeningRequest);
    }

    @Transactional
    public void deleteJobOpening(Long jobOpeningId, Long companyId) {
        JobOpening targetJobOpening = jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));

        if (!targetJobOpening.getCompanyId().equals(companyId)) {
            throw new CustomException(ErrorCode.INVALID_COMPANY_ACCESS);
        }

        jobOpeningRepository.delete(targetJobOpening);
    }

    @Transactional(readOnly = true)
    public List<JobOpeningOverViewResponse> getAllJobOpenings() {
        List<JobOpeningOverViewResponse> allJobOpening =  new ArrayList<>();
        List<JobOpening> jobOpenings = jobOpeningRepository.findAll();
        for (JobOpening jobOpening : jobOpenings) {
            Company company = companyRepository.findById(jobOpening.getCompanyId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

            JobOpeningOverViewResponse jobOpeningItem = JobOpeningOverViewResponse.from(jobOpening, company);
            allJobOpening.add(jobOpeningItem);
        }
        return allJobOpening;
    }

    @Transactional(readOnly = true)
    public JobOpeningDetailResponse getDetailJobOpening(Long jobOpeningId) {
        JobOpening targetJobOpening = jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));

        Company company = companyRepository.findById(targetJobOpening.getCompanyId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

        List<Long> otherJobOpeningIdsByCompany = jobOpeningRepository.findByCompanyId(targetJobOpening.getCompanyId())
                .stream()
                .map(JobOpening::getId)
                .filter(id -> !id.equals(targetJobOpening.getId()))
                .toList();

        return JobOpeningDetailResponse.from(targetJobOpening, company, otherJobOpeningIdsByCompany);
    }

    @Transactional(readOnly = true)
    public List<JobOpeningOverViewResponse> getJobOpeningBySearch(String keyword) {
        List<JobOpeningOverViewResponse> jobOpeningBySearch =  new ArrayList<>();
        List<JobOpening> jobOpenings = jobOpeningRepository.findByKeyword(keyword);
        for (JobOpening jobOpening : jobOpenings) {
            Company company = companyRepository.findById(jobOpening.getCompanyId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

            JobOpeningOverViewResponse jobOpeningItem = JobOpeningOverViewResponse.from(jobOpening, company);
            jobOpeningBySearch.add(jobOpeningItem);
        }
        return jobOpeningBySearch;
    }

}
