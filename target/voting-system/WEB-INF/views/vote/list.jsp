<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление голосованиями</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>📋 Голосования</h1>
            <div class="user-badge">
                👤 ${userName}
                <span class="role">${userRole eq 'ADMIN' ? '🔑 Администратор' : '👤 Пользователь'}</span>
            </div>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">🏠 Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote" class="active">📋 Голосования</a></li>
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
            <h2>Все голосования</h2>
            <p>Управление и просмотр голосований в системе</p>
        </div>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/vote/new" class="btn btn-primary">
                    <span>➕</span> Создать голосование
                </a>
            </c:if>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert success">
                <span>✅</span> Операция выполнена успешно
            </div>
        </c:if>

        <c:if test="${empty votes}">
            <div class="empty-state">
                <div class="empty-icon">📭</div>
                <h3>Голосований пока нет</h3>
                <p>Создайте первое голосование, чтобы начать работу</p>
                <c:if test="${userRole eq 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/vote/new" class="btn btn-primary">
                        <span>➕</span> Создать голосование
                    </a>
                </c:if>
            </div>
        </c:if>

        <c:if test="${not empty votes}">
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Название</th>
                            <th>Дата начала</th>
                            <th>Дата окончания</th>
                            <th>Статус</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="vote" items="${votes}">
                            <tr>
                                <td data-label="ID">${vote.id}</td>
                                <td data-label="Название"><strong>${vote.title}</strong></td>
                                <td data-label="Дата начала">${vote.dateStartFormatted}</td>
                                <td data-label="Дата окончания">${vote.dateFinishFormatted}</td>
                                <td data-label="Статус">
                                    <span class="status ${vote.status eq 'ACTIVE' ? 'active' : vote.status eq 'COMPLETED' ? 'finished' : ''}">
                                        <c:choose>
                                            <c:when test="${vote.status == 'ACTIVE'}">🟢 Активно</c:when>
                                            <c:when test="${vote.status == 'COMPLETED'}">⚫ Завершено</c:when>
                                            <c:when test="${vote.status == 'DRAFT'}">🟡 Черновик</c:when>
                                            <c:otherwise>${vote.status}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                                <td data-label="Действия">
                                    <div class="flex gap-sm">
                                        <a href="${pageContext.request.contextPath}/vote/view/${vote.id}" class="btn small btn-outline">
                                            👁️
                                        </a>
                                        <c:if test="${userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/vote/edit/${vote.id}" class="btn small btn-primary">
                                                ✏️
                                            </a>
                                            <a href="${pageContext.request.contextPath}/vote/delete/${vote.id}" class="btn small btn-danger"
                                               onclick="return confirm('Удалить голосование?')">
                                                🗑️
                                            </a>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </main>

    <footer>
        <p>&copy; 2026 Система голосования. Курсовой проект по Java EE.</p>
    </footer>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
