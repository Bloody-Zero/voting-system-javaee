<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
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
            <h1>👤 Профиль пользователя</h1>
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
        <div class="page-title">
            <h2>${user.fullName}</h2>
        </div>

        <div class="detail-card">
            <p>
                <span>🆔</span>
                <strong>ID:</strong> ${user.id}
            </p>
            <p>
                <span>👤</span>
                <strong>ФИО:</strong> ${user.lastName} ${user.firstName}
            </p>
            <p>
                <span>📧</span>
                <strong>Email:</strong> ${user.email}
            </p>
            <p>
                <span>📱</span>
                <strong>Телефон:</strong> ${user.phone}
            </p>
            <p>
                <span>📊</span>
                <strong>Статус:</strong>
                <span class="status ${user.status eq 'VOTED' ? 'voted' : 'not-voted'}">
                    <c:choose>
                        <c:when test="${user.status == 'VOTED'}">✅ Голосовал</c:when>
                        <c:when test="${user.status == 'NOT_VOTED'}">⏳ Не голосовал</c:when>
                        <c:otherwise>${user.status}</c:otherwise>
                    </c:choose>
                </span>
            </p>
        </div>

        <section>
            <h2 class="section-title">История голосов (${fn:length(user.choices)})</h2>
            
            <c:if test="${empty user.choices}">
                <div class="empty-state">
                    <div class="empty-icon">🗳️</div>
                    <h3>Пользователь ещё не голосовал</h3>
                    <p>Нет записей о голосовании</p>
                </div>
            </c:if>

            <c:forEach var="choice" items="${user.choices}">
                <div class="vote-card">
                    <h3>${choice.question.content}</h3>
                    <p>📋 <strong>Голосование:</strong> ${choice.question.vote.title}</p>
                    <p>✅ <strong>Выбор:</strong> ${choice.choiceUser}</p>
                </div>
            </c:forEach>
        </section>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/user/edit/${user.id}" class="btn btn-primary">
                    <span>✏️</span> Редактировать
                </a>
                <a href="${pageContext.request.contextPath}/user/delete/${user.id}" class="btn btn-danger"
                   onclick="return confirm('Удалить пользователя?')">
                    <span>🗑️</span> Удалить
                </a>
            </c:if>
            <a href="${pageContext.request.contextPath}/user" class="btn btn-outline">
                <span>←</span> К списку
            </a>
        </div>
    </main>

    <footer>
        <p>&copy; 2026 Система голосования. Курсовой проект по Java EE.</p>
    </footer>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
