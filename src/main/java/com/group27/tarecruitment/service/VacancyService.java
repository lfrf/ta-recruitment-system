package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.VacancyRepository;
import java.util.List;
import java.util.Optional;

public class VacancyService {
    private final VacancyRepository vacancyRepository = new VacancyRepository();

    public List<Vacancy> getOpenVacancies() {
        return vacancyRepository.findAll().stream()
                .filter(vacancy -> "OPEN".equalsIgnoreCase(vacancy.getStatus()))
                .toList();
    }

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    public Optional<Vacancy> getVacancy(String vacancyId) {
        return vacancyRepository.findById(vacancyId);
    }
}
