package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;
import java.util.Optional;

/**
 * ApplicationRepository class type.
 *
 * <p>Repository type that encapsulates persistence and query behavior.</p>
 * <p>Package: {@code com.group27.tarecruitment.repository}</p>
 */
public class ApplicationRepository {
    private static final String APPLICATIONS_RESOURCE = "data/applications.json";

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicationRecord> findAll() {
        return JsonFileUtil.readList(APPLICATIONS_RESOURCE, ApplicationRecord.class);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param applicantId input parameter of type {@code String}.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicationRecord> findByApplicantId(String applicantId) {
        return findAll().stream()
                .filter(record -> record.getApplicantId() != null && record.getApplicantId().equals(applicantId))
                .toList();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param vacancyId input parameter of type {@code String}.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicationRecord> findByVacancyId(String vacancyId) {
        return findAll().stream()
                .filter(record -> record.getVacancyId() != null && record.getVacancyId().equals(vacancyId))
                .toList();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param applicationId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<ApplicationRecord> findById(String applicationId) {
        return findAll().stream()
                .filter(record -> record.getApplicationId() != null && record.getApplicationId().equals(applicationId))
                .findFirst();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param applications input parameter of type {@code List<ApplicationRecord>}.
     */
    public void saveAll(List<ApplicationRecord> applications) {
        JsonFileUtil.writeList(APPLICATIONS_RESOURCE, applications);
    }
}
