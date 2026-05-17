package com.group27.tarecruitment.model;

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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(String vacancyId) {
        this.vacancyId = vacancyId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewNote() {
        return reviewNote;
    }

    public void setReviewNote(String reviewNote) {
        this.reviewNote = reviewNote;
    }

    public String getOptionalFeedback() {
        return optionalFeedback;
    }

    public void setOptionalFeedback(String optionalFeedback) {
        this.optionalFeedback = optionalFeedback;
    }

    public boolean isLeadTa() {
        return leadTa;
    }

    public void setLeadTa(boolean leadTa) {
        this.leadTa = leadTa;
    }

    public String getDecisionUpdatedAt() {
        return decisionUpdatedAt;
    }

    public void setDecisionUpdatedAt(String decisionUpdatedAt) {
        this.decisionUpdatedAt = decisionUpdatedAt;
    }

    public Boolean getDecisionRead() {
        return decisionRead;
    }

    public void setDecisionRead(Boolean decisionRead) {
        this.decisionRead = decisionRead;
    }
}
