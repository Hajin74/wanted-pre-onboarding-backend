package org.example.preonboarding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.preonboarding.domain.Apply;
import org.example.preonboarding.domain.JobOpening;
import org.example.preonboarding.domain.User;
import org.example.preonboarding.dto.CreateApplyRequest;
import org.example.preonboarding.repository.ApplyRepository;
import org.example.preonboarding.repository.JobOpeningRepository;
import org.example.preonboarding.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyService {

    private final JobOpeningRepository jobOpeningRepository;
    private final ApplyRepository applyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void applyForJobOpening(CreateApplyRequest createApplyRequest) {
        JobOpening targetJobOpening = jobOpeningRepository.findById(createApplyRequest.getJobOpeningId()).orElse(null);
        User applicant = userRepository.findById(createApplyRequest.getUserId()).orElse(null);

        if (targetJobOpening == null || applicant == null) {
            // todo: 예외 던지기
            log.info("<applyForJobOpening> 예외던지기");
            return;
        }

        Apply apply = Apply.builder()
                .userId(applicant.getId())
                .jobOpeningId(targetJobOpening.getId())
                .build();
        applyRepository.save(apply);
    }

}
