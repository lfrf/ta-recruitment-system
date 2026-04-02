package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;

import java.util.ArrayList;
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

    public String createVacancy(UserAccount organiser,
                                String moduleCode,
                                String moduleName,
                                String title,
                                String description,
                                String requiredSkillsCsv,
                                String preferredBackground,
                                String workloadValueText,
                                String deadline) {

        if (organiser == null) {
            return "Please log in before creating a vacancy.";
        }

        if (organiser.getRole() != UserRole.MO) {
            return "Only organiser accounts can create vacancies.";
        }

        if (ValidationUtil.isBlank(moduleCode)
                || ValidationUtil.isBlank(moduleName)
                || ValidationUtil.isBlank(title)
                || ValidationUtil.isBlank(description)
                || ValidationUtil.isBlank(requiredSkillsCsv)
                || ValidationUtil.isBlank(workloadValueText)
                || ValidationUtil.isBlank(deadline)) {
            return "Please complete all required vacancy fields.";
        }

        int workloadValue;
        try {
            workloadValue = Integer.parseInt(workloadValueText.trim());
        } catch (NumberFormatException e) {
            return "Workload value must be a valid number.";
        }

        if (workloadValue <= 0) {
            return "Workload value must be greater than 0.";
        }

        List<Vacancy> vacancies = new ArrayList<>(vacancyRepository.findAll());

        Vacancy vacancy = new Vacancy();
        vacancy.setVacancyId("vac-" + System.currentTimeMillis());
        vacancy.setModuleCode(moduleCode.trim());
        vacancy.setModuleName(moduleName.trim());
        vacancy.setTitle(title.trim());
        vacancy.setDescription(description.trim());
        vacancy.setRequiredSkills(ValidationUtil.splitCsv(requiredSkillsCsv));
        vacancy.setPreferredBackground(ValidationUtil.trimToEmpty(preferredBackground));
        vacancy.setWorkloadValue(workloadValue);
        vacancy.setDeadline(deadline.trim());
        vacancy.setStatus("OPEN");
        vacancy.setCreatedBy(organiser.getUsername());
        vacancy.setApplicantCount(0);

        vacancies.add(vacancy);
        vacancyRepository.saveAll(vacancies);

        return null;
    }
}
