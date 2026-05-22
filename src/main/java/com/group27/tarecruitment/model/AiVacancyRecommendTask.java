package com.group27.tarecruitment.model;

import java.util.List;

/**
 * AiVacancyRecommendTask class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
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

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param taskId input parameter of type {@code String}.
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param userId input parameter of type {@code String}.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param status input parameter of type {@code String}.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getCallbackToken() {
        return callbackToken;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param callbackToken input parameter of type {@code String}.
     */
    public void setCallbackToken(String callbackToken) {
        this.callbackToken = callbackToken;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param schemaVersion input parameter of type {@code String}.
     */
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `long` value for this operation.
     */
    public long getCreatedAtEpochMillis() {
        return createdAtEpochMillis;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param createdAtEpochMillis input parameter of type {@code long}.
     */
    public void setCreatedAtEpochMillis(long createdAtEpochMillis) {
        this.createdAtEpochMillis = createdAtEpochMillis;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `long` value for this operation.
     */
    public long getExpiresAtEpochMillis() {
        return expiresAtEpochMillis;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param expiresAtEpochMillis input parameter of type {@code long}.
     */
    public void setExpiresAtEpochMillis(long expiresAtEpochMillis) {
        this.expiresAtEpochMillis = expiresAtEpochMillis;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `Long` value for this operation.
     */
    public Long getReceivedAtEpochMillis() {
        return receivedAtEpochMillis;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param receivedAtEpochMillis input parameter of type {@code Long}.
     */
    public void setReceivedAtEpochMillis(Long receivedAtEpochMillis) {
        this.receivedAtEpochMillis = receivedAtEpochMillis;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `Long` value for this operation.
     */
    public Long getValidatedAtEpochMillis() {
        return validatedAtEpochMillis;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param validatedAtEpochMillis input parameter of type {@code Long}.
     */
    public void setValidatedAtEpochMillis(Long validatedAtEpochMillis) {
        this.validatedAtEpochMillis = validatedAtEpochMillis;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<String> getEligibleVacancyIds() {
        return eligibleVacancyIds;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param eligibleVacancyIds input parameter of type {@code List<String>}.
     */
    public void setEligibleVacancyIds(List<String> eligibleVacancyIds) {
        this.eligibleVacancyIds = eligibleVacancyIds;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getRawPayloadJson() {
        return rawPayloadJson;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param rawPayloadJson input parameter of type {@code String}.
     */
    public void setRawPayloadJson(String rawPayloadJson) {
        this.rawPayloadJson = rawPayloadJson;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<AiVacancyRecommendation> getRecommendations() {
        return recommendations;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param recommendations input parameter of type {@code List<AiVacancyRecommendation>}.
     */
    public void setRecommendations(List<AiVacancyRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<String> getValidationErrors() {
        return validationErrors;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param validationErrors input parameter of type {@code List<String>}.
     */
    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}

