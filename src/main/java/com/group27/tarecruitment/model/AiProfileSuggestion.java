package com.group27.tarecruitment.model;

import java.util.List;

/**
 * AiProfileSuggestion class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class AiProfileSuggestion {
    private String fullName;
    private String studentId;
    private String email;
    private String phone;
    private String degreeProgramme;
    private String yearOfStudy;
    private List<String> relevantCourses;
    private List<String> skills;
    private String taExperience;
    private String projectOrLeadershipExperience;
    private String availability;

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param fullName input parameter of type {@code String}.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param studentId input parameter of type {@code String}.
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param email input parameter of type {@code String}.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param phone input parameter of type {@code String}.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getDegreeProgramme() {
        return degreeProgramme;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param degreeProgramme input parameter of type {@code String}.
     */
    public void setDegreeProgramme(String degreeProgramme) {
        this.degreeProgramme = degreeProgramme;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param yearOfStudy input parameter of type {@code String}.
     */
    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<String> getRelevantCourses() {
        return relevantCourses;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param relevantCourses input parameter of type {@code List<String>}.
     */
    public void setRelevantCourses(List<String> relevantCourses) {
        this.relevantCourses = relevantCourses;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<String> getSkills() {
        return skills;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param skills input parameter of type {@code List<String>}.
     */
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getTaExperience() {
        return taExperience;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param taExperience input parameter of type {@code String}.
     */
    public void setTaExperience(String taExperience) {
        this.taExperience = taExperience;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getProjectOrLeadershipExperience() {
        return projectOrLeadershipExperience;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param projectOrLeadershipExperience input parameter of type {@code String}.
     */
    public void setProjectOrLeadershipExperience(String projectOrLeadershipExperience) {
        this.projectOrLeadershipExperience = projectOrLeadershipExperience;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getAvailability() {
        return availability;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param availability input parameter of type {@code String}.
     */
    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
