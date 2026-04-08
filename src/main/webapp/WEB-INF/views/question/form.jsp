<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty question ? 'Добавление' : 'Редактирование'} вопроса</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>${empty question ? 'Добавление' : 'Редактирование'} вопроса</h1>
            <div class="user-badge">
                👤 ${userName}
                <span class="role">${userRole eq 'ADMIN' ? '🔑 Администратор' : '👤 Пользователь'}</span>
            </div>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">🏠 Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote">📋 Голосования</a></li>
                <c:if test="${userRole eq 'ADMIN'}">
                    <li><a href="${pageContext.request.contextPath}/question">❓ Вопросы</a></li>
                    <li><a href="${pageContext.request.contextPath}/user">👥 Пользователи</a></li>
                </c:if>
                <li><a href="${pageContext.request.contextPath}/choice">📊 Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="logout">🚪 Выход</a></li>
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
                       value="${question.dateVote != null ? question.dateVote.format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd''T''HH:mm')) : ''}" required>
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