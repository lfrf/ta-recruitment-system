package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VacancyRepository {
    private static final String VACANCIES_RESOURCE = "data/vacancies.json";
    private final ApplicationRepository applicationRepository = new ApplicationRepository();

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

    public Optional<Vacancy> findById(String vacancyId) {
        return findAll().stream()
                .filter(vacancy -> vacancy.getVacancyId() != null && vacancy.getVacancyId().equals(vacancyId))
                .findFirst();
    }

    public void saveAll(List<Vacancy> vacancies) {
        JsonFileUtil.writeList(VACANCIES_RESOURCE, vacancies);
    }
}