package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.model.AiVacancyRecommendation;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.service.AiProfileImportService;
import com.group27.tarecruitment.service.AdminService;
import com.group27.tarecruitment.service.ApplicantProfileService;
import com.group27.tarecruitment.service.ApplicationService;
import com.group27.tarecruitment.service.VacancyService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@WebServlet("/vacancies")
public class PublicVacancyListServlet extends HttpServlet {
    private final VacancyService vacancyService = new VacancyService();
    private final AdminService adminService = new AdminService();
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();
    private final ApplicationService applicationService = new ApplicationService();
    private final AiProfileImportService aiProfileImportService = new AiProfileImportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        boolean allowVisitorBrowsing = adminService.getConfig().isAllowVisitorBrowsing();
        if (currentUser == null && !allowVisitorBrowsing) {
            SessionUtil.storeFlashError(request, "Visitor browsing is currently disabled. Please log in to continue.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean isApplicant = currentUser != null && currentUser.getRole() == UserRole.APPLICANT;
        Map<String, Boolean> appliedVacancyIds = new LinkedHashMap<>();
        Map<String, String> applicationStatusByVacancyId = new LinkedHashMap<>();
        Map<String, Boolean> leadTaByVacancyId = new LinkedHashMap<>();
        Map<String, String> activeApplicationIdByVacancyId = new LinkedHashMap<>();
        boolean profileReady = false;
        if (isApplicant) {
            profileReady = applicantProfileService.isProfileReady(currentUser.getUserId());
            List<ApplicationRecord> applications = applicationService.getApplicationsByApplicant(currentUser.getUserId());
            for (ApplicationRecord application : applications) {
                String normalizedStatus = ValidationUtil.normalizeApplicationStatus(application.getStatus());
                if (ValidationUtil.STATUS_WITHDRAWN.equalsIgnoreCase(normalizedStatus)) {
                    continue;
                }
                String vacancyId = application.getVacancyId();
                appliedVacancyIds.put(vacancyId, Boolean.TRUE);
                applicationStatusByVacancyId.put(vacancyId, normalizedStatus);
                leadTaByVacancyId.put(
                        vacancyId,
                        application.isLeadTa() && ValidationUtil.STATUS_OFFERED.equalsIgnoreCase(normalizedStatus)
                );
                activeApplicationIdByVacancyId.put(vacancyId, application.getApplicationId());
            }
        }

        List<Vacancy> browsableVacancies = vacancyService.getAllVacancies().stream()
                .filter(vacancy -> isBrowsableStatus(vacancy.getStatus()))
                .toList();
        String keyword = ValidationUtil.trimToEmpty(request.getParameter("keyword"));
        String selectedCampus = ValidationUtil.trimToEmpty(request.getParameter("campus"));
        String rankingMode = ValidationUtil.trimToEmpty(request.getParameter("rankingMode"));
        if (!"ai".equalsIgnoreCase(rankingMode)) {
            rankingMode = "standard";
        } else {
            rankingMode = "ai";
        }
        boolean filtersApplied = !keyword.isEmpty() || !selectedCampus.isEmpty();

        Set<String> campusOptions = new LinkedHashSet<>();
        Map<String, Integer> offeredCountByVacancyId = applicationService.getOfferedCountByVacancyId();
        Map<String, Boolean> vacancyFullById = new LinkedHashMap<>();
        for (Vacancy vacancy : browsableVacancies) {
            String campus = ValidationUtil.trimToEmpty(vacancy.getCampus());
            if (!campus.isEmpty()) {
                campusOptions.add(campus);
            }
            int offeredCount = offeredCountByVacancyId.getOrDefault(vacancy.getVacancyId(), 0);
            boolean full = vacancy.getPositionCount() > 0 && offeredCount >= vacancy.getPositionCount();
            vacancyFullById.put(vacancy.getVacancyId(), full);
        }

        Comparator<Vacancy> browseOrder = Comparator
                .comparing((Vacancy vacancy) -> vacancyFullById.getOrDefault(vacancy.getVacancyId(), false))
                .thenComparing(vacancy -> ValidationUtil.trimToEmpty(vacancy.getModuleCode()), String.CASE_INSENSITIVE_ORDER)
                .thenComparing(vacancy -> ValidationUtil.trimToEmpty(vacancy.getModuleName()), String.CASE_INSENSITIVE_ORDER);

        List<Vacancy> releaseVacancies = browsableVacancies.stream()
                .sorted(browseOrder)
                .toList();

        List<Vacancy> filteredVacancies = browsableVacancies.stream()
                .filter(vacancy -> matchesKeyword(vacancy, keyword))
                .filter(vacancy -> matchesCampus(vacancy, selectedCampus))
                .sorted(browseOrder)
                .toList();

        Map<String, AiVacancyRecommendation> aiRecommendationByVacancyId = new LinkedHashMap<>();
        List<Vacancy> aiTopVacancies = List.of();
        String aiRankingHint = "";
        String aiRankingStatus = "UNAVAILABLE";
        Optional<AiImportTask> latestRecommendTask = Optional.empty();
        if (isApplicant) {
            latestRecommendTask = aiProfileImportService.findLatestValidatedRankingTaskForUser(currentUser.getUserId());
            if (latestRecommendTask.isPresent() && latestRecommendTask.get().getRecommendations() != null) {
                for (AiVacancyRecommendation recommendation : latestRecommendTask.get().getRecommendations()) {
                    aiRecommendationByVacancyId.put(recommendation.getVacancyId(), recommendation);
                }
                aiRankingStatus = aiRecommendationByVacancyId.isEmpty() ? "EMPTY" : "READY";
            }
        }

        boolean aiRankingRequested = isApplicant && "ai".equalsIgnoreCase(rankingMode);
        boolean aiRankingEffective = false;
        if (aiRankingRequested && !aiRecommendationByVacancyId.isEmpty()) {
            Comparator<Vacancy> aiOrder = Comparator
                    .comparingInt((Vacancy vacancy) -> aiScore(aiRecommendationByVacancyId.get(vacancy.getVacancyId())))
                    .reversed()
                    .thenComparing(browseOrder);
            filteredVacancies = filteredVacancies.stream()
                    .sorted(aiOrder)
                    .toList();
            aiRankingEffective = true;
        } else if (aiRankingRequested) {
            aiRankingHint = "No validated AI recommendation is available yet. Please start AI import from My Profile, then return.";
        }

        if (!aiRecommendationByVacancyId.isEmpty()) {
            aiTopVacancies = filteredVacancies.stream()
                    .filter(vacancy -> aiRecommendationByVacancyId.containsKey(vacancy.getVacancyId()))
                    .sorted(Comparator
                            .comparingInt((Vacancy vacancy) -> aiScore(aiRecommendationByVacancyId.get(vacancy.getVacancyId())))
                            .reversed()
                            .thenComparing(browseOrder))
                    .limit(5)
                    .toList();
        }

        int releaseTotalCount = releaseVacancies.size();
        long releaseFullCount = releaseVacancies.stream()
                .filter(vacancy -> vacancyFullById.getOrDefault(vacancy.getVacancyId(), false))
                .count();
        long releaseClosedCount = releaseVacancies.stream()
                .filter(vacancy -> !vacancyFullById.getOrDefault(vacancy.getVacancyId(), false))
                .filter(vacancy -> !"OPEN".equalsIgnoreCase(ValidationUtil.trimToEmpty(vacancy.getStatus())))
                .count();
        long releaseOpenCount = releaseTotalCount - releaseFullCount - releaseClosedCount;

        long releaseShaheCount = releaseVacancies.stream()
                .filter(vacancy -> ValidationUtil.trimToEmpty(vacancy.getCampus()).toLowerCase().contains("shahe"))
                .count();

        request.setAttribute("vacancies", filteredVacancies);
        request.setAttribute("hasBrowseVacancies", !browsableVacancies.isEmpty());
        request.setAttribute("filtersApplied", filtersApplied);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCampus", selectedCampus);
        request.setAttribute("campusOptions", campusOptions);
        request.setAttribute("resultCount", filteredVacancies.size());
        request.setAttribute("releaseVacancies", releaseVacancies);
        request.setAttribute("releaseTotalCount", releaseTotalCount);
        request.setAttribute("releaseOpenCount", releaseOpenCount);
        request.setAttribute("releaseFullCount", releaseFullCount);
        request.setAttribute("releaseClosedCount", releaseClosedCount);
        request.setAttribute("releaseShaheCount", releaseShaheCount);
        request.setAttribute("vacancyFullById", vacancyFullById);
        request.setAttribute("rankingMode", rankingMode);
        request.setAttribute("aiRankingRequested", aiRankingRequested);
        request.setAttribute("aiRankingEffective", aiRankingEffective);
        request.setAttribute("aiRankingStatus", aiRankingStatus);
        request.setAttribute("aiRankingHint", aiRankingHint);
        request.setAttribute("aiRecommendationByVacancyId", aiRecommendationByVacancyId);
        request.setAttribute("aiTopVacancies", aiTopVacancies);
        request.setAttribute("aiLatestTaskId", latestRecommendTask.map(AiImportTask::getTaskId).orElse(""));
        request.setAttribute("loggedIn", currentUser != null);
        request.setAttribute("isApplicant", isApplicant);
        request.setAttribute("isMO", currentUser != null && currentUser.getRole() == UserRole.MO);
        request.setAttribute("isAdmin", currentUser != null && currentUser.getRole() == UserRole.ADMIN);
        request.setAttribute("profileReady", profileReady);
        request.setAttribute("appliedVacancyIds", appliedVacancyIds);
        request.setAttribute("applicationStatusByVacancyId", applicationStatusByVacancyId);
        request.setAttribute("leadTaByVacancyId", leadTaByVacancyId);
        request.setAttribute("activeApplicationIdByVacancyId", activeApplicationIdByVacancyId);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/visitor/vacancy-list.jsp").forward(request, response);
    }

    private boolean matchesCampus(Vacancy vacancy, String selectedCampus) {
        if (selectedCampus == null || selectedCampus.isBlank()) {
            return true;
        }
        String campus = ValidationUtil.trimToEmpty(vacancy.getCampus());
        return campus.equalsIgnoreCase(selectedCampus);
    }

    private boolean matchesKeyword(Vacancy vacancy, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String q = keyword.toLowerCase();
        return contains(vacancy.getModuleCode(), q)
                || contains(vacancy.getModuleName(), q)
                || contains(vacancy.getCampus(), q)
                || contains(vacancy.getDescription(), q)
                || contains(vacancy.getTitle(), q)
                || (vacancy.getRequiredSkills() != null
                && vacancy.getRequiredSkills().stream().anyMatch(skill -> contains(skill, q)));
    }

    private boolean contains(String source, String keyword) {
        return source != null && source.toLowerCase().contains(keyword);
    }

    private boolean isBrowsableStatus(String status) {
        String normalized = ValidationUtil.trimToEmpty(status);
        return "OPEN".equalsIgnoreCase(normalized) || "CLOSED".equalsIgnoreCase(normalized);
    }

    private int aiScore(AiVacancyRecommendation recommendation) {
        return recommendation == null || recommendation.getScore() == null ? -1 : recommendation.getScore();
    }
}
