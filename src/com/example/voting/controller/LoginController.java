package com.example.voting.controller;

import com.example.voting.entity.User;
import com.example.voting.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Если уже залогинен, редирект на главную
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        // В реальном проекте нужна проверка пароля, но для демо используем только email

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Введите email");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        User user = userService.getUserByEmail(email);
        if (user == null) {
            request.setAttribute("error", "Пользователь с таким email не найден");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // Успешный вход
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());

        response.sendRedirect(request.getContextPath() + "/");
    }
}