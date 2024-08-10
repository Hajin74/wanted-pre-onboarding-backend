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
@RequestMapping("/api/job-posting")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobOpeningService jobPostingService;

    @PostMapping
    public String createJobPosting(@RequestBody @Valid CreateJobOpeningRequest createJobPostingRequest) {
        jobPostingService.createJobPosting(createJobPostingRequest);
        return "create success!";
    }

    @PutMapping("/{jobPostingId}/company/{companyId}")
    public String updateJobPosting(@PathVariable Long jobPostingId, @PathVariable Long companyId, @RequestBody @Valid UpdateJobOpeningRequest updateJobPostingRequest) {
        jobPostingService.updateJobPosting(jobPostingId, companyId, updateJobPostingRequest);
        return "update success!";
    }

    @DeleteMapping("/{jobPostingId}/company/{companyId}")
    public String deleteJobPosting(@PathVariable Long jobPostingId, @PathVariable Long companyId) {
        jobPostingService.deleteJobPosting(jobPostingId, companyId);
        return "delete success!";
    }

    @GetMapping
    public List<JobOpeningOverViewResponse> getAllJobPostings() {
        return jobPostingService.getAllJobPostings();
    }

    @GetMapping("/{jobPostingId}")
    public JobOpeningDetailResponse getDetailJobPosting(@PathVariable Long jobPostingId) {
        return jobPostingService.getDetailJobPosting(jobPostingId);
    }

}
