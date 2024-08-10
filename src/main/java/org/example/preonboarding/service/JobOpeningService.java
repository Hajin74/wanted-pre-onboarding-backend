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

    private final JobOpeningRepository jobOpeningRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void createJobOpening(CreateJobOpeningRequest createJobOpeningRequest) {
        boolean isExistedCompany = companyRepository.existsById(createJobOpeningRequest.getCompanyId());
        if (!isExistedCompany) {
            return;
            // todo : 예외 던지기
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
        JobOpening targetJobOpening = jobOpeningRepository.findById(jobOpeningId).orElse(null);

        if (targetJobOpening == null) {
            // todo: null이면 예외 던지기
            return;
        }

        if (!targetJobOpening.getCompanyId().equals(companyId)) {
            // todo: 수정하려는 공고의 회사가, 접근 회사와 동일한지 확인
            return;
        }

        targetJobOpening.updateJobOpening(updateJobOpeningRequest);
    }

    @Transactional
    public void deleteJobOpening(Long jobOpeningId, Long companyId) {
        JobOpening targetJobOpening = jobOpeningRepository.findById(jobOpeningId).orElse(null);

        if (targetJobOpening == null) {
            // todo: null이면 예외 던지기
            return;
        }

        if (!targetJobOpening.getCompanyId().equals(companyId)) {
            // todo: 삭제하려는 공고의 회사가, 접근 회사와 동일한지 확인
            return;
        }

        jobOpeningRepository.delete(targetJobOpening);
    }

    @Transactional(readOnly = true)
    public List<JobOpeningOverViewResponse> getAllJobOpenings() {
        List<JobOpeningOverViewResponse> allJobOpening =  new ArrayList<>();
        List<JobOpening> jobOpenings = jobOpeningRepository.findAll();
        for (JobOpening jobOpening : jobOpenings) {
            Company company = companyRepository.findById(jobOpening.getCompanyId()).orElse(null);
            if (company == null) {
                // todo: 예외 처리하기
                break;
            }

            JobOpeningOverViewResponse jobOpeningItem = JobOpeningOverViewResponse.from(jobOpening, company);
            allJobOpening.add(jobOpeningItem);
        }
        return allJobOpening;
    }

    @Transactional(readOnly = true)
    public JobOpeningDetailResponse getDetailJobOpening(Long jobOpeningId) {
        JobOpening targetJobOpening = jobOpeningRepository.findById(jobOpeningId).orElse(null);
        if (targetJobOpening == null) {
            // todo: 예외 처리하기
            return null;
        }

        Company company = companyRepository.findById(targetJobOpening.getCompanyId()).orElse(null);
        if (company == null) {
            // todo: 예외 처리하기
            return null;
        }

        List<Long> otherJobOpeningIdsByCompany = jobOpeningRepository.findByCompanyId(targetJobOpening.getCompanyId())
                .stream()
                .map(JobOpening::getId)
                .filter(id -> !id.equals(targetJobOpening.getId()))
                .toList();

        return JobOpeningDetailResponse.from(targetJobOpening, company, otherJobOpeningIdsByCompany);
    }

}
