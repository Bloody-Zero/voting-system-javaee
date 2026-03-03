package com.example.voting.controller;

import com.example.voting.service.VoteService;
import com.example.voting.service.UserService;
import com.example.voting.service.QuestionService;
import com.example.voting.service.ChoiceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/", "/home"})
public class HomeController extends HttpServlet {

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

        // Статистика для главной страницы
        request.setAttribute("totalVotes", voteService.getAllVotes().size());
        request.setAttribute("totalUsers", userService.getAllUsers().size());
        request.setAttribute("totalQuestions", questionService.getAllQuestions().size());
        request.setAttribute("totalChoices", choiceService.getAllChoices().size());

        // Активные голосования
        request.setAttribute("activeVotes", voteService.getVotesByStatus("Активно"));

        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}