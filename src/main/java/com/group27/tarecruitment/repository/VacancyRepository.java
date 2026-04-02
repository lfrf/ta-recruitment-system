package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;
import java.util.Optional;

public class VacancyRepository {
    private static final String VACANCIES_RESOURCE = "data/vacancies.json";

    public List<Vacancy> findAll() {
        return JsonFileUtil.readList(VACANCIES_RESOURCE, Vacancy.class);
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
