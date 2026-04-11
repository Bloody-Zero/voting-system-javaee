<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Результат голосования — Детали</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>📊 Результат голосования</h1>
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
                <li><a href="${pageContext.request.contextPath}/choice" class="active">📊 Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="logout">🚪 Выход</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <div class="page-title">
            <h2>Информация о результате</h2>
        </div>

        <div class="detail-card">
            <p>
                <span>🆔</span>
                <strong>ID:</strong> ${choice.id}
            </p>
            <p>
                <span>❓</span>
                <strong>Вопрос:</strong><br>
                <span style="margin-left: 28px; color: var(--gray-700);">${choice.question.content}</span>
            </p>
            <p>
                <span>👤</span>
                <strong>Пользователь:</strong> ${choice.user.fullName}
            </p>
            <p>
                <span>✅</span>
                <strong>Выбор:</strong>
                <span class="badge">${choice.choiceUser}</span>
            </p>
        </div>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/choice/edit/${choice.id}" class="btn btn-primary">
                    <span>✏️</span> Редактировать
                </a>
            </c:if>
            <a href="${pageContext.request.contextPath}/choice" class="btn btn-outline">
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
