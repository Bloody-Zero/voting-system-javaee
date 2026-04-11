<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
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
            <h1>❓ Просмотр вопроса</h1>
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
            <h2>Информация о вопросе</h2>
        </div>

        <div class="detail-card">
            <p>
                <span>🆔</span>
                <strong>ID:</strong> ${question.id}
            </p>
            <p>
                <span>📋</span>
                <strong>Голосование:</strong>
                <span class="tag primary">${question.vote.title}</span>
            </p>
            <p>
                <span>📝</span>
                <strong>Содержание:</strong><br>
                <span style="margin-left: 28px; color: var(--gray-700);">${question.content}</span>
            </p>
            <p>
                <span>📅</span>
                <strong>Дата:</strong> ${question.dateVoteFormatted}
            </p>
        </div>

        <section>
            <h2 class="section-title">Результаты голосования (${fn:length(question.choices)})</h2>
            
            <c:if test="${empty question.choices}">
                <div class="empty-state">
                    <div class="empty-icon">🗳️</div>
                    <h3>Нет голосов</h3>
                    <p>По этому вопросу ещё не было голосований</p>
                </div>
            </c:if>

            <c:forEach var="choice" items="${question.choices}">
                <div class="choice-item">
                    <div class="flex items-center gap-md">
                        <div class="avatar">${choice.user.fullName.substring(0, 1)}</div>
                        <div>
                            <p class="font-bold mb-sm">${choice.user.fullName}</p>
                            <p class="text-muted text-sm">${choice.choiceUser}</p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </section>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/question/edit/${question.id}" class="btn btn-primary">
                    <span>✏️</span> Редактировать
                </a>
            </c:if>
            <a href="${pageContext.request.contextPath}/question" class="btn btn-outline">
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
