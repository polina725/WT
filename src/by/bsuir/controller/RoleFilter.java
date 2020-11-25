package by.bsuir.controller;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RoleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession();
        String role=(String)session.getAttribute("role");
        String status=(String)session.getAttribute("entrantStatus");
        if (!checkAccessRole(request, role,status)) {
            response.sendRedirect("login");
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    private Boolean checkAccessRole(HttpServletRequest req,String actualRole,String status) {
        if (req.getServletContext().getServletRegistrations().get("AdminsPage").getMappings().contains(req.getServletPath())) {
            if ("ADMIN".equals(actualRole))
                return true;
            else
                return false;
        }
        else if (req.getServletContext().getServletRegistrations().get("EnrollmentForm").getMappings().contains(req.getServletPath())) {
            if ("STUDENT".equals(actualRole) && ("UNDEFINED".equals(status) || "ENROLLING".equals(status)))
                return true;
            else
                return false;
        }
        else return true;
    }

}