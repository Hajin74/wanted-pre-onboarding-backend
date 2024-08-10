package org.example.preonboarding.ui;

import lombok.RequiredArgsConstructor;
import org.example.preonboarding.dto.ApiResponse;
import org.example.preonboarding.dto.CreateApplyRequest;
import org.example.preonboarding.service.ApplyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping
    public ApiResponse<Void> applyForJobOpening(@RequestBody CreateApplyRequest createApplyRequest) {
        applyService.applyForJobOpening(createApplyRequest);
        return ApiResponse.success("200", null);
    }

}
