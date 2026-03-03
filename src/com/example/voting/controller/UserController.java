package com.example.voting.controller;

import com.example.voting.entity.User;
import com.example.voting.service.UserService;
import com.example.voting.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Список пользователей
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/user/list.jsp").forward(request, response);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length < 2) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String action = splits[1];

        switch (action) {
            case "new":
                request.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(request, response);
                break;

            case "edit":
                Long editId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (editId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                User editUser = userService.getUser(editId);
                request.setAttribute("user", editUser);
                request.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(request, response);
                break;

            case "delete":
                Long deleteId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (deleteId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                userService.deleteUser(deleteId);
                response.sendRedirect(request.getContextPath() + "/user");
                break;

            case "view":
                Long viewId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (viewId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                User viewUser = userService.getUser(viewId);
                request.setAttribute("user", viewUser);
                request.getRequestDispatcher("/WEB-INF/views/user/view.jsp").forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String status = request.getParameter("status");

        Map<String, String> errors = ValidationUtil.validateUser(firstName, lastName, email, phone, status);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("status", status);
            request.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(request, response);
            return;
        }

        try {
            User user;
            if (idParam != null && !idParam.isEmpty()) {
                Long id = Long.parseLong(idParam);
                user = userService.getUser(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setStatus(status);
            } else {
                user = new User(firstName, lastName, email, phone, status);
            }
            userService.saveUser(user);
            response.sendRedirect(request.getContextPath() + "/user");

        } catch (Exception e) {
            request.setAttribute("error", "Ошибка сохранения: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(request, response);
        }
    }

    private Long parseId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) return null;
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}