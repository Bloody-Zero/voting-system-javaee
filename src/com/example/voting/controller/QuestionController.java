package com.example.voting.controller;

import com.example.voting.entity.Question;
import com.example.voting.entity.Vote;
import com.example.voting.service.QuestionService;
import com.example.voting.service.VoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/question/*")
public class QuestionController extends HttpServlet {

    private QuestionService questionService;
    private VoteService voteService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() throws ServletException {
        questionService = new QuestionService();
        voteService = new VoteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Список всех вопросов
            List<Question> questions = questionService.getAllQuestions();
            request.setAttribute("questions", questions);
            request.getRequestDispatcher("/WEB-INF/views/question/list.jsp").forward(request, response);
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
                // Форма создания вопроса, нужно передать список голосований для выбора
                Long voteIdParam = request.getParameter("voteId") != null ?
                        Long.parseLong(request.getParameter("voteId")) : null;
                if (voteIdParam != null) {
                    Vote vote = voteService.getVote(voteIdParam);
                    request.setAttribute("preselectedVote", vote);
                }
                List<Vote> votes = voteService.getAllVotes();
                request.setAttribute("votes", votes);
                request.getRequestDispatcher("/WEB-INF/views/question/form.jsp").forward(request, response);
                break;

            case "edit":
                Long editId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (editId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Question editQuestion = questionService.getQuestion(editId);
                request.setAttribute("question", editQuestion);
                // Также нужен список голосований для выбора (если разрешено менять голосование)
                request.setAttribute("votes", voteService.getAllVotes());
                request.getRequestDispatcher("/WEB-INF/views/question/form.jsp").forward(request, response);
                break;

            case "delete":
                Long deleteId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (deleteId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                questionService.deleteQuestion(deleteId);
                response.sendRedirect(request.getContextPath() + "/question");
                break;

            case "view":
                Long viewId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (viewId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Question viewQuestion = questionService.getQuestion(viewId);
                request.setAttribute("question", viewQuestion);
                request.getRequestDispatcher("/WEB-INF/views/question/view.jsp").forward(request, response);
                break;

            case "byVote":
                // Список вопросов для конкретного голосования
                Long voteId = parseId(request.getParameter("voteId"));
                if (voteId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                List<Question> questionsByVote = questionService.getQuestionsByVote(voteId);
                request.setAttribute("questions", questionsByVote);
                request.setAttribute("voteId", voteId);
                request.getRequestDispatcher("/WEB-INF/views/question/list.jsp").forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String content = request.getParameter("content");
        String dateVoteStr = request.getParameter("dateVote");
        String voteIdParam = request.getParameter("voteId");

        // Простейшая валидация
        if (content == null || content.trim().isEmpty() || dateVoteStr == null || voteIdParam == null) {
            request.setAttribute("error", "Все поля обязательны");
            request.setAttribute("votes", voteService.getAllVotes());
            request.getRequestDispatcher("/WEB-INF/views/question/form.jsp").forward(request, response);
            return;
        }

        try {
            LocalDateTime dateVote = LocalDateTime.parse(dateVoteStr, DATE_TIME_FORMATTER);
            Long voteId = Long.parseLong(voteIdParam);

            Question question;
            if (idParam != null && !idParam.isEmpty()) {
                // Обновление
                Long id = Long.parseLong(idParam);
                question = questionService.getQuestion(id);
                question.setContent(content);
                question.setDateVote(dateVote);
                // Если нужно сменить голосование (опционально)
                Vote vote = voteService.getVote(voteId);
                question.setVote(vote);
                questionService.updateQuestion(question);
            } else {
                // Создание
                question = new Question();
                question.setContent(content);
                question.setDateVote(dateVote);
                questionService.saveQuestion(question, voteId);
            }

            response.sendRedirect(request.getContextPath() + "/question");

        } catch (Exception e) {
            request.setAttribute("error", "Ошибка сохранения: " + e.getMessage());
            request.setAttribute("votes", voteService.getAllVotes());
            request.getRequestDispatcher("/WEB-INF/views/question/form.jsp").forward(request, response);
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