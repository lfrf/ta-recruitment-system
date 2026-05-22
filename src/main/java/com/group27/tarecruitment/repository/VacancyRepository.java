package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * VacancyRepository class type.
 *
 * <p>Repository type that encapsulates persistence and query behavior.</p>
 * <p>Package: {@code com.group27.tarecruitment.repository}</p>
 */
public class VacancyRepository {
    private static final String VACANCIES_RESOURCE = "data/vacancies.json";
    private final ApplicationRepository applicationRepository = new ApplicationRepository();

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<Vacancy> findAll() {
        List<Vacancy> vacancies = JsonFileUtil.readList(VACANCIES_RESOURCE, Vacancy.class);
        Map<String, Integer> applicantCountByVacancyId = new LinkedHashMap<>();
        for (ApplicationRecord application : applicationRepository.findAll()) {
            if (application.getVacancyId() == null) {
                continue;
            }
            applicantCountByVacancyId.merge(application.getVacancyId(), 1, Integer::sum);
        }
        for (Vacancy vacancy : vacancies) {
            vacancy.setApplicantCount(applicantCountByVacancyId.getOrDefault(vacancy.getVacancyId(), 0));
        }
        return vacancies;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param vacancyId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<Vacancy> findById(String vacancyId) {
        return findAll().stream()
                .filter(vacancy -> vacancy.getVacancyId() != null && vacancy.getVacancyId().equals(vacancyId))
                .findFirst();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancies input parameter of type {@code List<Vacancy>}.
     */
    public void saveAll(List<Vacancy> vacancies) {
        JsonFileUtil.writeList(VACANCIES_RESOURCE, vacancies);
    }
}
