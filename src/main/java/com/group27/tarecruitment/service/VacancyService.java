package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class VacancyService {
    private static final String CAMPUS_XTC = "Xitucheng Campus";
    private static final String CAMPUS_SH = "Shahe Campus";
    private static final Set<String> SUPPORTED_CAMPUSES = Set.of(
            CAMPUS_XTC.toLowerCase(),
            CAMPUS_SH.toLowerCase()
    );

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
                                     String campus,
                                     String description,
                                     String requiredSkills,
                                     String preferredBackground,
                                     String workloadValue,
                                     String positionCount) {
        if (currentUser == null || currentUser.getRole() != UserRole.MO) {
            return "Only organiser accounts can publish course jobs.";
        }
        if (ValidationUtil.isBlank(moduleCode)) {
            return "Module code is required.";
        }
        if (ValidationUtil.isBlank(moduleName)) {
            return "Module name is required.";
        }
        if (ValidationUtil.isBlank(campus)) {
            return "Campus is required.";
        }
        if (normalizeCampus(campus) == null) {
            return "Campus must be Xitucheng Campus or Shahe Campus.";
        }
        if (ValidationUtil.isBlank(description)) {
            return "Course job description is required.";
        }
        if (ValidationUtil.splitCsv(requiredSkills).isEmpty()) {
            return "At least one required skill must be provided.";
        }
        if (ValidationUtil.parsePositiveInt(workloadValue) == null) {
            return "Workload value must be a positive integer.";
        }
        if (ValidationUtil.parsePositiveInt(positionCount) == null) {
            return "TA places must be a positive integer.";
        }
        return null;
    }

    public Vacancy createVacancy(UserAccount currentUser,
                                 String moduleCode,
                                 String moduleName,
                                 String campus,
                                 String description,
                                 String requiredSkills,
                                 String preferredBackground,
                                 String workloadValue,
                                 String positionCount,
                                 boolean leaderRoleAvailable) {
        List<Vacancy> vacancies = new ArrayList<>(vacancyRepository.findAll());

        Vacancy vacancy = new Vacancy();
        vacancy.setVacancyId("vac-" + UUID.randomUUID().toString().substring(0, 8));
        vacancy.setModuleCode(ValidationUtil.trimToEmpty(moduleCode));
        vacancy.setModuleName(ValidationUtil.trimToEmpty(moduleName));
        vacancy.setCampus(normalizeCampus(campus));
        vacancy.setTitle("Teaching Assistant Team");
        vacancy.setDescription(ValidationUtil.trimToEmpty(description));
        vacancy.setRequiredSkills(ValidationUtil.splitCsv(requiredSkills));
        vacancy.setPreferredBackground(ValidationUtil.trimToEmpty(preferredBackground));
        vacancy.setWorkloadValue(ValidationUtil.parsePositiveInt(workloadValue));
        vacancy.setPositionCount(ValidationUtil.parsePositiveInt(positionCount));
        vacancy.setLeaderRoleAvailable(leaderRoleAvailable);
        vacancy.setDeadline("");
        vacancy.setStatus("OPEN");
        vacancy.setCreatedBy(currentUser.getUsername());
        vacancy.setApplicantCount(0);

        vacancies.add(vacancy);
        vacancyRepository.saveAll(vacancies);
        return vacancy;
    }

    private String normalizeCampus(String campus) {
        String value = ValidationUtil.trimToEmpty(campus);
        if (value.isEmpty()) {
            return null;
        }
        String lowered = value.toLowerCase();
        if (!SUPPORTED_CAMPUSES.contains(lowered)) {
            return null;
        }
        return CAMPUS_XTC.toLowerCase().equals(lowered) ? CAMPUS_XTC : CAMPUS_SH;
    }
}
