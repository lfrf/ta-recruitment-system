package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;
import java.util.Optional;

public class ApplicationRepository {
    private static final String APPLICATIONS_RESOURCE = "data/applications.json";

    public List<ApplicationRecord> findAll() {
        return JsonFileUtil.readList(APPLICATIONS_RESOURCE, ApplicationRecord.class);
    }

    public List<ApplicationRecord> findByApplicantId(String applicantId) {
        return findAll().stream()
                .filter(record -> record.getApplicantId() != null && record.getApplicantId().equals(applicantId))
                .toList();
    }

    public List<ApplicationRecord> findByVacancyId(String vacancyId) {
        return findAll().stream()
                .filter(record -> record.getVacancyId() != null && record.getVacancyId().equals(vacancyId))
                .toList();
    }

    public Optional<ApplicationRecord> findById(String applicationId) {
        return findAll().stream()
                .filter(record -> record.getApplicationId() != null && record.getApplicationId().equals(applicationId))
                .findFirst();
    }

    public void saveAll(List<ApplicationRecord> applications) {
        JsonFileUtil.writeList(APPLICATIONS_RESOURCE, applications);
    }
}
