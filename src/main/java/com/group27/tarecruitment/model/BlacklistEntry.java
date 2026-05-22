package com.group27.tarecruitment.model;

/**
 * BlacklistEntry class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class BlacklistEntry {
    private String entryId;
    private String applicantId;
    private String reason;
    private String createdAt;
    private String createdBy;
    private boolean active;

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getEntryId() {
        return entryId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param entryId input parameter of type {@code String}.
     */
    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

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
     * @return the computed `String` value for this operation.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param reason input parameter of type {@code String}.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param createdAt input parameter of type {@code String}.
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param createdBy input parameter of type {@code String}.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
}
