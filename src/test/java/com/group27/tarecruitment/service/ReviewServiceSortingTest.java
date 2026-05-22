package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicationRecord;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ReviewServiceSortingTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class ReviewServiceSortingTest {

    private final ReviewService reviewService = new ReviewService();

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void defaultOrderShouldPrioritizeSubmittedThenBySubmittedTime() {
        ApplicationRecord submittedLater = record("a2", "u2", "Submitted", "2026-05-18T12:00:00");
        ApplicationRecord submittedEarlier = record("a1", "u1", "Submitted", "2026-05-18T10:00:00");
        ApplicationRecord offered = record("a3", "u3", "Offered", "2026-05-18T09:00:00");

        List<ApplicationRecord> sorted = reviewService.sortApplicationsForReview(
                List.of(submittedLater, offered, submittedEarlier),
                Map.of(),
                ReviewService.ORDER_MODE_DEFAULT
        );

        assertEquals(List.of("a1", "a2", "a3"),
                sorted.stream().map(ApplicationRecord::getApplicationId).toList());
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void aiOrderShouldPrioritizeScoredSubmittedApplicantsByScore() {
        ApplicationRecord s1 = record("a1", "u1", "Submitted", "2026-05-18T10:00:00");
        ApplicationRecord s2 = record("a2", "u2", "Submitted", "2026-05-18T09:00:00");
        ApplicationRecord s3 = record("a3", "u3", "Submitted", "2026-05-18T08:00:00");

        Map<String, ReviewService.ApplicantAiFit> aiFit = new LinkedHashMap<>();
        aiFit.put("u1", fit(70));
        aiFit.put("u2", fit(95));

        List<ApplicationRecord> sorted = reviewService.sortApplicationsForReview(
                List.of(s1, s2, s3),
                aiFit,
                ReviewService.ORDER_MODE_AI
        );

        assertEquals(List.of("a2", "a1", "a3"),
                sorted.stream().map(ApplicationRecord::getApplicationId).toList());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param appId input parameter of type {@code String}.
     * @param applicantId input parameter of type {@code String}.
     * @param status input parameter of type {@code String}.
     * @param submittedAt input parameter of type {@code String}.
     * @return the computed `ApplicationRecord` value for this operation.
     */
    private ApplicationRecord record(String appId, String applicantId, String status, String submittedAt) {
        ApplicationRecord record = new ApplicationRecord();
        record.setApplicationId(appId);
        record.setApplicantId(applicantId);
        record.setStatus(status);
        record.setSubmittedAt(submittedAt);
        return record;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param score input parameter of type {@code int}.
     * @return the computed `ReviewService.ApplicantAiFit` value for this operation.
     */
    private ReviewService.ApplicantAiFit fit(int score) {
        ReviewService.ApplicantAiFit fit = new ReviewService.ApplicantAiFit();
        fit.setScore(score);
        fit.setReasons(List.of("sample"));
        return fit;
    }
}

