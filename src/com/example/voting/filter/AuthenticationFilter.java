package com.example.voting.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Фильтр аутентификации. Проверяет наличие пользователя в сессии для всех защищённых ресурсов.
 * Публичные пути (страница входа, статические ресурсы) доступны без аутентификации.
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    // Публичные пути, доступные без аутентификации
    private static final String[] PUBLIC_PATHS = {
        "/login",           // страница входа
        "/css/",            // стили
        "/js/",             // скрипты
        "/static/"          // прочие статические ресурсы (если есть)
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Инициализация не требуется
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Получаем путь запроса относительно контекста приложения
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Если путь публичный – пропускаем без проверки
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Проверяем наличие сессии и атрибута user
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Пользователь не аутентифицирован – перенаправляем на страницу входа
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Аутентифицирован – продолжаем обработку
        chain.doFilter(request, response);
    }

    /**
     * Проверяет, относится ли путь к публичным.
     * @param path путь запроса
     * @return true, если путь публичный
     */
    private boolean isPublicPath(String path) {
        // Корневой путь (главная страница) можно сделать публичным или защищённым.
        // Здесь оставим как публичный (при необходимости измените на false).
        if (path.equals("/") || path.isEmpty()) {
            return true;
        }
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // Освобождение ресурсов не требуется
    }
}