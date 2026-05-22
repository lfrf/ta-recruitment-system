package com.group27.tarecruitment.model;

/**
 * ApplicationRecord class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class ApplicationRecord {
    private String applicationId;
    private String vacancyId;
    private String applicantId;
    private String submittedAt;
    private String status;
    private String reviewNote;
    private String optionalFeedback;
    private boolean leadTa;
    private String decisionUpdatedAt;
    private Boolean decisionRead;

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param applicationId input parameter of type {@code String}.
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getVacancyId() {
        return vacancyId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param vacancyId input parameter of type {@code String}.
     */
    public void setVacancyId(String vacancyId) {
        this.vacancyId = vacancyId;
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
    public String getSubmittedAt() {
        return submittedAt;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param submittedAt input parameter of type {@code String}.
     */
    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
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
    public String getReviewNote() {
        return reviewNote;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param reviewNote input parameter of type {@code String}.
     */
    public void setReviewNote(String reviewNote) {
        this.reviewNote = reviewNote;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getOptionalFeedback() {
        return optionalFeedback;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param optionalFeedback input parameter of type {@code String}.
     */
    public void setOptionalFeedback(String optionalFeedback) {
        this.optionalFeedback = optionalFeedback;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isLeadTa() {
        return leadTa;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param leadTa input parameter of type {@code boolean}.
     */
    public void setLeadTa(boolean leadTa) {
        this.leadTa = leadTa;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getDecisionUpdatedAt() {
        return decisionUpdatedAt;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param decisionUpdatedAt input parameter of type {@code String}.
     */
    public void setDecisionUpdatedAt(String decisionUpdatedAt) {
        this.decisionUpdatedAt = decisionUpdatedAt;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return true when the condition is met; otherwise false.
     */
    public Boolean getDecisionRead() {
        return decisionRead;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param decisionRead input parameter of type {@code Boolean}.
     */
    public void setDecisionRead(Boolean decisionRead) {
        this.decisionRead = decisionRead;
    }
}
