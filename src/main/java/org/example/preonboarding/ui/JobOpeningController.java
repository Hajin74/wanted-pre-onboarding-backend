package org.example.preonboarding.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.preonboarding.dto.CreateJobOpeningRequest;
import org.example.preonboarding.dto.JobOpeningDetailResponse;
import org.example.preonboarding.dto.JobOpeningOverViewResponse;
import org.example.preonboarding.dto.UpdateJobOpeningRequest;
import org.example.preonboarding.service.JobOpeningService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-opening")
@RequiredArgsConstructor
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    @PostMapping
    public String createJobOpening(@RequestBody @Valid CreateJobOpeningRequest createJobOpeningRequest) {
        jobOpeningService.createJobOpening(createJobOpeningRequest);
        return "create success!";
    }

    @PutMapping("/{jobOpeningId}/company/{companyId}")
    public String updateJobOpening(@PathVariable Long jobOpeningId, @PathVariable Long companyId, @RequestBody @Valid UpdateJobOpeningRequest updateJobOpeningRequest) {
        jobOpeningService.updateJobOpening(jobOpeningId, companyId, updateJobOpeningRequest);
        return "update success!";
    }

    @DeleteMapping("/{jobOpeningId}/company/{companyId}")
    public String deleteJobOpening(@PathVariable Long jobOpeningId, @PathVariable Long companyId) {
        jobOpeningService.deleteJobOpening(jobOpeningId, companyId);
        return "delete success!";
    }

    @GetMapping
    public List<JobOpeningOverViewResponse> getAllJobOpenings() {
        return jobOpeningService.getAllJobOpenings();
    }

    @GetMapping("/{jobOpeningId}")
    public JobOpeningDetailResponse getDetailJobOpening(@PathVariable Long jobOpeningId) {
        return jobOpeningService.getDetailJobOpening(jobOpeningId);
    }

}
