package org.example.preonboarding.service;

import org.example.preonboarding.domain.Apply;
import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;
import org.example.preonboarding.domain.User;
import org.example.preonboarding.dto.CreateApplyRequest;
import org.example.preonboarding.exception.CustomException;
import org.example.preonboarding.exception.ErrorCode;
import org.example.preonboarding.repository.ApplyRepository;
import org.example.preonboarding.repository.CompanyRepository;
import org.example.preonboarding.repository.JobOpeningRepository;
import org.example.preonboarding.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ApplyServiceTest {

    @Autowired
    ApplyService applyService;
    @Autowired
    JobOpeningRepository jobOpeningRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplyRepository applyRepository;

    @BeforeEach
    void init() {
        jobOpeningRepository.deleteAll();
        companyRepository.deleteAll();
        userRepository.deleteAll();
        applyRepository.deleteAll();;
    }

    @Test
    @DisplayName("사용자가 채용 공고에 지원합니다.")
    void apply_for_job_opening() {
        // given
        User applicant = new User("minji@gmail.com", "1234", "김민지");
        userRepository.save(applicant);

        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        JobOpening jobOpening = JobOpening.builder()
                .companyId(company.getId())
                .jobPosition("파이썬 개발자 [채용솔루션팀]")
                .jobDescription("서비스 기능 제공을 위한 REST API 개발")
                .techStack("FastApi, Flask, Django")
                .signingBonus(1000000)
                .build();
        jobOpeningRepository.save(jobOpening);

        CreateApplyRequest applyRequest = new CreateApplyRequest();
        applyRequest.setUserId(applicant.getId());
        applyRequest.setJobOpeningId(jobOpening.getId());

        // when
        applyService.applyForJobOpening(applyRequest);

        // then
        Apply apply = applyRepository.findAll().get(0);
        assertNotNull(apply);
        assertEquals(jobOpening.getId(), apply.getJobOpeningId());
        assertEquals(applicant.getId(), apply.getUserId());
    }

    @Test
    @DisplayName("사용자가 존재하지 않은 채용 공고에 지원하여 실패합니다.")
    void apply_for_nonexistent_job_opening() {
        // given
        User applicant = new User("minji@gmail.com", "1234", "김민지");
        userRepository.save(applicant);

        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        CreateApplyRequest applyRequest = new CreateApplyRequest();
        applyRequest.setUserId(applicant.getId());
        applyRequest.setJobOpeningId(404L);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            applyService.applyForJobOpening(applyRequest);
        });

        // then
        assertEquals(ErrorCode.JOB_OPENING_NOT_FOUND, exception.getErrorCode());
    }

}