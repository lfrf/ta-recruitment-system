package com.group27.tarecruitment.model;

import java.util.List;

/**
 * AiVacancyRecommendation class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class AiVacancyRecommendation {
    private String vacancyId;
    private Integer score;
    private List<String> reasons;

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
     * @return the computed `Integer` value for this operation.
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param score input parameter of type {@code Integer}.
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<String> getReasons() {
        return reasons;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param reasons input parameter of type {@code List<String>}.
     */
    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}

