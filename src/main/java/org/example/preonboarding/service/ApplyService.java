package org.example.preonboarding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.preonboarding.domain.Apply;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;
import org.example.preonboarding.domain.User;
import org.example.preonboarding.dto.AcceptApplyRequest;
import org.example.preonboarding.dto.CreateApplyRequest;
import org.example.preonboarding.dto.RejectApplyRequest;
import org.example.preonboarding.dto.ReviewApplyRequest;
import org.example.preonboarding.enums.ApplyStatus;
import org.example.preonboarding.exception.CustomException;
import org.example.preonboarding.exception.ErrorCode;
import org.example.preonboarding.repository.ApplyRepository;
import org.example.preonboarding.repository.CompanyRepository;
import org.example.preonboarding.repository.JobOpeningRepository;
import org.example.preonboarding.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyService {

    private final JobOpeningRepository jobOpeningRepository;
    private final CompanyRepository companyRepository;
    private final ApplyRepository applyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void applyForJobOpening(CreateApplyRequest createApplyRequest) {
        JobOpening targetJobOpening = jobOpeningRepository.findById(createApplyRequest.getJobOpeningId())
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));
        User applicant = userRepository.findById(createApplyRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean isExistedApply = applyRepository.existsByUserIdAndJobOpeningId(applicant.getId(), targetJobOpening.getId());
        if (isExistedApply) {
            throw new CustomException(ErrorCode.APPLY_ALREADY_EXISTS);
        }

        Apply apply = Apply.builder()
                .userId(applicant.getId())
                .jobOpeningId(targetJobOpening.getId())
                .applyStatus(ApplyStatus.APPLIED)
                .build();
        applyRepository.save(apply);
    }

    @Transactional
    public void reviewApply(ReviewApplyRequest reviewApplyRequest) {
        Apply targetApply = applyRepository.findById(reviewApplyRequest.getApplyId())
                .orElseThrow(() -> new CustomException(ErrorCode.APPLY_NOT_FOUND));
        Company company = companyRepository.findById(reviewApplyRequest.getCompanyId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
        JobOpening jobOpening = jobOpeningRepository.findById(targetApply.getJobOpeningId())
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));

        if (!jobOpening.getCompanyId().equals(company.getId())) {
            throw new CustomException(ErrorCode.INVALID_COMPANY_ACCESS);
        }

        if (!targetApply.getApplyStatus().equals(ApplyStatus.APPLIED)) {
            throw new CustomException(ErrorCode.INVALID_APPLY_STATUS);
        }

        targetApply.reviewed();
    }

    @Transactional
    public void acceptApply(AcceptApplyRequest acceptApplyRequest) {
        Apply targetApply = applyRepository.findById(acceptApplyRequest.getApplyId())
                .orElseThrow(() -> new CustomException(ErrorCode.APPLY_NOT_FOUND));
        Company company = companyRepository.findById(acceptApplyRequest.getCompanyId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
        JobOpening jobOpening = jobOpeningRepository.findById(targetApply.getJobOpeningId())
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));

        if (!jobOpening.getCompanyId().equals(company.getId())) {
            throw new CustomException(ErrorCode.INVALID_COMPANY_ACCESS);
        }

        if (!targetApply.getApplyStatus().equals(ApplyStatus.REVIEWED)) {
            throw new CustomException(ErrorCode.INVALID_APPLY_STATUS);
        }

        targetApply.accepted();
    }

    @Transactional
    public void rejectApply(RejectApplyRequest rejectApplyRequest) {
        Apply targetApply = applyRepository.findById(rejectApplyRequest.getApplyId())
                .orElseThrow(() -> new CustomException(ErrorCode.APPLY_NOT_FOUND));
        Company company = companyRepository.findById(rejectApplyRequest.getCompanyId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
        JobOpening jobOpening = jobOpeningRepository.findById(targetApply.getJobOpeningId())
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));

        if (!jobOpening.getCompanyId().equals(company.getId())) {
            throw new CustomException(ErrorCode.INVALID_COMPANY_ACCESS);
        }

        if (!targetApply.getApplyStatus().equals(ApplyStatus.REVIEWED)) {
            throw new CustomException(ErrorCode.INVALID_APPLY_STATUS);
        }

        targetApply.rejected();
    }

}
