package com.example.voting.controller;

import com.example.voting.service.VoteService;
import com.example.voting.service.UserService;
import com.example.voting.service.QuestionService;
import com.example.voting.service.ChoiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/", "/home"})
public class HomeController extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(HomeController.class.getName());

    private VoteService voteService;
    private UserService userService;
    private QuestionService questionService;
    private ChoiceService choiceService;

    @Override
    public void init() throws ServletException {
        voteService = new VoteService();
        userService = new UserService();
        questionService = new QuestionService();
        choiceService = new ChoiceService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Статистика для главной страницы
            request.setAttribute("totalVotes", voteService.getAllVotes().size());
            request.setAttribute("totalUsers", userService.getAllUsers().size());
            request.setAttribute("totalQuestions", questionService.getAllQuestions().size());
            request.setAttribute("totalChoices", choiceService.getAllChoices().size());

            // Активные голосования
            request.setAttribute("activeVotes", voteService.getVotesByStatus("ACTIVE"));

            // Перенаправление на JSP
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Ошибка в HomeController", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
        }
    }
}