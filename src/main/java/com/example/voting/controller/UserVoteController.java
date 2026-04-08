package com.example.voting.controller;

import com.example.voting.entity.Choice;
import com.example.voting.entity.Question;
import com.example.voting.service.ChoiceService;
import com.example.voting.service.QuestionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Страница голосования для обычных пользователей.
 * URL: /votePage (чтобы не конфликтовать с /vote/*)
 */
@WebServlet("/votePage")
public class UserVoteController extends HttpServlet {

    private ChoiceService choiceService;
    private QuestionService questionService;

    @Override
    public void init() throws ServletException {
        choiceService = new ChoiceService();
        questionService = new QuestionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long currentUserId = (Long) session.getAttribute("userId");

        List<Question> questions = questionService.getAllQuestions();
        request.setAttribute("questions", questions);
        request.setAttribute("userId", currentUserId);

        List<Choice> userChoices = choiceService.getChoicesByUser(currentUserId);
        request.setAttribute("userChoices", userChoices);

        request.getRequestDispatcher("/WEB-INF/views/choice/vote.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        saveVote(request, response);
    }

    private void saveVote(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Long currentUserId = (session != null) ? (Long) session.getAttribute("userId") : null;

        if (currentUserId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String questionIdParam = request.getParameter("questionId");
        String choiceUser = request.getParameter("choiceUser");

        if (questionIdParam == null || choiceUser == null || choiceUser.trim().isEmpty()) {
            request.setAttribute("error", "Выберите вопрос и введите вариант ответа");
            request.setAttribute("questions", questionService.getAllQuestions());
            request.setAttribute("userId", currentUserId);
            List<Choice> userChoices = choiceService.getChoicesByUser(currentUserId);
            request.setAttribute("userChoices", userChoices);
            request.getRequestDispatcher("/WEB-INF/views/choice/vote.jsp").forward(request, response);
            return;
        }

        try {
            Long questionId = Long.parseLong(questionIdParam);

            // Проверка: не голосовал ли уже
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

            response.sendRedirect(request.getContextPath() + "/votePage?success=voted");

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
