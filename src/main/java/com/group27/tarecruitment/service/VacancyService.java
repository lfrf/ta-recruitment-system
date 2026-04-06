package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public String validateNewVacancy(UserAccount currentUser,
                                     String moduleCode,
                                     String moduleName,
                                     String title,
                                     String description,
                                     String requiredSkills,
                                     String preferredBackground,
                                     String workloadValue,
                                     String deadline) {
        if (currentUser == null || currentUser.getRole() != UserRole.MO) {
            return "Only organiser accounts can publish vacancies.";
        }
        if (ValidationUtil.isBlank(moduleCode)) {
            return "Module code is required.";
        }
        if (ValidationUtil.isBlank(moduleName)) {
            return "Module name is required.";
        }
        if (ValidationUtil.isBlank(title)) {
            return "Vacancy title is required.";
        }
        if (ValidationUtil.isBlank(description)) {
            return "Vacancy description is required.";
        }
        if (ValidationUtil.splitCsv(requiredSkills).isEmpty()) {
            return "At least one required skill must be provided.";
        }
        if (ValidationUtil.isBlank(workloadValue)) {
            return "Workload value is required.";
        }
        if (ValidationUtil.parsePositiveInt(workloadValue) == null) {
            return "Workload value must be a positive integer.";
        }
        if (ValidationUtil.isBlank(deadline)) {
            return "Application deadline is required.";
        }
        return null;
    }

    public Vacancy createVacancy(UserAccount currentUser,
                                 String moduleCode,
                                 String moduleName,
                                 String title,
                                 String description,
                                 String requiredSkills,
                                 String preferredBackground,
                                 String workloadValue,
                                 String deadline) {
        List<Vacancy> vacancies = new ArrayList<>(vacancyRepository.findAll());

        Vacancy vacancy = new Vacancy();
        vacancy.setVacancyId("vac-" + UUID.randomUUID().toString().substring(0, 8));
        vacancy.setModuleCode(ValidationUtil.trimToEmpty(moduleCode));
        vacancy.setModuleName(ValidationUtil.trimToEmpty(moduleName));
        vacancy.setTitle(ValidationUtil.trimToEmpty(title));
        vacancy.setDescription(ValidationUtil.trimToEmpty(description));
        vacancy.setRequiredSkills(ValidationUtil.splitCsv(requiredSkills));
        vacancy.setPreferredBackground(ValidationUtil.trimToEmpty(preferredBackground));
        vacancy.setWorkloadValue(ValidationUtil.parsePositiveInt(workloadValue));
        vacancy.setDeadline(ValidationUtil.trimToEmpty(deadline));
        vacancy.setStatus("OPEN");
        vacancy.setCreatedBy(currentUser.getUsername());
        vacancy.setApplicantCount(0);

        vacancies.add(vacancy);
        vacancyRepository.saveAll(vacancies);
        return vacancy;
    }
}
