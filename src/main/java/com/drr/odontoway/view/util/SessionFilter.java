package com.drr.odontoway.view.util;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName = "SessionFilter", urlPatterns = {"*.xhtml"})
public class SessionFilter implements Filter {

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String reqURI = req.getRequestURI();

        Boolean accesoLogin = reqURI.contains("login.xhtml") || reqURI.contains("resources");

        Boolean accesoRecursoJSF = reqURI.contains("jakarta.faces.resource");

        Boolean usuarioLogueado = (session != null && session.getAttribute("sessionDataView") != null);

        if (usuarioLogueado || accesoLogin || accesoRecursoJSF) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }
    
}
