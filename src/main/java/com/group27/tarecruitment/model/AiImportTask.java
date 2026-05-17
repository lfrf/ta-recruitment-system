package com.group27.tarecruitment.model;

import java.util.List;

public class AiImportTask {
    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_VALIDATED = "VALIDATED";
    public static final String STATUS_PARTIAL = "PARTIAL";
    public static final String STATUS_APPLIED = "APPLIED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_EXPIRED = "EXPIRED";

    public static final String IMPORT_STATUS_PENDING = "PENDING";
    public static final String IMPORT_STATUS_VALIDATED = "VALIDATED";
    public static final String IMPORT_STATUS_APPLIED = "APPLIED";
    public static final String IMPORT_STATUS_FAILED = "FAILED";

    private String taskId;
    private String userId;
    private String status;
    private String profileStatus;
    private String rankingStatus;
    private String callbackToken;
    private String schemaVersion;
    private long createdAtEpochMillis;
    private long expiresAtEpochMillis;
    private Long receivedAtEpochMillis;
    private Long validatedAtEpochMillis;
    private Long appliedAtEpochMillis;
    private String cvDownloadToken;
    private Long cvDownloadExpiresAtEpochMillis;
    private Long cvDownloadedAtEpochMillis;
    private Integer cvDownloadAccessCount;
    private String rawPayloadJson;
    private List<String> eligibleVacancyIds;
    private AiProfileSuggestion suggestion;
    private List<AiVacancyRecommendation> recommendations;
    private List<String> validationErrors;
    private List<String> profileValidationErrors;
    private List<String> rankingValidationErrors;

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

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getRankingStatus() {
        return rankingStatus;
    }

    public void setRankingStatus(String rankingStatus) {
        this.rankingStatus = rankingStatus;
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

    public Long getAppliedAtEpochMillis() {
        return appliedAtEpochMillis;
    }

    public void setAppliedAtEpochMillis(Long appliedAtEpochMillis) {
        this.appliedAtEpochMillis = appliedAtEpochMillis;
    }

    public String getCvDownloadToken() {
        return cvDownloadToken;
    }

    public void setCvDownloadToken(String cvDownloadToken) {
        this.cvDownloadToken = cvDownloadToken;
    }

    public Long getCvDownloadExpiresAtEpochMillis() {
        return cvDownloadExpiresAtEpochMillis;
    }

    public void setCvDownloadExpiresAtEpochMillis(Long cvDownloadExpiresAtEpochMillis) {
        this.cvDownloadExpiresAtEpochMillis = cvDownloadExpiresAtEpochMillis;
    }

    public Long getCvDownloadedAtEpochMillis() {
        return cvDownloadedAtEpochMillis;
    }

    public void setCvDownloadedAtEpochMillis(Long cvDownloadedAtEpochMillis) {
        this.cvDownloadedAtEpochMillis = cvDownloadedAtEpochMillis;
    }

    public Integer getCvDownloadAccessCount() {
        return cvDownloadAccessCount;
    }

    public void setCvDownloadAccessCount(Integer cvDownloadAccessCount) {
        this.cvDownloadAccessCount = cvDownloadAccessCount;
    }

    public String getRawPayloadJson() {
        return rawPayloadJson;
    }

    public void setRawPayloadJson(String rawPayloadJson) {
        this.rawPayloadJson = rawPayloadJson;
    }

    public List<String> getEligibleVacancyIds() {
        return eligibleVacancyIds;
    }

    public void setEligibleVacancyIds(List<String> eligibleVacancyIds) {
        this.eligibleVacancyIds = eligibleVacancyIds;
    }

    public AiProfileSuggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(AiProfileSuggestion suggestion) {
        this.suggestion = suggestion;
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

    public List<String> getProfileValidationErrors() {
        return profileValidationErrors;
    }

    public void setProfileValidationErrors(List<String> profileValidationErrors) {
        this.profileValidationErrors = profileValidationErrors;
    }

    public List<String> getRankingValidationErrors() {
        return rankingValidationErrors;
    }

    public void setRankingValidationErrors(List<String> rankingValidationErrors) {
        this.rankingValidationErrors = rankingValidationErrors;
    }
}
