<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Система голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Информационная подсистема ведения голосования</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote">Голосования</a></li>
                <li><a href="${pageContext.request.contextPath}/question">Вопросы</a></li>
                <li><a href="${pageContext.request.contextPath}/user">Пользователи</a></li>
                <li><a href="${pageContext.request.contextPath}/choice">Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Выход</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <section class="stats">
            <h2>Статистика системы</h2>
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>Голосования</h3>
                    <p class="stat-number">${totalVotes}</p>
                </div>
                <div class="stat-card">
                    <h3>Пользователи</h3>
                    <p class="stat-number">${totalUsers}</p>
                </div>
                <div class="stat-card">
                    <h3>Вопросы</h3>
                    <p class="stat-number">${totalQuestions}</p>
                </div>
                <div class="stat-card">
                    <h3>Голоса</h3>
                    <p class="stat-number">${totalChoices}</p>
                </div>
            </div>
        </section>

        <section class="active-votes">
            <h2>Активные голосования</h2>
            <c:if test="${empty activeVotes}">
                <p>Нет активных голосований</p>
            </c:if>
            <c:forEach var="vote" items="${activeVotes}">
                <div class="vote-card">
                    <h3>${vote.title}</h3>
                    <p>Начало: <fmt:formatDate value="${vote.dateStart}" pattern="dd.MM.yyyy HH:mm"/></p>
                    <p>Окончание: <fmt:formatDate value="${vote.dateFinish}" pattern="dd.MM.yyyy HH:mm"/></p>
                    <a href="${pageContext.request.contextPath}/vote/view/${vote.id}" class="btn">Подробнее</a>
                </div>
            </c:forEach>
        </section>
    </main>
    <footer>
        <p>&copy; 2025 Система голосования. Курсовой проект.</p>
    </footer>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>