package com.example.voting.controller;

import com.example.voting.entity.User;
import com.example.voting.service.UserService;
import com.example.voting.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getPathInfo();
        if (action == null || action.equals("/")) {
            listUsers(req, resp);
        } else {
            switch (action) {
                case "/new":
                    showForm(req, resp);
                    break;
                case "/edit":
                    showEditForm(req, resp);
                    break;
                case "/delete":
                    deleteUser(req, resp);
                    break;
                case "/view":
                    viewUser(req, resp);
                    break;
                default:
                    listUsers(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getPathInfo();
        if ("/save".equals(action)) {
            saveUser(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/user");
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/user/list.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        User user = userService.getUser(id);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
    }

    private void viewUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        User user = userService.getUserWithChoices(id);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/user/view.jsp").forward(req, resp);
    }

    private void saveUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String status = req.getParameter("status");

        Map<String, String> errors = ValidationUtil.validateUser(firstName, lastName, email, phone, status);
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("firstName", firstName);
            req.setAttribute("lastName", lastName);
            req.setAttribute("email", email);
            req.setAttribute("phone", phone);
            req.setAttribute("status", status);
            req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
            return;
        }

        try {
            User user;
            if (idParam != null && !idParam.isEmpty()) {
                user = userService.getUser(Long.parseLong(idParam));
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setStatus(status);
            } else {
                user = new User(firstName, lastName, email, phone, status);
            }
            userService.saveUser(user);
            resp.sendRedirect(req.getContextPath() + "/user?success=created");
        } catch (Exception e) {
            req.setAttribute("error", "Ошибка: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        userService.deleteUser(id);
        resp.sendRedirect(req.getContextPath() + "/user?success=deleted");
    }
}