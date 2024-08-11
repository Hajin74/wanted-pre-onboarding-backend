package org.example.preonboarding.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.preonboarding.enums.ApplyStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long jobOpeningId;

    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;

    @Builder
    public Apply(Long userId, Long jobOpeningId, ApplyStatus applyStatus) {
        this.userId = userId;
        this.jobOpeningId = jobOpeningId;
        this.applyStatus = applyStatus;
    }

    public void reviewed() {
        this.applyStatus = ApplyStatus.REVIEWED;
    }

    public void accepted() {
        this.applyStatus = ApplyStatus.ACCEPTED;
    }

    public void rejected() {
        this.applyStatus = ApplyStatus.REJECTED;
    }

}
