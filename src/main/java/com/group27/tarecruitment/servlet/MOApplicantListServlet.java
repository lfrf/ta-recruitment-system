package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.service.ReviewService;
import com.group27.tarecruitment.service.WorkloadService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/mo/applicants")
public class MOApplicantListServlet extends HttpServlet {
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();
    private final ReviewService reviewService = new ReviewService();
    private final UserRepository userRepository = new UserRepository();
    private final WorkloadService workloadService = new WorkloadService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please use the staff login page before accessing the organiser review area.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }
        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can review applicant records.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        String vacancyId = request.getParameter("vacancyId");
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("managedVacancies", reviewService.getManagedVacancies(currentUser));
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));

        if (vacancyId == null || vacancyId.isBlank()) {
            request.getRequestDispatcher("/WEB-INF/views/mo/applicant-list.jsp").forward(request, response);
            return;
        }

        Vacancy vacancy = reviewService.getManagedVacancy(currentUser, vacancyId).orElse(null);
        if (vacancy == null) {
            SessionUtil.storeFlashError(request, "The selected vacancy is not managed by your organiser account.");
            response.sendRedirect(request.getContextPath() + "/mo/applicants");
            return;
        }

        List<ApplicationRecord> applications = reviewService.getApplicationsForVacancy(vacancyId);
        Map<String, ApplicantProfile> profileByApplicantId = new LinkedHashMap<>();
        for (ApplicantProfile profile : applicantProfileRepository.findAll()) {
            profileByApplicantId.put(profile.getApplicantId(), profile);
        }
        Map<String, UserAccount> userByApplicantId = new LinkedHashMap<>();
        for (UserAccount user : userRepository.findAll()) {
            userByApplicantId.put(user.getUserId(), user);
        }

        request.setAttribute("vacancy", vacancy);
        request.setAttribute("applications", applications);
        request.setAttribute("profileByApplicantId", profileByApplicantId);
        request.setAttribute("userByApplicantId", userByApplicantId);
        request.setAttribute("activeCountByApplicantId", workloadService.getActiveCountByApplicantId());
        request.getRequestDispatcher("/WEB-INF/views/mo/review.jsp").forward(request, response);
    }
}
