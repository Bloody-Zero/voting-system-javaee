<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Просмотр пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>Пользователь: ${user.fullName}</h1>
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
            <p><strong>ID:</strong> ${user.id}</p>
            <p><strong>Фамилия:</strong> ${user.lastName}</p>
            <p><strong>Имя:</strong> ${user.firstName}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Телефон:</strong> ${user.phone}</p>
            <p><strong>Статус:</strong>
                <span class="status ${user.status eq 'VOTED' ? 'voted' : 'not-voted'}">
                    <c:choose>
                        <c:when test="${user.status == 'VOTED'}">Голосовал</c:when>
                        <c:when test="${user.status == 'NOT_VOTED'}">Не голосовал</c:when>
                        <c:otherwise>${user.status}</c:otherwise>
                    </c:choose>
                </span>
            </p>
        </div>

        <h2>Голоса пользователя</h2>
        <c:if test="${empty user.choices}">
            <p>Пользователь ещё не голосовал</p>
        </c:if>
        <c:forEach var="choice" items="${user.choices}">
            <div class="choice-item">
                <p><strong>Вопрос:</strong> ${choice.question.content} (голосование: ${choice.question.vote.title})</p>
                <p><strong>Выбор:</strong> ${choice.choiceUser}</p>
            </div>
        </c:forEach>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/user/edit/${user.id}" class="btn">Редактировать</a>
                <a href="${pageContext.request.contextPath}/user/delete/${user.id}" class="btn danger"
                   onclick="return confirm('Удалить пользователя?')">Удалить</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/user" class="btn">К списку</a>
        </div>
    </main>
</div>
</body>
</html>