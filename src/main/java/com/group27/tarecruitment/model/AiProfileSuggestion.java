package com.group27.tarecruitment.model;

import java.util.List;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDegreeProgramme() {
        return degreeProgramme;
    }

    public void setDegreeProgramme(String degreeProgramme) {
        this.degreeProgramme = degreeProgramme;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public List<String> getRelevantCourses() {
        return relevantCourses;
    }

    public void setRelevantCourses(List<String> relevantCourses) {
        this.relevantCourses = relevantCourses;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getTaExperience() {
        return taExperience;
    }

    public void setTaExperience(String taExperience) {
        this.taExperience = taExperience;
    }

    public String getProjectOrLeadershipExperience() {
        return projectOrLeadershipExperience;
    }

    public void setProjectOrLeadershipExperience(String projectOrLeadershipExperience) {
        this.projectOrLeadershipExperience = projectOrLeadershipExperience;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
