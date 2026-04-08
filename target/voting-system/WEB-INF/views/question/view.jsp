<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Просмотр вопроса</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>Вопрос</h1>
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
        <div class="detail-card">
            <p><strong>ID:</strong> ${question.id}</p>
            <p><strong>Голосование:</strong> ${question.vote.title}</p>
            <p><strong>Содержание:</strong> ${question.content}</p>
            <p><strong>Дата:</strong> ${question.dateVoteFormatted}</p>
        </div>

        <h2>Результаты по этому вопросу</h2>
        <c:if test="${empty question.choices}">
            <p>Нет голосов</p>
        </c:if>
        <c:forEach var="choice" items="${question.choices}">
            <div class="choice-item">
                <p><strong>${choice.user.fullName}</strong>: ${choice.choiceUser}</p>
            </div>
        </c:forEach>

        <div class="form-actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/question/edit/${question.id}" class="btn">Редактировать</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/question" class="btn btn-secondary">К списку</a>
        </div>
    </main>
</div>
</body>
</html>