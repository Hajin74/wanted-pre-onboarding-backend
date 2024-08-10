package org.example.preonboarding.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.preonboarding.dto.CreateJobPostingRequest;
import org.example.preonboarding.dto.JobPostingDetailResponse;
import org.example.preonboarding.dto.JobPostingOverViewResponse;
import org.example.preonboarding.dto.UpdateJobPostingRequest;
import org.example.preonboarding.service.JobPostingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-posting")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @PostMapping
    public String createJobPosting(@RequestBody @Valid CreateJobPostingRequest createJobPostingRequest) {
        jobPostingService.createJobPosting(createJobPostingRequest);
        return "create success!";
    }

    @PutMapping("/{jobPostingId}/company/{companyId}")
    public String updateJobPosting(@PathVariable Long jobPostingId, @PathVariable Long companyId, @RequestBody @Valid UpdateJobPostingRequest updateJobPostingRequest) {
        jobPostingService.updateJobPosting(jobPostingId, companyId, updateJobPostingRequest);
        return "update success!";
    }

    @DeleteMapping("/{jobPostingId}/company/{companyId}")
    public String deleteJobPosting(@PathVariable Long jobPostingId, @PathVariable Long companyId) {
        jobPostingService.deleteJobPosting(jobPostingId, companyId);
        return "delete success!";
    }

    @GetMapping
    public List<JobPostingOverViewResponse> getAllJobPostings() {
        return jobPostingService.getAllJobPostings();
    }

    @GetMapping("/{jobPostingId}")
    public JobPostingDetailResponse getDetailJobPosting(@PathVariable Long jobPostingId) {
        return jobPostingService.getDetailJobPosting(jobPostingId);
    }

}
