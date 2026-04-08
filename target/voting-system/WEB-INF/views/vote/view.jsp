<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Просмотр голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>Голосование: ${vote.title}</h1>
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
            <p><strong>ID:</strong> ${vote.id}</p>
            <p><strong>Название:</strong> ${vote.title}</p>
            <p><strong>Дата начала:</strong> ${vote.dateStartFormatted}</p>
            <p><strong>Дата окончания:</strong> ${vote.dateFinishFormatted}</p>
            <p><strong>Статус:</strong>
                <span class="status ${vote.status eq 'ACTIVE' ? 'active' : 'finished'}">
                    <c:choose>
                        <c:when test="${vote.status == 'ACTIVE'}">Активно</c:when>
                        <c:when test="${vote.status == 'COMPLETED'}">Завершено</c:when>
                        <c:when test="${vote.status == 'DRAFT'}">Черновик</c:when>
                        <c:otherwise>${vote.status}</c:otherwise>
                    </c:choose>
                </span>
            </p>
        </div>

        <h2>Вопросы в этом голосовании</h2>
        <c:if test="${empty vote.questions}">
            <p>Нет вопросов</p>
        </c:if>
        <c:forEach var="question" items="${vote.questions}">
            <div class="question-item">
                <p><strong>${question.content}</strong></p>
                <p>Дата: ${question.dateVoteFormatted}</p>
                <a href="${pageContext.request.contextPath}/question/view/${question.id}" class="btn small">Подробнее</a>
            </div>
        </c:forEach>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/vote/edit/${vote.id}" class="btn">Редактировать</a>
                <a href="${pageContext.request.contextPath}/vote/delete/${vote.id}" class="btn danger"
                   onclick="return confirm('Удалить голосование?')">Удалить</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/vote" class="btn">К списку</a>
        </div>
    </main>
</div>
</body>
</html>