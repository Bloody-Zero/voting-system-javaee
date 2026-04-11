<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
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
                <li><a href="${pageContext.request.contextPath}/" class="active">🏠 Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/votePage">🗳️ Голосовать</a></li>
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
            <h2>Панель управления</h2>
            <p>Обзор системы голосования и статистика</p>
        </div>

        <section class="stats">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon">📋</div>
                    <h3>Голосования</h3>
                    <p class="stat-number">${totalVotes}</p>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">👥</div>
                    <h3>Пользователи</h3>
                    <p class="stat-number">${totalUsers}</p>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">❓</div>
                    <h3>Вопросы</h3>
                    <p class="stat-number">${totalQuestions}</p>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">🗳️</div>
                    <h3>Голоса</h3>
                    <p class="stat-number">${totalChoices}</p>
                </div>
            </div>
        </section>

        <section class="active-votes">
            <div class="actions">
                <h2 class="section-title" style="margin: 0;">Активные голосования</h2>
            </div>
            <c:if test="${empty activeVotes}">
                <div class="empty-state">
                    <div class="empty-icon">📭</div>
                    <h3>Нет активных голосований</h3>
                    <p>В данный момент нет голосований, в которых можно принять участие.</p>
                    <c:if test="${userRole eq 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/vote/form" class="btn btn-primary">
                            <span>➕</span> Создать голосование
                        </a>
                    </c:if>
                </div>
            </c:if>
            <c:forEach var="vote" items="${activeVotes}">
                <div class="vote-card">
                    <h3>${vote.title}</h3>
                    <p>📅 <strong>Начало:</strong> ${vote.dateStartFormatted}</p>
                    <p>📅 <strong>Окончание:</strong> ${vote.dateFinishFormatted}</p>
                    <div style="margin-top: 1rem;">
                        <a href="${pageContext.request.contextPath}/vote/view/${vote.id}" class="btn btn-primary small">
                            Подробнее →
                        </a>
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
