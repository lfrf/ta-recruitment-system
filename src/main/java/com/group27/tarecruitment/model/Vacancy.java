package com.group27.tarecruitment.model;

import java.util.List;

/**
 * Vacancy class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class Vacancy {
    private String vacancyId;
    private String moduleCode;
    private String moduleName;
    private String campus;
    private String title;
    private String description;
    private List<String> requiredSkills;
    private String preferredBackground;
    private int workloadValue;
    private String deadline;
    private String status;
    private String createdBy;
    private int applicantCount;
    private int positionCount;
    private boolean leaderRoleAvailable;

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
    public String getModuleCode() {
        return moduleCode;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param moduleCode input parameter of type {@code String}.
     */
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param moduleName input parameter of type {@code String}.
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getCampus() {
        return campus;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param campus input parameter of type {@code String}.
     */
    public void setCampus(String campus) {
        this.campus = campus;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param title input parameter of type {@code String}.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param description input parameter of type {@code String}.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param requiredSkills input parameter of type {@code List<String>}.
     */
    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getPreferredBackground() {
        return preferredBackground;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param preferredBackground input parameter of type {@code String}.
     */
    public void setPreferredBackground(String preferredBackground) {
        this.preferredBackground = preferredBackground;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getWorkloadValue() {
        return workloadValue;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param workloadValue input parameter of type {@code int}.
     */
    public void setWorkloadValue(int workloadValue) {
        this.workloadValue = workloadValue;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param deadline input parameter of type {@code String}.
     */
    public void setDeadline(String deadline) {
        this.deadline = deadline;
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
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getApplicantCount() {
        return applicantCount;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param applicantCount input parameter of type {@code int}.
     */
    public void setApplicantCount(int applicantCount) {
        this.applicantCount = applicantCount;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `int` value for this operation.
     */
    public int getPositionCount() {
        return positionCount;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param positionCount input parameter of type {@code int}.
     */
    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isLeaderRoleAvailable() {
        return leaderRoleAvailable;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param leaderRoleAvailable input parameter of type {@code boolean}.
     */
    public void setLeaderRoleAvailable(boolean leaderRoleAvailable) {
        this.leaderRoleAvailable = leaderRoleAvailable;
    }
}
