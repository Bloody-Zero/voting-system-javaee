<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
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
            <h1>📋 Просмотр голосования</h1>
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
            <h2>${vote.title}</h2>
        </div>

        <div class="detail-card">
            <p>
                <span>🆔</span>
                <strong>ID:</strong> ${vote.id}
            </p>
            <p>
                <span>📅</span>
                <strong>Дата начала:</strong> ${vote.dateStartFormatted}
            </p>
            <p>
                <span>📅</span>
                <strong>Дата окончания:</strong> ${vote.dateFinishFormatted}
            </p>
            <p>
                <span>📊</span>
                <strong>Статус:</strong>
                <span class="status ${vote.status eq 'ACTIVE' ? 'active' : vote.status eq 'COMPLETED' ? 'finished' : ''}">
                    <c:choose>
                        <c:when test="${vote.status == 'ACTIVE'}">🟢 Активно</c:when>
                        <c:when test="${vote.status == 'COMPLETED'}">⚫ Завершено</c:when>
                        <c:when test="${vote.status == 'DRAFT'}">🟡 Черновик</c:when>
                        <c:otherwise>${vote.status}</c:otherwise>
                    </c:choose>
                </span>
            </p>
        </div>

        <section>
            <h2 class="section-title">Вопросы в голосовании (${fn:length(vote.questions)})</h2>
            
            <c:if test="${empty vote.questions}">
                <div class="empty-state">
                    <div class="empty-icon">❓</div>
                    <h3>Нет вопросов</h3>
                    <p>В этом голосовании ещё не добавлено ни одного вопроса</p>
                </div>
            </c:if>

            <c:forEach var="question" items="${vote.questions}">
                <div class="vote-card">
                    <h3>${question.content}</h3>
                    <p>📅 Дата: ${question.dateVoteFormatted}</p>
                    <div style="margin-top: 0.75rem;">
                        <a href="${pageContext.request.contextPath}/question/view/${question.id}" class="btn small btn-primary">
                            Подробнее →
                        </a>
                    </div>
                </div>
            </c:forEach>
        </section>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/vote/edit/${vote.id}" class="btn btn-primary">
                    <span>✏️</span> Редактировать
                </a>
                <a href="${pageContext.request.contextPath}/vote/delete/${vote.id}" class="btn btn-danger"
                   onclick="return confirm('Удалить голосование?')">
                    <span>🗑️</span> Удалить
                </a>
            </c:if>
            <a href="${pageContext.request.contextPath}/vote" class="btn btn-outline">
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
