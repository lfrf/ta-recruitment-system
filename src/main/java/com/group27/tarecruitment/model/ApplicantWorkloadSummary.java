package com.group27.tarecruitment.model;

import java.util.List;

/**
 * ApplicantWorkloadSummary class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class ApplicantWorkloadSummary {
    private String applicantId;
    private String displayName;
    private String studentId;
    private String email;
    private int totalApplicationsCount;
    private int submittedCount;
    private int unsuccessfulCount;
    private int offeredCount;
    private int activeCount;
    private int maxWorkload;
    private boolean blacklisted;
    private boolean overloaded;
    private List<String> activeModules;

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getApplicantId() { return applicantId; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param applicantId input parameter of type {@code String}.
     */
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getDisplayName() { return displayName; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param displayName input parameter of type {@code String}.
     */
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getStudentId() { return studentId; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param studentId input parameter of type {@code String}.
     */
    public void setStudentId(String studentId) { this.studentId = studentId; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getEmail() { return email; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param email input parameter of type {@code String}.
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getTotalApplicationsCount() { return totalApplicationsCount; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param totalApplicationsCount input parameter of type {@code int}.
     */
    public void setTotalApplicationsCount(int totalApplicationsCount) { this.totalApplicationsCount = totalApplicationsCount; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getSubmittedCount() { return submittedCount; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param submittedCount input parameter of type {@code int}.
     */
    public void setSubmittedCount(int submittedCount) { this.submittedCount = submittedCount; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getUnsuccessfulCount() { return unsuccessfulCount; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param unsuccessfulCount input parameter of type {@code int}.
     */
    public void setUnsuccessfulCount(int unsuccessfulCount) { this.unsuccessfulCount = unsuccessfulCount; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getOfferedCount() { return offeredCount; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param offeredCount input parameter of type {@code int}.
     */
    public void setOfferedCount(int offeredCount) { this.offeredCount = offeredCount; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getActiveCount() { return activeCount; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param activeCount input parameter of type {@code int}.
     */
    public void setActiveCount(int activeCount) { this.activeCount = activeCount; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getMaxWorkload() { return maxWorkload; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param maxWorkload input parameter of type {@code int}.
     */
    public void setMaxWorkload(int maxWorkload) { this.maxWorkload = maxWorkload; }
    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isBlacklisted() { return blacklisted; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param blacklisted input parameter of type {@code boolean}.
     */
    public void setBlacklisted(boolean blacklisted) { this.blacklisted = blacklisted; }
    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isOverloaded() { return overloaded; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param overloaded input parameter of type {@code boolean}.
     */
    public void setOverloaded(boolean overloaded) { this.overloaded = overloaded; }
    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<String> getActiveModules() { return activeModules; }
    /**
     * Updates existing state while preserving consistency constraints.
     * @param activeModules input parameter of type {@code List<String>}.
     */
    public void setActiveModules(List<String> activeModules) { this.activeModules = activeModules; }
}
