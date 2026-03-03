<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty question ? 'Добавление' : 'Редактирование'} вопроса</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>${empty question ? 'Добавление' : 'Редактирование'} вопроса</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/question">Вопросы</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/question/save" method="post" class="data-form">
            <input type="hidden" name="id" value="${question.id}">

            <div class="form-group">
                <label for="voteId">Голосование:</label>
                <select id="voteId" name="voteId" required>
                    <c:forEach var="vote" items="${votes}">
                        <option value="${vote.id}" ${question.vote.id == vote.id ? 'selected' : ''}>${vote.title}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="content">Содержание вопроса:</label>
                <textarea id="content" name="content" rows="4" required>${question.content}</textarea>
            </div>

            <div class="form-group">
                <label for="dateVote">Дата голосования:</label>
                <input type="datetime-local" id="dateVote" name="dateVote"
                       value="${question.dateVote != null ? question.dateVote.toLocalDateTime() : ''}" required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Сохранить</button>
                <a href="${pageContext.request.contextPath}/question" class="btn">Отмена</a>
            </div>
        </form>
    </main>
</div>
</body>
</html>