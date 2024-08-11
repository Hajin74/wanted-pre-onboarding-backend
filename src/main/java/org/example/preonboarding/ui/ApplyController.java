package org.example.preonboarding.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.preonboarding.dto.*;
import org.example.preonboarding.service.ApplyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping
    public ApiResponse<Void> applyForJobOpening(@RequestBody @Valid CreateApplyRequest createApplyRequest) {
        applyService.applyForJobOpening(createApplyRequest);
        return ApiResponse.success("200", null);
    }

    @PatchMapping("/review")
    public ApiResponse<Void> reviewApply(@RequestBody @Valid ReviewApplyRequest reviewApplyRequest) {
        applyService.reviewApply(reviewApplyRequest);
        return ApiResponse.success("200", null);
    }

    @PatchMapping("/accept")
    public ApiResponse<Void> acceptApply(@RequestBody @Valid AcceptApplyRequest acceptApplyRequest) {
        applyService.acceptApply(acceptApplyRequest);
        return ApiResponse.success("200", null);
    }

    @PatchMapping("/reject")
    public ApiResponse<Void> rejectApply(@RequestBody @Valid RejectApplyRequest rejectApplyRequest) {
        applyService.rejectApply(rejectApplyRequest);
        return ApiResponse.success("200", null);
    }

}
