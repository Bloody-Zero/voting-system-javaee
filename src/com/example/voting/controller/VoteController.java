package com.example.voting.controller;

import com.example.voting.entity.Vote;
import com.example.voting.service.VoteService;
import com.example.voting.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@WebServlet("/vote/*")
public class VoteController extends HttpServlet {

    private VoteService voteService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() throws ServletException {
        voteService = new VoteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Список всех голосований
            List<Vote> votes = voteService.getAllVotes();
            request.setAttribute("votes", votes);
            request.getRequestDispatcher("/WEB-INF/views/vote/list.jsp").forward(request, response);
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
                // Показать форму создания
                request.getRequestDispatcher("/WEB-INF/views/vote/form.jsp").forward(request, response);
                break;

            case "edit":
                // Форма редактирования
                Long editId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (editId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Vote editVote = voteService.getVote(editId);
                request.setAttribute("vote", editVote);
                request.getRequestDispatcher("/WEB-INF/views/vote/form.jsp").forward(request, response);
                break;

            case "delete":
                // Удаление
                Long deleteId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (deleteId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                voteService.deleteVote(deleteId);
                response.sendRedirect(request.getContextPath() + "/vote");
                break;

            case "view":
                // Просмотр деталей
                Long viewId = parseId(splits.length > 2 ? splits[2] : request.getParameter("id"));
                if (viewId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Vote viewVote = voteService.getVote(viewId);
                request.setAttribute("vote", viewVote);
                request.getRequestDispatcher("/WEB-INF/views/vote/view.jsp").forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Сохранение (create или update)
        String idParam = request.getParameter("id");
        String title = request.getParameter("title");
        String dateStartStr = request.getParameter("dateStart");
        String dateFinishStr = request.getParameter("dateFinish");
        String status = request.getParameter("status");

        // Валидация
        Map<String, String> errors = ValidationUtil.validateVote(
                title, dateStartStr, dateFinishStr, status);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("title", title);
            request.setAttribute("dateStart", dateStartStr);
            request.setAttribute("dateFinish", dateFinishStr);
            request.setAttribute("status", status);
            request.getRequestDispatcher("/WEB-INF/views/vote/form.jsp").forward(request, response);
            return;
        }

        try {
            Vote vote;
            LocalDateTime dateStart = LocalDateTime.parse(dateStartStr, DATE_TIME_FORMATTER);
            LocalDateTime dateFinish = LocalDateTime.parse(dateFinishStr, DATE_TIME_FORMATTER);

            if (idParam != null && !idParam.isEmpty()) {
                // Обновление
                Long id = Long.parseLong(idParam);
                vote = voteService.getVote(id);
                vote.setTitle(title);
                vote.setDateStart(dateStart);
                vote.setDateFinish(dateFinish);
                vote.setStatus(status);
            } else {
                // Создание
                vote = new Vote(title, dateStart, dateFinish, status);
            }

            voteService.saveVote(vote);
            response.sendRedirect(request.getContextPath() + "/vote");

        } catch (Exception e) {
            request.setAttribute("error", "Ошибка сохранения: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/vote/form.jsp").forward(request, response);
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