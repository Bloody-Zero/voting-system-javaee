package com.example.voting.filter;

import com.example.voting.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Фильтр аутентификации и авторизации.
 *
 * ADMIN — полный доступ ко всему.
 * USER  — только просмотр голосований/вопросов и голосование от своего имени.
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AuthenticationFilter.class.getName());

    private static final String[] PUBLIC_PATHS = {
        "/login",
        "/register",
        "/logout",
        "/css/",
        "/js/",
        "/static/",
        "/favicon.ico"
    };

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String path        = request.getRequestURI().substring(contextPath.length());
        String method      = request.getMethod();

        /* 1. Публичные пути — пропускаем */
        if (isPublicPath(path)) {
            LOG.fine("Public path, passing through: " + path);
            chain.doFilter(req, res);
            return;
        }

        /* 2. Проверяем сессию */
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            LOG.fine("Not authenticated, redirecting to login from: " + path);
            response.sendRedirect(contextPath + "/login");
            return;
        }

        /* Гарантируем что userRole есть в сессии */
        if (session.getAttribute("userRole") == null) {
            session.setAttribute("userRole", user.getRole());
        }
        if (session.getAttribute("userName") == null) {
            session.setAttribute("userName", user.getFullName());
        }

        /* 3. ADMIN — полный доступ */
        if ("ADMIN".equals(user.getRole())) {
            chain.doFilter(req, res);
            return;
        }

        /* 4. USER — проверяем разрешение */
        boolean allowed = isUserAllowed(path, method);
        LOG.fine("USER access to " + path + " (" + method + ") = " + allowed);

        if (allowed) {
            chain.doFilter(req, res);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage",
                    "Доступ запрещён. Требуются права администратора.");
            request.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(request, response);
        }
    }

    /** Какие пути разрешены роли USER */
    private boolean isUserAllowed(String path, String method) {

        // Главная
        if (path.equals("/") || path.equals("/home")) return true;

        // Страница голосования пользователя
        if (path.equals("/votePage")) return true;

        // Голосования — ТОЛЬКО просмотр списка и деталей
        if (path.startsWith("/vote")) {
            if (!"GET".equals(method)) return false;
            return !(path.contains("/new") || path.contains("/edit") ||
                     path.contains("/delete") || path.contains("/save"));
        }

        // Вопросы — ТОЛЬКО просмотр списка и деталей
        if (path.startsWith("/question")) {
            if (!"GET".equals(method)) return false;
            return !(path.contains("/new") || path.contains("/edit") ||
                     path.contains("/delete") || path.contains("/save") ||
                     path.contains("/byVote"));
        }

        // Результаты — просмотр + голосование (создание своего голоса)
        if (path.startsWith("/choice")) {
            if ("GET".equals(method)) {
                // list, view/*, vote (страница голосования)
                if (path.contains("/edit") || path.contains("/delete")) {
                    return false;
                }
                return true;
            }
            if ("POST".equals(method)) {
                // save (admin) и saveVote (user — голосование от своего имени)
                return path.contains("/save") || path.contains("/saveVote");
            }
            return false;
        }

        // Всё остальное (включая /user/*) — запрещено
        return false;
    }

    private boolean isPublicPath(String path) {
        if (path == null) return false;
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) return true;
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
