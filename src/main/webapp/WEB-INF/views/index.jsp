<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Система голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>🗳️ Система голосования</h1>
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
        <section class="stats">
            <h2>Статистика системы</h2>
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>📋 Голосования</h3>
                    <p class="stat-number">${totalVotes}</p>
                </div>
                <div class="stat-card">
                    <h3>👥 Пользователи</h3>
                    <p class="stat-number">${totalUsers}</p>
                </div>
                <div class="stat-card">
                    <h3>❓ Вопросы</h3>
                    <p class="stat-number">${totalQuestions}</p>
                </div>
                <div class="stat-card">
                    <h3>🗳️ Голоса</h3>
                    <p class="stat-number">${totalChoices}</p>
                </div>
            </div>
        </section>

        <section class="active-votes">
            <h2>Активные голосования</h2>
            <c:if test="${empty activeVotes}">
                <div class="detail-card" style="text-align:center; color:#6c757d;">
                    <p>Нет активных голосований</p>
                </div>
            </c:if>
            <c:forEach var="vote" items="${activeVotes}">
                <div class="vote-card">
                    <h3>${vote.title}</h3>
                    <p>📅 Начало: ${vote.dateStartFormatted}</p>
                    <p>📅 Окончание: ${vote.dateFinishFormatted}</p>
                    <div style="margin-top:0.8rem;">
                        <a href="${pageContext.request.contextPath}/vote/view/${vote.id}" class="btn btn-primary">Подробнее</a>
                    </div>
                </div>
            </c:forEach>
        </section>
    </main>

    <footer>
        <p>&copy; 2026 Система голосования. Курсовой проект по Java EE.</p>
    </footer>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
