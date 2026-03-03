<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Голосование: ${vote.title}</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote">Голосования</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <div class="detail-card">
            <p><strong>ID:</strong> ${vote.id}</p>
            <p><strong>Название:</strong> ${vote.title}</p>
            <p><strong>Дата начала:</strong> <fmt:formatDate value="${vote.dateStart}" pattern="dd.MM.yyyy HH:mm"/></p>
            <p><strong>Дата окончания:</strong> <fmt:formatDate value="${vote.dateFinish}" pattern="dd.MM.yyyy HH:mm"/></p>
            <p><strong>Статус:</strong> <span class="status ${vote.status eq 'Активно' ? 'active' : 'finished'}">${vote.status}</span></p>
        </div>

        <h2>Вопросы в этом голосовании</h2>
        <c:if test="${empty vote.questions}">
            <p>Нет вопросов</p>
        </c:if>
        <c:forEach var="question" items="${vote.questions}">
            <div class="question-item">
                <p><strong>${question.content}</strong></p>
                <p>Дата: <fmt:formatDate value="${question.dateVote}" pattern="dd.MM.yyyy HH:mm"/></p>
                <a href="${pageContext.request.contextPath}/question/view/${question.id}" class="btn small">Подробнее</a>
            </div>
        </c:forEach>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/vote/edit/${vote.id}" class="btn">Редактировать</a>
            <a href="${pageContext.request.contextPath}/vote/delete/${vote.id}" class="btn danger"
               onclick="return confirm('Удалить голосование?')">Удалить</a>
            <a href="${pageContext.request.contextPath}/vote" class="btn">К списку</a>
        </div>
    </main>
</div>
</body>
</html>