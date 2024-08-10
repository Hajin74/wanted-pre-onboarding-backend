package org.example.preonboarding.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.preonboarding.dto.*;
import org.example.preonboarding.service.JobOpeningService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-opening")
@RequiredArgsConstructor
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    @PostMapping
    public ApiResponse<Void> createJobOpening(@RequestBody @Valid CreateJobOpeningRequest createJobOpeningRequest) {
        jobOpeningService.createJobOpening(createJobOpeningRequest);
        return ApiResponse.success("200", null);
    }

    @PutMapping("/{jobOpeningId}/company/{companyId}")
    public ApiResponse<Void> updateJobOpening(@PathVariable Long jobOpeningId, @PathVariable Long companyId, @RequestBody @Valid UpdateJobOpeningRequest updateJobOpeningRequest) {
        jobOpeningService.updateJobOpening(jobOpeningId, companyId, updateJobOpeningRequest);
        return ApiResponse.success("200", null);
    }

    @DeleteMapping("/{jobOpeningId}/company/{companyId}")
    public ApiResponse<Void> deleteJobOpening(@PathVariable Long jobOpeningId, @PathVariable Long companyId) {
        jobOpeningService.deleteJobOpening(jobOpeningId, companyId);
        return ApiResponse.success("200", null);
    }

    @GetMapping
    public ApiResponse<List<JobOpeningOverViewResponse>> getAllJobOpenings() {
        List<JobOpeningOverViewResponse> jobOpenings = jobOpeningService.getAllJobOpenings();
        return ApiResponse.success("200", jobOpenings);
    }

    @GetMapping("/{jobOpeningId}")
    public ApiResponse<JobOpeningDetailResponse> getDetailJobOpening(@PathVariable Long jobOpeningId) {
        JobOpeningDetailResponse jobOpeningDetail = jobOpeningService.getDetailJobOpening(jobOpeningId);
        return ApiResponse.success("200", jobOpeningDetail);
    }

}
