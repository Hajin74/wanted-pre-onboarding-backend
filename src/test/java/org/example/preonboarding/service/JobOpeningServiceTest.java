package org.example.preonboarding.service;

import org.example.preonboarding.domain.Company;
import org.example.preonboarding.domain.JobOpening;
import org.example.preonboarding.dto.CreateJobOpeningRequest;
import org.example.preonboarding.dto.JobOpeningDetailResponse;
import org.example.preonboarding.dto.JobOpeningOverViewResponse;
import org.example.preonboarding.dto.UpdateJobOpeningRequest;
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

import java.util.List;

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

    @Test
    @DisplayName("채용 공고를 수정합니다.")
    void update_job_opening() {
        // given
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

        UpdateJobOpeningRequest updateJobOpeningRequest = new UpdateJobOpeningRequest();
        updateJobOpeningRequest.setJobPosition("파이썬 개발자 - 채용솔루션팀");
        updateJobOpeningRequest.setJobDescription("서비스 기능 제공을 위한 REST API 개발");
        updateJobOpeningRequest.setSigningBonus(500000);
        updateJobOpeningRequest.setTechStack("FastApi, Flask, Django 등");

        // when
        jobOpeningService.updateJobOpening(jobOpening.getId(), company.getId(), updateJobOpeningRequest);

        // then
        JobOpening updatedJobOpening = jobOpeningRepository.findById(jobOpening.getId()).orElse(null);
        assertNotNull(updatedJobOpening);
        assertEquals(company.getId(), updatedJobOpening.getCompanyId());
        assertEquals(updateJobOpeningRequest.getJobPosition(), updatedJobOpening.getJobPosition());
        assertEquals(updateJobOpeningRequest.getJobDescription(), updatedJobOpening.getJobDescription());
        assertEquals(updateJobOpeningRequest.getSigningBonus(), updatedJobOpening.getSigningBonus());
        assertEquals(updateJobOpeningRequest.getTechStack(), updatedJobOpening.getTechStack());
    }

    @Test
    @DisplayName("존재하지 않은 채용 공고를 수정하여 실패합니다.")
    void update_nonexistent_job_opening() {
        // given
        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        UpdateJobOpeningRequest updateJobOpeningRequest = new UpdateJobOpeningRequest();
        updateJobOpeningRequest.setJobPosition("파이썬 개발자 - 채용솔루션팀");
        updateJobOpeningRequest.setJobDescription("서비스 기능 제공을 위한 REST API 개발");
        updateJobOpeningRequest.setSigningBonus(500000);
        updateJobOpeningRequest.setTechStack("FastApi, Flask, Django 등");

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jobOpeningService.updateJobOpening(404L, company.getId(), updateJobOpeningRequest);
        });

        // then
        assertEquals(ErrorCode.JOB_OPENING_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("접근 권한이 없는 회사가 채용 공고를 수정하여 실패합니다.")
    void update_job_opening_by_invalid_company() {
        // given
        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        Company invalidAccessCompany = new Company("인프랩", "한국", "경기 성남시");
        companyRepository.save(company);

        JobOpening jobOpening = JobOpening.builder()
                .companyId(company.getId())
                .jobPosition("파이썬 개발자 [채용솔루션팀]")
                .jobDescription("서비스 기능 제공을 위한 REST API 개발")
                .techStack("FastApi, Flask, Django")
                .signingBonus(1000000)
                .build();
        jobOpeningRepository.save(jobOpening);

        UpdateJobOpeningRequest updateJobOpeningRequest = new UpdateJobOpeningRequest();
        updateJobOpeningRequest.setJobPosition("파이썬 개발자 - 채용솔루션팀");
        updateJobOpeningRequest.setJobDescription("서비스 기능 제공을 위한 REST API 개발");
        updateJobOpeningRequest.setSigningBonus(500000);
        updateJobOpeningRequest.setTechStack("FastApi, Flask, Django 등");

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jobOpeningService.updateJobOpening(jobOpening.getId(), invalidAccessCompany.getId(), updateJobOpeningRequest);
        });

        // then
        assertEquals(ErrorCode.INVALID_COMPANY_ACCESS, exception.getErrorCode());
    }

    @Test
    @DisplayName("채용 공고를 삭제합니다.")
    void delete_job_opening() {
        // given
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

        // when
        jobOpeningService.deleteJobOpening(jobOpening.getId(), company.getId());

        // then
        JobOpening deletedJobOpening = jobOpeningRepository.findById(jobOpening.getId()).orElse(null);
        assertNull(deletedJobOpening);
    }

    @Test
    @DisplayName("존재하지 않은 채용 공고를 수정하여 실패합니다.")
    void delete_nonexistent_job_opening() {
        // given
        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jobOpeningService.deleteJobOpening(404L, company.getId());
        });

        // then
        assertEquals(ErrorCode.JOB_OPENING_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("접근 권한이 없는 회사가 채용 공고를 삭제하여 실패합니다.")
    void delete_job_opening_by_invalid_company() {
        // given
        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        Company invalidAccessCompany = new Company("인프랩", "한국", "경기 성남시");
        companyRepository.save(company);

        JobOpening jobOpening = JobOpening.builder()
                .companyId(company.getId())
                .jobPosition("파이썬 개발자 [채용솔루션팀]")
                .jobDescription("서비스 기능 제공을 위한 REST API 개발")
                .techStack("FastApi, Flask, Django")
                .signingBonus(1000000)
                .build();
        jobOpeningRepository.save(jobOpening);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jobOpeningService.deleteJobOpening(jobOpening.getId(), invalidAccessCompany.getId());
        });

        // then
        assertEquals(ErrorCode.INVALID_COMPANY_ACCESS, exception.getErrorCode());
    }

    @Test
    @DisplayName("채용 공고 목록을 가져옵니다.")
    void get_all_job_openings() {
        // given
        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        JobOpening jobOpening1 = JobOpening.builder()
                .companyId(company.getId())
                .jobPosition("파이썬 개발자 [채용솔루션팀]")
                .jobDescription("서비스 기능 제공을 위한 REST API 개발")
                .techStack("FastApi, Flask, Django")
                .signingBonus(1000000)
                .build();
        jobOpeningRepository.save(jobOpening1);

        JobOpening jobOpening2 = JobOpening.builder()
                .companyId(company.getId())
                .jobPosition("Data Quality Specialist [데이터팀]")
                .jobDescription("Amplitude event & property 설계,작업요청,테스트,QA 프로세스를 자동화 및 고도화")
                .techStack("Python 고급, SQL 중급 능력")
                .signingBonus(1000000)
                .build();
        jobOpeningRepository.save(jobOpening2);

        // when
        List<JobOpeningOverViewResponse> allJobOpenings = jobOpeningService.getAllJobOpenings();

        // then
        assertEquals(2, allJobOpenings.size());

        assertEquals(jobOpening1.getId(), allJobOpenings.get(0).getJobOpeningId());
        assertEquals(jobOpening1.getJobPosition(), allJobOpenings.get(0).getJobPosition());
        assertEquals(jobOpening1.getTechStack(), allJobOpenings.get(0).getTechStack());
        assertEquals(jobOpening1.getSigningBonus(), allJobOpenings.get(0).getSigningBonus());

        assertEquals(jobOpening2.getId(), allJobOpenings.get(1).getJobOpeningId());
        assertEquals(jobOpening2.getJobPosition(), allJobOpenings.get(1).getJobPosition());
        assertEquals(jobOpening2.getTechStack(), allJobOpenings.get(1).getTechStack());
        assertEquals(jobOpening2.getSigningBonus(), allJobOpenings.get(1).getSigningBonus());
    }

    @Test
    @DisplayName("채용 공고 상세를 조회합니다.")
    void get_detail_job_opening() {
        // given
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

        // when
        JobOpeningDetailResponse jobOpeningDetail = jobOpeningService.getDetailJobOpening(jobOpening.getId());

        // then
        assertNotNull(jobOpeningDetail);
        assertEquals(jobOpening.getId(), jobOpeningDetail.getJobOpeningId());
        assertEquals(jobOpening.getJobDescription(), jobOpeningDetail.getJobDescription());
    }

    @Test
    @DisplayName("존재하지 않은 채용 공고를 조회하여 실패합니다.")
    void get_nonexistent_job_opening() {
        // given
        Company company = new Company("원티드랩", "한국", "서울 송파구");
        companyRepository.save(company);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            jobOpeningService.getDetailJobOpening(404L);
        });

        // then
        assertEquals(ErrorCode.JOB_OPENING_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("회사의 다른 채용 공고도 조회합니다.")
    void get_other_job_openings_by_company() {
        // given
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

        JobOpening otherJobOpening = JobOpening.builder()
                .companyId(company.getId())
                .jobPosition("Data Quality Specialist [데이터팀]")
                .jobDescription("Amplitude event & property 설계,작업요청,테스트,QA 프로세스를 자동화 및 고도화")
                .techStack("Python 고급, SQL 중급 능력")
                .signingBonus(1000000)
                .build();
        jobOpeningRepository.save(otherJobOpening);

        // when
        JobOpeningDetailResponse jobOpeningDetail = jobOpeningService.getDetailJobOpening(jobOpening.getId());

        // then
        List<Long> otherJobOpeningIds = jobOpeningDetail.getOtherJobOpeningIdsByCompany();
        assertEquals(1, otherJobOpeningIds.size());
        assertEquals(otherJobOpening.getId(), otherJobOpeningIds.get(0));
    }

}