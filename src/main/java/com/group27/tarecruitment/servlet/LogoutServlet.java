package com.group27.tarecruitment.servlet;
import com.group27.tarecruitment.util.SessionUtil; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.HttpServlet; import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpServletResponse; import java.io.IOException;
@WebServlet("/logout") public class LogoutServlet extends HttpServlet { @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { SessionUtil.clear(request); response.sendRedirect(request.getContextPath() + "/vacancies"); } }
