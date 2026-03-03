<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр вопроса</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Вопрос: ${question.content}</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/question">Вопросы</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <div class="detail-card">
            <p><strong>ID:</strong> ${question.id}</p>
            <p><strong>Голосование:</strong> ${question.vote.title}</p>
            <p><strong>Содержание:</strong> ${question.content}</p>
            <p><strong>Дата голосования:</strong> <fmt:formatDate value="${question.dateVote}" pattern="dd.MM.yyyy HH:mm"/></p>
        </div>

        <h2>Результаты голосования по этому вопросу</h2>
        <c:if test="${empty question.choices}">
            <p>Нет голосов</p>
        </c:if>
        <c:forEach var="choice" items="${question.choices}">
            <div class="choice-item">
                <p><strong>${choice.user.fullName}</strong> выбрал: ${choice.choiceUser}</p>
            </div>
        </c:forEach>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/question/edit/${question.id}" class="btn">Редактировать</a>
            <a href="${pageContext.request.contextPath}/question/delete/${question.id}" class="btn danger"
               onclick="return confirm('Удалить вопрос?')">Удалить</a>
            <a href="${pageContext.request.contextPath}/question" class="btn">К списку</a>
        </div>
    </main>
</div>
</body>
</html>