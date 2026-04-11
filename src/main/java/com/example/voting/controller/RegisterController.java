package com.example.voting.controller;

import com.example.voting.entity.User;
import com.example.voting.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер регистрации новых пользователей.
 */
@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        Map<String, String> errors = validateRegistration(firstName, lastName, email, phone,
                                                           username, password, confirmPassword);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        try {
            User user = userService.registerUser(firstName, lastName, email, phone, username, password);
            // Автоматический вход после регистрации
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("userId", user.getId());
            request.getSession().setAttribute("userName", user.getFullName());
            request.getSession().setAttribute("userRole", user.getRole());
            response.sendRedirect(request.getContextPath() + "/");
        } catch (Exception e) {
            request.setAttribute("error", "Ошибка регистрации: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }

    private Map<String, String> validateRegistration(String firstName, String lastName, String email,
                                                      String phone, String username,
                                                      String password, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();

        if (firstName == null || firstName.trim().isEmpty()) {
            errors.put("firstName", "Имя обязательно");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.put("lastName", "Фамилия обязательна");
        }
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email обязателен");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.put("email", "Некорректный формат email");
        }
        if (phone != null && !phone.trim().isEmpty() && !phone.matches("^\\+?[0-9]{10,15}$")) {
            errors.put("phone", "Некорректный формат телефона");
        }
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Логин обязателен");
        } else if (username.length() < 3) {
            errors.put("username", "Логин должен быть не менее 3 символов");
        }
        if (password == null || password.length() < 6) {
            errors.put("password", "Пароль должен быть не менее 6 символов");
        }
        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Пароли не совпадают");
        }

        return errors;
    }
}
