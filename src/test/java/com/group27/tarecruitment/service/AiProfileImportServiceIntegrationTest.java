package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.repository.AiImportTaskRepository;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.service.AiProfileImportService.ApplyResult;
import com.group27.tarecruitment.service.AiProfileImportService.CallbackResult;
import com.group27.tarecruitment.support.TestDataSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AiProfileImportServiceIntegrationTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class AiProfileImportServiceIntegrationTest {

    private final AiProfileImportService aiProfileImportService = new AiProfileImportService();
    private final AiImportTaskRepository aiImportTaskRepository = new AiImportTaskRepository();
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @BeforeEach
    void setUp() {
        TestDataSupport.resetRuntimeDataDir();
        TestDataSupport.seedAiImportTasks(List.of(
                task("ai-task-1", "u-app-1", "cb-token-1", Instant.now().toEpochMilli() + 600_000L)
        ));
        TestDataSupport.seedProfiles(List.of());
    }

    /**
     * Performs authentication or security-related validation logic.
     */
    @Test
    void acceptCallbackShouldRejectInvalidToken() {
        CallbackResult result = aiProfileImportService.acceptCallback("ai-task-1", "wrong-token", validPayloadJson());
        assertFalse(result.isOk());
        assertEquals("INVALID_TOKEN", result.getCode());
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void acceptCallbackShouldValidateAndPersistSuggestionAndRanking() {
        CallbackResult result = aiProfileImportService.acceptCallback("ai-task-1", "cb-token-1", validPayloadJson());
        assertTrue(result.isOk());
        assertEquals("OK", result.getCode());

        AiImportTask updatedTask = aiImportTaskRepository.findById("ai-task-1").orElse(null);
        assertNotNull(updatedTask);
        assertEquals(AiImportTask.STATUS_VALIDATED, updatedTask.getStatus());
        assertEquals(AiImportTask.IMPORT_STATUS_VALIDATED, updatedTask.getProfileStatus());
        assertEquals(AiImportTask.IMPORT_STATUS_VALIDATED, updatedTask.getRankingStatus());
        assertNotNull(updatedTask.getSuggestion());
        assertEquals("Alice Zhang", updatedTask.getSuggestion().getFullName());
        assertEquals(2, updatedTask.getRecommendations().size());
        assertEquals("vac-1", updatedTask.getRecommendations().get(0).getVacancyId());
        assertEquals(95, updatedTask.getRecommendations().get(0).getScore());
        assertEquals("vac-2", updatedTask.getRecommendations().get(1).getVacancyId());
        assertEquals(82, updatedTask.getRecommendations().get(1).getScore());
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void applySuggestionToProfileShouldPersistAndLockTask() {
        aiProfileImportService.acceptCallback("ai-task-1", "cb-token-1", validPayloadJson());
        ApplyResult result = aiProfileImportService.applySuggestionToProfile("u-app-1", "ai-task-1");

        assertTrue(result.isOk());
        assertEquals("OK", result.getCode());
        ApplicantProfile profile = applicantProfileRepository.findByApplicantId("u-app-1").orElse(null);
        assertNotNull(profile);
        assertEquals("Alice Zhang", profile.getFullName());
        assertEquals("S1234567", profile.getStudentId());
        assertEquals("alice.zhang@example.com", profile.getEmail());
        assertEquals(List.of("Java", "Python"), profile.getSkills());

        AiImportTask task = aiImportTaskRepository.findById("ai-task-1").orElse(null);
        assertNotNull(task);
        assertEquals(AiImportTask.STATUS_APPLIED, task.getStatus());
        assertEquals(AiImportTask.IMPORT_STATUS_APPLIED, task.getProfileStatus());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param taskId input parameter of type {@code String}.
     * @param userId input parameter of type {@code String}.
     * @param callbackToken input parameter of type {@code String}.
     * @param expiresAt input parameter of type {@code long}.
     * @return the computed `AiImportTask` value for this operation.
     */
    private AiImportTask task(String taskId, String userId, String callbackToken, long expiresAt) {
        AiImportTask task = new AiImportTask();
        task.setTaskId(taskId);
        task.setUserId(userId);
        task.setStatus(AiImportTask.STATUS_CREATED);
        task.setProfileStatus(AiImportTask.IMPORT_STATUS_PENDING);
        task.setRankingStatus(AiImportTask.IMPORT_STATUS_PENDING);
        task.setCallbackToken(callbackToken);
        task.setSchemaVersion(AiProfileImportService.SCHEMA_VERSION);
        task.setCreatedAtEpochMillis(Instant.now().toEpochMilli());
        task.setExpiresAtEpochMillis(expiresAt);
        task.setEligibleVacancyIds(List.of("vac-1", "vac-2", "vac-3"));
        task.setValidationErrors(List.of());
        task.setProfileValidationErrors(List.of());
        task.setRankingValidationErrors(List.of());
        return task;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @return the computed `String` value for this operation.
     */
    private String validPayloadJson() {
        return """
                {
                  "schemaVersion": "profile-and-ranking-v1",
                  "profile": {
                    "fullName": "Alice Zhang",
                    "studentId": "S1234567",
                    "email": "alice.zhang@example.com",
                    "phone": "+86 138-0013-8000",
                    "degreeProgramme": "BSc Computer Science",
                    "yearOfStudy": "2",
                    "relevantCourses": ["EBU6304 Software Engineering", "EBU4211 Programming"],
                    "skills": ["Java", "Python"],
                    "taExperience": "Lab assistant experience",
                    "projectOrLeadershipExperience": "Team project leadership",
                    "availability": "Tue/Thu afternoons"
                  },
                  "rankings": [
                    {"vacancyId": "vac-2", "score": 82, "reasons": ["Relevant course background"]},
                    {"vacancyId": "vac-1", "score": 95, "reasons": ["Strong coding and tutoring fit"]}
                  ]
                }
                """;
    }
}
