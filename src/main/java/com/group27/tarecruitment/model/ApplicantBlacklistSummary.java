package com.group27.tarecruitment.model;

/**
 * ApplicantBlacklistSummary class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class ApplicantBlacklistSummary {
    private String applicantId;
    private int listedTimes;
    private boolean active;
    private String activeEntryId;
    private String latestReason;
    private String latestCreatedAt;
    private String latestCreatedBy;

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getApplicantId() {
        return applicantId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param applicantId input parameter of type {@code String}.
     */
    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getListedTimes() {
        return listedTimes;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param listedTimes input parameter of type {@code int}.
     */
    public void setListedTimes(int listedTimes) {
        this.listedTimes = listedTimes;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param active input parameter of type {@code boolean}.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getActiveEntryId() {
        return activeEntryId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param activeEntryId input parameter of type {@code String}.
     */
    public void setActiveEntryId(String activeEntryId) {
        this.activeEntryId = activeEntryId;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getLatestReason() {
        return latestReason;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param latestReason input parameter of type {@code String}.
     */
    public void setLatestReason(String latestReason) {
        this.latestReason = latestReason;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getLatestCreatedAt() {
        return latestCreatedAt;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param latestCreatedAt input parameter of type {@code String}.
     */
    public void setLatestCreatedAt(String latestCreatedAt) {
        this.latestCreatedAt = latestCreatedAt;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getLatestCreatedBy() {
        return latestCreatedBy;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param latestCreatedBy input parameter of type {@code String}.
     */
    public void setLatestCreatedBy(String latestCreatedBy) {
        this.latestCreatedBy = latestCreatedBy;
    }
}
