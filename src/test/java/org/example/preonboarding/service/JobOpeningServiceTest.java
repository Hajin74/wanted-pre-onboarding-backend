package org.example.preonboarding.service;

import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;
import org.example.preonboarding.dto.CreateJobOpeningRequest;
import org.example.preonboarding.exception.CustomException;
import org.example.preonboarding.exception.ErrorCode;
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
class JobOpeningServiceTest {

    @Autowired
    JobOpeningService jobOpeningService;
    @Autowired
    JobOpeningRepository jobOpeningRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        jobOpeningRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("채용 공고를 등록합니다.")
    void create_job_opening() {
        // given
        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        CreateJobOpeningRequest createJobOpeningRequest = new CreateJobOpeningRequest();
        createJobOpeningRequest.setCompanyId(company.getId());
        createJobOpeningRequest.setJobPosition("파이썬 개발자 [채용솔루션팀]");
        createJobOpeningRequest.setSigningBonus(1000000);
        createJobOpeningRequest.setJobDescription("서비스 기능 제공을 위한 REST API 개발");
        createJobOpeningRequest.setTechStack("FastApi, Flask, Django");

        // when
        jobOpeningService.createJobOpening(createJobOpeningRequest);

        // then
        JobOpening newJobOpening = jobOpeningRepository.findAll().get(0);
        assertEquals(company.getId(), newJobOpening.getCompanyId());
        assertEquals("파이썬 개발자 [채용솔루션팀]", newJobOpening.getJobPosition());
        assertEquals("FastApi, Flask, Django", newJobOpening.getTechStack());
    }

    @Test
    @DisplayName("존재하지 않은 회사가 채용 공고를 등록하여 실패합니다.")
    void create_job_opening_by_nonexistent_company() {
        // given
        CreateJobOpeningRequest createJobOpeningRequest = new CreateJobOpeningRequest();
        createJobOpeningRequest.setCompanyId(404L);
        createJobOpeningRequest.setJobPosition("파이썬 개발자 [채용솔루션팀]");
        createJobOpeningRequest.setSigningBonus(1000000);
        createJobOpeningRequest.setJobDescription("서비스 기능 제공을 위한 REST API 개발");
        createJobOpeningRequest.setTechStack("FastApi, Flask, Django");

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jobOpeningService.createJobOpening(createJobOpeningRequest);
        });

        // then
        assertEquals(ErrorCode.COMPANY_NOT_FOUND, exception.getErrorCode());
    }

}