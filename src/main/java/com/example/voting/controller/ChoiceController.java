package com.example.voting.controller;

import com.example.voting.entity.Choice;
import com.example.voting.entity.Question;
import com.example.voting.entity.User;
import com.example.voting.service.ChoiceService;
import com.example.voting.service.QuestionService;
import com.example.voting.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/choice/*")
public class ChoiceController extends HttpServlet {

    private ChoiceService choiceService;
    private QuestionService questionService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        choiceService = new ChoiceService();
        questionService = new QuestionService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Список всех выборов
            List<Choice> choices = choiceService.getAllChoices();
            request.setAttribute("choices", choices);
            request.getRequestDispatcher("/WEB-INF/views/choice/list.jsp").forward(request, response);
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
                // Форма создания, нужно передать списки вопросов и пользователей
                List<Question> questions = questionService.getAllQuestions();
                List<User> users = userService.getAllUsers();
                request.setAttribute("questions", questions);
                request.setAttribute("users", users);
                request.getRequestDispatcher("/WEB-INF/views/choice/form.jsp").forward(request, response);
                break;

            case "edit":
                Long editId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (editId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Choice editChoice = choiceService.getChoice(editId);
                request.setAttribute("choice", editChoice);
                // Для выбора можно передать те же списки (если разрешено менять связи)
                request.setAttribute("questions", questionService.getAllQuestions());
                request.setAttribute("users", userService.getAllUsers());
                request.getRequestDispatcher("/WEB-INF/views/choice/form.jsp").forward(request, response);
                break;

            case "delete":
                Long deleteId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (deleteId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                choiceService.deleteChoice(deleteId);
                response.sendRedirect(request.getContextPath() + "/choice");
                break;

            case "view":
                Long viewId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (viewId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Choice viewChoice = choiceService.getChoice(viewId);
                request.setAttribute("choice", viewChoice);
                request.getRequestDispatcher("/WEB-INF/views/choice/view.jsp").forward(request, response);
                break;

            case "vote":
                // Форма голосования для обычного пользователя
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("userId") == null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                Long currentUserId = (Long) session.getAttribute("userId");
                User currentUser = (User) session.getAttribute("user");

                // Получаем все вопросы
                List<Question> allQuestions = questionService.getAllQuestions();
                request.setAttribute("questions", allQuestions);
                request.setAttribute("userId", currentUserId);

                // Проверяем, на какие вопросы пользователь уже голосовал
                if (currentUser != null) {
                    List<Choice> userChoices = choiceService.getChoicesByUser(currentUserId);
                    request.setAttribute("userChoices", userChoices);
                }

                request.getRequestDispatcher("/WEB-INF/views/choice/vote.jsp").forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // POST для сохранения голоса пользователя
        if (pathInfo != null && pathInfo.startsWith("/saveVote")) {
            saveUserVote(request, response);
            return;
        }
        String idParam = request.getParameter("id");
        String questionIdParam = request.getParameter("questionId");
        String userIdParam = request.getParameter("userId");
        String choiceUser = request.getParameter("choiceUser");

        // Валидация
        if (questionIdParam == null || userIdParam == null || choiceUser == null || choiceUser.trim().isEmpty()) {
            request.setAttribute("error", "Все поля обязательны");
            request.setAttribute("questions", questionService.getAllQuestions());
            request.setAttribute("users", userService.getAllUsers());
            request.getRequestDispatcher("/WEB-INF/views/choice/form.jsp").forward(request, response);
            return;
        }

        try {
            Long questionId = Long.parseLong(questionIdParam);
            Long userId = Long.parseLong(userIdParam);

            Choice choice;
            if (idParam != null && !idParam.isEmpty()) {
                // Обновление
                Long id = Long.parseLong(idParam);
                choice = choiceService.getChoice(id);
                choice.setChoiceUser(choiceUser);
                // Возможно, не разрешаем менять вопрос и пользователя после создания
                choiceService.updateChoice(choice);
            } else {
                // Создание
                choice = new Choice();
                choice.setChoiceUser(choiceUser);
                choiceService.saveChoice(choice, questionId, userId);
            }

            response.sendRedirect(request.getContextPath() + "/choice");

        } catch (Exception e) {
            request.setAttribute("error", "Ошибка сохранения: " + e.getMessage());
            request.setAttribute("questions", questionService.getAllQuestions());
            request.setAttribute("users", userService.getAllUsers());
            request.getRequestDispatcher("/WEB-INF/views/choice/form.jsp").forward(request, response);
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

    /**
     * Сохраняет голос текущего пользователя.
     * userId берётся из сессии — подделать нельзя.
     */
    private void saveUserVote(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long currentUserId = (Long) request.getSession().getAttribute("userId");
        if (currentUserId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String questionIdParam = request.getParameter("questionId");
        String choiceUser = request.getParameter("choiceUser");

        if (questionIdParam == null || choiceUser == null || choiceUser.trim().isEmpty()) {
            request.setAttribute("error", "Выберите вопрос и вариант ответа");
            request.setAttribute("questions", questionService.getAllQuestions());
            request.setAttribute("userId", currentUserId);
            request.getRequestDispatcher("/WEB-INF/views/choice/vote.jsp").forward(request, response);
            return;
        }

        try {
            Long questionId = Long.parseLong(questionIdParam);

            // Проверяем, не голосовал ли пользователь уже за этот вопрос
            Choice existing = choiceService.getChoiceByQuestionAndUser(questionId, currentUserId);
            if (existing != null) {
                request.setAttribute("error", "Вы уже голосовали по этому вопросу");
                request.setAttribute("questions", questionService.getAllQuestions());
                request.setAttribute("userId", currentUserId);
                List<Choice> userChoices = choiceService.getChoicesByUser(currentUserId);
                request.setAttribute("userChoices", userChoices);
                request.getRequestDispatcher("/WEB-INF/views/choice/vote.jsp").forward(request, response);
                return;
            }

            Choice choice = new Choice();
            choice.setChoiceUser(choiceUser);
            choiceService.saveChoice(choice, questionId, currentUserId);

            response.sendRedirect(request.getContextPath() + "/choice?success=voted");

        } catch (Exception e) {
            request.setAttribute("error", "Ошибка при голосовании: " + e.getMessage());
            request.setAttribute("questions", questionService.getAllQuestions());
            request.setAttribute("userId", currentUserId);
            List<Choice> userChoices = choiceService.getChoicesByUser(currentUserId);
            request.setAttribute("userChoices", userChoices);
            request.getRequestDispatcher("/WEB-INF/views/choice/vote.jsp").forward(request, response);
        }
    }
}