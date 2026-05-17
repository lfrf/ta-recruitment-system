package com.group27.tarecruitment.model;

import java.util.List;

public class AiVacancyRecommendTask {
    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_VALIDATED = "VALIDATED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_EXPIRED = "EXPIRED";

    private String taskId;
    private String userId;
    private String status;
    private String callbackToken;
    private String schemaVersion;
    private long createdAtEpochMillis;
    private long expiresAtEpochMillis;
    private Long receivedAtEpochMillis;
    private Long validatedAtEpochMillis;
    private List<String> eligibleVacancyIds;
    private String rawPayloadJson;
    private List<AiVacancyRecommendation> recommendations;
    private List<String> validationErrors;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCallbackToken() {
        return callbackToken;
    }

    public void setCallbackToken(String callbackToken) {
        this.callbackToken = callbackToken;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public long getCreatedAtEpochMillis() {
        return createdAtEpochMillis;
    }

    public void setCreatedAtEpochMillis(long createdAtEpochMillis) {
        this.createdAtEpochMillis = createdAtEpochMillis;
    }

    public long getExpiresAtEpochMillis() {
        return expiresAtEpochMillis;
    }

    public void setExpiresAtEpochMillis(long expiresAtEpochMillis) {
        this.expiresAtEpochMillis = expiresAtEpochMillis;
    }

    public Long getReceivedAtEpochMillis() {
        return receivedAtEpochMillis;
    }

    public void setReceivedAtEpochMillis(Long receivedAtEpochMillis) {
        this.receivedAtEpochMillis = receivedAtEpochMillis;
    }

    public Long getValidatedAtEpochMillis() {
        return validatedAtEpochMillis;
    }

    public void setValidatedAtEpochMillis(Long validatedAtEpochMillis) {
        this.validatedAtEpochMillis = validatedAtEpochMillis;
    }

    public List<String> getEligibleVacancyIds() {
        return eligibleVacancyIds;
    }

    public void setEligibleVacancyIds(List<String> eligibleVacancyIds) {
        this.eligibleVacancyIds = eligibleVacancyIds;
    }

    public String getRawPayloadJson() {
        return rawPayloadJson;
    }

    public void setRawPayloadJson(String rawPayloadJson) {
        this.rawPayloadJson = rawPayloadJson;
    }

    public List<AiVacancyRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<AiVacancyRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}

